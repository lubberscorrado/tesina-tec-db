package com.restaurant.android.cameriere.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.restaurant.android.DbManager;
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
	private Button button_aumentaQuantita;
	private Button button_diminuisciQuantita;
	
	private Ordinazione myOrdinazione;
	
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cameriere_gestione_ordinazione);
		
		/* Recupero i parametri che mi sono stati passati dall'Activity
		 * che mi ha invocato */
		Bundle bundle_received = getIntent().getExtras();
		myOrdinazione = (Ordinazione) bundle_received.getSerializable("ordinazione");
					
		/* Recupero i vari elementi della GUI */
		textView_nomeComanda = (TextView) findViewById(R.id.textView_gestioneOrdinazione_nomeComanda_value);
		
		Log.d("GestioneOrdinazione", "Nome dell'ordinazione " + myOrdinazione.getNome());
		textView_nomeComanda.setText(myOrdinazione.getNome());
			
		editText_quantita = (EditText) findViewById(R.id.editText_gestioneOrdinazione_quantita);
		editText_quantita.setText(new Integer(myOrdinazione.getQuantita()).toString());
		
		editText_note = (EditText) findViewById(R.id.editText_gestioneOrdinazione_note);
		editText_note.setText(myOrdinazione.getNote());
		
		button_modificaVariazioni = (Button) findViewById(R.id.button_gestioneOrdinazione_modificaVariazioni);
		
		/* Creo un'array di stringhe e lo visualizzo in una semplicissima ListView */
		elencoVariazioni_listView = (ListView) findViewById(R.id.listView_gestioneOrdinazioni_variazioniList);
		
		button_aumentaQuantita = (Button) findViewById(R.id.buttonAumentaQuantita);
		button_aumentaQuantita.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				editText_quantita.setText("" + (Integer.parseInt(editText_quantita.getText().toString()) + 1));
			}
		});
		
		button_diminuisciQuantita = (Button) findViewById(R.id.buttonDiminuisciQuantita);
		button_diminuisciQuantita.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int qty = Integer.parseInt(editText_quantita.getText().toString()) -1;
				if(qty < 0)
					qty = 0;
				editText_quantita.setText("" + qty);
			}
		});
		
		
		/** ***********************************************
		 * Alert Dialog per la gestione delle variazioni
		 * ************************************************ */
		final CharSequence[] items = {"Red", "Green", "Blue", "Red", "Green", "Blue", "Red", "Green", "Blue", "Red", "Green", "Blue"};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(GestioneOrdinazioneActivity.this);
		builder.setTitle("Pick a color");
		
		builder.setMultiChoiceItems(items, new boolean[] {false, true, false, false, true, false, false, true, false, false, true, false}, new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int whichButton,
                    boolean isChecked) {

                /* User clicked on a check box do some stuff */
            	Log.w("Checkbox Finestra Scelta Variazioni", Boolean.toString(isChecked));
//            	Toast.makeText(getApplicationContext(), Boolean.toString(isChecked), Toast.LENGTH_SHORT).show();
            	
            }
        });
				
		final AlertDialog alert = builder.create();

		/* Bottone per selezionare quali variazioni associare all'ordinazione */
		button_modificaVariazioni.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				alert.show();
			}
		});
		
		
		/** ***********************************************
		 * Conferma dell'ordinazione
		 * ************************************************ */
		Button confermaOrdinazione = (Button)findViewById(R.id.button_gestioneOrdinazione_confermaOrdinazione);
		confermaOrdinazione.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				DbManager dbManager = new DbManager(getApplicationContext());
				SQLiteDatabase db = dbManager.getWritableDatabase();
				
				if(myOrdinazione.getIdOrdinazione() == 0) {
					
					Log.d("GestioneOrdinazione" , "Nuova ordinazione");
					
					/* L'ordinazione non è presenta in database */
					ContentValues values = new ContentValues();
					values.clear();
					values.put("idVoceMenu", myOrdinazione.getIdVoceMenu());
					values.put("quantita", Integer.parseInt(editText_quantita.getText().toString()));
					values.put("note", editText_note.getText().toString());
					values.put("idTavolo", myOrdinazione.getIdTavolo());
								
					db.insertOrThrow("comanda", null, values);
					onDestroy();
				
				} else {
					
					Log.d("GestioneOrdinazione" , "Ordinazione in sospeso");
					
					/*********************************************************
					 * Aggiorno le informazioni sulla comanda all'interno del 
					 * database
					 *********************************************************/
					
					ContentValues ordinazioneModificata = new ContentValues();
					ordinazioneModificata.put("quantita", editText_quantita.getText().toString());
					ordinazioneModificata.put("note", editText_note.getText().toString());
					db.update("comanda", ordinazioneModificata, "idComanda=" + myOrdinazione.getIdOrdinazione(), null);
				}
				
				db.close();
				dbManager.close();
				finish();
			}
		});
		
		
		Object[] sArray = {};
		ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sArray);
		elencoVariazioni_listView.setAdapter(adp);
		
		/* Chiamo un metodo definito in classe Utility che forza il ricalcolo
		 * dell'altezza della ListView (poichè una ListView inserita in una ScrollView
		 * dà alcuni problemi col ricalcolo dell'altezza */
		Utility.setListViewHeightBasedOnChildren(elencoVariazioni_listView);
	}
	
}
