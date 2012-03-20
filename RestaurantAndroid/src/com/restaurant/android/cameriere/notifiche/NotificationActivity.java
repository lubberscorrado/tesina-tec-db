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
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import com.restaurant.android.cameriere.activities.GestioneOrdinazioneActivity;
import com.restaurant.android.cameriere.activities.Ordinazione;
import com.restaurant.android.cameriere.activities.Table;
import com.restaurant.android.cameriere.activities.TableCardActivity;
import com.restaurant.android.cameriere.activities.TablesListActivity;

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
     
      /* *********************************************************************
       * Assegnazione del listener per il click sulla notifica.
       ***********************************************************************/
      notificationListView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long it) {
			
			Notifica notifica = notification_arrayList.get(position);
			
			final int idComanda = notifica.getIdComanda();
			final int idNotifica = notifica.getIdNotifica();
			
			if(notifica.getTipoNotifica().equals(TipoNotifica.COMANDA_PRONTA)) {
			
						
				/* *********************************************************
				 * Gestione del passaggio di una comanda dallo stato PRONTA
				 * allo stato CONSEGNATA.
				 ***********************************************************/
				AlertDialog.Builder builder = 
	  	    			new AlertDialog.Builder(NotificationActivity.this);
	  	    	
	  	    	builder.setTitle("Gestione Notifica");
	  	    
	  	    	final String[] opzioni = new String[]{"Consegnata","Indietro"};
	  	    	
	  	    	builder.setItems(opzioni,new DialogInterface.OnClickListener() {
	  	    		
	  	    	    public void onClick(DialogInterface dialog, int item_position) {
	  	    	    	if(opzioni[item_position].equals("Consegnata")) {
	  	    	    		Object[] arrayId = new Object[]{idNotifica, idComanda};
	  	    	    		
	  	    	    		new ConsegnaComandaAsyncTask().execute(arrayId);
	  	    	    		
	  	    	    	} else if (opzioni[item_position].equals("Indietro")) {
	  	    	    		
	  	    	    	}
	  	    	    }
	  	    	});
	  	    	builder.show();	
			} else if(notifica.getTipoNotifica().equals(TipoNotifica.TAVOLO_DA_PULIRE)) {
				
				/* **********************************************************
				 * Gestione del passaggio di un tavolo dallo stato DA PULIRE
				 * allo stato LIBERO
				 ************************************************************/
				
				AlertDialog.Builder builder = 
						new AlertDialog.Builder(NotificationActivity.this);
				
				builder.setTitle("Gestione Notifica");
				final int idTavolo = notifica.getIdTavolo();
				final String[] opzioni = new String[] {"Pulito", "Indietro", "Elimina"};
				
				builder.setItems(opzioni, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int item_position) {
						if(opzioni[item_position].equals("Pulito")) {
							
							new PulisciTavoloAsyncTask().execute((Integer)idTavolo);
						} else if(opzioni[item_position].equals("Indietro")) {
							
						}else if(opzioni[item_position].equals("Elimina")) {
							
							DbManager dbManager = new DbManager(getApplicationContext());
							SQLiteDatabase db = dbManager.getWritableDatabase();
							
							db.delete(	"notifiche", 
										"idNotifica=" + idNotifica, 
										null);
							updateListViewNotificheFromDatabase();
							
							db.close();
							dbManager.close();
							
						}
						
					}
				});
				
				builder.show();
			}
		}
      });
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
			
			new GetNotificheAsyncTask().execute((Object[]) null);
			NotificationActivity.updateNofitications = false;
		} else {
			
			/* Non è stata segnalata la presenza di nuove notifiche. Viene semplicemente
			 * aggiornata la listview recuperando le notifiche non ancora gestite dal
			 * database locale */
			updateListViewNotificheFromDatabase();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(receiverRegistered) {
			unregisterReceiver(receiverNotifiche);
			NotificationActivity.receiverRegistered = false;
		}
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
		
		/* Rimuovo il receiver per i broadcast intent del serice */
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
                        
                if(	(textView_notificationType!=null) && (textView_notificationText!=null)) {
                	
                	if(notifica.getTipoNotifica().equals(TipoNotifica.TAVOLO_DA_PULIRE)) {
                		
                		textView_notificationType.setText(	"Tavolo da pulire " + 
                											notifica.getData());
                		textView_notificationText.setText("Tavolo '" + notifica.getNomeTavolo() +
                										  "' da pulire");
                    
                	} else if(notifica.getTipoNotifica().equals(TipoNotifica.COMANDA_PRONTA)) {
                    	textView_notificationType.setText(	"Comanda pronta " + 
                        	    							notifica.getData());
                    	textView_notificationText.setText(	"Consegna '" + 
                        									notifica.getVoceMenu() + 
                        									"'" +
                        									" al Tavolo '" + 
                        									notifica.getNomeTavolo() + 
                        									"'." );
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
			new GetNotificheAsyncTask().execute((Object[]) null);
			NotificationActivity.updateNofitications = false;
		}
	}
	
	/**
    * Async Task che acquisisce le notifiche presenti e aggiorna la list
    * view.
    * @author Guerri Marco
    */
   class GetNotificheAsyncTask extends AsyncTask<Object, Object, Error> {

	   	@Override
   		protected void onPreExecute() {
	   	}
   	
	   	@Override
		protected Error doInBackground(Object... params) {
	   		
	   		DbManager dbManager = new DbManager(getApplicationContext());
   			SQLiteDatabase db = dbManager.getWritableDatabase();
	   		
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
		   		
		   		System.out.println(response);
		   		JSONObject jsonObjectResponse = new JSONObject(response);
		   		
		   		if(jsonObjectResponse.getString("success").equals("true")) {
		   			
		   			/* Recupero dal server la data dell'ultimo aggiornamento in modo
		   			 * che sia sincronizzata con quella di MySQL per la successiva
		   			 * richiesta */
		   			
		   			String dataUltimoAggiornamento = jsonObjectResponse.getString("message");
		   			restApp.setLastNotificationCheckDate(dataUltimoAggiornamento);
		   			
		   			
		   			/* **************************************************************
		   			 * Aggiorno il database inserendo le notifiche appena ottenute
		   			 ****************************************************************/
		   			
		   			JSONArray jsonArrayNotifiche = new JSONArray();
		   			
		   			jsonArrayNotifiche = jsonObjectResponse.getJSONArray("notifiche");
		   			
		   			for(int i=0; i< jsonArrayNotifiche.length(); i++) {
		   				
		   				JSONObject jsonObjectNotifica =  jsonArrayNotifiche.getJSONObject(i);
		   				
		   				/* *********************************************************
		   				 * Inserimento all'interno del database della notifica 
		   				 * appena ottenute.
		   				 ***********************************************************/
		   				ContentValues notifica = new ContentValues();
			   			notifica.put("tipoNotifica", jsonObjectNotifica.getString("tipo"));
			   			notifica.put("nomeTavolo",jsonObjectNotifica.getString("nomeTavolo"));
			   			notifica.put("idTavolo", jsonObjectNotifica.getInt("idTavolo"));
			   			notifica.put("idVoceMenu", jsonObjectNotifica.getInt("idVoceMenu"));
			   			notifica.put("idComanda", jsonObjectNotifica.getInt("idComanda"));
			   			
			   				
			   			/* Se la notifica è di tipo comanda pronta, ricavo dal database 
 						 * la descrizione della voce di menu corrispondente all'id 
 						 * ritornato dal server per non doverla estrarre durante 
 						 * il rendering della list view. Se la notifica riguarda
 						 * un evento differente la voceMenu viene settata su "sconosciuta */
	
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
			   			notifica.put("data", jsonObjectNotifica.getString("lastModified"));
			   		
			   			db.insertOrThrow("notifiche",null, notifica);
		   			}
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
			}finally{
				db.close();
	   			dbManager.close();
			}
   			
	   		return new Error("",false);
	   	}
   	
	   	@Override
	   	protected void onPostExecute(Error error) {
	   		if(error.errorOccurred()) {
	   			Toast.makeText(getApplicationContext(), error.getError(), 50);
	   		} else {
	   			Log.d("NotificationActivity", "Aggiorno la listview delle notifiche");
	   			updateListViewNotificheFromDatabase();
	   		}
	   		
	    }
   }
   
   /**
    * Async Task che gestisce il passaggio della comanda da IN PREPARAZIONE
    * a CONSEGNATA
    * @author Guerri Marco
    */ 
   class ConsegnaComandaAsyncTask extends AsyncTask<Object, Object, Error> {

	   	@Override
   		protected void onPreExecute() {
	   	}
   	
	   	@Override
		protected Error doInBackground(Object... params) {
	   		DbManager dbManager = new DbManager(getApplicationContext());
   			SQLiteDatabase db = dbManager.getWritableDatabase();
   			
	   		try {
	   			int idNotifica= (Integer)params[0];
	   			int idComanda = (Integer)params[1];
		   		/* Richiesta per passare la comanda allo stato consegnata */
	   			
	   			RestaurantApplication restApp = (RestaurantApplication)getApplication();
		   		HashMap<String,String> requestParameters = new HashMap<String,String>();
		   		requestParameters.put("action","UPDATE_STATO");
		   		requestParameters.put("idComanda", "" + idComanda);
		   		requestParameters.put("stato", "CONSEGNATA");
		   		
		   		/* *************************************************
		   		 * Aggiorno la data di verifica delle notifiche. 
		   		 ***************************************************/
		   	
		   		String response = 
							restApp.makeHttpPostRequest(restApp.getHost() + 
														"ClientEJB/gestioneComande", 
														requestParameters);
		   		
		   		JSONObject jsonObjectResponse = new JSONObject(response);
		   		if(jsonObjectResponse.getString("success").equals("true")) {
		   			
		   			/* La comanda è passato nello stato consegnata e può essere 
		   			 * cancellata dal database locale */
		   			db.delete("notifiche", "idNotifica="+idNotifica, null);
		   			
		   		} else {
		   			String message = jsonObjectResponse.getString("message");
		   			return new Error(	"Errore durante la consegna della comanda (" + message +")",
		   								true);
		   		}
		   		
	   		} catch(Exception e) {
	   			
	   			e.printStackTrace();
	   			return new Error(	"Errore durante la consegna della comanda (" +e.toString()+")",
	   								true);
	   		
	   		}finally{
	   			db.close();
	   			dbManager.close();
	   		}
	   		
	   		return new Error("",false);
	   	}
   	
	   	@Override
	   	protected void onPostExecute(Error error) {
	   		
	   	
	   		if(error.errorOccurred()) {
	   			Toast.makeText(getApplicationContext(), error.getError(), 50).show();
	   		} else {
	   			/* Aggiorno la listview delle notifiche. La notifica che è appena stata
	   			 * gestita non compare più in elenco */
	   			updateListViewNotificheFromDatabase();
	   		}
	    }
   }
   
   /**
    * Async task che richiede al server di settare lo stato di un tavolo
    * da "PULIRE" a "LIBERO"
    * @author Guerri Marco
    *
    */
   
   class PulisciTavoloAsyncTask extends AsyncTask<Object, Object, Error> {

	   	@Override
  		protected void onPreExecute() {
	   	}
  	
	   	@Override
		protected Error doInBackground(Object... params) {
	   		
	   		DbManager dbManager = new DbManager(getApplicationContext());
  			SQLiteDatabase db = dbManager.getWritableDatabase();
  			
	   		try {
	   			int idTavolo = (Integer)params[0];
		   		
	   			/* Richiesta per passare la comanda allo stato consegnata */
	   			RestaurantApplication restApp = (RestaurantApplication)getApplication();
		   		HashMap<String,String> requestParameters = new HashMap<String,String>();
		   		
		   		requestParameters.put("action","PULISCI_TAVOLO");
		   		requestParameters.put("idTavolo", ""+idTavolo);
		   		
		   		
		   		/* *************************************************
		   		 * Aggiorno la data di verifica delle notifiche. 
		   		 ***************************************************/
		   	
		   		String response = 
							restApp.makeHttpPostRequest(restApp.getHost() + 
														"ClientEJB/gestioneComande", 
														requestParameters);
		   		
		   		JSONObject jsonObjectResponse = new JSONObject(response);
		   		
		   		if(jsonObjectResponse.getString("success").equals("true")) {
		   			
		   			/* Elimino la notifica relativa al tavolo da pulire */
		   			db.delete(	"notifiche", 
		   						"idTavolo="+idTavolo + " AND tipoNotifica='TAVOLO_DA_PULIRE'", 
		   						null);
		   		} else {
		   			return new Error(	"Errore durante la pulitura del tavolo ("+
		   								jsonObjectResponse.getString("message") + ")", true);
		   		}
		   		
	   		} catch(Exception e) {
	   			return new Error("Errore durante la pulitura del tavolo (" +e.toString()+")",
	   							true);
	   		}finally{
	   			db.close();
	   			dbManager.close();
	   		}
	   		
	   		return new Error("",false);
	   	}
  	
	   	@Override
	   	protected void onPostExecute(Error error) {
	   		
	   	
	   		if(error.errorOccurred()) {
	   			Toast.makeText(getApplicationContext(), error.getError(), 50).show();
	   		} else {
	   			/* Aggiorno la listview delle notifiche. La notifica che è appena stata
	   			 * gestita non compare più in elenco */
	   			updateListViewNotificheFromDatabase();
	   		}
	    }
  }
   
   
   /**
    * Aggiorna la lista associata alla listview delle notifiche
    * recuperandole dal database locale.
    */
   
   public void updateListViewNotificheFromDatabase() {
	   

	   DbManager dbManager = new DbManager(getApplicationContext());
	   SQLiteDatabase db = dbManager.getWritableDatabase();
	   notification_arrayList.clear();
	   
	   Cursor cursorNotifiche;
	
	   cursorNotifiche = 
			   db.query("notifiche", 
						new String[] {"idNotifica,tipoNotifica,idComanda,idTavolo,nomeTavolo,idVoceMenu,voceMenu,data"}, 
						"",
						null,
						null,
						null,
						"idNotifica DESC",
						null);
	   
	  
	   cursorNotifiche.moveToFirst();
	   while(!cursorNotifiche.isAfterLast()) {
		   
		   Notifica notifica = new Notifica();
		   
		   notifica.setIdNotifica(cursorNotifiche.getInt(0));
		   
		   notifica.setTipoNotifica(TipoNotifica.valueOf(cursorNotifiche.getString(1)));
		   notifica.setIdComanda(cursorNotifiche.getInt(2));
		   notifica.setIdTavolo(cursorNotifiche.getInt(3));
		   notifica.setNomeTavolo(cursorNotifiche.getString(4));
		   notifica.setIdVoceMenu(cursorNotifiche.getInt(5));
		   notifica.setVoceMenu(cursorNotifiche.getString(6));
		   notifica.setData(cursorNotifiche.getString(7));
		   
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