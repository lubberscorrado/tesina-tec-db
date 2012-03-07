package com.restaurant.android.cameriere.notifiche;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.restaurant.android.cameriere.activities.HomeActivity;

/**
 * Questo service ogni 60 secondi controlla se ci sono nuove notifiche
 * per il cameriere. 
 * @author Fabio Pierazzi
 */
public class NotificationUpdaterService extends Service {
	
	private static final String TAG = "NotificationUpdaterService"; 
	private static int demo_counter = 0;
	
	static final int DELAY = 120000; // 2 minuti
	private boolean runFlag = false;
	private NotificationUpdater notificationUpdater;

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
		
		this.notificationUpdater = new NotificationUpdater();
		
		/* Per la gestione notifiche nella status bar */
		this.notification = new Notification(android.R.drawable.stat_notify_more, "", 0);
		this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		demo_counter = 0;
		
		Log.d(TAG, "onCreated");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		
		this.runFlag = true;
		this.notificationUpdater.start();
		
		Log.d(TAG,"onStarted");
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		this.runFlag = false;
		this.notificationUpdater.interrupt();
		this.notificationUpdater = null;
		
		Log.d(TAG,"onDestroy");
	}
	
	/**
	 * Thread che effettua l'update effettivo del 
	 * servizio online.
	 */
	
	private class NotificationUpdater extends Thread {
		public NotificationUpdater() {
			super("NotificationUpdateService-NotificationUpdater");
		}
		
		@Override
		public void run() {
			NotificationUpdaterService notificationUpdaterService = NotificationUpdaterService.this;
			while(notificationUpdaterService.runFlag) {
				Log.d(TAG, "Updater Running");
				try {
					// Some work goes here...
					Log.d(TAG, "Updater ran! demo_counter = " + demo_counter);
					
					
					// BEGIN: parte da rimuovere
					demo_counter++;
//					if(demo_counter % 10 == 0) {
						sendNotifications();
//					}
					// END: parte da rimuovere
					
					Thread.sleep(DELAY);
				} catch(InterruptedException e) {
					notificationUpdaterService.runFlag = false;
				}
			}
		}
	} // Updater
	
	
	/** 
	 * Invia nuove notifiche nella StatusBar 
	 * @author Fabio Pierazzi
	 */
	private void sendNotifications() {
		
		PendingIntent pendingIntent = null;
		
		try {
			Log.d(TAG, "sendNotification'ing");
			
			 Intent contentIntent = new Intent(this, HomeActivity.class);
		     
			 // Questo flag consente di recuperare la HomeActivity, se già attiva, 
			 // eliminando tutte quelle eventualmente sopra di lei nello stack
			 contentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			pendingIntent = PendingIntent.getActivity(
					this,
					0,
					null,
					0);
					
//					this, 
//					-1, 
//					new Intent(this, HomeActivity.class), 
//					PendingIntent.FLAG_UPDATE_CURRENT);
		} catch (Exception e) {
			Log.e(TAG, "Eccezione 1");
		}
		
		
		try {
			this.notification.when = System.currentTimeMillis();

			// Affinché la notifica scompaia dalla StatusBar al click dell'utente
			this.notification.flags |= Notification.FLAG_AUTO_CANCEL;
			
			// Default Sound
			this.notification.defaults |= Notification.DEFAULT_SOUND;
			// Vibrazione
//			this.notification.defaults |= Notification.DEFAULT_VIBRATE;
			
//			CharSequence notificationTitle = "Nuova Notifiche";
//			CharSequence notificationSummary = "Ci sono tante belle nuove notifichelle";
			
			this.notification.setLatestEventInfo(this, "Nuova Notifica", "Ce ne sono tante belle nuove", pendingIntent);
			
		} catch (Exception e) {
			
			Log.e(TAG, "Eccezione 2");
		}


		try {
			
			this.notificationManager.notify(0, this.notification);
			Log.d(TAG, "Notification sent");
		
		} catch (Exception e) {
			Log.e(TAG, "Eccezione 3");
			e.printStackTrace();
		}
		
		
	}
	
	
}





//
//import android.R;
//import android.R.string;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.os.Binder;
//import android.os.IBinder;
//import android.util.Log;
//import android.widget.Toast;
//
//public class NotificationService extends Service {
//	private NotificationManager mNM;
//	
//    // Unique Identification Number for the Notification.
//    // We use it on Notification start, and to cancel it.
//	private int NOTIFICATION = 1;
//	
//    /**
//     * Class for clients to access.  Because we know this service always
//     * runs in the same process as its clients, we don't need to deal with
//     * IPC.
//     */
//    public class NotificationBinder extends Binder {
//        NotificationService getService() {
//            return NotificationService.this;
//        }
//    }
//    
//    
//    @Override
//    public void onCreate() {
//        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        // Display a notification about us starting.  We put an icon in the status bar.
//        showNotification();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i("LocalService", "Received start id " + startId + ": " + intent);
//        // We want this service to continue running until it is explicitly
//        // stopped, so return sticky.
//        return START_STICKY;
//    }
//
//    @Override
//    public void onDestroy() {
//        // Cancel the persistent notification.
//        mNM.cancel(NOTIFICATION);
//
//        // Tell the user we stopped.
//        Toast.makeText(this, "Local service stopped" , Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return mBinder;
//    }
//
//    // This is the object that receives interactions from clients.  See
//    // RemoteService for a more complete example.
//    private final IBinder mBinder = new LocalBinder();
//
//    /**
//     * Show a notification while this service is running.
//     */
//    private void showNotification() {
//        // In this sample, we'll use the same text for the ticker and the expanded notification
//        CharSequence text = getText(R.string.local_service_started);
//
//        // Set the icon, scrolling text and timestamp
//        Notification notification = new Notification(R.drawable.stat_sample, text,
//                System.currentTimeMillis());
//
//        // The PendingIntent to launch our activity if the user selects this notification
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, LocalServiceActivities.Controller.class), 0);
//
//        // Set the info for the views that show in the notification panel.
//        notification.setLatestEventInfo(this, getText(R.string.local_service_label),
//                       text, contentIntent);
//
//        // Send the notification.
//        mNM.notify(NOTIFICATION, notification);
//    }
//
//	
//}
//
