package com.restaurant.android.cucina.activities;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.restaurant.android.DbManager;
import com.restaurant.android.Error;
import com.restaurant.android.R;
import com.restaurant.android.RestaurantApplication;
import com.restaurant.android.Utility;
import com.restaurant.android.cameriere.activities.Ordinazione;
import com.restaurant.android.cameriere.activities.TableCardActivity;
import com.restaurant.android.cucina.comanda.ComandaCucina;
import com.restaurant.android.cucina.comanda.Variazione;

/**
 * Activity che contiene la scheda con le info su una delle comande della cucina.
 * Consente di preparare cibi\bevande attraverso l'interfaccia cucina.
 * @author Fabio Pierazzi
 */
public class SchedaComandaCucinaActivity extends Activity {

	private static final String TAG = "SchedaComandaCucinaActivity";
	
	private ListView list_variazioni = null;
	private VariazioniAdapter adapter_listVariazioni = null;
	private ArrayList<Variazione> arrayList_variazioni;
	
	ComandaCucina comandaCucina; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cucina_scheda_comanda);
		  
		Log.d(TAG, "onCreate");
		
		/* Recupero l'oggetto passato come parametro al momento dell'invocazione
		 * di questa activity */
		Bundle b = getIntent().getExtras();
		this.comandaCucina = (ComandaCucina) b.getSerializable("oggettoComanda");
		
		Log.d(TAG, "Ho recuperato l'oggetto ComandaCucina relativo a: " + comandaCucina.getNomeVoceMenu());
		  
		/* ***************************************
		 *  Bottoni
		 ****************************************/
		
			/* 1. preparazione comanda */
			Button buttonPreparaComanda = (Button)  findViewById(R.id.cucina_schedaComanda_buttonPrepara);
			buttonPreparaComanda.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View c) {
					new InviaPreparazioneComandaAsyncTask().execute((Object[]) null);
				}
			});
			
			/* 2. completamento preparazione */
			Button buttonPreparazioneCompletata = (Button)  findViewById(R.id.cucina_schedaComanda_buttonPreparazioneCompletata);
			buttonPreparazioneCompletata.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View c) {
					new InviaCompletataPreparazioneComandaAsyncTask().execute((Object[])null);
				}
			});
			
			/* 3. annullamento preparazione */
			Button buttonAnnullaPreparazione = (Button)  findViewById(R.id.cucina_schedaComanda_buttonAnnullaPreparazione);
			buttonAnnullaPreparazione.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View c) {
					new InviaAnnullaPreparazioneAsyncTask().execute((Object[])null);
				}
			});
		 	  
		
		/* ***************************************
		 * ListView delle Variazioni
		 *****************************************/
		this.list_variazioni = (ListView) findViewById(R.id.cucina_schedaComanda_variazioniListView);
		this.arrayList_variazioni = new ArrayList<Variazione>();

		  this.adapter_listVariazioni = new VariazioniAdapter(getApplicationContext(), 
					R.layout.cucina_scheda_comanda_variazioni_list_row, 
					arrayList_variazioni);
		  
		  this.list_variazioni.setAdapter(this.adapter_listVariazioni);
		  
		  
		  /** Aggiorno la scheda del tavolo con le informazioni 
		   * contenute nell'oggetto ComandaCucina che mi è stato
		   * passato come parametro */
		  
		  try {
			  runOnUiThread(new Runnable() {
	 				@Override
	 				public void run() {
	 					updateSchedaComandaCucina();
	 					
	 					// Provo a forzarlo di nuovo
	 		        	Utility.setListViewHeightBasedOnChildren(list_variazioni);
	 				}
	 			});
		  } catch (Exception e) {
			  Log.e(TAG, "Errore durante l'aggiornamento grafico della schedaComandaCucina: " + e.toString());
		  }
		 
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG,"OnResume");
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG,"OnStart");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG,"OnStop");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"OnDestroy");
	}
    
    /************************************************************************
	 * Adapter per mostrare un semplice elenco delle variazioni
	 * @author Fabio Pierazzi
	 ************************************************************************/
    
    private class VariazioniAdapter extends ArrayAdapter<Variazione> {

        private ArrayList<Variazione> items;

        public VariazioniAdapter(Context context, int textViewResourceId, ArrayList<Variazione> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        		
//        		Log.e(TAG, "getView() from VariazioniAdapter: beginning");
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.cucina_scheda_comanda_variazioni_list_row, null);
                }
                
                Variazione variazione = items.get(position);
                
                if ( variazione != null) {
                        
                		/* Aggiorno i campi in base a quel che ho letto */
                		TextView textView_nomeVariazione = (TextView) v.findViewById(R.id.cucina_schedaComanda_listVariazioni_textViewNomeVariazione);
                		if(textView_nomeVariazione != null) {
                			textView_nomeVariazione.setText(variazione.getNomeVariazione());
                        }
                }
                
