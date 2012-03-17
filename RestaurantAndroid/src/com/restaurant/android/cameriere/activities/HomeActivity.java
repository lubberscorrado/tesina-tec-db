package com.restaurant.android.cameriere.activities;

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
import android.database.Cursor;
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
import com.restaurant.android.cameriere.notifiche.NotificationActivity;
import com.restaurant.android.cameriere.notifiche.NotificationUpdaterService;
import com.restaurant.android.cameriere.prenotazioni.PrenotationsListActivity;


/** 
 * Activity principale da mostrare al cameriere
 * come Home Page successivamente al Login. E' una 
 * Tab Activity che mostra le risorse
 * @author fabio
 */
public class HomeActivity extends TabActivity {
	
	private static final String TAG = "HomeActivity"; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.cameriere_home);
	  
	  	/** 
	  	 * Faccio Partire NotificationUpdaterService, servizio che gira in 
	  	 * background e controlla se ci sono nuove notifiche per il cameriere
	  	 * @author Fabio Pierazzi
	  	 * */
	  	startNotificationUpdaterService();	  	
	  
	  	
	  	
	  	/** Alloco le risorse per mostrare i vari tab della Home page */
	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab
	    
	    /* Inserisco l'activity delle notifiche nel Tab */
	    intent = new Intent().setClass(this, NotificationActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("notifiche").setIndicator("Notifiche",
	                      res.getDrawable(R.drawable.ic_notifications))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    /* Inserisco l'activity con l'elenco dei tavoli nel Tab */
	    intent = new Intent().setClass(this, TablesListActivity.class);
	    spec = tabHost.newTabSpec("elencoTavoli").setIndicator("Elenco Tavoli",
	                      res.getDrawable(R.drawable.ic_food))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    /* Inserisco l'activity con l'elenco delle prenotazioni nel Tab */
	    intent = new Intent().setClass(this, PrenotationsListActivity.class);
	    spec = tabHost.newTabSpec("elencoPrenotazioni").setIndicator("Prenotazioni",
	                      res.getDrawable(R.drawable.ic_prenotazioni))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    /* Verifico se questa activity è stata attivata per effetto di un notifica. In caso
	     * affermativo devo settare come tab corrente quello delle notifiche. */
	    
	    if(	getIntent().getStringExtra("UPDATE_NOTIFICHE") != null &&
	    	getIntent().getStringExtra("UPDATE_NOTIFICHE").equals("TRUE")) {
	    	/* 
	    	 * L'activity è stata attivata per effetti di una notifica dalla status
	    	 * bar
	    	 */
	    	Log.d("HomeActivity", "Richiesto l'aggiornamento delle notifiche");
	    	tabHost.setCurrentTab(0);
	    	
	    } else {
	        /* Imposto il tab di default */
		    tabHost.setCurrentTab(1);
		}
	    
	    
	    
	    
	    
	    DbManager dbManager = new DbManager(getApplicationContext());
	 
	    //dbManager.dropTablesComande();
	    //dbManager.createTablesComande();
	   
	    //dbManager.dropTableNotifiche();
	    //dbManager.createTableNotifiche();
	    
	    //dbManager.BackUpDbToSD();
	    dbManager.close();
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
				 break;
				 
			case R.id.startNotificationUpdaterService:
				startNotificationUpdaterService();	  	
				break;
			case R.id.stopNotificationUpdaterService: 
				stopNotificationUpdaterService();
				break;
				 
				
			 
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
    			
    			/* ************************************************************************
    			 * Acquisizione di tutte le variazioni associate alla categoria corrente
    			 *************************************************************************/
    			getParametersMap = new HashMap<String,String>();
    			getParametersMap.put("idCategoria", idCategoria.toString());
    			
    			//Log.d("Ricerca variazioni", "Ricerco variazione per " + idCategoria);
    			
    			String response = ((RestaurantApplication)getApplication()).
    										makeHttpGetRequest(((RestaurantApplication)getApplication()).getHost() + "ClientEJB/variazioneVoceMenu",
    															getParametersMap);
    			
    			//Log.d("UpdateDatabaseService (variazioni)", response);
    			
    			JSONObject jsonObject = new JSONObject(response);
    			if(jsonObject.getString("success").equals("true")) {
    				JSONArray variazioni =  jsonObject.getJSONArray("data"); 
    				for(int i = 0; i< variazioni.length(); i++) {
    					if(variazioni.getJSONObject(i).getBoolean("isEreditata") == false) {
    						
    						/* Inserisco nel database solamente le variazioni non ereditate */
    						
    						//Log.d("UpdateDatabaseService","Ottenuta variazione " + variazioni.getJSONObject(i).getString("nome"));
    						ContentValues values = new ContentValues();
	    					values.clear();
	    					values.put("idVariazione", variazioni.getJSONObject(i).getInt("id"));
	    					values.put("idCategoria", variazioni.getJSONObject(i).getInt("idCategoria"));
	    					values.put("nome", variazioni.getJSONObject(i).getString("nome"));
	    					values.put("descrizione", variazioni.getJSONObject(i).getString("descrizione"));
	    					values.put("prezzo", variazioni.getJSONObject(i).getString("prezzo"));
	    					values.put("checked", 0);
	    					db.insertOrThrow("variazione", null, values);
    					}
    				}
    			}
    			  			
    			/* *************************************************************************
    			 * Acquisizione di tutte le categorie figlie della categoria corrente 
    			 **************************************************************************/
    			
    			getParametersMap = new HashMap<String,String>();
    			getParametersMap.put("node", "C" + idCategoria.toString());
    			
    			response =  ((RestaurantApplication)getApplication()).
    									makeHttpGetRequest(((RestaurantApplication)getApplication()).getHost() + "ClientEJB/gestioneMenu", 
    														getParametersMap);
    			
    			Log.d("UpdataDatabaseService (categorie)", response);
    			
    			/* *************************************************************************
    			 * Tutte le categoria ritornate dal server vengono inserite all'interno
    			 * del database e vengono acquisiti gli id per la richiesta delle categorie
    			 * figlie
    			 **************************************************************************/
    			
    			jsonObject = new JSONObject(response);
    			if(jsonObject.getString("success").equals("true")) {
    				
    				JSONArray categories = jsonObject.getJSONArray("data");
    				
    				for(int i = 0; i < categories.length(); i++) {
    					
    					String id = categories.getJSONObject(i).getString("id");
    					String idPadre = categories.getJSONObject(i).getString("parentId");
    					
    					if(id.startsWith("C"))  {
    						/* substr come fine richiede l'offset one past */
    						Log.d("UpdateDatabaseService", "Ottenuta categoria: " + categories.getJSONObject(i).getString("nome"));
    						categorieFiglie.add(Integer.parseInt(id.substring(1, id.length())));
    						
    						/* *********************************************************************
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
    						
    						/* ***********************************************************************
    						 * Inserimento dei parametri della voce di menu all'interno del database 
    						 ************************************************************************/
    						
    						ContentValues values = new ContentValues();
    						values.clear();
    						values.put("idVoceMenu", Integer.parseInt(id.substring(1, id.length())));
    						values.put("idCategoria", Integer.parseInt(idPadre.substring(1, idPadre.length())));
    						values.put("nome", categories.getJSONObject(i).getString("nome"));
    						values.put("descrizione", categories.getJSONObject(i).getString("descrizione"));
    						values.put("prezzo", categories.getJSONObject(i).getString("prezzo"));
    						
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
    
    /**
     * Metodo per verificare se NotificationUpdaterService (il servizio che controlla se ci sono nuove 
     * notifiche per il cameriere) è attivo. 
     * Serve per evitare che il servizio venga attivato due volte
     * 
     * @return true, se il servizio è attivo
     */
    private boolean isMyServiceRunning(String serviceName) {
 	    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
 	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
 	        if (serviceName.equals(service.service.getClassName())) {
 	            return true;
 	        }
 	    }
 	    return false;
 	}
    
    /**
     * Metodo per far partire il NotificationUpdaterService.
     * Controlla anche che il metodo non sia già attivo
     * @author Fabio Pierazzi
     */
    private void startNotificationUpdaterService() {
    	// Attivo NotificationUpdaterService solamente se non è già attivo
	  	if(isMyServiceRunning("com.restaurant.android.cameriere.notifiche.NotificationUpdaterService")) {
	  		// do nothing
	  		Log.d(TAG, "NotificationUpdaterService is already running!");
	  	} else {
	  		/** Faccio partire il service per la l'update delle notifiche */
		  	Log.d(TAG, "I'm starting the NotificationUpdaterService from HomeActivity!");
		  
		  	Intent serviceIntent = new Intent(this, NotificationUpdaterService.class);
//		  	serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		  	startService(serviceIntent);
		  	Toast.makeText(getApplicationContext(), "Notifiche Attivate", 20).show();
	  	}
    }

    /** Metodo per far interrompere NotificationUpdaterService, se è già attivo.
     * @author Fabio Pierazzi
     */
    private void stopNotificationUpdaterService() {
    	if(isMyServiceRunning("com.restaurant.android.cameriere.notifiche.NotificationUpdaterService")) {
    		Intent serviceIntent = new Intent(this, NotificationUpdaterService.class);
    		stopService(serviceIntent);
    		Toast.makeText(getApplicationContext(), "Notifiche Disattivate", 20).show();
    	}
    }
    
    

}
