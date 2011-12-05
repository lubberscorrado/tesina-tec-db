package com.restaurant.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	/* Alcune variabili private per la gestione del login.  */
	private EditText etUsername;
	private EditText etPassword;
	private Button btnLogin;
	private Button btnCancel;
	TextView lblResult;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /* Queste righe vanno DOPO "setContentView" */
        
        etUsername = (EditText)findViewById(R.id.username);
        etPassword = (EditText)findViewById(R.id.password);
        btnLogin = (Button)findViewById(R.id.login_button);
        btnCancel = (Button)findViewById(R.id.cancel_button);
        lblResult = (TextView)findViewById(R.id.result);
        
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
		  		
		        // We need an Editor object to make preference changes.
		        // All objects are from android.context.Context
		  		
		        SharedPreferences settings = getSharedPreferences("NextActivity", 0);
		        SharedPreferences.Editor editor = settings.edit();
		        editor.putString("username", username);
		        editor.putString("password", password);
		        
		        // Commit the edits!
		        editor.commit();
	
		  		if(username.equals("guest") && password.equals("guest")){
		  			
		  			// Mostro un messaggio in sovra-impressione
		  			Toast.makeText(getApplicationContext(), "Login successful!", 20).show();
		  			
		  			//-----------------------
		  			// Open New Activity
		  			//-----------------------
		  			Intent myIntent = new Intent(LoginActivity.this, HomeActivity.class);
		  			LoginActivity.this.startActivity(myIntent);
		  			
		  		} else {
		  			Toast.makeText(getApplicationContext(), "Login failed. Username and/or password doesn't match", 20).show();
		  			lblResult.setText("Login failed. Username and/or password doesn't match.");
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