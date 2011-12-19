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

	private ArrayList<Integer> elencoVariazioniAssociateAOrdinazione;
	private ArrayList<Integer> elencoVariazioniRimosse;
	
	private LinearLayout linearLayout;
	
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
		
		linearLayout = (LinearLayout) findViewById(R.id.layoutListaVariazioni);
		elencoVariazioniAssociateAOrdinazione = new ArrayList<Integer>();
		elencoVariazioniRimosse = new ArrayList<Integer>();
		
		
		/*******************************************************************************
		 * Aggiorno flag sul database e liste delle variazioni associate all'ordinazione
		 *******************************************************************************/
		
		resetFlagVariazioni();
		updateArrayListVariazioni();
		updateFlagVariazioni();
		updateLayoutVariazioni();
		     			
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
		
		/* Insieme degli id delle variazioni associabili alla categoria 
		 * a cui appartiene la voce di menu considerata. Le variazioni
		 * sono quelle associate alla categoria stessa e alle categorie padre */
		
		String insiemeCategorie = "";
		Cursor cursorIdCategoria = db.query("vocemenu", 
											new String[] {"idCategoria"}, 
											"idVoceMenu=" +myOrdinazione.getIdVoceMenu(),
											null, null, null, null);
		int idCategoria;
		cursorIdCategoria.moveToFirst();
		
		if(cursorIdCategoria.getCount() > 0 ) {
			idCategoria = cursorIdCategoria.getInt(0);
		} else {
			idCategoria = 0;
		}
		
		insiemeCategorie = "(" + idCategoria;
		cursorIdCategoria.close();
		
		while(idCategoria!= 0)  {
			cursorIdCategoria = db.query("categoria", new String[] {"idCategoriaPadre"}, "idCategoria="+idCategoria,null,null,null,null);
			
			if(cursorIdCategoria.getCount() > 0 ) {
				cursorIdCategoria.moveToFirst();
				idCategoria = cursorIdCategoria.getInt(0);
				insiemeCategorie = insiemeCategorie + "," + idCategoria;
				cursorIdCategoria.close();
			} else {
				cursorIdCategoria.close();
				break;
			}
		
		}
		
		insiemeCategorie = insiemeCategorie + ")";
		
		Log.d("GestioneOrdinazione", "Insieme delle categorie legali " + insiemeCategorie);
		
		builder.setTitle("Variazioni disponibili");
		cursorVariazioni = db.query("variazione", 
									new String[] {"idVariazione _id","nome","checked"},
									"idCategoria in " + insiemeCategorie,  null, null, null, null, null);
	
		/* Il cursor per visualizzare gli elementi in un dialog necessita di selezionare
		 * tra gli attributi della tabella  un id identificativo del record con alias _id */
		
		builder.setMultiChoiceItems(cursorVariazioni, "checked", "nome", new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int whichButton,
                    boolean isChecked) {
        
            	cursorVariazioni.moveToPosition(whichButton);
                int idVariazione = cursorVariazioni.getInt(0);
                
                /************************************************************
                 * Per aggiornare lo stato della checkbox è necessario
                 * aggiornare il valore della flag nel database e riformulare
                 * la query.
                 ************************************************************/
                
                ContentValues values = new ContentValues();
               	values.put("checked", isChecked);
                db.update("variazione", values , "idVariazione="+idVariazione, null);
                cursorVariazioni.requery();
                
                /*********************************************************
                 * Aggiorno la lista delle variazioni temporanee a seconda
                 * del click dell'utente.
                 *********************************************************/
             
                cursorVariazioni.moveToPosition(whichButton);
                
                /* Verifico se la variazione è stata selezionata o deselezionata */
                if(isChecked == false) {
                  	elencoVariazioniAssociateAOrdinazione.remove(new Integer(idVariazione));
                  	elencoVariazioniRimosse.add(new Integer(idVariazione));
                  	
                } else {
                	elencoVariazioniAssociateAOrdinazione.add(new Integer(idVariazione));
                }
                
            	/* User clicked on a check box do some stuff */
            	Log.w("Checkbox Finestra Scelta Variazioni", " Variazione: " +idVariazione +", checked=" + isChecked);
            }
        });
				
		/* Aggiorno la lista delle variazioni associata all'ordinazione al momento
		 * della chiusura della dialog box */
		
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				updateLayoutVariazioni();
				
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
		
		
		/*************************************************
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
					
					/* Recupero l'id dell'ultima ordinazione inserita nel database */
					String query = "SELECT idComanda from comanda order by idComanda DESC limit 1";
					Cursor c = db.rawQuery(query,  null);
					if (c != null && c.moveToFirst()) 
					    myOrdinazione.setIdOrdinazione(c.getInt(0)); 
					c.close();
					
				
				} else {
					
					Log.d("GestioneOrdinazione" , "Ordinazione in sospeso che viene modificata");
					
					/*********************************************************
					 * Aggiorno le informazioni sulla comanda all'interno del 
					 * database
					 *********************************************************/
					
					ContentValues ordinazioneModificata = new ContentValues();
					ordinazioneModificata.put("quantita", editText_quantita.getText().toString());
					ordinazioneModificata.put("note", editText_note.getText().toString());
					db.update("comanda", ordinazioneModificata, "idComanda=" + myOrdinazione.getIdOrdinazione(), null);
				}
				
				
				/*************************************************************************
				 * Aggiorno le variazioni associate all'ordinazione aggiungendo e inserendo
				 * nel database variazionecomanda (se replico una coppia
				 * idVariazione idOrdinazione non viene inserita nel db per il vincolo
				 * di unique).
				 *************************************************************************/
				
				for(Integer idNuovaVariazione : elencoVariazioniAssociateAOrdinazione) {
                	ContentValues nuovaVariazione = new ContentValues();
                	nuovaVariazione.put("idComanda", myOrdinazione.getIdOrdinazione());
                	nuovaVariazione.put("idVariazione",idNuovaVariazione);
                	try {
                		db.insertOrThrow("variazionecomanda", null, nuovaVariazione);
                	} catch (Exception e) {
                		/* Eccezione probabilmente derivante dal fatto che la variazione
                		 * è già associata all'ordinazione */
                		
                		Log.d("ConfermaOrdinazione", e.toString());
                	}
				}
				
				for(Integer idVecchiaVariazione : elencoVariazioniRimosse) {
					db.delete("variazionecomanda", "idVariazione="+ idVecchiaVariazione + " AND idComanda=" + myOrdinazione.getIdOrdinazione(), null);
					
				}
				
				
				
				finish();
			}
		});
	}
		
	public void resetFlagVariazioni() {
		ContentValues values = new ContentValues();
		values.put("checked", 0);
		db.update("variazione", values, "1=1", null);
	}

	public void updateFlagVariazioni() {
		ContentValues checked = new ContentValues();
		checked.put("checked", 1);
		for(Integer idVariazioneAssociataAOrdinazione : elencoVariazioniAssociateAOrdinazione) 
			db.update("variazione", checked, "idVariazione=" + idVariazioneAssociataAOrdinazione, null);
		
	}
	
	public void updateArrayListVariazioni() {
		
		Cursor cursorElencoVariazioni = db.query("variazionecomanda", new String[] {"idVariazione"}, "idComanda=" + myOrdinazione.getIdOrdinazione(), null, null, null, null);
		
		elencoVariazioniAssociateAOrdinazione.clear();
		
		cursorElencoVariazioni.moveToFirst();
		while(!cursorElencoVariazioni.isAfterLast()) {
			elencoVariazioniAssociateAOrdinazione.add(new Integer(cursorElencoVariazioni.getInt(0)));
			cursorElencoVariazioni.moveToNext();
		}
		
		cursorElencoVariazioni.close();
		
	}
	
	public void updateLayoutVariazioni() {
		
		linearLayout.removeAllViews();
			
		for(Integer idVariazioneAssociata : elencoVariazioniAssociateAOrdinazione) {
			
			Cursor nomeVariazione = db.query("variazione", new String[] {"nome"}, "idVariazione=" + idVariazioneAssociata, null, null, null, null, null);
			nomeVariazione.moveToFirst();
			
			if(nomeVariazione.getCount() != 1) {
				TextView variazione = new TextView(this);
				variazione.setText("Var. non più presente");
			    variazione.setTextSize(18);
			    variazione.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			    linearLayout.addView(variazione);
			} else {
				TextView variazione = new TextView(this);
			    variazione.setText(nomeVariazione.getString(0));
			    variazione.setTextSize(18);
			    variazione.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			    linearLayout.addView(variazione);
			}
			nomeVariazione.close();
		}
	}
		
	@Override
	public void onDestroy(){
		super.onDestroy();
		cursorVariazioni.close();
		dbManager.close();
		db.close();
	}
	
	
	public void onResume() {
		super.onResume();
		
		
		
	}
	
}
