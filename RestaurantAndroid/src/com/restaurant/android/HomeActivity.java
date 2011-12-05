package com.restaurant.android;

import android.app.Activity;
import android.os.Bundle;

/** 
 * Activity principale da mostrare al cameriere
 * come Home Page successivamente al Login 
 * 
 * @author fabio
 */
public class HomeActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.home_activity);
	  
	}

}
