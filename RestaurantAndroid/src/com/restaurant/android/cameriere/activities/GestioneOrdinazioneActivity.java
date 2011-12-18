package com.restaurant.android.cameriere.activities;


import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

	private Button button_modificaVariazioni = null; 
	private Button button_aumentaQuantita;
	private Button button_diminuisciQuantita;
	
	private Ordinazione myOrdinazione;
	
	private Cursor cursorVariazioni;
	private DbManager dbManager;
	private SQLiteDatabase db;

	private ArrayList<String> elencoVariazioni;
	
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cameriere_gestione_ordinazione);
		
		/*******************************************************
		 * Inizializzazione degli oggetti per la gestione del DB
		 *******************************************************/
		dbManager = new DbManager(getApplication());
		db = dbManager.getWritableDatabase();
		
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
		
		elencoVariazioni = new ArrayList<String>();
		
		Cursor cursorVariazioniSelezionata = db.query("variazione", new String[] {"nome"}, null,null,null,null,null);
		
		cursorVariazioniSelezionata.moveToFirst();
		while(!cursorVariazioniSelezionata.isAfterLast()) {
			elencoVariazioni.add(cursorVariazioniSelezionata.getString(0));
			cursorVariazioniSelezionata.moveToNext();
		}
		
		cursorVariazioniSelezionata.close();
		
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layoutListaVariazioni);
		
		int i=0;
		for(String s : elencoVariazioni) {
			 TextView variazione = new TextView(this);
		     variazione.setText(s);
		     variazione.setId(i++);
		     variazione.setTextSize(18);
		     variazione.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		     linearLayout.addView(variazione);
		}
		
		
			
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
		
		
		
		/*******************************************************
		 * Alert Dialog per la gestione delle variazioni
		 *******************************************************/
		AlertDialog.Builder builder = new AlertDialog.Builder(GestioneOrdinazioneActivity.this);
		
		builder.setTitle("Variazioni disponibili");
		cursorVariazioni = db.query("variazione", 
									new String[] {"idVariazione _id","nome","checked"},
									null, null, null, null, null, null);
	
		/* Il cursor per visualizzare gli elementi in un dialog necessita di selezionare
		 * tra gli attributi della tabella  un id identificativo del record con alias _id */
		
		builder.setMultiChoiceItems(cursorVariazioni, "checked", "nome", new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int whichButton,
                    boolean isChecked) {
        
            	cursorVariazioni.moveToPosition(whichButton);
                int idVariazione = cursorVariazioni.getInt(0);
                
                ContentValues values = new ContentValues();
               	values.put("checked", isChecked);
                
                db.update("variazione", values , "idVariazione="+idVariazione, null);
                cursorVariazioni.requery();
                
                /*********************************************************
                 * Aggiorno l'associazione comanda-variazione all'interno
                 * del database per la voce selezionata
                 *********************************************************/
             
                cursorVariazioni.moveToPosition(whichButton);
                
                /* Verifico se la comanda è checked */
                if(cursorVariazioni.getInt(2) == 0) {
			
                	/******************************************************************
                	 * Rimuovo qualsiasi associazione tra la comanda e la variazione 
                	 * correntemente considerata.
                	 ******************************************************************/
                	db.delete(	"variazionecomanda", 
                				"idComanda=" + myOrdinazione.getIdOrdinazione() + 
                				" AND idVariazione=" + idVariazione, null);
                	
                } else {
                		
                	/*******************************************************************
                	 * La coppia idComanda, idVariazione ha un vincolo unique, quindi un 
                	 * ulteriore inserimento genera un'eccezione. In ogni caso, se la 
                	 * comanda è ora in stato checked, in precedenza era sicuramente non
                	 * selezionata e quindi non presente nel db.
                	 *******************************************************************/
                	ContentValues nuovaVariazione = new ContentValues();
                	nuovaVariazione.put("idComanda", myOrdinazione.getIdOrdinazione());
                	nuovaVariazione.put("idVariazione",idVariazione);
                		
                	db.insertOrThrow("variazionecomanda", null, nuovaVariazione);
                }
                
            	/* User clicked on a check box do some stuff */
            	Log.w("Checkbox Finestra Scelta Variazioni", " Variazione: " +idVariazione +", checked=" + isChecked);
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
				
				finish();
			}
		});
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		dbManager.close();
		db.close();
	}
	
	
	public void onResume() {
		super.onResume();
		
		
		
	}
	
}
