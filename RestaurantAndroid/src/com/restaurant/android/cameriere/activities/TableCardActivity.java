package com.restaurant.android.cameriere.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.restaurant.android.Error;
import com.restaurant.android.R;
import com.restaurant.android.RestaurantApplication;
import com.restaurant.android.Utility;
import com.restaurant.android.cameriere.activities.HomeActivity.MenuSyncTask;

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
	private PrenotationAdapter prenotationListView_adapter;
	
	Table myTable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.cameriere_table_card);
		  
		  /* ******************************************
		   * Recupero il valore col nome del tavolo dall'oggetto
		   * passato durante l'invocazione di questa activity 
		   * dall'elenco tavoli
		   * ***************************************** */
		  Bundle b = getIntent().getExtras();
		  String tableName = b.getString("tableName");
		  myTable= (Table) b.getSerializable("tableObject");
		  
		  /* Tavolo de-serializzato */
		  Log.d("Scheda Tavolo", "De-serializzato il tavolo: " + myTable.getTableName());
		  
		  /* Cambio il titolo della scheda del tavolo */
		  TextView textView_tableName = (TextView) findViewById(R.id.tableCard_textView_Title);
		  textView_tableName.setText("Scheda " + tableName);
		  
		  // Aggiungo Listener al bottone di "Prendi Ordinazione"
		  Button button_prendiOrdinazione = (Button) findViewById(R.id.button_tableCard_prendiOrdinazione);
			
			// Provo a cambiare activity
		  button_prendiOrdinazione.setOnClickListener(new OnClickListener() {
			  	@Override
			  	public void onClick(View v) {
			  		//-----------------------
			        // Open New Activity
			  		//-----------------------
			  		Intent myIntent = new Intent(TableCardActivity.this, MenuListActivity.class);
			  		TableCardActivity.this.startActivity(myIntent);
			  	}
		});

		  
		  
	      /** *****************************************
		   * Configuro ListView per le Prenotazioni
		   * *****************************************/
	      
		  this.prenotationListView = (ListView) findViewById(R.id.listView_prenotationList);
		  
		  this.prenotationListView_arrayPrenotazioni = new ArrayList<Prenotazione>();
		  
		  this.prenotationListView_adapter = new PrenotationAdapter(getApplicationContext(), 
					R.layout.cameriere_table_card_prenotation_list_row, 
					prenotationListView_arrayPrenotazioni);
		  
		  this.prenotationListView.setAdapter(this.prenotationListView_adapter);
		  
		  /* *************************************************************
	       * Listener per il click su un elemento della lista dei tavoli 
	       **************************************************************/
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
		  	    
      }); // fine itemClickListener
		  
		  /** *****************************************
		   * Configuro ListView per il Conto
		   * *****************************************/
		  
		  // Recupero riferimento a conto
		  this.contoListView = (ListView) findViewById(R.id.listView_contoList);
	  
		  this.contoListView_arrayOrdinazioni = new ArrayList<Ordinazione>();
		  
		  this.contoListView_adapter = new ContoAdapter(getApplicationContext(), 
					R.layout.cameriere_table_card_conto_list_row, 
					contoListView_arrayOrdinazioni);
		  
		  this.contoListView.setAdapter(this.contoListView_adapter);
		  
		  
		  /* *************************************************************
	       * Listener per il click su un elemento della lista dei tavoli 
	       **************************************************************/
	      contoListView.setOnItemClickListener(new OnItemClickListener() {
		  	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		  	    
		  	    		
		 	    	 /* Sapendo la posizione dell'elemento che è stato 
		  	    	  * cliccato, ricavo l'oggetto dell'adapter */
		  	    	 Log.i(TAG, "Hai cliccato su un'ordinazione: " + contoListView_adapter.getItem(position).getNome());
		  	    	  
		  	    /* Apro una nuova activity con la scheda dell'ordinazione */
		  	    	 
//		  	   	Intent myIntent = new Intent(TableCardActivity.this, TableCardActivity.class);
//		  	    	  
//		  	     /* Creo un bundle per passare dei dati alla nuova activity */
//			  	     Bundle b = new Bundle();
//			  	     b.putSerializable("tableObject", (Table) m_adapter.getItem(position));
//			  	     b.putString("tableName", m_adapter.getItem(position).getTableName());
//			  	     myIntent.putExtras(b);
//			  	     startActivity(myIntent);
		  	    	 
		  	    } 
		  	    
      }); // fine itemClickListener

	     
		  
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d("TableCardActivity","OnResume");
		new TableListAsyncTask().execute(null);
		
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
		Log.d("TableCardActivity","OnDestroy");
		
		Log.d("TableCardActivity","Stoppo il thread di aggiornamento dei tavoli");
		
		
	}
	
	/**
     * Metodo per reperire l'elenco delle ordinazioni
     * @author Fabio Pierazzi
     */
    private void getOrders(){
    	
    	Log.i(TAG, "Entrato in getOrders();");
          try{
//        	  
//        	  RestaurantApplication restApplication = (RestaurantApplication)getApplication();
//        	  String url = ((RestaurantApplication)getApplication()).getHost();
//        	  String response = restApplication.makeHttpGetRequest(url + "ClientEJB/statoTavolo", new HashMap<String, String>());
//        	  
        	  contoListView_arrayOrdinazioni.clear();
        	  
        	  /**********************************************************
        	   * Decodifica della risposa del server contenente lo stato 
        	   * dei tavoli.
        	   **********************************************************/
        	  // JSONObject jsonObject = new JSONObject(response);
        	  
        	  // if(jsonObject.getBoolean("success") == true) {
        		  
        		 //  JSONArray jsonArray = jsonObject.getJSONArray("statoTavolo");
        		  
        		  // for(int i=0; i< jsonArray.length() ; i++) {
        		  for(int i=0; i< 15; i++) {
        			  Ordinazione o = new Ordinazione();
        			  
        			  if(i%3==0) {
        				  o.setNome("Penne all'amatriciana");
        				  o.setQuantita(12);
        				  o.setStato("In sospeso");
        			  } else {
        				  o.setNome("Lasagne");
            			  o.setQuantita(2);
            			  o.setStato("Inviata in cucina");
        			  }
        			  
        			  
//        			  o.setTableName(jsonArray.getJSONObject(i).getString("nomeTavolo"));
//        			  o.setTableStatus(jsonArray.getJSONObject(i).getString("statoTavolo"));
//        			  o.setTableId(Integer.parseInt(jsonArray.getJSONObject(i).getString("idTavolo")));
        			  
        			  contoListView_arrayOrdinazioni.add(o);
          		  }
        		  
        		 
//         	  }
        	  
        	  /**************************************************************************
        	   * Aggiornamento dell'interfaccia grafica. Solo l'UI thread può modificare
        	   * la view.
        	   **************************************************************************/
        	  runOnUiThread(new Runnable() {
        		  public void run() {
        			  contoListView_adapter.notifyDataSetChanged();
        			  
        			  Log.w(TAG, "Cambio altezza ListView");
            		  /* Modifico manualmente la nuova altezza della listView */
                      Utility.setListViewHeightBasedOnChildren(contoListView);
        		  }
        	  });
        	  
        	  Log.i(	"TablesListService" + ": getOrders()", 
		        			  	"Number of Ordinations Loaded: " + 
		            		  	contoListView_arrayOrdinazioni.size());
              
        } catch (Exception e) {
        	Log.e("OdersListService" + ": BACKGROUND_PROC", e.getMessage());
        }
          
        Log.i(TAG, "Uscito da getOrders()");
    }
    
    /************************************************************************
	 * Adapter per gestire il rendering personalizzato degli elementi
	 * della lista con le ordinazioni del conto
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
    
    

	/**
     * Metodo per reperire l'elenco delle ordinazioni
     * @author Fabio Pierazzi
     */
    private void getPrenotations(){
    	
    	Log.i(TAG, "Entrato in getPrenotations();");
          try{

//        	  RestaurantApplication restApplication = (RestaurantApplication)getApplication();
//        	  String url = ((RestaurantApplication)getApplication()).getHost();
//        	  String response = restApplication.makeHttpGetRequest(url + "ClientEJB/statoTavolo", new HashMap<String, String>());
//        	  
        	  prenotationListView_arrayPrenotazioni.clear();
        	  
        	  /**********************************************************
        	   * Decodifica della risposa del server contenente lo stato 
        	   * dei tavoli.
        	   **********************************************************/
        	  // JSONObject jsonObject = new JSONObject(response);
        	  
        	  // if(jsonObject.getBoolean("success") == true) {
        		  
        		 //  JSONArray jsonArray = jsonObject.getJSONArray("statoTavolo");
        		  
        		  // for(int i=0; i< jsonArray.length() ; i++) {
        		  for(int i=0; i< 15; i++) {
        			  Prenotazione p = new Prenotazione();
        			  
        			  if(i%3==0) {
        				  p.setNomeCliente("Turoldo");
        				  p.setNumPersone(10);
        				  p.setTimeAndDate("23/12/2011 22:30");
        			  } else {
        				  p.setNomeCliente("Bazinga");
        				  p.setNumPersone(5);
        				  p.setTimeAndDate("25/12/2011 21:30");
        			  }
        			  
//        			  o.setTableName(jsonArray.getJSONObject(i).getString("nomeTavolo"));
//        			  o.setTableStatus(jsonArray.getJSONObject(i).getString("statoTavolo"));
//        			  o.setTableId(Integer.parseInt(jsonArray.getJSONObject(i).getString("idTavolo")));
        			  
        			  prenotationListView_arrayPrenotazioni.add(p);
          		  }
        		  
        		 
//         	  }
        	  
        	  /**************************************************************************
        	   * Aggiornamento dell'interfaccia grafica. Solo l'UI thread può modificare
        	   * la view.
        	   **************************************************************************/
        	  runOnUiThread(new Runnable() {
        		  public void run() {
        			  prenotationListView_adapter.notifyDataSetChanged();
        			  
        			  Log.w(TAG, "Cambio altezza prenotationListView ");
            		  /* Modifico manualmente la nuova altezza della listView */
                      Utility.setListViewHeightBasedOnChildren(prenotationListView);
        		  }
        	  });
        	  
        	  Log.i(	"TablesListService" + ": getPrenotations()", 
		        			  	"Number of Ordinations Loaded: " + 
		            		  	contoListView_arrayOrdinazioni.size());
              
        } catch (Exception e) {
        	Log.e("OdersListService" + ": BACKGROUND_PROC", e.getMessage());
        }
          
        Log.i(TAG, "Uscito da getPrenotations()");
    }
    
    /**
     * Adapter le prenotazioni. 
     * @author Fabio Pierazzi
     */
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
    } // end of PrenotationAdapter
    
    
    public void updateStatoTavolo() {
    	
    	/**************************************************
    	 * Aggiornamento delle textbox
    	 **************************************************/
    	TextView nomeTavolo = (TextView)findViewById(R.id.textNomeCameriere);
		nomeTavolo.setText(myTable.getCameriere());
		
		TextView nomeArea = (TextView)findViewById(R.id.textNomeArea);
		nomeArea.setText(myTable.getArea());
		
		TextView stato = (TextView)findViewById(R.id.textStato);
		stato.setText(myTable.getTableStatus());
		
		TextView numeroPiano = (TextView)findViewById(R.id.textNumeroPiano);
		numeroPiano.setText(myTable.getPiano());

		/****************************************************
		 * Visibilità dei pulsanti
		 ****************************************************/
		
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
    
    
    /**
     * Async Task per l'aggiornamento delle informazioni della scheda 
     * del tavolo
     * @author Marco Guerri
     *
     */
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
}
