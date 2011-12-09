package com.restaurant.android.cameriere.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import com.restaurant.android.DbManager;
import com.restaurant.android.R;
import com.restaurant.android.UpdateDatabaseService;

/** 
 * Activity principale da mostrare al cameriere
 * come Home Page successivamente al Login. E' una 
 * Tab Activity che mostra le risorse
 * @author fabio
 */
public class HomeActivity extends TabActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.cameriere_home);
	  
	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, NotificationActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("artists").setIndicator("Notifiche",
	                      res.getDrawable(R.drawable.ic_notifications))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, TablesListActivity.class);
	    spec = tabHost.newTabSpec("albums").setIndicator("Elenco Tavoli",
	                      res.getDrawable(R.drawable.ic_food))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(1);
	    
	    
	    startService(new Intent(this, UpdateDatabaseService.class));
	}

}
