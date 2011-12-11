package com.restaurant.android.cameriere.activities;


import java.util.ArrayList;

import com.restaurant.android.DbManager;
import com.restaurant.android.R;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  Classe che mostra l'elenco del Menu
 * 	@author fabio
 *
 */
public class MenuListActivity extends Activity {
	
	
	private ArrayList<String> vociMenu;
	private ListView tableListView;
	private ListAdapter listAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);

	  vociMenu = new ArrayList<String>();
	
	  
	  
	  setContentView(R.layout.cameriere_menu_list);
	  tableListView = (ListView) findViewById(R.id.list_view_voci_menu);
	  
	  listAdapter = new ListAdapter(getApplicationContext(), R.layout.cameriere_menu_list_row, vociMenu);
	  tableListView.setAdapter(listAdapter);
			  
	  DbManager dbManager =  new DbManager(getApplicationContext());
	  SQLiteDatabase db = dbManager.getWritableDatabase();
	  
	  Cursor cursor = db.query("categoria", new String[] {"nome", "descrizione"} , null, null ,null, null,null,null);
	  
	  cursor.moveToFirst();
	  while(!cursor.isAfterLast()) {
		  vociMenu.add(cursor.getString(0));
		  cursor.moveToNext();
	  }
	}


	/************************************************************************
	 * Adapter per gestire il rendering personalizzato degli elementi della
	 * lista 
	 ************************************************************************/
	
	private class ListAdapter extends ArrayAdapter<String> {
	
	    private ArrayList<String> items;
	
	    public ListAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
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
	            
	            String s = items.get(position);
	            if (s != null) {
	                    
	            		TextView tt = (TextView) v.findViewById(R.id.textVoceMenu);
	                    if(tt != null) {
	                          tt.setText(s);                            
	                    }
	                    
	            }
	            return v;
	    }
	}
}
