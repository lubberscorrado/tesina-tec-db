package com.restaurant.android.cameriere.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.restaurant.android.R;
import com.restaurant.android.Utility;

public class GestioneOrdinazioneActivity extends Activity {
	
	/* Attributi header */ 
	private TextView textView_nomeComanda = null;
	private TextView textView_nomeCameriere = null;
	
	/* Quantita */
	private EditText editText_quantita = null;
	
	/* Note */
	private EditText editText_note = null;
	
	/* ListView Elenco Variazioni */
	private ListView elencoVariazioni_listView = null;
	private ArrayAdapter elencoVariazioni_adapter = null;
	private Button button_modificaVariazioni = null; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cameriere_gestione_ordinazione);
		
		/* Recupero i vari elementi della GUI */
		textView_nomeComanda = (TextView) findViewById(R.id.textView_gestioneOrdinazione_nomeComanda_value);
		textView_nomeCameriere = (TextView) findViewById(R.id.textView_gestioneOrdinazione_nomeCameriere_value);
		
		editText_quantita = (EditText) findViewById(R.id.editText_gestioneOrdinazione_quantita);
		editText_note = (EditText) findViewById(R.id.editText_gestioneOrdinazione_note);
		
		button_modificaVariazioni = (Button) findViewById(R.id.button_gestioneOrdinazione_modificaVariazioni);
		
		// button_modificaVariazioni.setOnClickListener();
		
		/* Creo un'array di stringhe e lo visualizzo in una semplicissima ListView */
		elencoVariazioni_listView = (ListView) findViewById(R.id.listView_gestioneOrdinazioni_variazioniList);
		
		Object[] sArray = {"This", "is", 3.5, true, 2, "for", "bla"};
		ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sArray);
		elencoVariazioni_listView.setAdapter(adp);
		
		/* Chiamo un metodo definito in classe Utility che forza il ricalcolo
		 * dell'altezza della ListView (poichè una ListView inserita in una ScrollView
		 * dà alcuni problemi col ricalcolo dell'altezza */
		Utility.setListViewHeightBasedOnChildren(elencoVariazioni_listView);
	}
	
}
