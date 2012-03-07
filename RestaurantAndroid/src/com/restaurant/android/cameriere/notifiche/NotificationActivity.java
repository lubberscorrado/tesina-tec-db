package com.restaurant.android.cameriere.notifiche;

import java.util.ArrayList;

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

/** 
 * Activity per mostrare l'elenco delle notifiche.
 * @author Fabio Pierazzi
 */
public class NotificationActivity extends Activity {
	
	private static final String TAG = "NotificationActivity"; 
//	public static Date lastNotification;
	
	private ListView notificationListView;
	private boolean runThread;
	private boolean pauseThread;
	// private ProgressDialog m_ProgressDialog = null;
	private ArrayList<Notifica> notification_arrayList = null;
    private NotificationAdapter notification_adapter;
    private NotificationLocalUpdaterThread notificationLocalUpdaterThread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.cameriere_notifications_list);
	  
	  
	  // Recupero il riferimento ad una ListView
	  this.notificationListView = (ListView) findViewById(R.id.listView_notificationsList);
	  
	  // Operazioni necessarie al corretto funzionamento della listView
	  notification_arrayList = new ArrayList<Notifica>();
      this.notification_adapter = new NotificationAdapter(getApplicationContext(), 
    		  							R.layout.cameriere_tables_list_row, 
    		  							notification_arrayList);
      
      notificationListView.setAdapter(this.notification_adapter);
      
      /**************************************************************
       * Avvio del thread di aggiornamento della lista dei tavoli
       **************************************************************/
      notificationLocalUpdaterThread = new NotificationLocalUpdaterThread();
      runThread = true;
      pauseThread = false;
      Log.d("TablesListActivity","Avvio il thread di aggiornamento dei tavoli");
      notificationLocalUpdaterThread.start();
      
      
      /**************************************************************
       * Listener per il click su un elemento della lista dei tavoli 
       **************************************************************/
      
      notificationListView.setOnItemClickListener(new OnItemClickListener() {
  	    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {
//  	    
//  	    		
// 	    	 /* Sapendo la posizione dell'elemento che è stato 
//  	    	  * cliccato, ricavo l'oggetto dell'adapter */
//  	    	 Log.i(TAG, "Hai cliccato su: " + 
//  	    		  	m_adapter.getItem(position).getTableName() + ", che è " 
//  	    		  + m_adapter.getItem(position).getTableStatus());
//  	    	  
//  	    	 /* Apro una nuova activity con la scheda del tavolo */
//  	    	 Intent myIntent = new Intent(TablesListActivity.this, TableCardActivity.class);
//  	    	 // TablesListActivity.this.startActivity(myIntent);
//  	    	  
//  	    	 /* Creo un bundle per passare dei dati alla nuova activity */
//	  	     Bundle b = new Bundle();
//	  	     
//	  	     
//	  	     b.putSerializable("tableObject", (Table) m_adapter.getItem(position));
//	  	     b.putString("tableName", m_adapter.getItem(position).getTableName());
//	  	     myIntent.putExtras(b);
//	  	     startActivity(myIntent);
  	    }
      });

	}
	
	@Override
	public void onResume() {
		super.onResume();
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
		notificationLocalUpdaterThread.Signal();
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
		notificationLocalUpdaterThread.Signal();
		
	}
	
	
	/**
     * Metodo per reperire l'elenco di notifiche relative a questo 
     * cameriere dal DB.
     *  
     * @author Fabio Pierazzi
     */
    private void getNotifications(){
          try{

        	  // Inizio parte da rimuovere (DEMO) 
        	  
        	  notification_arrayList.clear();
        	  
        	  for(int i=0; i<100; ++i) {
        		  
        		  Notifica n = new Notifica();
        		  
        		  if(i%5==0) 
        			  n.setNotificationType("ComandaPronta");
        		  else 
        			  n.setNotificationType("NuovoTavolo");
        		  
        		  // n.setNotificationText("Ti è stato assegnato il tavolo " + (i+1));
        		  n.setNomeTavolo("Tavolo " + (i+1)); 
        		  n.setNomeOrdinazione("Penne all'arrabbiata");
        		  notification_arrayList.add(n);
        	  }
        	  
        	  // Fine parte da rimuovere (DEMO)
        	  
//        	  RestaurantApplication restApplication = (RestaurantApplication)getApplication();
//        	  String url = ((RestaurantApplication)getApplication()).getHost();
//        	  String response = restApplication.makeHttpGetRequest(url + "ClientEJB/statoTavolo", new HashMap<String, String>());
//        	  
//        	  notification_arrayList.clear();
//        	  
//        	  /**********************************************************
//        	   * Decodifica della risposa del server contenente lo stato 
//        	   * dei tavoli.
//        	   **********************************************************/
//        	  JSONObject jsonObject = new JSONObject(response);
//        	  
//        	  if(jsonObject.getBoolean("success") == true) {
//        		  
//        		  JSONArray jsonArray = jsonObject.getJSONArray("statoTavolo");
//        		  
//        		  for(int i=0; i< jsonArray.length(); i++) {
//        			  Table t = new Table();
//        			  t.setTableName(jsonArray.getJSONObject(i).getString("nomeTavolo"));
//        			  t.setTableStatus(jsonArray.getJSONObject(i).getString("statoTavolo"));
//        			  t.setTableId(
//        					  Integer.parseInt(
//        							  jsonArray.getJSONObject(i).getString("idTavolo")));
//        			  notification_arrayList.add(t);
//          		  }
//          	  }
//        	  
        	  /**************************************************************************
        	   * Aggiornamento dell'interfaccia grafica. Solo l'UI thread può modificare
        	   * la view.
        	   **************************************************************************/
        	  runOnUiThread(new Runnable() {
        		  public void run() {
        			  notification_adapter.notifyDataSetChanged();
        		  }
        	  });
        	  
        	  Log.i(	TAG + ": getNotifications()", "Number of Notifications Loaded: " + 
            		  	notification_arrayList.size());
              
        } catch (Exception e) {
        	Log.e(TAG + ": BACKGROUND_PROC", e.getMessage());
        }
    }
	   
	/************************************************************************************
	 * Thread per l'aggiornamento grafico dell'elenco delle notifiche.
	 * 
	 * A differenza del "NotificationUpdaterThread" utilizzato da "NotificationService", 
	 * questo metodo aggiorna graficamente la ListView delle notifiche solo quando il cameriere
	 * la sta visualizzando. "NotificationUpdaterThread", invece, controlla in background ogni 60 
	 * secondi "se ci sono nuove notifiche", ma non aggiorna graficamente nulla.  
	 ************************************************************************************/
	private class NotificationLocalUpdaterThread extends Thread {
		
		private static final String TAG = "NotificationLocalUpdaterThread";
		
   		final int DELAY = 15000; // 15 seconds
   		public void run() {
   			while(runThread) {
   				Log.d(TAG, "UPDATING...");
   				getNotifications();
   				
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
	
	/** Gestisce il rendering personalizzato degli elementi (righe) della lista
	 * contenente le varie notifiche */
	private class NotificationAdapter extends ArrayAdapter<Notifica> {
		
        private ArrayList<Notifica> items;

        public NotificationAdapter(Context context, int textViewResourceId, ArrayList<Notifica> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                	/* getSystemService può essere chiamato solo all'interno di una classe che eredita da
                	 * android.context.Context, come ad esempio un'activity */
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.cameriere_notifications_list_row, null);
                }
                Notifica notifica = items.get(position);
                
                if (notifica != null) {
                        
                		// Recupero le due textView della lista delle notifiche
                		TextView textView_notificationType = (TextView) v.findViewById(R.id.textView_notificationType);
                        TextView textView_notificationText = (TextView) v.findViewById(R.id.textView_notificationText);
                        
                        Log.d(TAG, "Entered NotificationAdapter. notifica.getNotificationType(): " + notifica.getNotificationType()); 
                        
                        // Modifico il contenuto delle TextView in ciascuna delle righe della tabella
                        if((textView_notificationType != null) && (textView_notificationText != null)) {
                        	
                        	// A seconda del tipo di notifica (3 tipi), mostro un messaggio diverso
                        	if(notifica.getNotificationType().equals("NuovoTavolo")) {
                        		Log.d(TAG, "Entered NotificationAdapter: type = NuovoTavolo"); 
                        		textView_notificationType.setText("Nuovo Tavolo Assegnato");
                        		textView_notificationText.setText("Ti e' stato assegnato il tavolo '" + notifica.getNomeTavolo() + "'.");
                        		
                        	} else if(notifica.getNotificationType().equals( "ComandaPronta" )) {
                        		textView_notificationType.setText("Nuova Comanda da Consegnare");
                        		textView_notificationText.setText("Consegna '" + notifica.getNomeOrdinazione() + "' al Tavolo '" + notifica.getNomeTavolo() + "'." );
                        		
                        	} else if(notifica.getNotificationType().equals("TavoloDaPulire")) {
                        		textView_notificationType.setText("Tavolo da Pulire");
                        		textView_notificationText.setText("Pulisci il tavolo '" + notifica.getNomeTavolo() + "'." );
                        	}
                        		
                        }
                        
                }
                return v;
        }
}

	

}