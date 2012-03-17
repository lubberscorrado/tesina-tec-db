package com.restaurant.android.cameriere.notifiche;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.restaurant.android.DbManager;
import com.restaurant.android.Error;
import com.restaurant.android.R;
import com.restaurant.android.RestaurantApplication;
import com.restaurant.android.Utility;

/** 
 * Activity per mostrare l'elenco delle notifiche.
 * @author Fabio Pierazzi
 */
public class NotificationActivity extends Activity {
	
	private static final String TAG = "NotificationActivity"; 
	
	/* Oggetti per la gestione dei messaggi inviati dal service di verifica delle notifiche */
	private IntentFilter filter;
	private ReceiverNotifiche receiverNotifiche;
	
	private static boolean receiverRegistered = false;
	public static boolean updateNofitications = false;
	
	private ListView notificationListView;
	private boolean runThread;
	private boolean pauseThread;
	// private ProgressDialog m_ProgressDialog = null;
	private ArrayList<Notifica> notification_arrayList = null;
    private NotificationAdapter notification_adapter;
   

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
	  super.onCreate(savedInstanceState);
	  Log.d(TAG,"OnCreate");
	  setContentView(R.layout.cameriere_notifications_list);
	  
	  
	  // Recupero il riferimento ad una ListView
	  this.notificationListView = (ListView) findViewById(R.id.listView_notificationsList);
	  
	  // Operazioni necessarie al corretto funzionamento della listView
	  notification_arrayList = new ArrayList<Notifica>();
      this.notification_adapter = new NotificationAdapter(getApplicationContext(), 
    		  							R.layout.cameriere_tables_list_row, 
    		  							notification_arrayList);
      
      notificationListView.setAdapter(this.notification_adapter);
      
      /* Creo l'intent filter che nel metodo onResume sarà associato
       * al receivere del broadcast intent */
      filter = new IntentFilter("com.restaurant.android.NUOVA_NOTIFICA");
      receiverNotifiche = new ReceiverNotifiche();
     
      
      
      
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
		Log.d(TAG,"OnResume");
		
		Log.d("NotificationActivity", "REGISTRO IL RECEIVER");
		
		/* Registro il receiver per il broadcast intent inviato dal service
		 * di gestione delle notifiche */
		
		if(!receiverRegistered) {
			registerReceiver(receiverNotifiche, filter);
			NotificationActivity.receiverRegistered = true;
		}
		
