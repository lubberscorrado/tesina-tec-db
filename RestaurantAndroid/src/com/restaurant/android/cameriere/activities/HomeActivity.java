package com.restaurant.android.cameriere.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.restaurant.android.LoginActivity;
import com.restaurant.android.R;
import com.restaurant.android.RestaurantApplication;
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
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.syncMenu :
			//startService(new Intent(this, UpdateDatabaseService.class));
			 new MenuSyncTask().execute(null);
		}
		return false;
	}
	
	
	/**********************************************************************
	 * Async Task che gestisce la sincronizzazione del menu con il server 
	 **********************************************************************/
    class MenuSyncTask extends AsyncTask<Object, Object, Error> {
       	 
    	ProgressDialog progressDialog;
    	DbManager dbManager;
    	SQLiteDatabase db;
    	
    	
    	@Override
    	protected void onPreExecute() {
    		progressDialog = ProgressDialog.show(HomeActivity.this, "Attendere", "Sincronizzazione del menu",true);
    	}
    	
    	@Override
		protected Error doInBackground(Object ...params) {
    		
    		
    		dbManager = new DbManager(getApplicationContext());
    		dbManager.dropTables();
    		dbManager.createTables();
    		
    		db = dbManager.getWritableDatabase();
    			
    		/**********************************************************
    		 * Richieste per popolare il database delle categorie 	
    		 ***********************************************************/
    		
    		List<Integer> categoriePadre = new ArrayList<Integer>();
    		List<Integer> temp;
    		categoriePadre.add(1);
    		
    		List<Integer> categorieFiglie;
    		try {
    			categorieFiglie = getChild(categoriePadre);
    		
    			while(categoriePadre.size() != 0) {
    				categorieFiglie = getChild(categoriePadre);
    			
    				/* Swap delle categorie, quelle che sono state tornate come figlie
    				 * diventano ora padre */
    				categoriePadre = categorieFiglie;
    			}
    			
    		} catch (ClientProtocolException e) {
    			Log.e("UpdateDatabaseService", e.toString());
    			return new Error("Errore di comunicazione", true);
    		} catch (IOException e) {
    			Log.e("UpdateDatabaseService", e.toString());
    			return new Error("Errore di comunicazione", true);
    		} catch (JSONException e) {
    			Log.e("UpdateDatabaseService", e.toString());
    			return new Error("Errore di comunicazione", true);
    		} finally {
    			db.close();
    		}
    		
    		Log.d("UpdateDatabaseService", "Trasferimento DB su SD");
    		dbManager.BackUpDbToSD();
			
    		return new Error("",false);
		 
		}
    	    	
    	/**
    	 * Acquisisce tutte le categorie figlie (comprese le voci di menu) a partire 
    	 * da una lista di categorie padre. 
    	 * @param categoriePadre Lista degli id delle categorie padre
    	 * @return Lista degli id delle categorie figlie ottenute
    	 * @throws IOException Eccezione sollevata se si verifica un errore di connettività
    	 * @throws ClientProtocolException  Eccezione sollevata se si verifica un errore di comunicazione
    	 * con il server
    	 * @throws JSONException Eccezione sollevata se si verificano errori nel parsing della
    	 * risposta JSON
    	 */
    	
    	public List<Integer> getChild(List<Integer> categoriePadre) throws ClientProtocolException, IOException, JSONException {
    		
    		/* Lista che contiene gli id delle categorie che vengono acquisite
    		 * a partire dall id in ingresso */
    		List<Integer> categorieFiglie = new ArrayList<Integer>();
    		
    		/* Hashmap per i parametri della richiesta HTTP */
    		HashMap<String, String> getParametersMap;
    		
    		for(Integer idCategoria : categoriePadre) {
    			
    			getParametersMap = new HashMap<String,String>();
    			getParametersMap.put("node", "C" + idCategoria.toString());
    			
    			String response =  ((RestaurantApplication)getApplication()).
    										makeHttpGetRequest(	"http://192.168.1.101:8080/ClientEJB/gestioneMenu", 
    															getParametersMap);
    			
    			Log.d("UpdataDatabaseService", response);
    			
    			/**************************************************************************
    			 * Tutte le categoria ritornate dal server vengono inserite all'interno
    			 * del database e vengono acquisiti gli id per la richiesta delle categorie
    			 * figlie
    			 **************************************************************************/
    			
    			JSONObject jsonObject = new JSONObject(response);
    			if(jsonObject.getString("message").equals("OK")) {
    				
    				JSONArray categories = jsonObject.getJSONArray("data");
    				
    				for(int i = 0; i < categories.length(); i++) {
    					
    					String id = categories.getJSONObject(i).getString("id");
    					String idPadre = categories.getJSONObject(i).getString("parentId");
    					
    					if(id.startsWith("C"))  {
    						/* substr come fine richiede l'offset one past */
    						Log.d("UpdateDatabaseService", "Ottenuta categoria: " + categories.getJSONObject(i).getString("nome"));
    						categorieFiglie.add(Integer.parseInt(id.substring(1, id.length())));
    						
    						/**********************************************************************
    						 * Inserimento dei parametri della categoria all'interno del database 
    						 **********************************************************************/
    						
    						ContentValues values = new ContentValues();
    						values.clear();
    						
    						values.put("idCategoria", Integer.parseInt(id.substring(1, id.length())));
    						values.put("idCategoriaPadre", Integer.parseInt(idPadre.substring(1, idPadre.length())));
    						values.put("nome", categories.getJSONObject(i).getString("nome"));
    						values.put("descrizione", "TODO");
    						
    						db.insertOrThrow("categoria", null, values);
    												
    					} else if (id.startsWith("V")) {
    						
    						Log.d("UpdateDatabaseService", "Ottenuta voce menu: " + categories.getJSONObject(i).getString("nome"));
    						/************************************************************************
    						 * Inserimento dei parametri della voce di menu all'interno del database 
    						 ************************************************************************/
    						
    						ContentValues values = new ContentValues();
    						values.clear();
    						values.put("idVoceMenu", Integer.parseInt(id.substring(1, id.length())));
    						values.put("idCategoria", Integer.parseInt(idPadre.substring(1, idPadre.length())));
    						values.put("nome", categories.getJSONObject(i).getString("nome"));
    						values.put("descrizione", categories.getJSONObject(i).getString("descrizione"));
    						values.put("nome", categories.getJSONObject(i).getString("prezzo"));
    						
    						db.insertOrThrow("vocemenu", null, values);
    				  						
    					}
    				}	
    			}
    		}
    		
    		return categorieFiglie;
    	}
       	
       @Override
       protected void onPostExecute(Error error) {
    	   progressDialog.dismiss();
    	   
    	   if(error.errorOccurred()) 
    		   Toast.makeText(getApplicationContext(), error.getError(), 20).show();
    	   else
    		   Toast.makeText(getApplicationContext(), "Sincronizzazione completata", 20).show();
    	   		
    	   progressDialog.dismiss();
       }
  
   }
    
    

}
