package com.restaurant.android.cameriere.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
				new OccupaTavoloAsyncTask().execute((Object[])null);
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
		  	    	 
//		  	   	Intent myIntent = new Intent(TableCardActivity.this, TableCardActivity.class);
//		  	    	  
//		  	     /* Creo un bundle per passare dei dati alla nuova activity */
//			  	     Bundle b = new Bundle();
//			  	     b.putSerializable("tableObject", (Table) m_adapter.getItem(position));
//			  	     b.putString("tableName", m_adapter.getItem(position).getTableName());
//			  	     myIntent.putExtras(b);
//			  	     startActivity(myIntent);
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
	  	  	  	    		b.putSerializable("ordinazione", (Ordinazione) contoListView_arrayOrdinazioni.get(item_position));     
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
			  	    	    		
			  	    	    		/********************************************************
			  	    	    		 * Avvio l'activity di modifica dell'ordinazione sospesa
			  	    	    		 ********************************************************/
			  	    	    		
			  	    	    		Intent myIntent = new Intent(getApplicationContext(), GestioneOrdinazioneActivity.class);
			  	  	  	    		Bundle b = new Bundle();
			  	  	  	    		b.putSerializable("ordinazione", (Ordinazione) ordersWaitingListView_arrayOrdinazioni.get(positionClicked));     
			  	  	  	    		myIntent.putExtras(b);
			  	  	  	    	
			  	  	  	    		startActivity(myIntent);
			  	  		  	     
			  	    	    	} else if (dialogMenuItems[item_position].equals("Rimuovi")) {
			  	    	    		Log.w(TAG, dialogMenuItems[item_position]);
			  	    	    		
			  	    	    		/********************************************************
			  	    	    		 * Rimuovo la comanda selezionata 
			  	    	    		 ********************************************************/
			  	    	    		DbManager dbManager = new DbManager(getApplicationContext());
			  	    	    		SQLiteDatabase db;
			  	    	    		
			  	    	    		db = dbManager.getWritableDatabase();
			  	    	    		db.delete("comanda", "idComanda=" + ordersWaitingListView_arrayOrdinazioni.get(positionClicked).getIdOrdinazione(), null);
			  	    	    		db.close();
			  	    	    		dbManager.close();
			  	    	    		  	    	    		
			  	    	    		/********************************************************
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
				RestaurantApplication restApp = (RestaurantApplication) getApplication();
				
				
				
				if(ordersWaitingListView_arrayOrdinazioni.size() == 0)
					return;
				else 
					new InviaOrdinazioniAsyncTask().execute((Object[])null);
				
				
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d("TableCardActivity","OnResume");
		
		new TableListAsyncTask().execute((Object[])null);
		
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
		Log.d("TableCardActivity","OnStop!!!!!!!!!!!!!!!");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("TableCardActivity","OnDestroy");
		Log.d("TableCardActivity","Stoppo il thread di aggiornamento dei tavoli");
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
        	  
        	  /**************************************************************************
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
    
    /************************************************************************
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
											new String[] {"idComanda", "idVoceMenu", "quantita", "note", "idRemotoComanda"} , 
											"idTavolo=" + myTable.getTableId() + " and stato=\"INVIATA\"", 
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
        		  o.setStato("INVIATA");
        		  
        		  cursorOrdinazioniInviate.moveToNext();
        		  
        		  cursorNomeVoceMenu.close();
        		  contoListView_arrayOrdinazioni.add(o);
           	  }
        	  
        	  cursorOrdinazioniInviate.close();
        	  db.close();
        	  dbManager.close();
        	  
        	  /**************************************************************************
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
    
    
    /************************************************************************
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

    /************************************************************************
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
  
    /************************************************************************
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
		
		TextView numeroPiano = (TextView)findViewById(R.id.textNumeroPiano);
		numeroPiano.setText(myTable.getPiano());

		/* Visibilità dei pulsanti */
		
		if(myTable.getTableStatus().equals("LIBERO")) {
			
			Button btnOccupa = (Button)findViewById(R.id.button_tableCard_occupaTavolo);
			btnOccupa.setEnabled(true);
			Button btnOrdina = (Button)findViewById(R.id.button_tableCard_prendiOrdinazione);
			btnOrdina.setEnabled(false);
			Button btnCedi= (Button)findViewById(R.id.button_tableCard_cediTavolo);
			btnCedi.setEnabled(false);
			Button btnLibera = (Button)findViewById(R.id.button_tableCard_liberaTavolo);
			btnLibera.setEnabled(false);
			
		} else if(myTable.getTableStatus().equals("OCCUPATO")) {
			
			Button btnOccupa = (Button)findViewById(R.id.button_tableCard_occupaTavolo);
			btnOccupa.setEnabled(false);
			Button btnOrdina = (Button)findViewById(R.id.button_tableCard_prendiOrdinazione);
			btnOrdina.setEnabled(true);
			Button btnCedi= (Button)findViewById(R.id.button_tableCard_cediTavolo);
			btnCedi.setEnabled(true);
			Button btnLibera = (Button)findViewById(R.id.button_tableCard_liberaTavolo);
			btnLibera.setEnabled(true);
			
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
					
					/************************************************************
					 * Cambio lo stato all'ordinazione sul database
					 * locale.
					 ************************************************************/
					ContentValues ordinazioneInviata = new ContentValues();
					ordinazioneInviata.put("stato","INVIATA");
					db.update("comanda", ordinazioneInviata, "idComanda=" + o.getIdOrdinazione(), null);
					
					/************************************************************
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
					return new Error(responseJsonObject.getString("message"), true);
				} else {
					
					/***********************************************************************
					 * Le comande sono state inviate al server e in risposta vengono
					 * ritornati gli id remoti per future modifiche. Il database locale
					 * viene aggiornato con gli id remoti
					 ***********************************************************************/
					
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
					return new Error("Comande inviate", false);
				}
				
			} catch (Exception e) {
				return new Error("Errore durante la conferma delle comande", true);
			} finally {
				progressDialog.dismiss();
				db.close();
				dbManager.close();
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
     * Async Task per l'aggiornamento delle informazioni della scheda 
     * del tavolo
     * @author Guerri Marco
     *************************************************************************/
    class TableListAsyncTask extends AsyncTask<Object, Object, Error> {

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
					myTable.setTableStatus("LIBERO");
					myTable.setCameriere("Non definito");
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
					return new Error("Errore durante la cancellazione",true);
				}
				
			} catch (ClientProtocolException e) {
			  return new Error("Errore durante la comunicazione con il server",true);
			} catch (IOException e) {
			  return new Error("Errore durante la comunicazione con il server",true);
			} catch (JSONException e) {
			  return new Error("Errore durante la lettura della risposta dal server",true);
			}
		}
	
	   	@Override
	   	protected void onPostExecute(Error error) {
	    	  if(error.errorOccurred()) 
	    		   Toast.makeText(getApplicationContext(), error.getError(), 40).show();
	    }
  }  
   
   
   /************************************************************************
    * Async Task per recuperare le informazioni sul conto aperto associato
    * al tavolo. Recupera i dati di tutte le ordinazioni che sono state
    * inviate in cucina. Non è essenziale nel momento in cui mantengo
    * le ordinazioni sul database locale. Posso evitare quindi di
    * richiedere le informazioni al server.
    * @author Guerri Marco
    ************************************************************************/
