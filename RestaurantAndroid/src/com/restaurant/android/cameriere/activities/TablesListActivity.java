package com.restaurant.android.cameriere.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.restaurant.android.R;

/** 
 * Activity per mostrare al cameriere l'elenco dei tavoli.
 * @author Fabio Pierazzi
 */
public class TablesListActivity extends Activity {
	
	/* Variabile usata per il logging */
	private static final String TAG = "TablesListActivity";
	
	private ListView tableListView;
	private ProgressDialog m_ProgressDialog = null;
	private ArrayList<Table> m_tables = null;
    private TableAdapter m_adapter;
    private Runnable viewTables;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.cameriere_tables_list);
	  
	  // Recupero il riferimento ad una ListView
	  this.tableListView = (ListView) findViewById(R.id.listView_tablesList);
	  
	  // Operazioni necessarie al corretto funzionamento della listView
	  m_tables = new ArrayList<Table>();
      this.m_adapter = new TableAdapter(this, R.layout.cameriere_tables_list_row, m_tables);
      tableListView.setAdapter(this.m_adapter);
      
      tableListView.setOnItemClickListener(new OnItemClickListener() {
  	    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {
  	    	
  	    	
//  	    	Log.d(TAG, "Hai cliccato una voce della lista" + 
//  	    			  		"\n position: " + position + 
//  	    			  		"\n parent: " + parent + 
//  	    			  		"\n view: " + view +
//  	    			  		"\n id: " + id);
  	    	
  	    	  /* Sapendo la posizione dell'elemento che è stato 
  	    	   * cliccato, ricavo l'oggetto dell'adapter */
  	    	  Log.i(TAG, "Hai cliccato su: " + 
  	    			  	m_adapter.getItem(position).getTableName() + ", che è " 
  	    			  + m_adapter.getItem(position).getTableStatus());
  	    	  
  	    	  /* Apro una nuova activity con la scheda del tavolo */
  	    	  Intent myIntent = new Intent(TablesListActivity.this, TableCardActivity.class);
  	    	  // TablesListActivity.this.startActivity(myIntent);
  	    	  
  	    	  /* Creo un bundle per passare dei dati alla nuova activity */
	  	      Bundle b = new Bundle();
	  	      b.putSerializable("tableObject", (Table) m_adapter.getItem(position));
	  	      b.putString("tableName", m_adapter.getItem(position).getTableName());
	  	      myIntent.putExtras(b);
	  	      startActivity(myIntent);
	  	      
	  	      /* chiude definitivamente questa activity
	  	       * (premendo il pulsante "indietro" dall'activity 
	  	       * successiva si ritornerebbe direttamente all'activity
	  	       * di Login) */
	  	      // finish();
  	    	
  	    	
		      // When clicked, show a toast with the TextView text
		      // Toast.makeText(getApplicationContext(), "Hai cliccato!", 20).show();
//		      
//		      //------------------------------
//		      // OPEN NEW ACTIVITY!
//		      //------------------------------
//		      Intent myIntent = new Intent(TablesListActivity.this, NextActivity.class);
//		      
//		      // AGGIUNGO PARAMETRI DA PASSARE ALLA NUOVA ACTIVITY 
//		      
//		      //Next create the bundle and initialize it
//		      Bundle bundle = new Bundle();
//
//		      //Add the parameters to bundle as
//		      bundle.putString("NAME","my name");
//
//		      // bundle.putString("COMPANY","wissen");
//
//		      // bundle.putInt("AGE", 22);
//
//		      //Add this bundle to the intent
//		      myIntent.putExtras(bundle);
//		      
//		      // Faccio partire la nuova activity
//		      HomeActivity.this.startActivity(myIntent);
		      
		    }
      });
      
      /* Creo un thread che andrà a prendere le
       * informazioni dal database */
      viewTables = new Runnable() {
    	  @Override
          public void run() {
    		  /* Richiamo un metodo privato */
              getTables();
    	  }
      };
    	    
	    Thread thread =  new Thread(null, viewTables, "TablesListThread");
	    thread.start();
	    m_ProgressDialog = ProgressDialog.show(TablesListActivity.this,    
	          "Attendere...", "Scaricamento dati in corso...", true);
	}
	
	
	
	
	public void onResume() {
		super.onResume();
 
		// Messaggio di prova
		Toast.makeText(getApplicationContext(), "Resumed TablesListActivity", 5).show();
	}
	

	/** Definisco una variabile che mi consente di rieffettuare
	 * la richiesta di risultati */
	private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
            if(m_tables != null && m_tables.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_tables.size();i++)
                	m_adapter.add(m_tables.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
    };
    
    /**
     * Metodo per reperire l'elenco di tavoli da mostrare
     * all'interno della ListView. 
     * @author Fabio Pierazzi
     */
    private void getTables(){
          try{
        	  /* m_orders contiene gli ordini da mostrare
        	   * nella ListView */
              m_tables = new ArrayList<Table>();
              
              /* Creo alcuni tavoli manualmente, 
               * come test iniziale */
              for(int i=0; i<100; ++i) {
            	  
            	  /* Creo un oggetto tavolo (test) 
            	   * (potrei prenderlo da DB) */ 
            	  Table t = new Table();
            	  t.setTableName("Tavolo " + (i+1)); 
            	  
            	  if(i%3==0) {
            		  t.setTableStatus("Libero"); 
            	  } else {
            		  t.setTableStatus("Occupato"); 
            	  }
            	  
            	  /* Aggiungo l'oggetto tavolo all'array
            	   * m_tables */
            	  this.m_tables.add(t);
            	  
              }
              
              /* Impongo un'attesa di 5 secondi per simulare 
               * il tempo necessario ad effettuare una richiesta
               * remota */
              Thread.sleep(5000);
              
              Log.i(TAG + ": getTables()", "Number of Tables Loaded: " + m_tables.size());
              
        } catch (Exception e) {
              Log.e(TAG + ": BACKGROUND_PROC", e.getMessage());
            }
          
          	/* Fa partire sull'interfaccia grafica il thread che 
          	 * aggiunge all'adapter il contenuto */
            runOnUiThread(returnRes);
        }
	
	
	/* Adapter che stabilisce coem mostrare la roba in ordine */
    private class TableAdapter extends ArrayAdapter<Table> {

        private ArrayList<Table> items;

        public TableAdapter(Context context, int textViewResourceId, ArrayList<Table> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.cameriere_tables_list_row, null);
                }
                Table t = items.get(position);
                if (t != null) {
                        
                		TextView tt = (TextView) v.findViewById(R.id.toptext);
                        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                        if(tt != null) {
                              tt.setText("Name: " + t.getTableName());                            }
                        if(bt != null){
                              bt.setText("Status: "+ t.getTableStatus());
                        }
                        
                }
                return v;
        }
    }

}