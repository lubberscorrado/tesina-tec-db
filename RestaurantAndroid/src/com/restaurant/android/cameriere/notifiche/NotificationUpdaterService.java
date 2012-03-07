package com.restaurant.android.cameriere.notifiche;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Questo service ogni 60 secondi controlla se ci sono nuove notifiche
 * per il cameriere. 
 * @author Fabio Pierazzi
 */
public class NotificationUpdaterService extends Service {
	
	private static final String TAG = "NotificationUpdaterService"; 
	
	static final int DELAY = 30000; // 30 secondi
	private boolean runFlag = false;
	private NotificationUpdater notificationUpdater;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		this.notificationUpdater = new NotificationUpdater();
		
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
					Log.d(TAG, "Updater ran");
					Thread.sleep(DELAY);
				} catch(InterruptedException e) {
					notificationUpdaterService.runFlag = false;
				}
			}
		}
	} // Updater
	
	
	
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
