package com.restaurant.android.cameriere.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.restaurant.android.DbManager;
import com.restaurant.android.Error;
import com.restaurant.android.R;
import com.restaurant.android.RestaurantApplication;
import com.restaurant.android.Utility;
import com.restaurant.android.cameriere.prenotazioni.Prenotazione;

/**
 * Activity che rappresenta la scheda di un singolo
 * tavolo. Viene tipicamente mostrata a seguito di un 
 * click su uno degli elementi della ListView della 
 * pagina precedente. 
 * 
 * @author Fabio Pierazzi
 */
public class TableCardActivity extends Activity {
	
	/* Variabile usata per il logging */
	private static final String TAG = "TableCardActivity";
	
	/* Flag che indica se il conto deve essere sincronizzato con il server */
	public static boolean updateConto = true;
	
	/* Variabili necessarie alla ListView per la 
	 * visualizzazione dell'elenco delle ordinazioni */
	private ListView contoListView = null;
	private ContoAdapter contoListView_adapter = null;
	private ArrayList<Ordinazione> contoListView_arrayOrdinazioni = null;
			
	/* Variabili necessarie alla ListView per mostrare
	 * l'elenco delle prenotazioni effettuate */
	private ListView prenotationListView = null;
	private ArrayList<Prenotazione> prenotationListView_arrayPrenotazioni = null;
	private PrenotationAdapter prenotationListView_adapter = null;
	
	/* Variaibli necessarie alla ListView per mostrare
	 * l'elenco di comande in attesa di conferma */
	private ListView ordersWaitingListView = null;
	private ArrayList<Ordinazione> ordersWaitingListView_arrayOrdinazioni = null;
	private OrdersWaitingAdapter ordersWaitingListView_adapter = null;
	