//   class GetContoAsyncTask extends AsyncTask<Object, Object, Error> {
//	   	@Override
//   		protected void onPreExecute() {
//	   	}
//   	
//	   	@Override
//		protected Error doInBackground(Object... params) {
//			
//	   		Log.d("GetContoAsyncTask", "Richiesta del conto");
//	   		RestaurantApplication restApp = (RestaurantApplication)getApplication();
//			HashMap<String,String> requestParameters = new HashMap<String,String>();
//			requestParameters.put("action","GET_CONTO");
//			requestParameters.put("idTavolo", new Integer(myTable.getTableId()).toString());
//			  
//			try {
//				String response = restApp.makeHttpPostRequest(restApp.getHost() + "ClientEJB/gestioneConti", requestParameters);
//				Log.d("GetContoAsyncTask", response);
//				
//				/**********************************************************
//				 * Decodifica della risposta inviata dal server
//				 **********************************************************/
//				JSONObject jsonObject = new JSONObject(response);
//				
//				contoListView_arrayOrdinazioni.clear();
//				
//				if(jsonObject.getBoolean("success") == true) {
//					
//					JSONArray jsonArrayOrdinazioni = jsonObject.getJSONArray("vocimenu");
//					
//					for(int i=0; i<jsonArrayOrdinazioni.length(); i++) {
//						JSONObject jsonObjectOrdinazione = jsonArrayOrdinazioni.getJSONObject(i);
//						
//						Ordinazione ordinazione = new Ordinazione();
//						ordinazione.setIdOrdinazione(jsonObjectOrdinazione.getInt("idComanda"));
//						ordinazione.setNome(jsonObjectOrdinazione.getString("nome"));
//						ordinazione.setStato(jsonObjectOrdinazione.getString("stato"));
//						ordinazione.setQuantita(jsonObjectOrdinazione.getInt("quantita"));
//						contoListView_arrayOrdinazioni.add(ordinazione);
//					}
//				}
//				return new Error("",false);
//				
//			} catch (Exception e) {
//				return new Error(e.toString(),true);
//			}
//		}
//	  	@Override
//	   	protected void onPostExecute(Error error) {
//	  		if(error.errorOccurred()) {
//	  			Toast.makeText(getApplicationContext(), error.getError(), 30).show();
//	  		} else {
//	  			runOnUiThread(new Runnable() {
//					public void run() {
//						contoListView_adapter.notifyDataSetChanged();
//						Utility.setListViewHeightBasedOnChildren(contoListView);
//					}
//				});
//	  		}
//	   	}
//   }  
}
