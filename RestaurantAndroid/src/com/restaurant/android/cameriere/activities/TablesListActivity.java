package com.restaurant.android.cameriere.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.restaurant.android.DbManager;
import com.restaurant.android.R;
import com.restaurant.android.RestaurantApplication;

/** 
 * Activity per mostrare al cameriere l'elenco dei tavoli.
 * @author Fabio Pierazzi
 */
public class TablesListActivity extends Activity {
	
	/* Variabile usata per il logging */
	private static final String TAG = "TablesListActivity";
	
	private ListView tableListView;
	private boolean runThread;
	private boolean pauseThread;
	private ProgressDialog m_ProgressDialog = null;
	private ArrayList<Table> m_tables = null;
    private TableAdapter m_adapter;
    private UpdaterThread updaterThread;
	private Semaphore sem;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.cameriere_tables_list);
	 
	  // Recupero il riferimento ad una ListView
	  this.tableListView = (ListView) findViewById(R.id.listView_tablesList);
	  
	  // Operazioni necessarie al corretto funzionamento della listView
	  m_tables = new ArrayList<Table>();
      this.m_adapter = new TableAdapter(getApplicationContext(), 
    		  							R.layout.cameriere_tables_list_row, 
    		  							m_tables);
      
      tableListView.setAdapter(this.m_adapter);
      
      sem = new Semaphore(0);
      
      /**************************************************************
       * Avvio del thread di aggiornamento della lista dei tavoli
       **************************************************************/
      updaterThread = new UpdaterThread();
      runThread = true;
      pauseThread = false;
      Log.d("TablesListActivity","Avvio il thread di aggiornamento dei tavoli");
      updaterThread.start();
      
      
      /**************************************************************
       * Listener per il click su un elemento della lista dei tavoli 
       **************************************************************/
      
      tableListView.setOnItemClickListener(new OnItemClickListener() {
  	    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {
  	    
  	    		
 	    	 /* Sapendo la posizione dell'elemento che è stato 
  	    	  * cliccato, ricavo l'oggetto dell'adapter */
  	    	 Log.i(TAG, "Hai cliccato su: " + 
  	    		  	m_adapter.getItem(position).getTableName() + ", che è " 
  	    		  + m_adapter.getItem(position).getTableStatus());
  	    	  
  	    	 /* Apro una nuova activity con la scheda del tavolo */
  	    	 Intent myIntent = new Intent(TablesListActivity.this, TableCardActivity.class);
  	    	 // TablesListActivity.this.startActivity(myIntent);
  	    	  
  	    	 /* Creo un bundle per passare dei dati alla nuova activity */
	  	     Bundle b = new Bundle();
	  	     b.putSerializable("tableObject", (Table) m_adapter.getItem(position));
	  	     b.putString("tableName", m_adapter.getItem(position).getTableName());
	  	     myIntent.putExtras(b);
	  	     startActivity(myIntent);
  	    }
      });
    }
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d("TablesListActivity","OnResume");
		/* Sveglio il thread sospeso */
		pauseThread = false;
		updaterThread.Signal();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d("TablesListActivity","OnStart");
		pauseThread = false;
		updaterThread.Signal();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d("TablesListActivity","OnStop");
		pauseThread = true;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("TablesListActivity","OnDestroy");
		
		
		/* Imposto pauseThread a false in modo da non avere un problema di race
		 * condition dovuto al fatto che il thread si sospende dopo la signal */
		pauseThread = false;
		runThread =false;
		Log.d("TablesListActivity","Stoppo il thread di aggiornamento dei tavoli");
		runThread =false;
		updaterThread.Signal();
		
		/* Non dovrebbe essere strettamente necessario, il thread dovrebbe terminare
		 * ugualmente */
//		try {
//			updaterThread.join();
//		} catch (InterruptedException e) {
//			Log.e("TablesListActivity","Errore durante il join del thread");
//		}
	}

	/**
     * Metodo per reperire l'elenco di tavoli da mostrare
     * all'interno della ListView. 
     * @author Fabio Pierazzi
     */
    private void getTables(){
          try{
        	  
        	  RestaurantApplication restApplication = (RestaurantApplication)getApplication();
        	  String url = ((RestaurantApplication)getApplication()).getHost();
        	  String response = restApplication.makeHttpGetRequest(url + "ClientEJB/statoTavolo", new HashMap<String, String>());
        	  
        	  m_tables.clear();
        	  
        	  /**********************************************************
        	   * Decodifica della risposa del server contenente lo stato 
        	   * dei tavoli.
        	   **********************************************************/
        	  JSONObject jsonObject = new JSONObject(response);
        	  
        	  if(jsonObject.getBoolean("success") == true) {
        		  
        		  JSONArray jsonArray = jsonObject.getJSONArray("statoTavolo");
        		  
        		  for(int i=0; i< jsonArray.length(); i++) {
        			  Table t = new Table();
        			  t.setTableName(jsonArray.getJSONObject(i).getString("nomeTavolo"));
        			  t.setTableStatus(jsonArray.getJSONObject(i).getString("statoTavolo"));
        			  t.setTableId(
        					  Integer.parseInt(
        							  jsonArray.getJSONObject(i).getString("idTavolo")));
        			  m_tables.add(t);
          		  }
          	  }
        	  
        	  /**************************************************************************
        	   * Aggiornamento dell'interfaccia grafica. Solo l'UI thread può modificare
        	   * la view.
        	   **************************************************************************/
        	  runOnUiThread(new Runnable() {
        		  public void run() {
        			  m_adapter.notifyDataSetChanged();
        		  }
        	  });
        	  
        	  Log.i(	"TablesListService" + ": getTables()", "Number of Tables Loaded: " + 
            		  	m_tables.size());
              
        } catch (Exception e) {
        	Log.e("TablesListService" + ": BACKGROUND_PROC", e.getMessage());
        }
    }
	   
	/**********************************************
	 * Thread per l'aggiornamento della lista view 
	 **********************************************/
	private class UpdaterThread extends Thread {
		
   		final int DELAY = 3000;
   		public void run() {
   			while(runThread) {
   				Log.d("UpdaterThread", "UPDATING...");
   				getTables();
   				
   				try {
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					Log.d("UpdaterThread", "Errore durante lo sleep del thread");
				}
   				if(pauseThread)
					try {
						Log.d("UpdaterThread", "Vado a letto");
						this.Wait();
					} catch (InterruptedException e) {
						Log.d("UpdaterThread","Errore durante il wait()");
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
	 * Adapter per gestire il rendering personalizzato degli elementi della
	 * lista 
	 ************************************************************************/
    
	/* Adapter che stabilisce coem mostrare la roba in ordine */
    private class TableAdapter extends ArrayAdapter<Table> {

        private ArrayList<Table> items;

        public TableAdapter(Context context, int textViewResourceId, ArrayList<Table> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.cameriere_tables_list_row, null);
                }
                Table t = items.get(position);
                if (t != null) {
                        
                		TextView tt = (TextView) v.findViewById(R.id.toptext);
                        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                        if(tt != null) {
                              tt.setText("Name: " + t.getTableName());                            
                        }
                        if(bt != null){
                              bt.setText("Status: "+ t.getTableStatus());
                        }
                        
                }
                return v;
        }
    }
}