	/* Oggetto in cui salvare le caratteristiche del tavolo contenuto
	 * in questa scheda */
	Table myTable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cameriere_table_card);
		  
		/* *****************************************************************
		 * Recupero il valore col nome del tavolo dall'oggetto passato 
		 * durante l'invocazione di questa activity dall'elenco tavoli
		 ******************************************************************/
		Bundle b = getIntent().getExtras();
		myTable= (Table) b.getSerializable("tableObject");
		  
		/* Tavolo de-serializzato */
		Log.d("Scheda Tavolo", "De-serializzato il tavolo: " + myTable.getTableName());
		 
		/* ****************************************************************
		 * Bottone per l'occupazione del tavolo e l'apertura del conto
		 *****************************************************************/
		Button occupaTavolo = (Button) findViewById(R.id.button_tableCard_occupaTavolo);
		occupaTavolo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View c) {
				
					/* **********************************************************************
					 *  Mostro una finestra di dialogo dove chiedo quante persone ci sono
					 *  ******************************************************************** */
				    AlertDialog.Builder alert = new AlertDialog.Builder(TableCardActivity.this);                 
				    alert.setTitle("Occupa Tavolo");  
				    alert.setMessage("Numero persone: "); 
				    
				    /* EdiText per prendere l'input dell'utente */
				    final EditText inputText_numPersone = new EditText(TableCardActivity.this); 
				    DigitsKeyListener myDigitKeyListener =  new DigitsKeyListener(false, true);
				    inputText_numPersone.setKeyListener(myDigitKeyListener);
				    alert.setView(inputText_numPersone);

				    /* Imposto bottone "Ok" e relativo Listener alla sua pressione */
				    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
				    
				    public void onClick(DialogInterface dialog, int whichButton) {  
				    	
					    	   // recupero il valore dato in input dall'utente
					           int numeroPersone_input = Integer.parseInt(inputText_numPersone.getText().toString());
					           int numeroPostiDisponibili =  myTable.getNumPosti();
					           
					           // Valore booleano per verificare la correttezza dell'input
					           boolean correct_value = false;
							      
					           if((numeroPersone_input > 0) && (numeroPersone_input <= numeroPostiDisponibili)) 
							   	  correct_value = true;
							     
					           /* Se il valore è corretto: aggiorno la GUI, salvo il numeroPersone nell'oggetto del tavolo */
					           if(correct_value) {
										
					        	   		/* Aggiorno l'oggetto myTable */
										myTable.setNumPersone(numeroPersone_input);
										
										/* Invio la richiesta di aggiornamento per occupare il tavolo */
										new OccupaTavoloAsyncTask().execute((Object[])null);
										
									} else {
										/* Mostro messaggio d'errore */
					    	    		Toast.makeText(getApplicationContext(), "Numero Persone inserito non " +
					    	    				"accettabile! Riprovare.", Toast.LENGTH_SHORT).show();
									}
								
								
				           return;                  
				          }  
				        });  

				       alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

				           public void onClick(DialogInterface dialog, int which) {
				               // TODO Auto-generated method stub
				               return;   
				           }
				       });
				      alert.show();
				      
				     
				
			}
		});
		 	  
		/* ****************************************************************
		 * Bottone per la liberazione del tavolo e il passaggio del 
		 * conto allo stato DA PAGARE
		 *****************************************************************/
		Button liberaTavolo = (Button)findViewById(R.id.button_tableCard_liberaTavolo);
		liberaTavolo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View c) {
				new LiberaTavoloAsyncTask().execute((Object[])null);
			}
		});
		
		Button btnPulisci = (Button)findViewById(R.id.button_tableCard_pulisciTavolo);
		
		
		/* ****************************************************************
		 * 	Bottone per pulire il tavolo
		 ******************************************************************/
		
		btnPulisci.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				new PulisciTavoloAsyncTask().execute((Object[])null);
			}
		});
		
		Button button_prendiOrdinazione = (Button) findViewById(R.id.button_tableCard_prendiOrdinazione);
		  
		/* ****************************************************************
		 * 	Bottone per prendere le ordinazioni per il tavolo 
		 ******************************************************************/
		 button_prendiOrdinazione.setOnClickListener(new OnClickListener() {
			  	@Override
			  	public void onClick(View v) {
			  		Intent myIntent = new Intent(TableCardActivity.this, MenuListActivity.class);
			  		Bundle b = new Bundle();
			  		b.putInt("tableId", myTable.getTableId());
			  		b.putString("tableName", myTable.getTableName());
			  		b.putString("cameriere", myTable.getCameriere());
			  		
			  		myIntent.putExtras(b);
			  		TableCardActivity.this.startActivity(myIntent);
			  	}
		  });

	      /* ****************************************************************
		   * Configuro ListView per le Prenotazioni
		   *****************************************************************/
	      
		  this.prenotationListView = (ListView) findViewById(R.id.listView_prenotationList);
		  
		  this.prenotationListView_arrayPrenotazioni = new ArrayList<Prenotazione>();
		  
		  this.prenotationListView_adapter = new PrenotationAdapter(getApplicationContext(), 
					R.layout.cameriere_table_card_prenotation_list_row, 
					prenotationListView_arrayPrenotazioni);
		  
		  this.prenotationListView.setAdapter(this.prenotationListView_adapter);
		  
		  
		  /* ****************************************************************
	       * Listener per il click su un elemento della lista delle 
	       * prenotazioni
	       *****************************************************************/
	      prenotationListView.setOnItemClickListener(new OnItemClickListener() {
		  	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		  	    	 Log.i(TAG, "Hai cliccato su una prenotazione: " + prenotationListView_adapter.getItem(position).getNomeCliente());

		  	    	 /* Apro una nuova finestra con l'opzione di occupare il tavolo 
		  	    	  * secondo la prenotazione */
		  	    	 
		  	    	 // TODO
		  	    } 
		  });
		  
		  /* ****************************************************************
		   * ListView per le ordinazioni confermate che entrano a far parte
		   * del conto
		   ******************************************************************/
		  
		  // Recupero riferimento a conto
		  this.contoListView = (ListView) findViewById(R.id.listView_contoList);
	  
		  this.contoListView_arrayOrdinazioni = new ArrayList<Ordinazione>();
		  
		  this.contoListView_adapter = new ContoAdapter(getApplicationContext(), 
					R.layout.cameriere_table_card_conto_list_row, 
					contoListView_arrayOrdinazioni);
		  
		  this.contoListView.setAdapter(this.contoListView_adapter);
		  
		      
	      /* ****************************************************************
	       * Listener per il click lungo su un elemento della lista delle 
	       * ordinazioni inviate in cucina. Consente di aprire il menu
	       * per modificare o eliminare un'ordinazione.
	       *****************************************************************/
		  
		  contoListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			  @Override
			  public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				 final String[] dialogMenuItems = {"Modifica", "Elimina"};
			  	 
				 /* La posizione cliccata nella list view deve essere final per poter essere acceduta 
			  	  * dal listener della dialog  */
				 final int contoPosition = position;
			  	 
				 /* Creo la finestra di dialogo */
			  	 AlertDialog.Builder builder = new AlertDialog.Builder(TableCardActivity.this);
			  	 builder.setTitle("Gestione Ordinazione");
			  	 
			  	 builder.setItems(dialogMenuItems, new DialogInterface.OnClickListener() {
			  		 public void onClick(DialogInterface dialog, int item_position) {
			  			 if(dialogMenuItems[item_position].equals("Modifica")) {
			  				 
			  				 /* Avvio l'activity di modifica di una comanda. Le modifiche effettuate devono essere 
			  				  * inviate immediatamente al server */
			  				 
			  				Intent myIntent = new Intent(getApplicationContext(), GestioneOrdinazioneActivity.class);
	  	  	  	    		Bundle b = new Bundle();

	  	  	  	    		b.putSerializable("ordinazione", (Ordinazione) contoListView_arrayOrdinazioni.get(contoPosition));     
	  	  	  	    		myIntent.putExtras(b);
	  	  	  	    	
	  	  	  	    		startActivity(myIntent);
			  			 } else if(dialogMenuItems[item_position].equals("Elimina")) {
			  				 
			  				 Object[] eliminaComandaParams =  { 	contoListView_arrayOrdinazioni
																	.get(contoPosition)
																	.getIdRemotoOrdinazione(),
																	
																	contoListView_arrayOrdinazioni
																	.get(contoPosition)
																	.getIdOrdinazione()};
							 
			  				 /* Avvio l'async task di eliminazione della comanda */
			  				 new EliminaComandaAsyncTask().execute(eliminaComandaParams);
			  				 
			  			 }
			  		 }
			  	});
			
			  	AlertDialog alert = builder.create();
	  	    	alert.show(); 
			  	return true;
			 }
		  });

		  /* ****************************************************************
		   * ListView per le ordinazioni in attesa
		   *****************************************************************/
		  
		  this.ordersWaitingListView = (ListView) findViewById(R.id.listView_comandeSospeseList);
	  
		  this.ordersWaitingListView_arrayOrdinazioni = new ArrayList<Ordinazione>();
		  
		  this.ordersWaitingListView_adapter = new OrdersWaitingAdapter(getApplicationContext(), 
					R.layout.cameriere_table_card_ordinazioni_in_sospeso_list_row, 
					ordersWaitingListView_arrayOrdinazioni);
		  
		  this.ordersWaitingListView.setAdapter(this.ordersWaitingListView_adapter);
		  
		  /* ****************************************************************
	       * Listener per il click su un'ordinazione in sospeso (passaggio
	       * di stato da selezionata a deselezionata)
	       *****************************************************************/
		  
		  ordersWaitingListView.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					 Log.i(TAG, "Hai cliccato (longClick) su un'ordinazione in sospeso: " + ordersWaitingListView_adapter.getItem(position).getNome());
			  	    	
			  	    	final String[] dialogMenuItems = {"", "", ""};
			  	    	
			  	    	/* Elementi del menu della finestra di dialogo */
			  	    	/* Se la voce è già selezionata */
			  	    	 if(ordersWaitingListView_adapter.getItem(position).getStato().equals("Selezionata")) {
			  	    		 dialogMenuItems[0] = "Deseleziona"; 
			  	    		 dialogMenuItems[1] = "Modifica"; 
			  	    		 dialogMenuItems[2] = "Rimuovi"; 
			  	    	/* Se la voce non è ancora selezionata */
			  	    	 } else if(ordersWaitingListView_adapter.getItem(position).getStato().equals("Deselezionata")) {
			  	    		 dialogMenuItems[0] = "Seleziona"; 
			  	    		 dialogMenuItems[1] = "Modifica"; 
			  	    		 dialogMenuItems[2] = "Rimuovi"; 
			  	    	 }
			  	    	 
			  	    	 /* Creo la finestra di dialogo */
			  	    	AlertDialog.Builder builder = new AlertDialog.Builder(TableCardActivity.this);
			  	    	builder.setTitle("Gestione Ordinazione");
			  	    	
			  	    	/* Salvo in una variabile final la posizione in cui ho cliccato */
			  	    	final int positionClicked = position;
			  	    	
			  	    	builder.setItems(dialogMenuItems, new DialogInterface.OnClickListener() {
			  	    	    public void onClick(DialogInterface dialog, int item_position) {
			  	    	    	
			  	    	    	/* ******************************************
			  	    	    	 * OPERAZIONI A MENU APERTO 
			  	    	    	 * ****************************************** */
			  	    	    	/* Distinguo le operazioni da farsi a seconda del menu che viene aperto */
			  	    	    	if(dialogMenuItems[item_position].equals("Seleziona")) {
			  	    	    		
			  	    	    		/* Seleziono la casella */
			  	    	    		ordersWaitingListView_adapter.getItem(positionClicked).setStato("Selezionata");
			  	    	    		/* Aggiorno la ListView */
		  			  	    		ordersWaitingListView_adapter.notifyDataSetChanged();
			  	    	    		
			  	    	    		Log.w(TAG, dialogMenuItems[item_position]);
			  	    	    		
			  	    	    	} else if (dialogMenuItems[item_position].equals("Deseleziona")) {
			  	    	    		
			  	    	    		Log.w(TAG, dialogMenuItems[item_position]);
			  	    	    		
			  	    	    		/* Deseleziono la casella */
			  	    	    		ordersWaitingListView_adapter.getItem(positionClicked).setStato("Deselezionata");
			  	    	    		/* Aggiorno la ListView */
		  	    	    			ordersWaitingListView_adapter.notifyDataSetChanged();
			  	    	    		
			  	    	    	} else if (dialogMenuItems[item_position].equals("Modifica")) {
			  	    	    		Log.w(TAG, dialogMenuItems[item_position]);
			  	    	    		
			  	    	    		/* *******************************************************
			  	    	    		 * Avvio l'activity di modifica dell'ordinazione sospesa
			  	    	    		 ********************************************************/
			  	    	    		
			  	    	    		Intent myIntent = new Intent(getApplicationContext(), GestioneOrdinazioneActivity.class);
			  	  	  	    		Bundle b = new Bundle();
			  	  	  	    		b.putSerializable("ordinazione", (Ordinazione) ordersWaitingListView_arrayOrdinazioni.get(positionClicked));     
			  	  	  	    		myIntent.putExtras(b);
			  	  	  	    	
			  	  	  	    		startActivity(myIntent);
			  	  		  	     
			  	    	    	} else if (dialogMenuItems[item_position].equals("Rimuovi")) {
			  	    	    		Log.w(TAG, dialogMenuItems[item_position]);
			  	    	    		
			  	    	    		/* *******************************************************
			  	    	    		 * Rimuovo la comanda selezionata 
			  	    	    		 ********************************************************/
			  	    	    		DbManager dbManager = new DbManager(getApplicationContext());
			  	    	    		SQLiteDatabase db;
			  	    	    		
			  	    	    		db = dbManager.getWritableDatabase();
			  	    	    		db.delete("comanda", "idComanda=" + ordersWaitingListView_arrayOrdinazioni.get(positionClicked).getIdOrdinazione(), null);
			  	    	    		db.close();
			  	    	    		dbManager.close();
			  	    	    		  	    	    		
			  	    	    		/* *******************************************************
			  	    	    		 * Aggiorno gli ordini da confermare nella listview
			  	    	    		 ********************************************************/
			  	    	    		getOrdersWaitingToBeConfirmed();
			  	    	    		Toast.makeText(getApplicationContext(), dialogMenuItems[item_position], Toast.LENGTH_SHORT).show();
			  	    	    	}
			  	    	    }
			  	    	}); 
			  	    	
			  	    	AlertDialog alert = builder.create();
			  	    	alert.show();
						return false;
				}
		  });
		  
		  /* **************************************************************** 
		   * Gestisco lo Short Click sull'elemento della lista degli ordini 
		   * in sospeso. Seleziono e deseleziono l'ordine sospeso da inviare  
		   ******************************************************************/
		  
		  ordersWaitingListView.setOnItemClickListener(new OnItemClickListener() {
		  	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		  	    	
		  	    	 Log.i(TAG, "Hai cliccato (shortClick) su un'ordinazione in sospeso: " + ordersWaitingListView_adapter.getItem(position).getNome());
		  	    	
		  	    	if(ordersWaitingListView_adapter.getItem(position).getStato().equals("Deselezionata")) {
		  	    		ordersWaitingListView_adapter.getItem(position).setStato("Selezionata");
	    	    		ordersWaitingListView_adapter.notifyDataSetChanged();
		  	    	} else if(ordersWaitingListView_adapter.getItem(position).getStato().equals("Selezionata")) {
		  	    		ordersWaitingListView_adapter.getItem(position).setStato("Deselezionata");
  	    	    		ordersWaitingListView_adapter.notifyDataSetChanged();
		  	    	}
		  	    } 
	      }); 
		  
		  /* ****************************************************************
		   * Bottone per la conferma delle ordinazioni in sospeso
		   *****************************************************************/
		  Button buttonInviaOrdinazioni = (Button)findViewById(R.id.button_tableCard_inviaGruppoOrdinazioni);
		  buttonInviaOrdinazioni.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ordersWaitingListView_arrayOrdinazioni.size() == 0)
					return;
				else 
					new InviaOrdinazioniAsyncTask().execute((Object[])null);
				
				
			}
		});
		  
		 /* **************************************************************** 
		  * Sincronizzo il conto con il server se la flag è true
		  ******************************************************************/
		  if(TableCardActivity.updateConto)
			  new GetContoAsyncTask().execute((Object[]) null);
		  
		  TableCardActivity.updateConto = false;
		  
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d("TableCardActivity","OnResume");
		
		new TableCardAsyncTask().execute((Object[])null);
		
		/* **************************************************************** 
		  * Sincronizzo il conto con il server se la flag è true
		  ******************************************************************/
		  if(TableCardActivity.updateConto)
			  new GetContoAsyncTask().execute((Object[]) null);
		  
		  TableCardActivity.updateConto = false;
		  
		/* Aggiorno la lista delle ordinazioni sospese e delle ordinazioni confermate */
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				getOrdersConfirmed();
				getOrdersWaitingToBeConfirmed();
				
			}
		});
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d("TableCardActivity","OnStart");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d("TableCardActivity","OnStop");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		TableCardActivity.updateConto = true;
		Log.d("TableCardActivity","OnDestroy");
	}
    
    /************************************************************************
	 * Adapter per gestire la lista delle ordinazioni che sono state
	 * inviate in cucina e che fanno parte del conto
	 * @author Guerri Marco
	 ************************************************************************/
    
    private class ContoAdapter extends ArrayAdapter<Ordinazione> {

        private ArrayList<Ordinazione> items;

        public ContoAdapter(Context context, int textViewResourceId, ArrayList<Ordinazione> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        		
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.cameriere_table_card_conto_list_row, null);
                }
                
                Ordinazione o = items.get(position);
                
                if (o != null) {
                        
                		/* Aggiorno i campi in base a quel che ho letto */
                		TextView textView_nomeOrdinazione = (TextView) v.findViewById(R.id.textView_contoList_nomeOrdinazione);
                		TextView textView_quantita = (TextView) v.findViewById(R.id.textView_contoList_quantity);
                		TextView textView_stato = (TextView) v.findViewById(R.id.textView_contoList_stato);
                		if(textView_nomeOrdinazione != null) {
                			textView_nomeOrdinazione.setText(o.getNome());                            
                        }
                		if(textView_quantita != null){
                			textView_quantita.setText(Integer.toString(o.getQuantita()));
                        }
                		if(textView_stato != null) {
                			textView_stato.setText(o.getStato());
                        }
                }
                return v;
          }
    }
    
    /************************************************************************
     * Metodo per reperire l'elenco delle ordinazioni che sono
     * in attesa di essere confermate dal cameriere, e che sono
     * salvate solo locamente
     * @author Fabio Pierazzi
     ************************************************************************/
    private void getOrdersWaitingToBeConfirmed(){
    	
    	Log.i(TAG, "Entrato in getOrdersWaitingToBeConfirmed();");
          try{
        	  
        	  ordersWaitingListView_arrayOrdinazioni.clear();
        	  
        	  DbManager dbManager = new DbManager(getApplicationContext());
        	  SQLiteDatabase db;
        	  db = dbManager.getWritableDatabase();
        	  
        	  Cursor cursorOrdinazioniSospese;
        	  
        	  cursorOrdinazioniSospese = db.query("comanda", 
											new String[] {"idComanda", "idVoceMenu", "quantita", "note"} , 
											"idTavolo=" + myTable.getTableId() + " and stato=\"SOSPESA\"", 
											null, null, null, null, null);
        	  
        	  cursorOrdinazioniSospese.moveToFirst();
        	  
        	  while(!cursorOrdinazioniSospese.isAfterLast()) {
        		  
        		  /* Recupero il nome della voce di menu relativa all'id
        		   * apperna acquisito dal database */
        		  
        		  Cursor cursorNomeVoceMenu;
        		  
        		  cursorNomeVoceMenu = db.query("vocemenu", new String[] {"nome"}, "idVoceMenu=" + cursorOrdinazioniSospese.getInt(1),
        				  						null, null, null, null, null);
        		  
        		  cursorNomeVoceMenu.moveToFirst();
        		  
        		  Ordinazione o = new Ordinazione();
        		  
        		  o.setIdOrdinazione(cursorOrdinazioniSospese.getInt(0));
        		  o.setIdVoceMenu(cursorOrdinazioniSospese.getInt(1));
        		  o.setIdTavolo(myTable.getTableId());
        		  o.setNome(cursorNomeVoceMenu.getString(0));
        		  o.setQuantita(cursorOrdinazioniSospese.getInt(2));
        		  o.setNote(cursorOrdinazioniSospese.getString(3));
        		  o.setStato("Selezionata");
        		  
        		  cursorOrdinazioniSospese.moveToNext();
        		  
        		  cursorNomeVoceMenu.close();
        		  ordersWaitingListView_arrayOrdinazioni.add(o);
           	  }
        	  
        	  cursorOrdinazioniSospese.close();
        	  db.close();
        	  dbManager.close();
        	  
        	  /* *************************************************************************
        	   * Aggiornamento dell'interfaccia grafica. Solo l'UI thread può modificare
        	   * la view.
        	   **************************************************************************/
        	  runOnUiThread(new Runnable() {
        		  public void run() {
        	
        			  ordersWaitingListView_adapter.notifyDataSetChanged();
        			         			  
            		  /* Modifico manualmente la nuova altezza della listView */
                      Utility.setListViewHeightBasedOnChildren(ordersWaitingListView);
        		  }
        	  });
       
          } catch (Exception e) {
        	Log.e("TableCardActivity", e.toString());
        }
          
    }
    
    /* ***********************************************************************
     * Metodo per reperire l'elenco delle ordinazioni che sono state inviate
     * in cucina (direttamente da database).
     * @author Guerri Marco
     ************************************************************************/
    private void getOrdersConfirmed(){
    	
    	Log.i(TAG, "getOrdersConfirmed()");
          try{
        	  
        	  contoListView_arrayOrdinazioni.clear();
        	  
        	  DbManager dbManager = new DbManager(getApplicationContext());
        	  SQLiteDatabase db;
        	  db = dbManager.getWritableDatabase();
        	  
        	  Cursor cursorOrdinazioniInviate;
        	  
        	  cursorOrdinazioniInviate = db.query("comanda", 
											new String[] {"idComanda", "idVoceMenu", "quantita", "note", "idRemotoComanda", "stato"} , 
											"idTavolo=" + myTable.getTableId() + " and stato IS NOT 'SOSPESA'", 
											null, null, null, null, null);
        	  
        	  cursorOrdinazioniInviate.moveToFirst();
        	  
        	  while(!cursorOrdinazioniInviate.isAfterLast()) {
        		  
        		 
        		  /* Recupero il nome della voce di menu relativa all'id apperna acquisito dal database */
        		  
        		  Cursor cursorNomeVoceMenu;
        		  cursorNomeVoceMenu = db.query("vocemenu", new String[] {"nome"}, "idVoceMenu=" + cursorOrdinazioniInviate.getInt(1),
        				  						null, null, null, null, null);
        		  
        		  cursorNomeVoceMenu.moveToFirst();
        		  
        		  Ordinazione o = new Ordinazione();
        		  
        		  o.setIdOrdinazione(cursorOrdinazioniInviate.getInt(0));
        		  o.setIdVoceMenu(cursorOrdinazioniInviate.getInt(1));
        		  o.setIdTavolo(myTable.getTableId());
        		  o.setNome(cursorNomeVoceMenu.getString(0));
        		  o.setQuantita(cursorOrdinazioniInviate.getInt(2));
        		  o.setNote(cursorOrdinazioniInviate.getString(3));
        		  o.setIdRemotoOrdinazione(cursorOrdinazioniInviate.getInt(4));
        		  o.setStato(cursorOrdinazioniInviate.getString(5));
        		  
        		  cursorOrdinazioniInviate.moveToNext();
        		  
        		  cursorNomeVoceMenu.close();
        		  contoListView_arrayOrdinazioni.add(o);
           	  }
        	  
        	  cursorOrdinazioniInviate.close();
        	  db.close();
        	  dbManager.close();
        	  
        	  /* *************************************************************************
        	   * Aggiornamento dell'interfaccia grafica. Solo l'UI thread può modificare
        	   * la view.
        	   **************************************************************************/
        	  runOnUiThread(new Runnable() {
        		  public void run() {
        			
        			  contoListView_adapter.notifyDataSetChanged();

        		      Utility.setListViewHeightBasedOnChildren(contoListView);
        		     
        		  }
        	  });
              
        } catch (Exception e) {
        	Log.e("TableCardActivity", e.toString());
        }
     
    }
    
    
    /* ***********************************************************************
	 * Adapter per gestire la lista delle ordinaizoni che devono essere
	 * confermate dal cameriere prima di essere inviate in cucina.
	 ************************************************************************/
    
    private class OrdersWaitingAdapter extends ArrayAdapter<Ordinazione> {

        private ArrayList<Ordinazione> items;

        public OrdersWaitingAdapter(Context context, int textViewResourceId, ArrayList<Ordinazione> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        		
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.cameriere_table_card_ordinazioni_in_sospeso_list_row, null);
                }
                
                Ordinazione o = items.get(position);
                
                if (o != null) {
                        
                		/* Aggiorno i campi in base a quel che ho letto */
                		TextView textView_nomeOrdinazione = (TextView) v.findViewById(R.id.textView_ordinazioniInSopesoList_nomeOrdinazione);
                		TextView textView_quantita = (TextView) v.findViewById(R.id.textView_ordinazioniInSopesoList_quantity);
                		TextView textView_stato = (TextView) v.findViewById(R.id.textView_ordinazioniInSopesoList_stato);
                		
                		if(textView_nomeOrdinazione != null) {
                			textView_nomeOrdinazione.setText(o.getNome());                            
                        }
                		
                		if(textView_quantita != null){
                			textView_quantita.setText(Integer.toString(o.getQuantita()));
                        }
                		
                		if(textView_stato != null) {
                			textView_stato.setTextColor(Color.BLACK);
                			textView_stato.setText(o.getStato());
                        }
                		
                		if(textView_stato.getText().equals("Selezionata")) {
                			// Metto a sfondo bianco le caselle stato
                			textView_stato.setBackgroundColor(Color.GREEN);
                		} else if (textView_stato.getText().equals("Deselezionata")) {
                			textView_stato.setBackgroundColor(Color.RED);
                		}
                }
                return v;
        }
    }

    /* ***********************************************************************
     * Adapter le prenotazioni. 
     * @author Fabio Pierazzi
     ************************************************************************/
    private class PrenotationAdapter extends ArrayAdapter<Prenotazione> {

        private ArrayList<Prenotazione> items;

        public PrenotationAdapter(Context context, int textViewResourceId, ArrayList<Prenotazione> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	
        		/* Eseguo un inflate del Layout */
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.cameriere_table_card_prenotation_list_row, null);
                }
                
                /* Recupero l'oggetto Prenotazione e utilizzo i suoi campi per
                 * scriverli all'interno della lista delle prenotazioni */
                Prenotazione prenotazione = items.get(position);
                
                if (prenotazione != null) {
                	
                		TextView textView_dataPrenotazione = (TextView) v.findViewById(R.id.textView_tableCard_prenotationList_data);
                		TextView textView_numPersone = (TextView) v.findViewById(R.id.textView_tableCard_prenotationList_numPersone);
                		TextView textView_nomeCliente = (TextView) v.findViewById(R.id.textView_tableCard_prenotationList_nomeCliente);
                		
                		if(textView_dataPrenotazione != null) {
                			textView_dataPrenotazione.setText(prenotazione.getTimeAndDate());                            
                        }
                		
                		if(textView_numPersone != null){
                			textView_numPersone.setText(Integer.toString(prenotazione.getNumPersone()));
                        }

                		if(textView_nomeCliente != null) {
                			textView_nomeCliente.setText(prenotazione.getNomeCliente());
                        }
                		
                }

                return v;
        }
    }
  
    /* ***********************************************************************
     * Aggiorna le textbox con le informazioni sul tavolo
     * @author Guerri Marco
     *************************************************************************/
    public void updateStatoTavolo() {
    	
 
		TextView textView_tableName = (TextView) findViewById(R.id.tableCard_textView_Title);
		textView_tableName.setText("Scheda " + myTable.getTableName());

		TextView nomeCameriere= (TextView)findViewById(R.id.textNomeCameriere);
		nomeCameriere.setText(myTable.getCameriere());
		
		TextView nomeArea = (TextView)findViewById(R.id.textNomeArea);
		nomeArea.setText(myTable.getArea());
		
		TextView stato = (TextView)findViewById(R.id.textStato);
		stato.setText(myTable.getTableStatus());
		
		TextView numposti = (TextView) findViewById(R.id.textNumeroPosti);
		numposti.setText(Integer.toString(myTable.getNumPosti()));
		
		TextView numpersone = (TextView) findViewById(R.id.textNumeroPersone);
		numpersone.setText(Integer.toString(myTable.getNumPersone()));
		
		TextView numeroPiano = (TextView)findViewById(R.id.textNumeroPiano);
		numeroPiano.setText(myTable.getPiano());

		/* Visibilità dei pulsanti */
		
		if(	myTable.getTableStatus().equals("OCCUPATO")) {
			
			Button btnOccupa = (Button)findViewById(R.id.button_tableCard_occupaTavolo);
			btnOccupa.setEnabled(false);
			Button btnPulisci = (Button)findViewById(R.id.button_tableCard_pulisciTavolo);
			btnPulisci.setVisibility(0);;
			Button btnOrdina = (Button)findViewById(R.id.button_tableCard_prendiOrdinazione);
			btnOrdina.setEnabled(true);
			//Button btnCedi= (Button)findViewById(R.id.button_tableCard_cediTavolo);
			//btnCedi.setEnabled(true);
			Button btnLibera = (Button)findViewById(R.id.button_tableCard_liberaTavolo);
			btnLibera.setEnabled(true);
			
		} else if(myTable.getTableStatus().equals("PULIRE")) {
		
			Button btnOccupa = (Button)findViewById(R.id.button_tableCard_occupaTavolo);
			btnOccupa.setEnabled(true);
			Button btnPulisci = (Button)findViewById(R.id.button_tableCard_pulisciTavolo);
			btnPulisci.setEnabled(true);
			Button btnOrdina = (Button)findViewById(R.id.button_tableCard_prendiOrdinazione);
			btnOrdina.setEnabled(false);
			//Button btnCedi= (Button)findViewById(R.id.button_tableCard_cediTavolo);
			//btnCedi.setEnabled(false);
			Button btnLibera = (Button)findViewById(R.id.button_tableCard_liberaTavolo);
			btnLibera.setEnabled(false);
			
		} else {
			Button btnOccupa = (Button)findViewById(R.id.button_tableCard_occupaTavolo);
			btnOccupa.setEnabled(true);
			Button btnPulisci = (Button)findViewById(R.id.button_tableCard_pulisciTavolo);
			btnPulisci.setEnabled(false); 
			Button btnOrdina = (Button)findViewById(R.id.button_tableCard_prendiOrdinazione);
			btnOrdina.setEnabled(false);
			//Button btnCedi= (Button)findViewById(R.id.button_tableCard_cediTavolo);
			//btnCedi.setEnabled(false);
			Button btnLibera = (Button)findViewById(R.id.button_tableCard_liberaTavolo);
			btnLibera.setEnabled(false);
		}
	}
    
    /************************************************************************
     * Async Task per l'invio delle comanda in cucina 
     * @author Guerri Marco
     ***********************************************************************/
    
    class InviaOrdinazioniAsyncTask extends AsyncTask<Object, Object, Error> {

    	private ProgressDialog progressDialog;
    	   	
    	@Override
    	protected void onPreExecute() {
    		progressDialog = ProgressDialog.show(TableCardActivity.this, "Attendere", "Invio comande in cucina");
    	}
    	
    	@Override
		protected Error doInBackground(Object... params) {
			
    		DbManager dbManager = new DbManager(getApplicationContext());
			SQLiteDatabase db = dbManager.getWritableDatabase();
			RestaurantApplication restApp = ((RestaurantApplication)getApplication());
    	
			
			try {
				
				/****************************************************************
				 * Costruisco l'oggetto JSON che rappresenta tutte le ordinazioni
				 * con tutte le voci di menu e le variazioni associate.
				 ****************************************************************/
			
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("idTavolo", myTable.getTableId());
				
				JSONArray jsonArrayOrdinazioni = new JSONArray();
				
				for(Ordinazione o: ordersWaitingListView_arrayOrdinazioni) {
					
					if(o.getStato().equals("Deselezionata"))
						continue;
				
					
					/* ***********************************************************
					 * Creazione dell'oggetto JSON che rappresenta la comanda
					 * con tutte le variazioni associate
					 ************************************************************/
					
					JSONObject jsonObjectOrdinazione = new JSONObject();
					
					jsonObjectOrdinazione.put("idLocale", o.getIdOrdinazione());
					jsonObjectOrdinazione.put("idVoceMenu", o.getIdVoceMenu());
					jsonObjectOrdinazione.put("quantita", o.getQuantita());
					jsonObjectOrdinazione.put("note", o.getNote());
					
					JSONArray jsonArrayVariazioni = new JSONArray();
				
					Cursor cursorVariazioni = db.query(	"variazionecomanda", 
														new String[] {"idVariazione"}, 
														"idComanda="+o.getIdOrdinazione(), 
														null, null, null, null);
					
					cursorVariazioni.moveToFirst();
					
					while(!cursorVariazioni.isAfterLast()) {
						JSONObject jsonObjectVariazione = new JSONObject();
						jsonObjectVariazione.put("idVariazione", cursorVariazioni.getInt(0));
						jsonArrayVariazioni.put(jsonObjectVariazione);
						cursorVariazioni.moveToNext();
					}
					
					cursorVariazioni.close();
					
					jsonObjectOrdinazione.put("variazioni", jsonArrayVariazioni);
					jsonArrayOrdinazioni.put(jsonObjectOrdinazione);
				}
				
				jsonObject.put("comande", jsonArrayOrdinazioni);
				
				/* Il valore di action viene inviato tramite query string perchè il corpo della richiesta
				 * è completamente dedicato al JSONObject che codifica le comande */
				 
				String response = restApp.makeHttpJsonPostRequest(	restApp.getHost() +
																	"ClientEJB/gestioneComande?action=INSERISCI_COMANDE", 
																	jsonObject);
			
				
				JSONObject responseJsonObject = new JSONObject(response);
									
				if(responseJsonObject.getBoolean("success") == false) {
					
					/* **********************************************************
					 * Se l'inserimento delle voci di menu all'interno del
					 * databse sul server fallisce viene ritornato success:false.
					 * La logica di business garantisce che se anche solo 1
					 * inserimento non va a buon fine, viene fatto il 
					 * rollback della transazione. A questo punto deve essere
					 * notificato al cameriere l'errore. Se questo deriva dalla
					 * mancanza di sincronizzazione tra il menu locale e il 
					 * menu remoto (ad esempio perchè una voce di menu non
					 * esiste più), sul server viene generata un'eccezione
					 * che causa il rollback della transazione. Bisogna
					 * notificare all'utente da quale comanda deriva l'errore
					 ***********************************************************/
					
					return new Error(responseJsonObject.getString("message"), true);
					
				} else if(responseJsonObject.getBoolean("success") == true) {
					
					
					/* ***********************************************************
					 * L'inserimento delle comande è andato a buon fine. Cambio
					 * lo stato di ciascuna comanda all'interno del database
					 * locale.
					 ************************************************************/
					for(Ordinazione o: ordersWaitingListView_arrayOrdinazioni) {
						ContentValues ordinazioneInviata = new ContentValues();
						ordinazioneInviata.put("stato","INVIATA");
						db.update(	"comanda", 
									ordinazioneInviata, "idComanda=" + o.getIdOrdinazione(), 
									null);
					}
					
					/* ****************************************************************
					 * Le comande sono state inviate al server e in risposta vengono
					 * ritornati gli id remoti per future modifiche. Il database locale
					 * viene aggiornato con gli id remoti
					 ******************************************************************/
					
					JSONArray jsonArrayIdComande = responseJsonObject.getJSONArray("idComande");
					
					for(int i=0; i<jsonArrayIdComande.length(); i++) {
						
						ContentValues comandaIdGlobale = new ContentValues();
						comandaIdGlobale.put("idRemotoComanda", jsonArrayIdComande.getJSONObject(i).getInt("id"));
						db.update(	"comanda", 
									comandaIdGlobale, 
									"idComanda=" + jsonArrayOrdinazioni.getJSONObject(i).getInt("idLocale"), 
									null);
					}
					
					/* Aggiorno la lista delle ordinazioni sospese e delle ordinazioni confermate */
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							getOrdersWaitingToBeConfirmed();
							getOrdersConfirmed();
						}
					});
				}
				
			} catch (Exception e) {
				return new Error("Errore durante la conferma delle comande", true);
			} finally {
				progressDialog.dismiss();
				db.close();
				dbManager.close();
			}
			
			return new Error("Comande inviate", false);
    	}
    	
    	@Override
    	protected void onPostExecute(Error error) {
     	   progressDialog.dismiss();
     	   if(error.errorOccurred()) 
     		   Toast.makeText(getApplicationContext(), error.getError(), 20).show();
     	 }
    }
    
    
    /************************************************************************
     * Async Task per l'aggiornamento delle informazioni della scheda 
     * del tavolo
     * @author Guerri Marco
     *************************************************************************/
    class TableCardAsyncTask extends AsyncTask<Object, Object, Error> {

    	private ProgressDialog progressDialog;
    	   	
    	@Override
    	protected void onPreExecute() {
    		progressDialog = ProgressDialog.show(TableCardActivity.this, "Attendere", "Caricamento tavolo");
    	}
    	
    	@Override
		protected Error doInBackground(Object... params) {
			
			RestaurantApplication restApp = (RestaurantApplication)getApplication();
			String response = "";
					
			try {
				response = restApp.makeHttpGetRequest(restApp.getHost() + "ClientEJB/statoTavolo", new HashMap<String,String>());
			} catch (ClientProtocolException e) {
				
			} catch (IOException e) {
				return new Error("Richiesta effettuata correttamente", false );
			}
			
			Log.d("TableListAsyncTask" , response);
			
			/*****************************************************************
			 * Aggiornamento dell'interfaccia grafica dello stato del tavolo
			 *****************************************************************/
			try {
				
				JSONObject jsonObject = new JSONObject(response);
			
						
				if(jsonObject.getString("success").equals("true")) {
				
					JSONArray jsonArray = jsonObject.getJSONArray("statoTavolo");
					
					int i=0;
					while(jsonArray.getJSONObject(i).getInt("idTavolo") != myTable.getTableId())
						i++;
					
					if(i == jsonArray.length()) {
						/* Non sono stati trovati tavoli che fanno match con l'id */
						return new Error("Impossibile trovare il tavolo richiesto", true);
					}
					myTable.setArea(jsonArray.getJSONObject(i).getString("nomeArea"));
					myTable.setTableName(jsonArray.getJSONObject(i).getString("nomeTavolo"));
					myTable.setTableStatus(jsonArray.getJSONObject(i).getString("statoTavolo"));
					myTable.setPiano(jsonArray.getJSONObject(i).getString("numeroPiano"));
					myTable.setCameriere(jsonArray.getJSONObject(i).getString("cameriere"));
					runOnUiThread(new Runnable() {
						public void run() {
							updateStatoTavolo();
							getOrdersWaitingToBeConfirmed();
						}
					});
								
					return new Error("Richiesta effettuata correttamente", false );
			
				} else {
					return new Error("Errore durante l'aggiornamento del tavolo", true);
				}
				
			} catch (JSONException e) {
				
				return new Error("Errore durante l'aggiornamento del tavolo (" + e.toString() + ")", true);
			}
		}
    	
    	@Override
    	protected void onPostExecute(Error error) {
     	   progressDialog.dismiss();
     	   if(error.errorOccurred()) 
     		   Toast.makeText(getApplicationContext(), error.getError(), 20).show();
     	 }
    }
   
    /************************************************************************
    * Async Task per l'occupazione del tavolo e l'apertura del conto
    * @author Guerri Marco
    *************************************************************************/
   class OccupaTavoloAsyncTask extends AsyncTask<Object, Object, Error> {

	   	@Override
   		protected void onPreExecute() {
	   	}
   	
	   	@Override
		protected Error doInBackground(Object... params) {
				
	   		RestaurantApplication restApp = (RestaurantApplication)getApplication();
	   		HashMap<String,String> requestParameters = new HashMap<String,String>();
	   		requestParameters.put("action","OCCUPA_TAVOLO");
	   		requestParameters.put("idTavolo", new Integer(myTable.getTableId()).toString());
	   		requestParameters.put("numeroPersone", new Integer(myTable.getNumPersone()).toString());
		  
	   		try {
	   			String response = restApp.makeHttpPostRequest(	restApp.getHost() + "ClientEJB/gestioneComande", 
																requestParameters);
	   			Log.d("OccupaTavoloAsyncTask", response);
	   			JSONObject jsonObject = new JSONObject(response);
	   			
	   			if(jsonObject.getString("success").equals("true")) {
					
	   				myTable.setTableStatus("OCCUPATO");
					myTable.setCameriere(jsonObject.getString("cameriere"));
					
				} else {
					return new Error(jsonObject.getString("message"),true);
	   			}
	   			
	   		} catch (ClientProtocolException e) {
	   			return new Error("Errore durante la comunicazione con il server",true);
	   		} catch (IOException e) {
	   			return new Error("Errore durante la comunicazione con il server",true);
	   		} catch (JSONException e) {
	   			return new Error("Errore durante la lettura della risposta dal server",true);
	   		} finally {
	   			runOnUiThread(new Runnable() {
	   				@Override
	   				public void run() {
	   					updateStatoTavolo();
	   				}
	   			});
	   		}
	   		return new Error("",false);
	   	}
   	
	   	@Override
	   	protected void onPostExecute(Error error) {
	    	  if(error.errorOccurred()) 
	    		   Toast.makeText(getApplicationContext(), error.getError(), 20).show();
	    }
   }
   
   
   /************************************************************************
    * Async Task per liberare il tavolo e settare lo stato del conto su
    * DAPAGARE
    * @author Guerri Marco
    ************************************************************************/
   class LiberaTavoloAsyncTask extends AsyncTask<Object, Object, Error> {
	   	@Override
   		protected void onPreExecute() {
	   	}
   	
	   	@Override
		protected Error doInBackground(Object... params) {
				
	   		RestaurantApplication restApp = (RestaurantApplication)getApplication();
			HashMap<String,String> requestParameters = new HashMap<String,String>();
			requestParameters.put("action","LIBERA_TAVOLO");
			requestParameters.put("idTavolo", new Integer(myTable.getTableId()).toString());
			  
			try {
				String response = restApp.makeHttpPostRequest(	restApp.getHost() + "ClientEJB/gestioneComande", 
																requestParameters);
				Log.d("TableCardActivity", response);
				
				JSONObject jsonObject = new JSONObject(response);
				if(jsonObject.getString("success").equals("true")) {
					myTable.setTableStatus("PULIRE");
					myTable.setCameriere("Non definito");
					myTable.setNumPersone(0);
					
					/* Svuoto la list view delle ordinazioni inviate. Tutte le 
					 * ordinazioni entrano a far parte di un conto non attivo */
					contoListView_arrayOrdinazioni.clear();
					
					
				} else {
					return new Error(jsonObject.getString("message"),true);
				}
			} catch (ClientProtocolException e) {
			  return new Error("Errore durante la comunicazione con il server",true);
			} catch (IOException e) {
			  return new Error("Errore durante la comunicazione con il server",true);
			} catch (JSONException e) {
			  return new Error("Errore durante la lettura della risposta dal server",true);
			} finally {
				runOnUiThread(new Runnable() {
	   				@Override
	   				public void run() {
	   					contoListView_adapter.notifyDataSetChanged();
	   					Utility.setListViewHeightBasedOnChildren(contoListView);
	   					updateStatoTavolo();
	   				}
	   			});
			}
			return new Error("",false);
	   	}
	
	   	@Override
	   	protected void onPostExecute(Error error) {
	    	  if(error.errorOccurred()) 
	    		   Toast.makeText(getApplicationContext(), error.getError(), 40).show();
	    }
   }  
   
   
   /************************************************************************
    * Async Task per l'eliminazione di una comanda che è già stata inviata 
    * in cucina
    * @author Guerri Marco
    ************************************************************************/
   class EliminaComandaAsyncTask extends AsyncTask<Object, Object, Error> {
	   	@Override
  		protected void onPreExecute() {
	   	}
  	
	   	@Override
		protected Error doInBackground(Object... params) {
			/*	
			 * Parametri passati all'Async Task
			 * params[0]: idRemotoComanda
			 * params[1]: idComanda
			 */
	   		
	   		RestaurantApplication restApp = (RestaurantApplication)getApplication();
			HashMap<String,String> requestParameters = new HashMap<String,String>();
			requestParameters.put("action","ELIMINA_COMANDA");
			requestParameters.put("idRemotoComanda", ((Integer)params[0]).toString());
			  
			try {
				String response = restApp.makeHttpPostRequest(	restApp.getHost() + "ClientEJB/gestioneComande", 
																requestParameters);
				
				
				JSONObject jsonObject = new JSONObject(response);
				
				if(jsonObject.getString("success").equals("true"))  {
					
					/* Elimino la comanda dal database locale e l'associazione con le variazioni  */
					DbManager dbManager = new DbManager(getApplicationContext());
					SQLiteDatabase db = dbManager.getWritableDatabase();
					
					db.delete("comanda", "idComanda=" + ((Integer)params[1]).toString(),null);
					db.delete("variazionecomanda", "idComanda=" +  ((Integer)params[1]).toString(), null);
					db.close();
					
					
					dbManager.close();
					
					/* Aggiorno la list view del conto ricaricando gli ordini dal database */
					runOnUiThread(new Runnable() {
		   				@Override
		   				public void run() {
		   					getOrdersConfirmed();
		   				}
		   			});
					
					
					return new Error("Comanda eliminata",false);
				
				} else {
					
					/* Forzo la sincronizzazione del conto vista l'alta probabilità che
					 * l'errore sia dato dalla mancanza di sincronia sugli stati */
					TableCardActivity.updateConto = true;
					return new Error(jsonObject.getString("message"),true);
				}
				
			} catch (ClientProtocolException e) {
			  return new Error("Errore durante la comunicazione con il server (" + e.toString() + ")",true);
			} catch (IOException e) {
			  return new Error("Errore durante la comunicazione con il server (" + e.toString() + ")",true);
			} catch (JSONException e) {
			  return new Error("Errore durante la lettura della risposta dal server (" + e.toString() +")",true);
			}
		}
	
	   	@Override
	   	protected void onPostExecute(Error error) {
	    	  if(error.errorOccurred()) 
	    		   Toast.makeText(getApplicationContext(), error.getError(), 40).show();
	    }
  }  
   /************************************************************************
    * Async task che richiede al server di settare lo stato di un tavolo
    * da "PULIRE" a "LIBERO"
    * @author Guerri Marco
    ************************************************************************/
   
   class PulisciTavoloAsyncTask extends AsyncTask<Object, Object, Error> {

	   	@Override
  		protected void onPreExecute() {
	   	}
  	
	   	@Override
		protected Error doInBackground(Object... params) {
	   	
	   		try {
	   		
		   		/* Richiesta per passare la comanda allo stato consegnata */
	   			RestaurantApplication restApp = (RestaurantApplication)getApplication();
		   		HashMap<String,String> requestParameters = new HashMap<String,String>();
		   		
		   		requestParameters.put("action","PULISCI_TAVOLO");
		   		requestParameters.put("idTavolo", ""+ myTable.getTableId());
		   		
		   		
		   		/* *************************************************
		   		 * Aggiorno la data di verifica delle notifiche. 
		   		 ***************************************************/
		   	
		   		String response = 
							restApp.makeHttpPostRequest(restApp.getHost() + 
														"ClientEJB/gestioneComande", 
														requestParameters);
		   		
		   		JSONObject jsonObjectResponse = new JSONObject(response);
		   		
		   		if(jsonObjectResponse.getString("success").equals("true")) {
		   			return new Error("", false);
		   			
		   		} else {
		   			return new Error(	"Errore durante la pulizia del tavolo",
   										true);
		   		}
		   		
	   		} catch(Exception e) {
	   			return new Error("Errore durante la pulizia del tavolo (" +e.toString()+")",
	   							true);
	   		}
	   	}
  	
	   	@Override
	   	protected void onPostExecute(Error error) {
	   		if(error.errorOccurred()) {
	   			Toast.makeText(getApplicationContext(), error.getError(), 50).show();
	   		} else {
	   			new TableCardAsyncTask().execute((Object[]) null);
	   		}
	    }
  }
   
   /************************************************************************
    * Async Task che recupera le informazioni sul conto aperto associato
    * al tavolo e le salve sul database locale. 
    * @author Guerri Marco
    ************************************************************************/
   class GetContoAsyncTask extends AsyncTask<Object, Object, Error> {
	   
	   	DbManager dbManager;
	   	SQLiteDatabase db;
	   
	   	@Override
   		protected void onPreExecute() {
	   	}
   	
	   	@Override
		protected Error doInBackground(Object... params) {
			
	   		dbManager = new DbManager(getApplicationContext());
	   		db = dbManager.getWritableDatabase();
	   		
	   		RestaurantApplication restApp = (RestaurantApplication)getApplication();
			HashMap<String,String> requestParameters = new HashMap<String,String>();
			requestParameters.put("action","GET_CONTO");
			requestParameters.put("idTavolo", new Integer(myTable.getTableId()).toString());
			  
			Log.d("TableCardActivity", "****** AGGIORNO IL CONTO ********");
			try {
				
				/* ********************************************************
				 * Cancello qualsiasi entry del database comande creata in
				 * precedenza che NON SIA nello stato SOSPESO (comprese
				 * le variazioni associate. Se è in stato SOSPESO è 
				 * legittima la sua presenza.
				 **********************************************************/
				
				/* ********************************************************
				 * Recupero gli id delle comande da cancellare, che non 
				 * sono quindi nello stato sospeso 
				 **********************************************************/
				
				List<Integer> listaComandeNonSospese = new ArrayList<Integer>();
				
				Cursor cursorOrdinazioniNonSospese;
				cursorOrdinazioniNonSospese = db.query(	"comanda", new String[] {"idComanda"}, 
														"stato IS NOT 'SOSPESA'",
														null,
														null,
														null,
														null,
														null);
				
				cursorOrdinazioniNonSospese.moveToFirst();
				
				while(!cursorOrdinazioniNonSospese.isAfterLast()) {
					listaComandeNonSospese.add(new Integer(cursorOrdinazioniNonSospese.getInt(0)));
					cursorOrdinazioniNonSospese.moveToNext();
				}
				
				cursorOrdinazioniNonSospese.close();
				
				
				/* ******************************************************
				 * Cancello comande e variazioni associate in base agli
				 * id recuperati in precedenza 
				 ********************************************************/
				String idComande = "(";
			
				for(Integer id : listaComandeNonSospese)
					idComande = idComande + id + ",";
				
				idComande = idComande.substring(0, 	idComande.lastIndexOf(",") > 0 ? 
													idComande.lastIndexOf(",") : 1 );
				idComande = idComande + ")";
				Log.d("TableCardActivity", idComande);
				
				Log.d("TableCardActivity" , "ID comande: " + idComande);
				db.delete("comanda", "idComanda IN " + idComande, null);
				db.delete("variazionecomanda","idComanda IN " + idComande, null);
				
				
				/* ******************************************************
				 * Richiesta al server per ottenere il conto associato
				 * al tavolo
				 ********************************************************/
				
				String response = 
					restApp.makeHttpPostRequest(restApp.getHost() + "ClientEJB/gestioneConti", 
												requestParameters);
				
				/* *********************************************************
				 * Decodifica della risposta inviata dal server e inserisce
				 * nel database le comande con le variazioni richieste
				 **********************************************************/
				JSONObject jsonObject = new JSONObject(response);
				
				if(jsonObject.getBoolean("success") == true) {
					
					
					JSONArray jsonArrayComande = jsonObject.getJSONArray("comande");
					
					for(int i=0; i<jsonArrayComande.length(); i++) {
						
						JSONObject jsonObjectComanda = jsonArrayComande.getJSONObject(i);
						
						ContentValues comanda = new ContentValues();
						comanda.put("idRemotoComanda", jsonObjectComanda.getInt("idRemoto"));
						comanda.put("idVoceMenu", jsonObjectComanda.getInt("idVoceMenu"));
						comanda.put("idTavolo", myTable.getTableId());
						comanda.put("quantita", jsonObjectComanda.getInt("quantita"));
						comanda.put("note", jsonObjectComanda.getString("note"));
						comanda.put("stato", jsonObjectComanda.getString("stato"));
						db.insertOrThrow("comanda", null, comanda);
						
						
						/* **************************************************************
						 * Inserisce nella tabella variazionecomanda tutte le variazioni
						 * richieste associandole all'ultimo id comanda inserito
						 ****************************************************************/
						
						int lastId = 0;
						String query = "SELECT idComanda from comanda order by idComanda DESC limit 1";
						Cursor c = db.rawQuery(query,  null);
						if (c != null && c.moveToFirst()) {
							lastId = c.getInt(0);
							c.close();
						} else {
							c.close();
							return new Error("Errore durante la ricerca dell'id della comanda", true);
						}
						
						JSONArray jsonArrayVariazioni = jsonObjectComanda.getJSONArray("variazioni");
						
						for(int j=0; j<jsonArrayVariazioni.length(); j++) {
							JSONObject jsonObjectVariazione = jsonArrayVariazioni.getJSONObject(j);
							
							ContentValues variazione = new ContentValues();
							variazione.put("idComanda", lastId);
							variazione.put("idVariazione", jsonObjectVariazione.getInt("id"));
							
							db.insertOrThrow("variazionecomanda", null, variazione);
						}
					}
					dbManager.close();
					db.close();
					return new Error("",false);
					
				} else {
					dbManager.close();
					db.close();
					return new Error(jsonObject.getString("message"), true);
				}
				
			} catch (Exception e) {
				return new Error(e.toString(),true);
			}
		}
	  	@Override
	   	protected void onPostExecute(Error error) {
	  		if(error.errorOccurred()) {
	  			Toast.makeText(getApplicationContext(), error.getError(), 50).show();
	  		} else {
	  			
	  			/* Aggiorno la listview del conto */
	  			getOrdersConfirmed();
				contoListView_adapter.notifyDataSetChanged();
				Utility.setListViewHeightBasedOnChildren(contoListView);
				
	  		}
	   	}
   }  
}
