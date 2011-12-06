package com.restaurant.android.cameriere.activities;

import android.app.Activity;
import android.os.Bundle;

import com.restaurant.android.R;

/** 
 * Activity per mostrare l'elenco delle Notifiche.
 * @author Fabio Pierazzi
 */
public class NotificationActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.cameriere_notifications_list);
	  
	}

}