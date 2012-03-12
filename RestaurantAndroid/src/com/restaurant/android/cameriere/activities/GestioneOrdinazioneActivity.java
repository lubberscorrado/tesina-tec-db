package com.restaurant.android.cameriere.activities;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.text.TabExpander;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.restaurant.android.DbManager;
import com.restaurant.android.Error;
import com.restaurant.android.R;
import com.restaurant.android.RestaurantApplication;
import com.restaurant.android.Utility;
import com.restaurant.android.cameriere.activities.TableCardActivity.TableListAsyncTask;

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
		
		/* ******************************************************
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
		
		
		/* ******************************************************************************
		 * Aggiorno flag sul database e liste delle variazioni associate all'ordinazione
		 *******************************************************************************/
		/* Resetta tutte le flag checked per il dialog Multi Choice */
		resetFlagVariazioni();
		
		/* Recupera gli id delle variazioni associate alla comanda corrente */
		updateArrayListVariazioni();
		
		/* Ripristina correttamente tutte le flag delle variazioni */
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
		
		/* ******************************************************
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
                
                /* ***********************************************************
                 * Per aggiornare lo stato della checkbox è necessario
                 * aggiornare il valore della flag nel database e riformulare
                 * la query.
                 ************************************************************/
                
                ContentValues values = new ContentValues();
               	values.put("checked", isChecked);
                db.update("variazione", values , "idVariazione="+idVariazione, null);
                cursorVariazioni.requery();
                
                /* ********************************************************
                 * Aggiorno la lista delle variazioni temporanee a seconda
                 * del click dell'utente.
                 *********************************************************/
             
                cursorVariazioni.moveToPosition(whichButton);
                              
                if(isChecked == false) {
                  	elencoVariazioniAssociateAOrdinazione.remove(new Integer(idVariazione));
                  	elencoVariazioniRimosse.add(new Integer(idVariazione));
                  	
                } else {
                	elencoVariazioniAssociateAOrdinazione.add(new Integer(idVariazione));
                }
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
		button_modificaVariazioni.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.show();
			}
		});
		
		/* *******************************************************************
		 * Listener per la gestione della conferma dell'ordinazione
		 *********************************************************************/
		
		Button confermaOrdinazione = (Button)findViewById(R.id.button_gestioneOrdinazione_confermaOrdinazione);
		confermaOrdinazione.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					
				if(myOrdinazione.getIdOrdinazione() == 0) {
					
					/* ************************************************************************
					 * L'ordinazione non è presente nel database (quindi nuova ordinazione)
					 *************************************************************************/
					
					ContentValues values = new ContentValues();
					values.clear();
					values.put("idVoceMenu", myOrdinazione.getIdVoceMenu());
					values.put("quantita", Integer.parseInt(editText_quantita.getText().toString()));
					values.put("note", editText_note.getText().toString());
					values.put("idTavolo", myOrdinazione.getIdTavolo());
					values.put("stato", "SOSPESA");
					db.insertOrThrow("comanda", null, values);
					
					/* Recupero l'id dell'ultima ordinazione inserita nel database */
					String query = "SELECT idComanda from comanda order by idComanda DESC limit 1";
					Cursor c = db.rawQuery(query,  null);
					if (c != null && c.moveToFirst()) 
					    myOrdinazione.setIdOrdinazione(c.getInt(0)); 
					c.close();
					
					updateVariazioni();
					finish();
				
				} else if(myOrdinazione.getStato().equals("SOSPESA")) {
					
					/* ************************************************************************
					 * Aggiorno le informazioni della comanda sospesa all'interno del 
					 * database locale
					 *************************************************************************/
					updateOrdinazione();
					updateVariazioni();
					finish();
				} else {
				
					/* ***********************************************************************
					 * La comanda è già stata inviata al server ed è in un qualsiasi stato
					 * valido dopo l'invio. Posso verificare subito se la comanda è in uno stato
					 * diverso da INVIATA. In caso affermativo è già stata presa in carico e non
					 * può essere modificata. Nel caso sia inviata devo verificare la possibilità
					 * di modifica tramite il server. Se il procedimento di modifica
					 * va a buon fine, riporto i cambiamenti anche sul database locale. 
					 * La modifica in locale deve avvenire solo se in remoto la comanda è 
					 * modificata con successo.
					 ************************************************************************/
					
					if(!myOrdinazione.getStato().equals("INVIATA")) {
						Toast.makeText(getApplicationContext(), "Impossibile modificare la comanda (stato:" + 
																myOrdinazione.getStato()+")", 50).show();
					} else {
						new ModificaComandaAsyncTask().execute( (Object[]) null);
					}
				}
			}
		});
	}
	
	/**
	 * Async Task per la modfica di un ordinazione che è già stata 
	 * inviata al server
	 * @author Guerri Marco
	 */
	class ModificaComandaAsyncTask extends AsyncTask<Object, Object, Error> {

    	private ProgressDialog progressDialog;
    	   	
    	@Override
    	protected void onPreExecute() {
    		progressDialog = ProgressDialog.show(GestioneOrdinazioneActivity.this, "Attendere", "Modifica in corso");
    	}
    	
    	@Override
		protected Error doInBackground(Object... params) {
			
    		/************************************************************************
    		 * Creo il JSONObject da inviare al server per la modifica delle comanda,
    		 * recuperando tutte le variazioni associate all'ordinazione 
    		 * dall'ArrayList aggiornata
    		 ************************************************************************/
    		try {
    			
	    		JSONObject jsonObjectOrdinazione = new JSONObject();
				jsonObjectOrdinazione.put("idRemotoComanda", myOrdinazione.getIdRemotoOrdinazione());
				jsonObjectOrdinazione.put("quantita", Integer.parseInt(editText_quantita.getText().toString()));
				jsonObjectOrdinazione.put("note", editText_note.getText().toString());
					
				
				JSONArray jsonArrayVariazioni = new JSONArray();
				
				for(int i=0; i< elencoVariazioniAssociateAOrdinazione.size(); i++) {
					JSONObject jsonObjectVariazione = new JSONObject();
					jsonObjectVariazione.put("idVariazione", elencoVariazioniAssociateAOrdinazione.get(i));
					jsonArrayVariazioni.put(jsonObjectVariazione);
				}
				jsonObjectOrdinazione.put("variazioni", jsonArrayVariazioni);
				
				
				RestaurantApplication restApp = (RestaurantApplication)getApplication();
				String response = restApp.makeHttpJsonPostRequest(	restApp.getHost() + "ClientEJB/gestioneComande?action=MODIFICA_COMANDA", 
																	jsonObjectOrdinazione);
				
				JSONObject jsonObjectResponse = new JSONObject(response);
					
				if(jsonObjectResponse.getBoolean("success") == true)  {
				
					/* L'ordinazione deve essere ora modificato in locale */
					updateOrdinazione();
					updateVariazioni();
					
					return new Error("Ordinazione modificata", false );
				
				
				} else {
					
					/* Forzo la sincronizzazione del conto vista l'alta probabilità che
					 * l'errore sia dato dalla mancanza di sincronia sugli stati */
					TableCardActivity.updateConto = true;
					return new Error(jsonObjectResponse.getString("message"), true );
				}	
				
			} catch (ClientProtocolException e) {
			
				return new Error(e.toString(), true );
			} catch (IOException e) {
				return new Error(e.toString(), true );
			} catch (JSONException e) {
				return new Error(e.toString(), true );
			}
		}
    	
    	@Override
    	protected void onPostExecute(Error error) {
     	   progressDialog.dismiss();
     	  
     		Toast.makeText(getApplicationContext(), error.getError(), 50).show();
     	     
     	  finish();
    	}
    		
    }
	
	/**
	 * Aggiorna le variazioni associate ad un'ordinazione all'interno del database 
	 * locale
	 * @author Guerri Marco
	 */
	public void updateVariazioni() {
		
		/* Aggiorno le variazioni associate all'ordinazione aggiungendo e inserendo
		  nel database variazionecomanda (se replico una coppia
		  idVariazione idOrdinazione non viene inserita nel db per il vincolo
		  di unique). */
		 
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
		
		for(Integer idVecchiaVariazione : elencoVariazioniRimosse) 
			db.delete("variazionecomanda", "idVariazione="+ idVecchiaVariazione + " AND idComanda=" + myOrdinazione.getIdOrdinazione(), null);
	}
		
	/**
	 * Aggiorna le informazioni sull'ordinazione all'interno del database locale
	 * @author Guerri Marco
	 */
	
	public void updateOrdinazione() {
		ContentValues ordinazioneModificata = new ContentValues();
		ordinazioneModificata.put("quantita", editText_quantita.getText().toString());
		ordinazioneModificata.put("note", editText_note.getText().toString());
		db.update("comanda", ordinazioneModificata, "idComanda=" + myOrdinazione.getIdOrdinazione(), null);
	}
	
	/**
	 * Imposta a 0 il valore checked per tutte le variazioni presenti nel database.
	 * Le flag vengono ripristinate a seconda della comanda che si sta considerando.
	 */
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
	
	/**
	 * Recupera gli id delle variazioni che sono associate all'ordinazione corrente
	 * dal database.
	 */
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
	
	/**
	 * Refresha la ListView delle variazioni associate alla comanda andando a recuperare
	 * dal database il nome delle variazioni. Potrebbe essere ottimizzato per evitare
	 * un ulteriore accesso al DB, usando informazioni estratte in precedenza.
	 */
	public void updateLayoutVariazioni() {
		
		linearLayout.removeAllViews();
			
		for(Integer idVariazioneAssociata : elencoVariazioniAssociateAOrdinazione) {
			
			Log.d("VARIAZIONE!!!", "VARIAZIONE!!!");
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
