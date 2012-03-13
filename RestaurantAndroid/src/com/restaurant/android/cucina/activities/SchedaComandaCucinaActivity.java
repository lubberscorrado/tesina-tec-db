package com.restaurant.android.cucina.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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

import com.restaurant.android.R;
import com.restaurant.android.Utility;
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
	//				new OccupaTavoloAsyncTask().execute((Object[])null);
	//				new LiberaTavoloAsyncTask().execute((Object[])null);
	
				}
			});
			
			/* 2. completamento preparazione */
			Button buttonPreparazioneCompletata = (Button)  findViewById(R.id.cucina_schedaComanda_buttonPreparazioneCompletata);
			buttonPreparazioneCompletata.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View c) {
	//				new OccupaTavoloAsyncTask().execute((Object[])null);
				}
			});
			
			/* 3. annullamento preparazione */
			Button buttonAnnullaPreparazione = (Button)  findViewById(R.id.cucina_schedaComanda_buttonAnnullaPreparazione);
			buttonAnnullaPreparazione.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View c) {
	//				new OccupaTavoloAsyncTask().execute((Object[])null);
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
        		
        		Log.e(TAG, "getView() from VariazioniAdapter: beginning");
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
                
                Log.e(TAG, "getView() from VariazioniAdapter: ending");

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
    
    /* Aggiorno la list view del conto ricaricando gli ordini dal database */
//	runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				getOrdersConfirmed();
//			}
//	});
}
