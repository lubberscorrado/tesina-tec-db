package com.restaurant.android.cucina.activities;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.restaurant.android.R;
import com.restaurant.android.RestaurantApplication;
import com.restaurant.android.cucina.comanda.ComandaCucina;

/** 
 * Activity della cucina che serve a mostrare l'elenco dei cibi
 * richiesti. 
 * 
 * @author Fabio Pierazzi
 */
public class ElencoComandeCibiActivity extends Activity {
	
	/* Variabile usata per il logging */
	private static final String TAG = "ElencoComandeCibiActivity";

	private ListView elencoComandeCibiListView;
	private boolean runThread;
	private boolean pauseThread;
	// private ProgressDialog m_ProgressDialog = null;
	private ArrayList<ComandaCucina> arrayList_elencoCibi = null;
    private ElencoComandeCibi_Adapter adapter_elencoCibi;
    private ElencoCibiUpdaterThread elencoCibi_updaterThread;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cucina_elenco_cibi_list);
		  // Recupero il riferimento ad una ListView
		  this.elencoComandeCibiListView = (ListView) findViewById(R.id.cucina_listView_elencoComandeCibi);
		  
		  // Operazioni necessarie al corretto funzionamento della listView
		  arrayList_elencoCibi = new ArrayList<ComandaCucina>();
	      this.adapter_elencoCibi = new ElencoComandeCibi_Adapter(getApplicationContext(), 
	    		  							R.layout.cucina_elenco_cibi_list_row, 
	    		  							arrayList_elencoCibi);
	      
	      elencoComandeCibiListView.setAdapter(this.adapter_elencoCibi);
	      
	      /**************************************************************
	       * Avvio del thread di aggiornamento della lista dei tavoli
	       **************************************************************/
	      elencoCibi_updaterThread = new ElencoCibiUpdaterThread();
	      runThread = true;
	      pauseThread = false;
	      Log.d(TAG,"Avvio il thread di aggiornamento dell'elenco delle comande");
	      elencoCibi_updaterThread.start();
	      
	      
	      /**************************************************************
	       * Listener per il click su un elemento della lista dei tavoli 
	       **************************************************************/
	      
	      elencoComandeCibiListView.setOnItemClickListener(new OnItemClickListener() {
	  	    public void onItemClick(AdapterView<?> parent, View view,
			        int position, long id) {
	  	    
	  	    		
//	 	    	 /* Sapendo la posizione dell'elemento che è stato 
//	  	    	  * cliccato, ricavo l'oggetto dell'adapter */
//	  	    	 Log.i(TAG, "Hai cliccato su: " + 
//	  	    		  	m_adapter.getItem(position).getTableName() + ", che è " 
//	  	    		  + m_adapter.getItem(position).getTableStatus());
//	  	    	  
//	  	    	 /* Apro una nuova activity con la scheda del tavolo */
//	  	    	 Intent myIntent = new Intent(TablesListActivity.this, TableCardActivity.class);
//	  	    	 // TablesListActivity.this.startActivity(myIntent);
//	  	    	  
//	  	    	 /* Creo un bundle per passare dei dati alla nuova activity */
//		  	     Bundle b = new Bundle();
//		  	     
//		  	     
//		  	     b.putSerializable("tableObject", (Table) m_adapter.getItem(position));
//		  	     b.putString("tableName", m_adapter.getItem(position).getTableName());
//		  	     myIntent.putExtras(b);
//		  	     startActivity(myIntent);
	  	    }
	      });
	    }
		
		@Override
		public void onResume() {
			super.onResume();
			Log.d(TAG,"OnResume");
			/* Sveglio il thread sospeso */
			pauseThread = false;
			elencoCibi_updaterThread.Signal();
		}
		@Override
		public void onPause() {
			super.onPause();
			Log.d(TAG,"OnPause");
			pauseThread = true;
		}
		
		@Override
		public void onStart() {
			super.onStart();
			Log.d(TAG,"OnStart");
			pauseThread = false;
			elencoCibi_updaterThread.Signal();
		}
		
		@Override
		public void onStop() {
			super.onStop();
			Log.d(TAG,"OnStop");
			pauseThread = true;
		}
		
		@Override
		public void onDestroy() {
			super.onDestroy();
			Log.d(TAG,"OnDestroy");
			
			
			/* Imposto pauseThread a false in modo da non avere un problema di race
			 * condition dovuto al fatto che il thread si sospende dopo la signal */
			pauseThread = false;
			runThread =false;
			Log.d(TAG,"Stoppo il thread di aggiornamento dei tavoli");
			runThread =false;
			elencoCibi_updaterThread.Signal();
			
			/* Non dovrebbe essere strettamente necessario, il thread dovrebbe terminare
			 * ugualmente */
//			try {
//				updaterThread.join();
//			} catch (InterruptedException e) {
//				Log.e("TablesListActivity","Errore durante il join del thread");
//			}
		}
	
	/**
     * Metodo per reperire l'elenco delle comande da mostrare
     * all'interno della ListView.
     * @author Fabio Pierazzi
     */
    private void getElencoComandeCibi(){
          try{
        	  
        	  RestaurantApplication restApplication = (RestaurantApplication)getApplication();
        	  String url = ((RestaurantApplication)getApplication()).getHost();
        	  
	        	HashMap<String,String> getParametersMap = new HashMap<String,String>();
	        	getParametersMap.put("action", "ELENCO_COMANDE");
	  			getParametersMap.put("tipoComande", "CIBO");

	  			Log.d(TAG, "Punto 1");
	  			String response = restApplication.makeHttpGetRequest(url + "ClientEJB/gestioneComande", getParametersMap);
	  			Log.d(TAG, "Punto 2");
	  			arrayList_elencoCibi.clear();
        	  
//        	  /** Test: da rimuovere */
//        	  for(int i=0; i<100; ++i) {
//        		  ComandaCucina c = new ComandaCucina();
//        		  c.setNomeVoceMenu("Spaghetti all'amatriciana magica");
//        		  c.setStatoComanda("Pronto");
//        		  c.setQuantita(3);
//        		  arrayList_elencoCibi.add(c);
//        	  }
//        	  /** Fine Test*/
        	  
	  		   Log.d(TAG, "Punto 2.5a: response: " + response);
        	  /**********************************************************
        	   * Decodifica della risposa del server contenente lo stato 
        	   * dei tavoli.
        	   **********************************************************/
	  		   if(response == null || response.equals("")) {
	  			   Log.e(TAG, "Risposta vuota dal server per l'elenco comande"); 
	  			   return;
	  		   }
        	  JSONObject jsonObject = new JSONObject(response);
        	  
        	  Log.d(TAG, "Punto 2.5: jsonObject.getBoolean('success'): " + jsonObject.getBoolean("success") );
        	  
        	  if(jsonObject.getBoolean("success") == true) {
        		  
        		  Log.d(TAG, "Punto 3");
        		  
        		  JSONArray jsonArray = jsonObject.getJSONArray("elencoComande");
        		  
        		  for(int i=0; i< jsonArray.length(); i++) {
        			  ComandaCucina comandaCucina = new ComandaCucina();
        			  
        			  comandaCucina.setNomeVoceMenu(jsonArray.getJSONObject(i).getString("nomeVoceMenu"));
        			  comandaCucina.setNomeTavolo(jsonArray.getJSONObject(i).getString("nomeTavolo"));
        			  comandaCucina.setQuantita(Integer.parseInt(jsonArray.getJSONObject(i).getString("quantita")));
        			  comandaCucina.setStatoComanda(jsonArray.getJSONObject(i).getString("statoComanda"));
        			  
        			  arrayList_elencoCibi.add(comandaCucina);
          		  }
          	  }
        	  
        	  /**************************************************************************
        	   * Aggiornamento dell'interfaccia grafica. Solo l'UI thread può modificare
        	   * la view.
        	   **************************************************************************/
        	  runOnUiThread(new Runnable() {
        		  public void run() {
        			  adapter_elencoCibi.notifyDataSetChanged();
        		  }
        	  });
        	  
        	  Log.i(	TAG + "[service]" + ": getElencoComandeCibi()", "Number of ComandeCibi Loaded: " + 
            		  	arrayList_elencoCibi.size());
              
        } catch (Exception e) {
        	Log.e(TAG + "[service]" + ": BACKGROUND_PROC", e.getMessage());
        }
    }
	   
	/**********************************************
	 * Thread per l'aggiornamento della lista view 
	 **********************************************/
	private class ElencoCibiUpdaterThread extends Thread {
		
		private static final String TAG = "ElencoCibiUpdaterThread";
   		final int DELAY = 3000;
   		public void run() {
   			while(runThread) {
   				Log.d(TAG, "UPDATING...");
   				getElencoComandeCibi();
   				
   				try {
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					Log.d(TAG, "Errore durante lo sleep del thread");
				}
   				if(pauseThread)
					try {
						Log.d(TAG, "Vado a letto");
						this.Wait();
					} catch (InterruptedException e) {
						Log.d(TAG,"Errore durante il wait()");
					}
   			}
   		}
   		public synchronized void Wait() throws InterruptedException {
   			wait();
   			
   		}
   		public synchronized void Signal() {
   			notify();
   		}
   	}
	
	
	/************************************************************************
	 * Adapter per gestire il rendering personalizzato degli elementi della lista 
	 ************************************************************************/
    
	/* Adapter che stabilisce coem mostrare la roba in ordine */
    private class ElencoComandeCibi_Adapter extends ArrayAdapter<ComandaCucina> {

        private ArrayList<ComandaCucina> items;

        public ElencoComandeCibi_Adapter(Context context, int textViewResourceId, ArrayList<ComandaCucina> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.cucina_elenco_cibi_list_row, null);
                }
                
                ComandaCucina c = items.get(position);
                
                if (c != null) {
                        
                	try {
	                		TextView textView_nomeComanda = (TextView) v.findViewById(R.id.cucina_elencoComandeCibi_textView_nomeComanda);
	                        TextView textView_statoComanda = (TextView) v.findViewById(R.id.cucina_elencoComandeCibi_textView_statoComanda);
	                        TextView textView_quantitaComanda = (TextView) v.findViewById(R.id.cucina_elencoComandeCibi_textView_quantitaComanda);
	                        TextView textView_nomeTavolo = (TextView) v.findViewById(R.id.cucina_elencoComandeCibi_textView_nomeTavolo);
	                        
	                        if(textView_nomeComanda != null) {
	                        	textView_nomeComanda.setText(c.getNomeVoceMenu());                            
	                        }
	                        if(textView_statoComanda != null){
	                        	textView_statoComanda.setText(c.getStatoComanda());
	                        }
	                        if(textView_quantitaComanda != null) {
	                        	textView_quantitaComanda.setText(Integer.toString(c.getQuantita()));
	                        }
	                        if(textView_nomeTavolo != null) {
	                        	textView_nomeTavolo.setText(c.getNomeTavolo());
	                        }
                	} catch(Exception e) {
                		e.printStackTrace();
                		Log.e(TAG, e.getMessage());
                	}
                		
                        
                }
                return v;
        }
    }

}
