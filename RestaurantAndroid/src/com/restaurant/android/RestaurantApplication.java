package com.restaurant.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

public class RestaurantApplication extends Application {
	
	@Override
	public void onCreate() {
		
	}
		
	public synchronized String makeHttpPostRequest(String url, HashMap<String, String > postParametersMap) throws 	ClientProtocolException,
																													IOException {
		
		HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://192.168.1.101:8080/ClientEJB/login");
        
       	List<NameValuePair> listPostParameters = new ArrayList<NameValuePair>(2);
        	
       	for(String key : postParametersMap.keySet()){
        	
        	/* Costruisco la lista dei parametri da passare alla richiesta POST */
        	listPostParameters.add(new BasicNameValuePair(key, postParametersMap.get("key")));
        }
        
        httpPost.setEntity(new UrlEncodedFormEntity(listPostParameters));
        	
        HttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());
        	
        return responseBody;
		
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