//                Log.e(TAG, "getView() from VariazioniAdapter: ending");

                return v;
          }
    }
    
    /************************************************************************
     * Metodo per reperire le variazioni
     * **********************************************************************/
    private void getVariazioni() {
    	
    	try {
    		arrayList_variazioni.clear();
    		
    		// TODO: Check
    		int size = comandaCucina.getArrayList_variazioni().size();
    		for(int k=0; k<size; ++k) {
    			
    			Variazione var = new Variazione();
    			
    			var.setNomeVariazione(comandaCucina.getArrayList_variazioni().get(k).getNomeVariazione());
    			var.setIdVariazione(comandaCucina.getArrayList_variazioni().get(k).getIdVariazione());
    			
    			Log.e(TAG + "adapter", "Aggiunta variazione: " + var.getNomeVariazione());
    			
    			this.arrayList_variazioni.add(var);
    		}
    		
    		/**************************************************************************
      	   * Aggiornamento dell'interfaccia grafica. Solo l'UI thread può modificare
      	   * la view.
      	   **************************************************************************/
      	  runOnUiThread(new Runnable() {
      		  public void run() {
      			adapter_listVariazioni.notifyDataSetChanged();
      			
      			/* Modifico manualmente la nuova altezza della listView */
                Utility.setListViewHeightBasedOnChildren(list_variazioni);
      		  }
      	  });
    		
    	} catch (Exception e) {
        	Log.e(TAG, e.toString());
        }
    	
    	
    }
    
    /** Metodo per aggiornare la scheda della comanda cucina con le 
     * informazioni passate durante l'attivazione dell'activity
     * @author Fabio Pierazzi
     */
    private void updateSchedaComandaCucina() {
    	
    	Log.d(TAG, "updateSchedaComandaCucina: beginning");
    	
    	String nomeTavolo = this.comandaCucina.getNomeTavolo();
    	String nomeVoceMenu = this.comandaCucina.getNomeVoceMenu();
    	int quantita = this.comandaCucina.getQuantita();
    	String statoComanda = this.comandaCucina.getStatoComanda();
    	String nomeCategoria = this.comandaCucina.getNomeCategoria();
    	
    	String noteComanda = this.comandaCucina.getNote();
    	
    	TextView tv_nomeTavolo = (TextView) findViewById(R.id.cucina_schedaComanda_textNomeTavolo);
    	tv_nomeTavolo.setText(nomeTavolo);
    	
    	TextView tv_nomeVoceMenu = (TextView) findViewById(R.id.cucina_schedaComanda_textNomeVoceMenu);
    	tv_nomeVoceMenu.setText(nomeVoceMenu);
    	
    	TextView tv_quantita = (TextView) findViewById(R.id.cucina_schedaComanda_textQuantitaComanda);
    	tv_quantita.setText(Integer.toString(quantita));

    	TextView tv_statoComanda = (TextView) findViewById(R.id.cucina_schedaComanda_textStatoComanda);
    	tv_statoComanda.setText(statoComanda);
    	
    	TextView tv_nomeCategoria = (TextView) findViewById(R.id.cucina_schedaComanda_textCategoriaComanda);
    	tv_nomeCategoria.setText(nomeCategoria);
    	
    	TextView tv_noteComanda = (TextView) findViewById(R.id.cucina_schedaComanda_textNoteComanda);
    	tv_noteComanda.setText(noteComanda);
    	
    	/* Visibilità dei pulsanti */
		
		if(comandaCucina.getStatoComanda().equals("INVIATA")) {
			
			
			Button btnPrepara = (Button)findViewById(R.id.cucina_schedaComanda_buttonPrepara);
			btnPrepara.setEnabled(true);
			Button btnCompleta= (Button)findViewById(R.id.cucina_schedaComanda_buttonPreparazioneCompletata);
			btnCompleta.setEnabled(false);
			Button btnAnnulla = (Button)findViewById(R.id.cucina_schedaComanda_buttonAnnullaPreparazione);
			btnAnnulla.setEnabled(false);
			
		} else if(comandaCucina.getStatoComanda().equals("INPREPARAZIONE")) {
			
			Button btnPrepara = (Button)findViewById(R.id.cucina_schedaComanda_buttonPrepara);
			btnPrepara.setEnabled(false);
			Button btnCompleta= (Button)findViewById(R.id.cucina_schedaComanda_buttonPreparazioneCompletata);
			btnCompleta.setEnabled(true);
			Button btnAnnulla = (Button)findViewById(R.id.cucina_schedaComanda_buttonAnnullaPreparazione);
			btnAnnulla.setEnabled(true);
			
		}
		
		/** Recupero variazioni */
    	try {
        	getVariazioni();
        	
        	// Provo a forzarlo di nuovo
        	Utility.setListViewHeightBasedOnChildren(list_variazioni);
    	} catch(Exception e) {
    		Log.e(TAG + ":updateSchedaComandaCucina()", "Errore nell'esecuzione di getVariazioni();");
    	}
		
    	Log.d(TAG, "updateSchedaComandaCucina: ending");
    }
    
    
    /**
     * Invio della richiesta per far passare la comanda dallo stato "INVIATA"
     * allo stato "INPREPARAZIONE".
     * @author Fabio Pierazzi
     */
    class InviaPreparazioneComandaAsyncTask extends AsyncTask<Object, Object, Error> {

    	private ProgressDialog progressDialog;
    	   	
    	@Override
    	protected void onPreExecute() {
    		progressDialog = ProgressDialog.show(SchedaComandaCucinaActivity.this, "Attendere", "Invio richiesta di inizio preparazione della comanda");
    	}
    	
    	@Override
		protected Error doInBackground(Object... params) {
			
			RestaurantApplication restApp = ((RestaurantApplication)getApplication());
			
			try {
				
				HashMap<String,String> requestParameters = new HashMap<String,String>();
		   		requestParameters.put("action","UPDATE_STATO");
		   		requestParameters.put("idComanda", new Integer(comandaCucina.getIdComanda()).toString());
		   		requestParameters.put("stato", "INPREPARAZIONE");
		   		
				String response = restApp.makeHttpPostRequest(	restApp.getHost() + "ClientEJB/gestioneComande", 
						requestParameters);
				
				JSONObject responseJsonObject = new JSONObject(response);
									
				if(responseJsonObject.getBoolean("success") == false) {
					Log.e(TAG, "Errore nell'aggiornamento di stato della comanda");
					return new Error(responseJsonObject.getString("message"), true);
				} else {
					
					/* Aggiorno la lista delle ordinazioni sospese e delle ordinazioni confermate */
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							/** Aggiorno manualmente la GUI, visto che comunque quando una comanda 
				    		 * passa "INPREPARAZIONE" non può più essere modificata */
				    		comandaCucina.setStatoComanda("INPREPARAZIONE");
				    		
				    		updateSchedaComandaCucina();
				    		
							Log.d(TAG, "Aggiornamento stato comanda avvenuto con successo");
							
						}
					});
					return new Error("Preparazione Comande", false);
				}
				
			} catch (Exception e) {
				return new Error("Errore durante la conferma delle comande", true);
			} finally {
				progressDialog.dismiss();
			}
			
		}
    	
    	@Override
    	protected void onPostExecute(Error error) {
    		
    		
     	   progressDialog.dismiss();
     	   if(error.errorOccurred()) 
     		   Toast.makeText(getApplicationContext(), error.getError(), 20).show();
     	 }
    }
    
    /**
     * Aggiorno lo stato della comanda: "INPREPARAZIONE" -> "PRONTA"
     * @author Fabio Pierazzi
     */
    class InviaCompletataPreparazioneComandaAsyncTask extends AsyncTask<Object, Object, Error> {

    	private ProgressDialog progressDialog;
    	   	
    	@Override
    	protected void onPreExecute() {
    		progressDialog = ProgressDialog.show(SchedaComandaCucinaActivity.this, "Attendere", "Invio richiesta di completamento preparazione della comanda");
    	}
    	
    	@Override
		protected Error doInBackground(Object... params) {
			
			RestaurantApplication restApp = ((RestaurantApplication)getApplication());
			
			try {
				
				HashMap<String,String> requestParameters = new HashMap<String,String>();
		   		requestParameters.put("action","UPDATE_STATO");
		   		requestParameters.put("idComanda", new Integer(comandaCucina.getIdComanda()).toString());
		   		requestParameters.put("stato", "PRONTA");
		   		
				String response = restApp.makeHttpPostRequest(	restApp.getHost() + "ClientEJB/gestioneComande", 
						requestParameters);
				
				JSONObject responseJsonObject = new JSONObject(response);
									
				if(responseJsonObject.getBoolean("success") == false) {
					Log.e(TAG, "Errore nell'aggiornamento di stato della comanda");
					return new Error(responseJsonObject.getString("message"), true);
				} else {
					
					/* Aggiorno la lista delle ordinazioni sospese e delle ordinazioni confermate */
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							/** Aggiorno manualmente la GUI, visto che comunque quando una comanda 
				    		 * passa "INPREPARAZIONE" non può più essere modificata */
				    		comandaCucina.setStatoComanda("PRONTA");
				    		
//				    		updateSchedaComandaCucina();
				    		Toast.makeText(getApplicationContext(), "Operazione completata con successo", 20).show();
				    		
				    		Log.d(TAG, "Aggiornamento stato comanda avvenuto con successo");
				    		
				    		
						}
					});
					return new Error("Preparazione Comande", false);
				}
				
			} catch (Exception e) {
				return new Error("Errore durante la conferma delle comande", true);
			} finally {
				progressDialog.dismiss();
			}
			
		}
    	
    	@Override
    	protected void onPostExecute(Error error) {
    		
    		
     	   progressDialog.dismiss();
     	   if(error.errorOccurred()) 
     		   Toast.makeText(getApplicationContext(), error.getError(), 20).show();
     	   
     	   /** Chiudo l'activity */
     	   finish();
     	 }
    }

    /** Cambio lo stato della comanda: "INPREPARAZIONE" -> "INVIATA"
     * @author Fabio Pierazzi
     */
    class InviaAnnullaPreparazioneAsyncTask extends AsyncTask<Object, Object, Error> {

    	private ProgressDialog progressDialog;
    	   	
    	@Override
    	protected void onPreExecute() {
    		progressDialog = ProgressDialog.show(SchedaComandaCucinaActivity.this, "Attendere", "Invio richiesta di annullamento preparazione della comanda");
    	}
    	
    	@Override
		protected Error doInBackground(Object... params) {
			
			RestaurantApplication restApp = ((RestaurantApplication)getApplication());
			
			try {
				
				HashMap<String,String> requestParameters = new HashMap<String,String>();
		   		requestParameters.put("action","UPDATE_STATO");
		   		requestParameters.put("idComanda", new Integer(comandaCucina.getIdComanda()).toString());
		   		requestParameters.put("stato", "INVIATA");
		   		
				String response = restApp.makeHttpPostRequest(	restApp.getHost() + "ClientEJB/gestioneComande", 
						requestParameters);
				
				JSONObject responseJsonObject = new JSONObject(response);
									
				if(responseJsonObject.getBoolean("success") == false) {
					Log.e(TAG, "Errore nell'aggiornamento di stato della comanda");
					return new Error(responseJsonObject.getString("message"), true);
				} else {
					
					/* Aggiorno la lista delle ordinazioni sospese e delle ordinazioni confermate */
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							/** Aggiorno manualmente la GUI, visto che comunque quando una comanda 
				    		 * passa "INPREPARAZIONE" non può più essere modificata */
				    		comandaCucina.setStatoComanda("INVIATA");
				    		
				    		updateSchedaComandaCucina();
				    		
							Log.d(TAG, "Aggiornamento stato comanda avvenuto con successo");
							
						}
					});
					return new Error("Preparazione Comande", false);
				}
				
			} catch (Exception e) {
				return new Error("Errore durante la conferma delle comande", true);
			} finally {
				progressDialog.dismiss();
			}
			
		}
    	
    	@Override
    	protected void onPostExecute(Error error) {
    		
    		
     	   progressDialog.dismiss();
     	   if(error.errorOccurred()) 
     		   Toast.makeText(getApplicationContext(), error.getError(), 20).show();
     	 }
    }
    
    /* Aggiorno la list view del conto ricaricando gli ordini dal database */
//	runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				getOrdersConfirmed();
//			}
//	});
}
