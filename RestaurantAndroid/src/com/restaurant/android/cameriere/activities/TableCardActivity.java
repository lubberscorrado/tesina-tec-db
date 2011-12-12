package com.restaurant.android.cameriere.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.restaurant.android.R;
import com.restaurant.android.Utility;

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
	private UpdaterThread_ContoListView updaterThread = null;
	private boolean runThread_contoListView = false;
	private boolean pauseThread_contoListView = false;
	
	/* Variabili necessarie alla ListView per mostrare
	 * l'elenco delle prenotazioni effettuate */
	private ListView prenotationListView = null;
	private ArrayList<Prenotazione> prenotationListView_arrayPrenotazioni = null;
	private PrenotationAdapter prenotationListView_adapter;
	private UpdaterThread_PrenotationListView prenotationListView_updaterThread = null;
	private boolean runThread_prenotationListView = false;
	private boolean pauseThread_prenotationListView = false;
	
	/* Variabili utili per la gestione del thread */
	
	
	/* ProgressDialog per mostrare una barra di caricamento */
	// private ProgressDialog progressDialog = null;

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
		  Table myTable= (Table) b.getSerializable("tableObject");
		  
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
	       * Avvio del thread di aggiornamento della lista dei tavoli
	       **************************************************************/
	      this.updaterThread = new UpdaterThread_ContoListView();
	      runThread_contoListView = true;
	      pauseThread_contoListView = false;
	      Log.d("TableCardActivity","Avvio il thread di aggiornamento delle ordinazioni");
	      
	      try {
	    	  updaterThread.start();
	      } catch(Exception e) {
	    	  Log.d(TAG, "Eccezione nel thread");
	      }
	      
		  
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
	       * Avvio del thread di aggiornamento della lista dei tavoli
	       **************************************************************/
	      this.prenotationListView_updaterThread = new UpdaterThread_PrenotationListView();
	      
	      runThread_prenotationListView = true;
	      pauseThread_prenotationListView = false;
	      
	      Log.d("TableCardActivity","Avvio il thread di aggiornamento delle prenotazioni");
	      
	      try {
	    	  prenotationListView_updaterThread.start();
	      } catch(Exception e) {
	    	  Log.d(TAG, "Eccezione nel thread dell'aggiornamento delle prenotazioni");
	      }
	      
		  
		  /* *************************************************************
	       * Listener per il click su un elemento della lista dei tavoli 
	       **************************************************************/
	      contoListView.setOnItemClickListener(new OnItemClickListener() {
		  	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		  	    	 Log.i(TAG, "Hai cliccato su un'ordinazione: " + prenotationListView_adapter.getItem(position).getNomeCliente());

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
		  
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d("TableCardActivity","OnResume");
		/* Sveglio il thread sospeso */
		pauseThread_contoListView = false;
		updaterThread.Signal();
		
		/* Thread Prenotazioni */
		pauseThread_prenotationListView = false;
		prenotationListView_updaterThread.Signal();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d("TableCardActivity","OnStart");
		pauseThread_contoListView = false;
		updaterThread.Signal();
		
		/* Thread Prenotazioni */
		pauseThread_prenotationListView = false;
		prenotationListView_updaterThread.Signal();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d("TableCardActivity","OnStop");
		pauseThread_contoListView = true;
		
		/* Thread Prenotazioni */
		pauseThread_prenotationListView = true;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("TableCardActivity","OnDestroy");
		
		Log.d("TableCardActivity","Stoppo il thread di aggiornamento dei tavoli");
		
		runThread_contoListView =false;
		
//		try {
//			updaterThread.join();
//		} catch (InterruptedException e) {
//			Log.e("TableCardActivity","Errore durante il join del thread");
//		}
		
		runThread_prenotationListView = false;
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
    
    
	private class UpdaterThread_ContoListView extends Thread {
		
   		final int DELAY = 30000;
   		public void run() {

   			Log.d(TAG, "Entrato in run() di UpdaterThread");
   			
   			while(runThread_contoListView) {
   				Log.d("UpdaterThread (TableCardActivity)", "UPDATING...");
   				getOrders();
   				
   				try {
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					Log.d("UpdaterThread (TableCardActivity)", "Errore durante lo sleep del thread");
				}
   				if(pauseThread_contoListView)
					try {
						Log.d("UpdaterThread (TableCardActivity)", "Vado a letto");
						this.Wait();
					} catch (InterruptedException e) {
						Log.d("UpdaterThread (TableCardActivity)","Errore durante il wait()");
					}
   			}
   		}
   		
   		public synchronized void Wait() throws InterruptedException {
   			Log.d(TAG, "Entrato in Wait() di UpdaterThread");
   			wait();
   			
   		}
   		
   		public synchronized void Signal() {
   			Log.d(TAG, "Entrato in Signal() di UpdaterThread");
   			notify();
   		}
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
     * Thread per l'aggiornamento della lista delle prenotazioni.
     * @author Fabio Pierazzi
     */
    private class UpdaterThread_PrenotationListView extends Thread {
		
   		final int DELAY = 30000;
   		public void run() {

   			Log.d(TAG, "Entrato in run() di UpdaterThread_PrenotationListView.");
   			
   			while(runThread_prenotationListView) {
   				Log.d("UpdaterThread (TableCardActivity)", "UPDATING Prenotations...");
   				getPrenotations();
   				
   				try {
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					Log.d("UpdaterThread (TableCardActivity)", "Errore durante lo sleep del thread");
				}
   				if(pauseThread_prenotationListView)
					try {
						Log.d("UpdaterThread (TableCardActivity)", "Vado a letto");
						this.Wait();
					} catch (InterruptedException e) {
						Log.d("UpdaterThread (TableCardActivity)","Errore durante il wait()");
					}
   			}
   		}
   		
		public synchronized void Wait() throws InterruptedException {
   			Log.d(TAG, "Entrato in Wait() di UpdaterThread");
   			wait();
   			
   		}
   		
   		public synchronized void Signal() {
   			Log.d(TAG, "Entrato in Signal() di UpdaterThread");
   			notify();
   		}
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
    
}
