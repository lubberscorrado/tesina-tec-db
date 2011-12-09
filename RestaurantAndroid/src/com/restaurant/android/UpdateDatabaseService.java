package com.restaurant.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

public class UpdateDatabaseService extends Service {

	DbManager dbManager;
	SQLiteDatabase db;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("UpdateDatabaseService", "Creating service");
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent,  flags, startId);
		
		Log.d("UpdateDatabaseService", "Avvio della sincronizzazione del database");
		
		dbManager = new DbManager(this);
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
		} catch (IOException e) {
			Log.e("UpdateDatabaseService", e.toString());
		} catch (JSONException e) {
			Log.e("UpdateDatabaseService", e.toString());
		} finally {
			db.close();
		}
		
		Log.d("UpdateDatabaseService", "Trasferimento DB su SD");
		dbManager.BackUpDbToSD();
			
		return START_STICKY;
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
			
			String response =  ((RestaurantApplication)getApplication()).makeHttpGetRequest(	"http://192.168.1.101:8080/ClientEJB/gestioneMenu", 
																								getParametersMap);
			
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
					}
				}	
			}
		}
		
		return categorieFiglie;
	}
}
