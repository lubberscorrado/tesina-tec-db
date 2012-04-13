package com.restaurant.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieStore;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.restaurant.android.cameriere.activities.Table;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class RestaurantApplication extends Application {
	
	private DefaultHttpClient httpClient;

	private String host;
	private SharedPreferences sharedPref;
	
	private String lastNotificationCheckDate;
	
	@Override
	public void onCreate() {
		host = "http://192.168.1.104:8080/";
		httpClient = new DefaultHttpClient();
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		this.lastNotificationCheckDate = sharedPref.getString("lastNotificationCheckDate", "0000-00-00 00:00:00");
		
	}
		
	/**
	 * Effettua una richiesta POST ad un URL con determinati parametri
	 * @param url Url da contattare tramite la richiesta POST
	 * @param postParametersMap Mappa dei parametri da passare nella richiesta
	 * @return Corpo della risposta ricevuta dal server
	 * @throws ClientProtocolException Eccezione che si verifica se non si riesce a decodificare la risposta del server
	 * @throws IOException Eccezione che si verifica quando ci sono problemi di connettività
	 */
	public synchronized String makeHttpPostRequest(	String url, 
													HashMap<String, String > postParametersMap) throws 	ClientProtocolException, IOException {
		try {
			HttpPost httpPost = new HttpPost(url);
	      
	       	List<NameValuePair> listPostParameters = new ArrayList<NameValuePair>(2);
	        	
	       	for(String key : postParametersMap.keySet()){
	       		/* Costruisco la lista dei parametri da passare alla richiesta POST */
	        	listPostParameters.add(new BasicNameValuePair(key, postParametersMap.get(key)));
	        }
	       	
	       	/* Setto i parametri per la richiesta POST codificandoli come URL encoded */
	       	httpPost.setEntity(new UrlEncodedFormEntity(listPostParameters));
	        	
	       
	       	/* Effettuo la richiesta HTTP */
	        HttpResponse response = httpClient.execute(httpPost);
	                
	        /* Log dei cookies */
	        List<Cookie> cookies = httpClient.getCookieStore().getCookies();
	
	       	if (cookies.isEmpty()) {
	       	   Log.d("RestaurantApplication", "POST REQUEST, No cookies");
	       	} else {
	       		for(Cookie c : cookies) {
	       		    if(c.getName().equals("JSESSIONID")) { 
	       				Log.e("POST REQUEST, JSESSIONID" , c.getValue());
	       			} else {
	       				Log.e("POST REQUEST, Cookie" , c.getName() + " - " + c.getValue());
	       			}
	       		}
	       	}
	       	String responseBody = EntityUtils.toString(response.getEntity());
	        	
	        return responseBody;
		}catch(Exception e) {
			return "";
		}
	}
	
	/**
	 * Effettua una richiesta post al server inviando come corpo della richiesta un oggetto JSON
	 * @param jsonObject
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public synchronized String makeHttpJsonPostRequest(String url, JSONObject jsonObject) throws ClientProtocolException, IOException {
		
		try {
			HttpPost httpPost = new HttpPost(url);
		      
			StringEntity jsonBody = new StringEntity(jsonObject.toString());
			httpPost.setEntity(jsonBody);
	        
	       	/* Effettuo la richiesta HTTP */
	        HttpResponse response = httpClient.execute(httpPost);
	                
	       	String responseBody = EntityUtils.toString(response.getEntity());
	        	
	        return responseBody;
		}catch(Exception e) {
			return "";
		}
	}
	
	/**
	 * Effettua una richista GET ad un URL passando sulla query string dei parametri di input
	 * @param url Url a cui effettuare la richiesta
	 * @param getParametersMap Mappa dei parametri da passare tramite la richiesta GET
	 * @return Corpo dalla risposta ricevuta dal server
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public synchronized String makeHttpGetRequest(	String url, 
													HashMap<String, String > getParametersMap) throws	ClientProtocolException, IOException {

				
		List<NameValuePair> listGetParameters = new ArrayList<NameValuePair>(2);
				
		/************************************************************************
		 * Costruzione della lista di coppie chiave e valore che rappresentano i
		 * parametri della query string 
		 * **********************************************************************/
		
		for(String key : getParametersMap.keySet())
			listGetParameters.add(new BasicNameValuePair(key, getParametersMap.get(key)));
		
		String queryString = URLEncodedUtils.format(listGetParameters, "utf-8");
		String url_query_string = url + "?" +  queryString;
		
		HttpGet httpGet = new HttpGet(url_query_string);
		HttpResponse response = httpClient.execute(httpGet);
		
		/********************************************************************************
		 * Stampo tutto i cookies ritornati dal server e imposto, se ritornato l'id 
		 * della sessione 
		*********************************************************************************/
		List<Cookie> cookies = httpClient.getCookieStore().getCookies();
		
		if (cookies.isEmpty()) {
			Log.d("RestaurantApplication", "POST REQUEST, No cookies");
		} else {
			for(Cookie c : cookies) {
		
				if(c.getName().equals("JSESSIONID")) { 
					Log.e("GET REQUEST, JSESSIONID" , c.getValue());
				} else {
					Log.e("GET REQUEST, Cookie" , c.getName() + " - " + c.getValue());
				}
			}
		}
		
		String responseBody = EntityUtils.toString(response.getEntity());
		return responseBody;

	}
	
	public String getHost() {
		return host;
		
	}
	
	public void setHost(String host) {
		this.host = host;
		
	}
	@Override
	public void onTerminate() {
		super.onTerminate();
		
	}

	public String getLastNotificationCheckDate() {
		return lastNotificationCheckDate;
	}

	public void setLastNotificationCheckDate(String lastNotificationCheckDate) {
		
		SharedPreferences.Editor editor= sharedPref.edit();
		editor.remove("lastNotificationCheckDate");
		editor.putString("lastNotificationCheckDate", lastNotificationCheckDate);
		this.lastNotificationCheckDate = lastNotificationCheckDate;
	}
}
