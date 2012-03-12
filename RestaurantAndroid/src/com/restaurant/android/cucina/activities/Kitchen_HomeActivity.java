package com.restaurant.android.cucina.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.restaurant.android.DbManager;
import com.restaurant.android.Error;
import com.restaurant.android.R;
import com.restaurant.android.RestaurantApplication;
import com.restaurant.android.cameriere.activities.TablesListActivity;
import com.restaurant.android.cameriere.notifiche.NotificationActivity;
import com.restaurant.android.cameriere.notifiche.NotificationUpdaterService;
import com.restaurant.android.cameriere.prenotazioni.PrenotationsListActivity;

/**
 * Activity principale dell'interfaccia della cucina. 
 * Contiene i Tab che mostrano i cibi e le bevande.
 * @author Fabio Pierazzi
 */
public class Kitchen_HomeActivity extends TabActivity {
	private static final String TAG = "Kitchen_HomeActivity"; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cucina_home);
		
		/** Alloco le risorse per mostrare i vari tab della Home page */
	    Resources res = getResources(); 
	    TabHost tabHost = getTabHost(); 
	    TabHost.TabSpec spec;  
	    Intent intent; 

	    /* Inserisco l'activity con l'elenco delle comande relative al cibo */
	    intent = new Intent().setClass(this, ElencoComandeCibiActivity.class);

	    spec = tabHost.newTabSpec("elencoComandeCibi").setIndicator("Cibi",
	                      res.getDrawable(R.drawable.ic_food))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    /* Inserisco l'activity con l'elenco dei tavoli nel Tab */
	    intent = new Intent().setClass(this, ElencoComandeBevandeActivity.class);
	    spec = tabHost.newTabSpec("elencoComandeBevande").setIndicator("Bevande",
	                      res.getDrawable(R.drawable.ic_coffecup))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    /* Imposto elencoCibi come tab di default */
	    tabHost.setCurrentTab(0);
	}
}
