package com.restaurant.android.cameriere.activities;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;



public class TablesListService extends Service {

   @Override
   	public IBinder onBind(Intent intent) {
   		return null;
   	}

   	@Override
   	public void onCreate() {
   		super.onCreate();
   	
   	}
    	
   	@Override
   	public int onStartCommand(Intent intent, int flags, int startId) {
   		super.onStartCommand(intent,  flags, startId);
   		
   		return START_STICKY;
   	}
    	
   	@Override
   	public void onDestroy() {
   	
   	}
        
}