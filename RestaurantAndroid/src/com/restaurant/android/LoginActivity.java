package com.restaurant.android;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.restaurant.android.cameriere.activities.HomeActivity;
import com.restaurant.android.cucina.activities.Kitchen_HomeActivity;

public class LoginActivity extends Activity implements OnClickListener {
	
	private final static String TAG = "LoginActivity";
	
	private Boolean logged = false;
	
	/* Alcune variabili private per la gestione del login.  */
	private EditText etUsername;
	private EditText etPassword;
	private EditText etIdTenant;
	private Button btnLogin;
	private Button btnCancel;
	private RadioButton radioButtonCameriere;
	private RadioButton radioButtonCucina;
	private ProgressDialog progressDialog;
	
	/* HashMap contenente i parametri da inviare nella richiesta post */
	private HashMap<String, String> postParameters;
	
	TextView lblResult;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        
        /* logged = true, se può effettuare il login */
        this.logged = false;
        
        /* Queste righe vanno DOPO "setContentView" */
        etIdTenant = (EditText)findViewById(R.id.login_idTenant);
        etUsername = (EditText)findViewById(R.id.login_username);
        etPassword = (EditText)findViewById(R.id.login_password);
        btnLogin = (Button)findViewById(R.id.login_button);
        btnCancel = (Button)findViewById(R.id.cancel_button);

        
        radioButtonCameriere = (RadioButton)findViewById(R.id.radioButtonCameriere);
        radioButtonCucina = (RadioButton)findViewById(R.id.radioButtonCucina);
        
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences settings = getSharedPreferences("RESTAURANT", 0);
        String username = settings.getString("username", "");
        String password = settings.getString("password", "");
        String idTenant = settings.getString("idTenant", "");
        
        etIdTenant.setText(idTenant);
        etUsername.setText(username);
        etPassword.setText(password);
       
        /* Imposto il listener del bottone di Login */
        btnLogin.setOnClickListener(this);
        
	     /* Se clicco su "Cancel", esce */
	     btnCancel.setOnClickListener(new OnClickListener() {
			  	@Override
			  	public void onClick(View v) {
			  		// Close the application
			  		finish();
			  	}
	    });
    }
    
    @Override
	public void onResume() {
		super.onResume();
		
		Log.d("HomeActivity", "Effettuo il logout");
		RestaurantApplication restApp = (RestaurantApplication)getApplication();
		HashMap<String, String> postParametersMap = new HashMap<String,String>();;
		
		postParametersMap.put("action", "logout");
	
		try {
			String response = ((RestaurantApplication)getApplication()).
									makeHttpPostRequest(restApp.getHost() + 
														"ClientEJB/login", 
														postParametersMap);
			JSONObject jsonObjecResponse = new JSONObject(response);
			
			if(jsonObjecResponse.getString("success").equals("true")) {
				this.logged = false;
			}
		
		} catch (Exception e) {
			/* Errore durante il logout, inutile notificarlo... */
		}
	}
    
	 /*****************************************************************
	  * Async Task che gestisce le richieste di login verso il server *
	  *****************************************************************/
     class LoginTask extends AsyncTask<HashMap<String, String>, Object, Error> {
        	  
     	@Override
		protected Error doInBackground(HashMap<String, String>... hashMap) {
		    		
     		String errorMessage = "";
       		logged = false;
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
		        else
		        	errorMessage = jObject.getString("message");
		        	
				Log.e("LoginTask", "isCameriere: " + jObject.getJSONObject("privs").getString("isCameriere"));
				Log.e("LoginTask", "isCuoco: " + jObject.getJSONObject("privs").getString("isCuoco"));
					
				if(logged) {
					
					if(radioButtonCameriere.isChecked() && !jObject.getJSONObject("privs").getString("isCameriere").equals("true")) {
						logged = false;
						return new Error("Permessi non opportuni per visualizzare l'interfaccia Cameriere.", true);
					}
					
					if( radioButtonCucina.isChecked() && !jObject.getJSONObject("privs").getString("isCuoco").equals("true")) {
						logged = false;
						return new Error("Permessi non opportuni per visualizzare l'interfaccia Cucina.", true);
					}
				}
				
						
			} catch (ClientProtocolException e) {
				Log.e("LoginTask", "Eccezione ClientProtocolException");
				return  new Error("Errore durante la comunicazione con il server", true);
			} catch (IOException e) {
				Log.e("LoginTask", "Eccezione IO" + e.toString());
				return  new Error("Errore di connettività", true);
			} catch (JSONException e) {
				if(errorMessage.equals(""))
					return  new Error("Errore durante la lettura della risposta dal server", true);
				else 
					return  new Error(errorMessage, true);
			} finally {
				progressDialog.dismiss();
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
				if(radioButtonCameriere.isChecked() && logged == true) {
					Log.i(TAG, "Login eseguito con successo per la richiesta dell'interfaccia Cameriere");
					Intent myIntent = new Intent(LoginActivity.this, HomeActivity.class);
			  		LoginActivity.this.startActivity(myIntent);
				} else if(radioButtonCucina.isChecked() && logged == true) {
					Log.i(TAG, "Login eseguito con successo per la richiesta dell'interfaccia Cucina");
					Intent myIntent = new Intent(LoginActivity.this, Kitchen_HomeActivity.class);
			  		LoginActivity.this.startActivity(myIntent);
				}
				
		  		//Toast.makeText(getApplicationContext(), "Login effettuato con successo!", 20).show();
		  		
        	}
        }
    }

	@Override
	public void onClick(View v) {
		// Check Login
  		String idTenant = etIdTenant.getText().toString();
		String username = etUsername.getText().toString();
  		String password = etPassword.getText().toString();
  		
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
  		SharedPreferences settings = getSharedPreferences("RESTAURANT", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("idTenant", idTenant);
        
        // Commit the edits!
        editor.commit();
        
        postParameters = new HashMap<String,String>();
        postParameters.put("username", username);
        postParameters.put("password", password);
        postParameters.put("idTenant", idTenant);
        
        if(radioButtonCameriere.isChecked()) 
        	postParameters.put("tipoAccesso", "CAMERIERE");
        else if(radioButtonCucina.isChecked()) 
        	postParameters.put("tipoAccesso", "CUOCO");
        
        progressDialog = ProgressDialog.show(this, "Attendere", "Logging in...");
            
        new LoginTask().execute(postParameters);
		
	}
}