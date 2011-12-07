package com.restaurant.android.cameriere.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

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
	
	public void onResume() {
		super.onResume();
		Toast.makeText(getApplicationContext(), "Resumed NotificationActivity", 5).show();
		
		Button buttonTest = (Button) findViewById(R.id.button1);
		
		// Provo a cambiare activity
		buttonTest.setOnClickListener(new OnClickListener() {
		  	@Override
		  	public void onClick(View v) {
		  		//-----------------------
		        // Open New Activity
		  		//-----------------------
		  		Intent myIntent = new Intent(NotificationActivity.this, MenuListActivity.class);
		  		NotificationActivity.this.startActivity(myIntent);
		  	}
		  });
		
	}
	

}