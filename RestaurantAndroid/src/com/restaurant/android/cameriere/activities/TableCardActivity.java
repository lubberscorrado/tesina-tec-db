package com.restaurant.android.cameriere.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.cameriere_table_card);
	  
	  /* Recupero il valore col nome del tavolo */
	  Bundle b = getIntent().getExtras();
	  String tableName = b.getString("tableName");
	  Table myTable= (Table) b.getSerializable("tableObject");
	  
	  /* Tavolo de-serializzato */
	  Log.d("Scheda Tavolo", "De-serializzato il tavolo: " + myTable.getTableName());
	  
	  /* Cambio il titolo della scheda del tavolo */
	  TextView textView_tableName = (TextView) findViewById(R.id.tableCard_textView1);
	  textView_tableName.setText("Scheda " + tableName);
	}
}
