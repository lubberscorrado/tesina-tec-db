package com.restaurant.android;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.restaurant.android.cameriere.activities.HomeActivity;

public class LoginActivity extends Activity {
	
	/* Alcune variabili private per la gestione del login.  */
	private EditText etUsername;
	private EditText etPassword;
	private Button btnLogin;
	private Button btnCancel;
	private RadioButton radioButtonCameriere;
	private RadioButton radioButtonCucina;
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
//        lblResult = (TextView)findViewById(R.id.result);
        
        radioButtonCameriere = (RadioButton)findViewById(R.id.radioButtonCameriere);
        radioButtonCucina = (RadioButton)findViewById(R.id.radioButtonCucina);
        
        SharedPreferences settings = getSharedPreferences("NextActivity", 0);
        String username = settings.getString("username", "");
        String password = settings.getString("password", "");
        etUsername.setText(username);
        etPassword.setText(password);
       
        /* Imposto il listener del bottone di Login */
        btnLogin.setOnClickListener(new OnClickListener() {
		  	@Override
		  	public void onClick(View v) {
		  		// Check Login
		  		String username = etUsername.getText().toString();
		  		String password = etPassword.getText().toString();
		  		boolean logged = false;
		        // We need an Editor object to make preference changes.
		        // All objects are from android.context.Context
		  		
		        SharedPreferences settings = getSharedPreferences("NextActivity", 0);
		        SharedPreferences.Editor editor = settings.edit();
		        editor.putString("username", username);
		        editor.putString("password", password);
		        
		        // Commit the edits!
		        editor.commit();
		        
		        HttpClient httpClient = new DefaultHttpClient();
		        HttpPost httpPost = new HttpPost("http://192.168.1.102:8080/ClientEJB/login");
		        
		        String responseBody = "";
		        
		        try {
		        	List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
		        	postParameters.add(new BasicNameValuePair("user", username));
		        	postParameters.add(new BasicNameValuePair("password", password ));
		        	httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
		        	
		        	HttpResponse response = httpClient.execute(httpPost);
		        	responseBody = EntityUtils.toString(response.getEntity());
		        	
		        	Log.e("LoginActivity", responseBody);
		        	//Toast.makeText(getApplicationContext(), responseBody, 20).show();
		        			        	
		        } catch (ClientProtocolException e) {
		        	Toast.makeText(getApplicationContext(), "Errore durante la comunicazione con il server", 20).show();
		        	Log.e("LoginActivity", e.toString());
		        } catch (IOException e) {
		        	Toast.makeText(getApplicationContext(), "Impossibile contattare il server", 20).show();
		        	Log.e("LoginActivity", e.toString());
		        }
		        
		        
		        try {
					
		        	
		        	JSONObject jObject = new JSONObject(responseBody);
					
		        	if(jObject.getBoolean("success") == true) 
		        		logged = true;
		        	
					Log.e("LoginActivity", "isCassiere: " + jObject.getJSONObject("privs").getString("isCassiere"));
					Log.e("LoginActivity", "isCuoco: " + jObject.getJSONObject("privs").getString("isCuoco"));
					
					if(logged) {
						if(radioButtonCameriere.isChecked() && jObject.getJSONObject("privs").getString("isCassiere").equals("true"))
							Toast.makeText(getApplicationContext(), "Hai i privilegi per essere cameriere", 20).show();
						else if(radioButtonCucina.isChecked() && jObject.getJSONObject("privs").getString("isCuoco").equals("true"))
							Toast.makeText(getApplicationContext(), "Hai i privilegi per essere cuoco", 20).show();
					
						//-----------------------
				        // Open New Activity
				  		//-----------------------
				  		Intent myIntent = new Intent(LoginActivity.this, HomeActivity.class);
				  		LoginActivity.this.startActivity(myIntent);
				  		
					} else {
						Toast.makeText(getApplicationContext(), "Login failed. Username and/or password doesn't match", 20).show();
					}
					
		        } catch (JSONException e) {
					Log.e("LoginActivity", e.toString());
				}

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
}