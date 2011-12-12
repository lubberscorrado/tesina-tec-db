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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.restaurant.android.R;

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
	
	private ListView contoListView = null;
	private boolean runThread = false;
	private boolean pauseThread = false;
	// private ProgressDialog progressDialog = null;
	private ArrayList<Ordinazione> array_ordinazioni = null;
	
	/* Adapter per il conto */
	private ContoAdapter conto_adapter = null;
	// private PrenotationAdapter prenotation_adapter;

	private UpdaterThread2 updaterThread = null;
    	
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

		  
		  
		  /* *****************************************
		   * Configuro le ListViews
		   * *****************************************/
		  
		  // Recupero riferimento a conto
		  this.contoListView = (ListView) findViewById(R.id.listView_contoList);
	  
		  this.array_ordinazioni = new ArrayList<Ordinazione>();
		  
		  this.conto_adapter = new ContoAdapter(getApplicationContext(), 
					R.layout.cameriere_table_card_conto_list_row, 
					array_ordinazioni);
		  
		  this.contoListView.setAdapter(this.conto_adapter);
		  
		  
		  /**************************************************************
	       * Avvio del thread di aggiornamento della lista dei tavoli
	       **************************************************************/
	      this.updaterThread = new UpdaterThread2();
	      runThread = true;
	      pauseThread = false;
	      Log.d("TableCardActivity","Avvio il thread di aggiornamento delle ordinazioni");
	      
	      try {
	    	  updaterThread.start();
	      } catch(Exception e) {
	    	  Log.d(TAG, "Eccezione nel thread");
	      }
	      
		  
		  /**************************************************************
	       * Listener per il click su un elemento della lista dei tavoli 
	       **************************************************************/
	      contoListView.setOnItemClickListener(new OnItemClickListener() {
		  	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		  	    
		  	    		
		 	    	 /* Sapendo la posizione dell'elemento che è stato 
		  	    	  * cliccato, ricavo l'oggetto dell'adapter */
		  	    	 Log.i(TAG, "Hai cliccato su un'ordinazione: " + conto_adapter.getItem(position).getNome());
		  	    	  
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
		  	    
		  
      });

		  
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d("TableCardActivity","OnResume");
		/* Sveglio il thread sospeso */
		pauseThread = false;
		updaterThread.Signal();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d("TableCardActivity","OnStart");
		pauseThread = false;
		updaterThread.Signal();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d("TableCardActivity","OnStop");
		pauseThread = true;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("TableCardActivity","OnDestroy");
		
		Log.d("TableCardActivity","Stoppo il thread di aggiornamento dei tavoli");
		runThread =false;
//		try {
//			updaterThread.join();
//		} catch (InterruptedException e) {
//			Log.e("TableCardActivity","Errore durante il join del thread");
//		}
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
        	  array_ordinazioni.clear();
        	  
        	  /**********************************************************
        	   * Decodifica della risposa del server contenente lo stato 
        	   * dei tavoli.
        	   **********************************************************/
        	  // JSONObject jsonObject = new JSONObject(response);
        	  
        	  // if(jsonObject.getBoolean("success") == true) {
        		  
        		 //  JSONArray jsonArray = jsonObject.getJSONArray("statoTavolo");
        		  
        		  // for(int i=0; i< jsonArray.length() ; i++) {
        		  for(int i=0; i< 3; i++) {
        			  Ordinazione o = new Ordinazione();
        			  
        			  o.setNome("Penne all'amatriciana");
        			  o.setQuantita(2);
        			  o.setStato("In attesa");
        			  
//        			  o.setTableName(jsonArray.getJSONObject(i).getString("nomeTavolo"));
//        			  o.setTableStatus(jsonArray.getJSONObject(i).getString("statoTavolo"));
//        			  o.setTableId(Integer.parseInt(jsonArray.getJSONObject(i).getString("idTavolo")));
        			  
        			  array_ordinazioni.add(o);
          		  }
//         	  }
        	  
        	  /**************************************************************************
        	   * Aggiornamento dell'interfaccia grafica. Solo l'UI thread può modificare
        	   * la view.
        	   **************************************************************************/
        	  runOnUiThread(new Runnable() {
        		  public void run() {
        			  conto_adapter.notifyDataSetChanged();
        		  }
        	  });
        	  
        	  Log.i(	"TablesListService" + ": getOrders()", 
		        			  	"Number of Ordinations Loaded: " + 
		            		  	array_ordinazioni.size());
              
        } catch (Exception e) {
        	Log.e("OdersListService" + ": BACKGROUND_PROC", e.getMessage());
        }
          
        Log.i(TAG, "Uscito da getOrders()");
    }
    
    
	private class UpdaterThread2 extends Thread {
		
   		final int DELAY = 30000;
   		public void run() {

   			Log.d(TAG, "Entrato in run() di UpdaterThread");
   			
   			while(runThread) {
   				Log.d("UpdaterThread (TableCardActivity)", "UPDATING...");
   				getOrders();
   				
   				try {
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					Log.d("UpdaterThread (TableCardActivity)", "Errore durante lo sleep del thread");
				}
   				if(pauseThread)
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
        	
        		Log.d(TAG, "Entrato nel renderer getView() del ContoAdapter.");
        		
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.cameriere_table_card_conto_list_row, null);
                }
                
                Log.d(TAG, "Usato il Layout Inflater.");
                
                Ordinazione o = items.get(position);
                
                if (o != null) {
                        
                		/* Aggiorno i campi in base a quel che ho letto */
                		TextView textView_nomeOrdinazione = (TextView) v.findViewById(R.id.textView_contoList_nomeOrdinazione);
                		TextView textView_quantita = (TextView) v.findViewById(R.id.textView_contoList_quantity);
                		TextView textView_stato = (TextView) v.findViewById(R.id.textView_contoList_stato);
                		
                		Log.d(TAG, "Recuperate le 3 TextViews.");
                		
                		if(textView_nomeOrdinazione != null) {
                			textView_nomeOrdinazione.setText(o.getNome());                            
                        }
                		
                		Log.d(TAG, "Settato il nome.");
                		
                		
                		if(textView_quantita != null){
                			textView_quantita.setText(Integer.toString(o.getQuantita()));
                        }
                        
                		Log.d(TAG, "Settata la quantita.");
                		
                		if(textView_stato != null) {
                			textView_stato.setText(o.getStato());
                        }

                		Log.d(TAG, "Settato lo stato.");
                		
                }
                
                contoListView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                
                Log.d(TAG, "Uscito dal renderer getView() del ContoAdapter.");
                
                return v;
                
        }
    }
}
