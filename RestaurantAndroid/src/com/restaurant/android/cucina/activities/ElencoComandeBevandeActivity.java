package com.restaurant.android.cucina.activities;

import com.restaurant.android.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activity della cucina che serve per mostrare l'elenco delle 
 * bevande richieste. 
 * 
 * @author Fabio Pierazzi
 */
public class ElencoComandeBevandeActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cucina_elenco_bevande_list);
		
	}
}
