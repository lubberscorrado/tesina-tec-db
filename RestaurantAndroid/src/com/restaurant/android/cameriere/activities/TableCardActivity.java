package com.restaurant.android.cameriere.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.restaurant.android.R;

/**
 * Activity che rappresenta la scheda di un singolo
 * tavolo. Viene tipicamente mostrata a seguito di un 
 * click su uno degli elementi della ListView della 
 * pagina precedente. 
 * 
 * @author Fabio Pierazzi
 */
public class TableCardActivity extends Activity {
	
//	
//	/* Variabile usata per il logging */
//	private static final String TAG = "TablesListActivity";
//	
//	private ListView contoListView;
//	private boolean runThread;
//	private boolean pauseThread;
//	private ProgressDialog progressDialog = null;
//	private ArrayList<Ordinazione> array_ordinazioni = null;
//	
//	/* Adapter per il conto */
//	private ContoAdapter conto_adapter;
//	// private PrenotationAdapter prenotation_adapter;
//
//	// private UpdaterThread updaterThread;
//    	
//    	
    	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.cameriere_table_card);
		  
		  /* ******************************************
		   * Recupero il valore col nome del tavolo dall'oggetto
		   * passato durante l'invocazione di questa activity 
		   * dall'elenco tavoli
		   * ***************************************** */
		  Bundle b = getIntent().getExtras();
		  String tableName = b.getString("tableName");
		  Table myTable= (Table) b.getSerializable("tableObject");
		  
		  /* Tavolo de-serializzato */
		  Log.d("Scheda Tavolo", "De-serializzato il tavolo: " + myTable.getTableName());
		  
		  /* Cambio il titolo della scheda del tavolo */
		  TextView textView_tableName = (TextView) findViewById(R.id.tableCard_textView_Title);
		  textView_tableName.setText("Scheda " + tableName);
		  
		  
		  
		  // Aggiungo Listener al bottone di "Prendi Ordinazione"
		  Button button_prendiOrdinazione = (Button) findViewById(R.id.button_tableCard_prendiOrdinazione);
			
			// Provo a cambiare activity
		  button_prendiOrdinazione.setOnClickListener(new OnClickListener() {
			  	@Override
			  	public void onClick(View v) {
			  		//-----------------------
			        // Open New Activity
			  		//-----------------------
			  		Intent myIntent = new Intent(TableCardActivity.this, MenuListActivity.class);
			  		TableCardActivity.this.startActivity(myIntent);
			  	}
		});

		  
		  
		  /* *****************************************
		   * Configuro le ListViews
		   * *****************************************/
//		  
//		  // Recupero riferimento a conto
//		  this.contoListView = (ListView) findViewById(R.id.listView_contoList);
//	  
//		  array_ordinazioni = new ArrayList<Ordinazione>();
//		  
//		  /*
//		  this.conto_adapter = new ContoAdapter(getApplicationContext(), 
//					R.layout.cameriere_tables_list_row, 
//					m_tables);
//		  */
	}
	
	
	/************************************************************************
	 * Adapter per gestire il rendering personalizzato degli elementi
	 * della lista con le ordinazioni del conto
	 ************************************************************************/
//    
//    private class ContoAdapter extends ArrayAdapter<Ordinazione> {
//
//        private ArrayList<Ordinazione> items;
//
//        public ContoAdapter(Context context, int textViewResourceId, ArrayList<Ordinazione> items) {
//                super(context, textViewResourceId, items);
//                this.items = items;
//        }
//        
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//                View v = convertView;
//                if (v == null) {
//                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    v = vi.inflate(R.layout.cameriere_tables_list_row, null);
//                }
//                Ordinazione o = items.get(position);
//                if (o != null) {
//                        
//                		TextView tt = (TextView) v.findViewById(R.id.toptext);
//                        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
//                        if(tt != null) {
//                              tt.setText("Name: " + t.getTableName());                            
//                        }
//                        if(bt != null){
//                              bt.setText("Status: "+ t.getTableStatus());
//                        }
//                        
//                }
//                return v;
//        }
//    }
}