		if(NotificationActivity.updateNofitications == true) {
			
			/* E' stata segnalata la presenza di nuove notifiche da parte del
			 * service. Vengono richieste le nuove notifiche, salvate sul database
			 * locale e aggiornata la list view */
			
			Log.d("NOtificationActivity", "Notifica avvenuta quando non ero attiva");
			new GetNotificheAsyncTask().execute((Object[]) null);
			NotificationActivity.updateNofitications = false;
		} else {
			
			/* Non è stata segnalata la presenza di nuove notifiche. Viene semplicemente
			 * aggiornata la listview recuperando le notifiche non ancora gestite dal
			 * database locale */
			updateListViewNotifiche();
			
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG,"OnPause");
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG,"OnStart");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG,"OnStop");
		
		/* Rimouvo il receiver per i broadcast intent del serice */
		if(receiverRegistered) {
			unregisterReceiver(receiverNotifiche);
			NotificationActivity.receiverRegistered = false;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"OnDestroy");
	}

	
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
                        
                		TextView textView_notificationType = 
                				(TextView) v.findViewById(R.id.textView_notificationType);
                        
                		TextView textView_notificationText = 
                        		(TextView) v.findViewById(R.id.textView_notificationText);
                        
                        // Modifico il contenuto delle TextView in ciascuna delle righe della tabella
                        if(	(textView_notificationType != null) && 
                        	(textView_notificationText != null)) {
                        	
                        	// A seconda del tipo di notifica (3 tipi), mostro un messaggio diverso
                        	if(notifica.getTipoNotifica().equals(TipoNotifica.TAVOLO_ASSEGNATO)) {

                        		textView_notificationText.setText("Ti e' stato assegnato il tavolo '" + notifica.getNomeTavolo() + "'.");
                        		
                        	} else if(	notifica.getTipoNotifica()
                        				.equals(TipoNotifica.COMANDA_PRONTA)) {
                        		
                        	    textView_notificationType.setText(	"Comanda pronta " + 
                        	    									notifica.getData());
                        	    
                        		textView_notificationText.setText(	"Consegna '" + 
                        											notifica.getVoceMenu() + 
                        											"'" +
                        											" al Tavolo '" + 
                        											notifica.getNomeTavolo() + 
                        											"'." );
                        		
                        	} else if(notifica.getTipoNotifica().equals(TipoNotifica.TAVOLO_DA_PULIRE)) {
                        		textView_notificationType.setText("Tavolo da Pulire");
                        		textView_notificationText.setText("Pulisci il tavolo '" + notifica.getNomeTavolo() + "'." );
                        	}
                        		
                        }
                        
                }
                return v;
        }
	}
	
	/**
	 * Classe che rappresenta il broadcast receiver per il messaggio inviato dal service
	 * di verifica delle notifiche.
	 * @author Guerri Marco 
	 */
	
	class ReceiverNotifiche extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("NotificationActivity","Ricevuto intent di aggiornamento");
			new GetNotificheAsyncTask().execute((Object[]) null);
			NotificationActivity.updateNofitications = false;
		}
	}
	
	/************************************************************************
    * Async Task che acquisisce le notifiche presenti e aggiorna la list
    * view.
    * @author Guerri Marco
    *************************************************************************/
   class GetNotificheAsyncTask extends AsyncTask<Object, Object, Error> {

	   	@Override
   		protected void onPreExecute() {
	   	}
   	
	   	@Override
		protected Error doInBackground(Object... params) {
	  
	   		try {
				RestaurantApplication restApp = (RestaurantApplication)getApplication();
		   		HashMap<String,String> requestParameters = new HashMap<String,String>();
		   		requestParameters.put("action","GET_NOTIFICHE");
		   		requestParameters.put(	"lastDate", 
		   								((RestaurantApplication)getApplication())
		   								.getLastNotificationCheckDate());
		   		
		   		
		   		/* *************************************************
		   		 * Aggiorno la data di verifica delle notifiche. 
		   		 ***************************************************/
		   		restApp.setLastNotificationCheckDate(
		   				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		   		
		   		String response = 
							restApp.makeHttpPostRequest(restApp.getHost() + 
														"ClientEJB/gestioneNotifiche", 
														requestParameters);
		   		
		   		JSONObject jsonObjectResponse = new JSONObject(response);
		   		
		   		if(jsonObjectResponse.getString("success").equals("true")) {
		   			
		   			/* Recupero dal server la data dell'ultimo aggiornamento in modo
		   			 * che sia sincronizzata con quella di MySQL per la successiva
		   			 * richiesta */
		   			
		   			String dataUltimoAggiornamento = jsonObjectResponse.getString("message");
		   			restApp.setLastNotificationCheckDate(dataUltimoAggiornamento);
		   			
		   			Log.d(	"Notification Activity", "EFFETTUATA RICHIESTA DI AGGIORNAMENTO");
		   			
		   			/* **************************************************************
		   			 * Aggiorno il database inserendo le notifiche appena ottenute
		   			 ****************************************************************/
		   			DbManager dbManager = new DbManager(getApplicationContext());
		   			SQLiteDatabase db = dbManager.getWritableDatabase();
		   			
		   			JSONArray jsonArrayNotifiche = new JSONArray();
		   			
		   			jsonArrayNotifiche = jsonObjectResponse.getJSONArray("notifiche");
		   			
		   			Log.d("NotificationActivity", response);
		   			for(int i=0; i< jsonArrayNotifiche.length(); i++) {
		   				JSONObject jsonObjectNotifica =  jsonArrayNotifiche.getJSONObject(i);
		   				
		   				if(jsonObjectNotifica.getString("tipo").equals("COMANDA_PRONTA")) {
		   					
		   					Log.d("NotificationActivity", "Inserisco comanda pronta");
		   					/* *********************************************************
		   					 * Inserimento all'interno del database della notifica di
		   					 * comanda pronta 
		   					 ***********************************************************/
		   					ContentValues notifica = new ContentValues();
			   				notifica.put("tipoNotifica", jsonObjectNotifica.getString("tipo"));
			   				notifica.put("idTavolo", jsonObjectNotifica.getInt("idTavolo"));
			   				notifica.put("nomeTavolo",jsonObjectNotifica.getString("nomeTavolo"));
			   				notifica.put("idVoceMenu", jsonObjectNotifica.getInt("idVoceMenu"));
			   				
			   				/* Ricavo dal database anche la descrizione della voce di menu
			   				 * per non doverla estrarre durante il rendering della list
			   				 * view */
			   				Cursor cursorVoceMenu;
			   				
			   				cursorVoceMenu = db.query(	"vocemenu", 
			   											new String[] {"nome"}, 
			   											"idVoceMenu=" + 
			   											jsonObjectNotifica.getInt("idVoceMenu"),
			   											null,
			   											null,
			   											null,
			   											null,
			   											null);
			   				
			   				
			   				String voceMenu = "Sconosciuta";
			   				
			   				cursorVoceMenu.moveToFirst();
			   				while(!cursorVoceMenu.isAfterLast()) {
			   					/* Il ciclo dovrebbe essere eseguito una sola volta */
			   					voceMenu = cursorVoceMenu.getString(0);
			   					cursorVoceMenu.moveToNext();
			   				}
			   				cursorVoceMenu.close();
			   				notifica.put("voceMenu", voceMenu);
			   				
			   				Date dataNotifica;
							try {
								dataNotifica = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataUltimoAggiornamento);
							} catch (ParseException e) {
								
								/* Se si verifica un qualche errore durante il parsing assegno
								 * semplicemente la data dello smart phone locale */
								dataNotifica = new Date();
							}
			   				
							notifica.put("data",new SimpleDateFormat("HH:mm").format(dataNotifica));
			   				db.insertOrThrow("notifiche",null, notifica);
		   				}
		   			}
		   			db.close();
		   			dbManager.close();
		   		} else {
		   			
		   			Toast.makeText(getApplicationContext(), 
		   							"Errore durante la richiesta delle notifiche", 
		   							50);
		   		}
		   		
			} catch (ClientProtocolException e) {
				return new Error(	"Errore di comunicazione durante la richiesta delle " +
									"notifiche",true);
				
			} catch (IOException e) {
				return new Error(	"Errore di comunicazione durante la richiesta delle " +
									"notifiche", true);
			} catch (JSONException e) {
				return new Error(	"Errore di decodifica della risposta dal server durante "+
									"la richiesta delle modifiche", true);
			}
	   		return new Error("",false);
	   	}
   	
	   	@Override
	   	protected void onPostExecute(Error error) {
	   		if(error.errorOccurred()) {
	   			Toast.makeText(getApplicationContext(), error.getError(), 50);
	   		} else {
	   			Log.d("NotificationActivity", "Aggiorno la listview delle notifiche");
	   			updateListViewNotifiche();
	   		}
	   		
	    }
   }
   
   /**
    * Aggiorna la lista associata alla listview delle notifiche
    * recuperandole dal database locale.
    */
   
   public void updateListViewNotifiche() {
	   
	   Log.d("List view notifiche","UPDATE UPDATE");
	   DbManager dbManager = new DbManager(getApplicationContext());
	   SQLiteDatabase db = dbManager.getWritableDatabase();
	   notification_arrayList.clear();
	   
	   Cursor cursorNotifiche;
	
	   cursorNotifiche = 
			   db.query("notifiche", 
						new String[] {"tipoNotifica,idTavolo,nomeTavolo,idVoceMenu,voceMenu,data"}, 
						"",
						null,
						null,
						null,
						"idNotifica DESC",
						null);
	   
	  
	   cursorNotifiche.moveToFirst();
	   while(!cursorNotifiche.isAfterLast()) {
		   
		 
		   Notifica notifica = new Notifica();
		   
		   notifica.setTipoNotifica(TipoNotifica.valueOf(cursorNotifiche.getString(0)));
		   notifica.setData(cursorNotifiche.getString(5));
		   notifica.setIdTavolo(cursorNotifiche.getInt(1));
		   notifica.setIdVoceMenu(cursorNotifiche.getInt(3));
		   notifica.setVoceMenu(cursorNotifiche.getString(4));
		   notifica.setNomeTavolo(cursorNotifiche.getString(2));
		   
		   notification_arrayList.add(notifica);
		   
		   cursorNotifiche.moveToNext();
		   
	   }
	   notification_adapter.notifyDataSetChanged();
	   Utility.setListViewHeightBasedOnChildren(notificationListView);
	   
	   cursorNotifiche.close();
	   db.close();
	   dbManager.close();
	   
   }
}