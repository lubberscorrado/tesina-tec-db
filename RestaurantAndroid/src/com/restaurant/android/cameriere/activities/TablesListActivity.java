package com.restaurant.android.cameriere.activities;

import android.app.Activity;
import android.os.Bundle;

import com.restaurant.android.R;

/** 
 * Activity per mostrare al cameriere l'elenco dei tavoli.
 * @author Fabio Pierazzi
 */
public class TablesListActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.cameriere_tables_list);
	  
	}

}