package com.restaurant.android;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.CookieManager;
import java.net.CookieStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.cookie.Cookie;
import org.json.JSONException;
import org.json.JSONObject;

import com.restaurant.android.cameriere.activities.HomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	/* Alcune variabili private per la gestione del login.  */
	private EditText etUsername;
	private EditText etPassword;
	private Button btnLogin;
	private Button btnCancel;
	private RadioButton radioButtonCameriere;
	private RadioButton radioButtonCucina;
	
	/* HashMap contenente i parametri da inviare nella richiesta post */
	private HashMap<String, String> postParameters;
	
	TextView lblResult;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        
        /* Queste righe vanno DOPO "setContentView" */
        
        etUsername = (EditText)findViewById(R.id.username);
        etPassword = (EditText)findViewById(R.id.password);
        btnLogin = (Button)findViewById(R.id.login_button);
        btnCancel = (Button)findViewById(R.id.cancel_button);

        
        radioButtonCameriere = (RadioButton)findViewById(R.id.radioButtonCameriere);
        radioButtonCucina = (RadioButton)findViewById(R.id.radioButtonCucina);
        
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String username = settings.getString("username", "");
        String password = settings.getString("password", "");
        etUsername.setText(username);
        etPassword.setText(password);
       
        /* Imposto il listener del bottone di Login */
        btnLogin.setOnClickListener(new OnClickListener() {
		  	@SuppressWarnings("unchecked")
			@Override
		  	public void onClick(View v) {
		  		
		  		// Check Login
		  		String username = etUsername.getText().toString();
		  		String password = etPassword.getText().toString();
		  		
		        // We need an Editor object to make preference changes.
		        // All objects are from android.context.Context
		  		SharedPreferences settings = getSharedPreferences("RESTAURANT", 0);
		        SharedPreferences.Editor editor = settings.edit();
		        editor.putString("username", username);
		        editor.putString("password", password);
		        
		        // Commit the edits!
		        editor.commit();
		  
		        postParameters = new HashMap<String,String>();
		        postParameters.put("user", username);
		        postParameters.put("password", password);
		        
		        new LoginTask().execute(postParameters);

		  	}
        });

     /* Se clicco su "Cancel", esce */
     btnCancel.setOnClickListener(new OnClickListener() {
		  	@Override
		  	public void onClick(View v) {
		  		// Close the application
		  		finish();
		  	}
		  });
    }
    
	 /*****************************************************************
	  * Async Task che gestisce le richieste di login verso il server *
	  *****************************************************************/
     class LoginTask extends AsyncTask<HashMap<String, String>, Object, Error> {
        	  
     	@Override
		protected Error doInBackground(HashMap<String, String>... hashMap) {
		    		
       		Boolean logged = false;
			RestaurantApplication restApp = ((RestaurantApplication)getApplication());

			try {
				
				String url = ((RestaurantApplication)getApplication()).getHost();
				String responseBody = restApp.makeHttpPostRequest(	url + "ClientEJB/login", 
																	hashMap[0]);
			        			
				Log.e("ResponseBody", responseBody);
				
				/*****************************************
				 * Decodifica della risposta del server **
				 *****************************************/
					
				JSONObject jObject = new JSONObject(responseBody);
					
		        if(jObject.getBoolean("success") == true) 
		        	logged = true;
		        	
				Log.e("LoginTask", "isCassiere: " + jObject.getJSONObject("privs").getString("isCassiere"));
				Log.e("LoginTask", "isCuoco: " + jObject.getJSONObject("privs").getString("isCuoco"));
					
				if(logged) {
						
					if( (radioButtonCameriere.isChecked() && !jObject.getJSONObject("privs").getString("isCassiere").equals("true")) ||
						(radioButtonCucina.isChecked() && !jObject.getJSONObject("privs").getString("isCuoco").equals("true"))) {
							
						/* Il login è stato effettuato correttamente ma non sia hanno i privilegi per accedere alla 
						 * funzionalità richiesta  */
				
						logged = false;
					}
				}
						
			} catch (ClientProtocolException e) {
				Log.e("LoginTask", "Eccezione ClientProtocolException");
				return  new Error("Errore durante la comunicazione con il server", true);
			} catch (IOException e) {
				Log.e("LoginTask", "Eccezione IO" + e.toString());
				return  new Error("Errore di connettività", true);
			} catch (JSONException e) {
				return  new Error("Errore durante la lettura della risposta dal server", true);
			} 
			return new Error("Log in effettuato con successo", false);
		}
        	
        /* Metodo chiamato dal thread che crea l'oggetto AsyncTask */
        @Override
        protected void onPostExecute(Error error) {
        	if(error.errorOccurred()) { 
        		
        		Toast.makeText(getApplicationContext(), error.getError(), 20).show();
        		
        	} else {
        		/* Apro la nuova attività a seconda della funzionalità richiesta dall'utente */
				
				Intent myIntent = new Intent(LoginActivity.this, HomeActivity.class);
		  		LoginActivity.this.startActivity(myIntent);
		  		Toast.makeText(getApplicationContext(), "Login effettuato con successo!", 20).show();
		  		
        	}
        }
   
    }
}