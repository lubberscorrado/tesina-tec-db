package com.restaurant.android.cameriere.notifiche;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.restaurant.android.RestaurantApplication;
import com.restaurant.android.cameriere.activities.HomeActivity;

/**
 * Questo service ogni 60 secondi controlla se ci sono nuove notifiche
 * per il cameriere. 
 * @author Fabio Pierazzi, Guerri Marco
 */
public class NotificationUpdaterService extends Service {
	
	private static final String TAG = "NotificationUpdaterService"; 
	
	static final int DELAY = 30000; // 2 minuti
	private boolean runFlag = false;
	private NotificationUpdaterThread notificationUpdaterThread;

	/** Per la gestione dell'invio delle notifiche nella StatusBar */
	private static final String NEW_NOTIFICATION_INTENT = "com.restaurant.android.NEW_NOTIFICATIONS";
	private static final String NEW_NOTIFICATIONS_EXTRA_COUNT = "NEW_NOTIFICATIONS_EXTRA_COUNTS";
	private static final String RECEIVE_TIMELINE_NOTIFICATIONS = "com.restaurant.android.RECEIVE_TIMELINE_NOTIFICATIONS";
	
	private NotificationManager notificationManager; 
	private Notification notification; 
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		
		super.onCreate();
		
		this.notificationUpdaterThread = new NotificationUpdaterThread();
		/* Per la gestione notifiche nella status bar */
		this.notification = new Notification(android.R.drawable.stat_notify_more, "", 0);
		this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Log.d(TAG, "onCreated");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		
		/* Avvio il thread di verifica delle notifiche */
		this.runFlag = true;
		this.notificationUpdaterThread.start();
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		this.runFlag = false;
		this.notificationUpdaterThread.interrupt();
		this.notificationUpdaterThread = null;
	}
	
	/**
	 * Thread che richiede al server eventuali notifiche da segnalare
	 * all'utente.
	 */
	
	private class NotificationUpdaterThread extends Thread {
		public NotificationUpdaterThread() {
			super("NotificationUpdateService-NotificationUpdater");
		}
		
		@Override
		public void run() {
			NotificationUpdaterService notificationUpdaterService = NotificationUpdaterService.this;
			while(notificationUpdaterService.runFlag) {
				
				try {
					
					/* ********************************************************************
					 * Verifico se sono presenti notifiche successive alla data di ultmo
					 * controllo. La data di ultimo controllo non deve essere modficata
					 * poichè le notifiche effettive devono essere recuperate dell'activity
					 **********************************************************************/
					RestaurantApplication restApp = (RestaurantApplication)getApplication();
			   		HashMap<String,String> requestParameters = new HashMap<String,String>();
			   		
			   		requestParameters.put("action","CHECK_NOTIFICHE");
			   		
			   		requestParameters.put(	"lastDate", 
			   								((RestaurantApplication)getApplication())
			   								.getLastNotificationCheckDate());
			   		
			   		Log.d("NotificationUpdater", "Verifico con data " + 
			   									((RestaurantApplication)getApplication())
			   									.getLastNotificationCheckDate());
			   		
			   		String response = 
			   				restApp.makeHttpPostRequest(restApp.getHost() + 
			   											"ClientEJB/gestioneNotifiche", 
														requestParameters);
			   	
			   		JSONObject jsonObjectResponse = new JSONObject(response);
			   		
			   		if(jsonObjectResponse.getString("success").equals("true")) {
			   			
			   			if(jsonObjectResponse.getString("message").equals("CHECK_NOTIFICHE_PRESENTI")) {
			   				
			   				/* *********************************************************** 
			   				 * Sono presenti nuove notifiche rispetto all'ultima data di
			   				 * aggiornamento. 
			   				 * - Notifico all'activity l'evento. Se è running provvederà
			   				 * a recuperare gli aggiornamenti
			   				 * - Invio una notifica nella status bar
			   				 *************************************************************/
			   				Intent intent = new Intent("com.restaurant.android.NUOVA_NOTIFICA");
							notificationUpdaterService.sendBroadcast(intent);
							sendNotifications("Sono presenti nuove notifiche");
						}
			   		} else {
			   			
			   			
			   		}
			   		
										
				
					
					Thread.sleep(DELAY);
					
				} catch(InterruptedException e) {
					notificationUpdaterService.runFlag = false;
				} catch (ClientProtocolException e) {
					
				} catch (IOException e) {
					
				} catch (JSONException e) {
					
				}
			}
		}
	} 
	
	/** 
	 * Invia nuove notifiche nella StatusBar 
	 * @author Fabio Pierazzi
	 */
	private void sendNotifications(String notifica) {
		
		PendingIntent pendingIntent = null;
		try {
			
			Intent intent = new Intent(this, HomeActivity.class);
			
			/* Segnalo all'activity, tramite l'intent, la necessità di refreshare la lista
			 * delle notifiche Tramite l'intent non è possibile attivare in modo specifico
			 * il tab delle notifiche. E' necessario attivare prima la TabActivity e
			 * riconoscere tramite un parametro passato con l'intent, la richiesta di 
			 * attivazione del tab di notifiche */
			
			intent.putExtra("UPDATE_NOTIFICHE", "TRUE");
			
			/* Segnalo all'activity delle notifiche che deve aggiornarsi. La flag viene
			 * controllata all'avvio dell'activity di notifiche per permettere l'aggiornamento
			 * nel caso venga avviata senza ricevere un broadcast intent. Viene resettata
			 * dopo che le notifiche vengono acquisite.  */
			
			/* Se l'activity non è in esecuzione l'accesso a una variabile statica fa esplodere
			 * tutto? */
			
			NotificationActivity.updateNofitications = true;
			
			pendingIntent = PendingIntent.getActivity(	this, 
														-1, 
														intent, 
														PendingIntent.FLAG_UPDATE_CURRENT);
			
			this.notification.when = System.currentTimeMillis();
			// Affinché la notifica scompaia dalla StatusBar al click dell'utente
			this.notification.flags |= Notification.FLAG_AUTO_CANCEL;
			// Default Sound
			this.notification.defaults |= Notification.DEFAULT_SOUND;
			// Vibrazione
			//this.notification.defaults |= Notification.DEFAULT_VIBRATE;
			this.notification.setLatestEventInfo(this, "Nuova Notifica", "Ce ne sono tante belle nuove", pendingIntent);
			this.notificationManager.notify(0, this.notification);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

