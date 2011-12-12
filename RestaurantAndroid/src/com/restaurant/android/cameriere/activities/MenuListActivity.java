package com.restaurant.android.cameriere.activities;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.restaurant.android.DbManager;
import com.restaurant.android.R;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  Classe che mostra l'elenco del Menu
 * 	@author fabio
 *
 */
public class MenuListActivity extends Activity implements OnItemClickListener {
	
	private ArrayList<VoceMenu> vociMenu;
	
	private ListView tableListView;
	private ListAdapter listAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);

	  vociMenu = new ArrayList<VoceMenu>();
	
	  setContentView(R.layout.cameriere_menu_list);
	  tableListView = (ListView) findViewById(R.id.list_view_voci_menu);
	  tableListView.setOnItemClickListener(this);
	  
	  listAdapter = new ListAdapter(getApplicationContext(), R.layout.cameriere_menu_list_row, vociMenu);
	  tableListView.setAdapter(listAdapter);
			  
	  

	  fillListView(1);
	}
	
		
	public void fillListView(int idCategoriaPadre) {
		DbManager dbManager =  new DbManager(getApplicationContext());
		SQLiteDatabase db = dbManager.getWritableDatabase();
		
		Cursor cursorCategoria = null;
		Cursor cursorVoceMenu = null;
		
		
		/* Lista temporanea che contiene le voci recuperate dal database
		 * che devono poi essere riportate nella lista vociMenu */
		List<VoceMenu> temp = new LinkedList<VoceMenu>();
		
		vociMenu.clear();
		
		try {
		
			/*********************************************************
			 * Aquisizione delle categorie figlie della categoria 
			 * con id idCategoriaPadre
			 *********************************************************/
			cursorCategoria = db.query(	"categoria", 
										new String[] {"nome", "descrizione", "idCategoria"}, 
										"idCategoriaPadre=" + idCategoriaPadre, null ,null, null,null,null);
			cursorCategoria.moveToFirst();
			
			while(!cursorCategoria.isAfterLast()) {
				
				
				temp.add(new VoceMenu(	cursorCategoria.getString(0),
										cursorCategoria.getString(1),
										cursorCategoria.getInt(2),
										true));
				
						
				cursorCategoria.moveToNext();
			}
			cursorCategoria.close();
			
			/*********************************************************
			 * Aquisizione delle voci di menu associate alla categoria
			 * padre corrente
			 *********************************************************/
			cursorVoceMenu = db.query(	"vocemenu", 
										new String[] {"nome", "descrizione", "idCategoria"} , 
										"idCategoria=" + idCategoriaPadre, 
										null, null, null, null, null);
		
			cursorVoceMenu.moveToFirst();
			

			while(!cursorVoceMenu.isAfterLast()) {
				temp.add(new VoceMenu(	cursorVoceMenu.getString(0),
										cursorVoceMenu.getString(1),
										0,
										false));
				
				cursorVoceMenu.moveToNext();
			}
			cursorVoceMenu.close();
			
			/* Aggiungo alla lista vociMenu tutte le categorie */
			Iterator<VoceMenu> it = temp.iterator();
			
			while(it.hasNext()) {
				VoceMenu v = it.next();
				if(v.isCategoria()) {
					vociMenu.add(v);
					it.remove();
				}
			}
			
			/* Aggiungo alla lista vociMenu tutte le voci di menu restanti */
			for(VoceMenu v : temp) 
				vociMenu.add(v);
					
			/* Per la voce indietro l'id categoria è considerato come l'id della categoria padre
			 * delle voci correntemente visualizzate. La voce indietro non è mostrata per le 
			 * categoria di primo livello (subito sotto la radice) */
			
			if(idCategoriaPadre != 1)
				vociMenu.add(new VoceMenu("..Indietro..", "", idCategoriaPadre, false));
			
			listAdapter.notifyDataSetChanged();
			
			
			db.close();
			dbManager.close();
			
		  } catch (Exception e) {
			  e.printStackTrace();
			  Log.e("MenuListActivity", "Errore query database " + e.toString());
		  }
	}

	/************************************************************************
	 * Adapter per gestire il rendering personalizzato degli elementi della
	 * lista 
	 ************************************************************************/
	private class ListAdapter extends ArrayAdapter<VoceMenu> {
	
	    private ArrayList<VoceMenu> items;
	
	    public ListAdapter(Context context, int textViewResourceId, ArrayList<VoceMenu> items) {
	            super(context, textViewResourceId, items);
	            this.items = items;
	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	            View v = convertView;
	            if (v == null) {
	                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                v = vi.inflate(R.layout.cameriere_menu_list_row, null);
	            }
	            
	            VoceMenu voceMenu = items.get(position);
	            if (voceMenu != null) {
	                    
	            		TextView textView = (TextView) v.findViewById(R.id.textVoceMenu);
	                    if(textView != null) {
	                          textView.setText(voceMenu.getNome());                            
	                    }
	        	                
	                    
	                    ImageView imageView = (ImageView)v.findViewById(R.id.imageVoceMenu);
	                    imageView.setVisibility(0);
	                    
	                    if(imageView != null) {
	                    	if(voceMenu.getNome().equals("..Indietro..")) 
	                    		imageView.setImageResource(R.drawable.ic_notifications);
	                    	else if (voceMenu.isCategoria())
	                    		imageView.setImageResource(R.drawable.ic_launcher);
	                    	else
	                    		imageView.setImageResource(R.drawable.ic_food);
	                    }
	            }
	            return v;
	    }
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		VoceMenu voceMenu = vociMenu.get(position);
		
		
		if(voceMenu.getNome().equals("..Indietro..")) {
			DbManager dbManager =  new DbManager(getApplicationContext());
			SQLiteDatabase db = dbManager.getWritableDatabase();
			
			/*************************************************************
			 * Ricerco l'id della categoria padre che ha come idCategoria 
			 * l'idCategoriaPadre attuale 
			 *************************************************************/
			Cursor cursorPrecedente;
			cursorPrecedente = db.query("categoria", 
										new String[] {"nome", "descrizione", "idCategoriaPadre"}, 
										"idCategoria=" + voceMenu.getIdCategoria(), null ,null, null,null,null);
			
			cursorPrecedente.moveToFirst();
			int idPrecedente = cursorPrecedente.getInt(2);
			cursorPrecedente.close();
			
			db.close();
			dbManager.close();
			
			Log.d("MenuListActivity", "L'id precedente è " + idPrecedente);
			fillListView(idPrecedente);
		
		} else {
			if(!voceMenu.isCategoria()) 
				Toast.makeText(getApplicationContext(), "VOCE MENU", 30);
			else
				fillListView(voceMenu.getIdCategoria());
		}
	}


}
