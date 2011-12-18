package com.restaurant.android.cameriere.prenotazioni;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.restaurant.android.DbManager;
import com.restaurant.android.Error;
import com.restaurant.android.R;
import com.restaurant.android.Utility;
import com.restaurant.android.cameriere.activities.GestioneOrdinazioneActivity;
import com.restaurant.android.cameriere.activities.MenuListActivity;
import com.restaurant.android.cameriere.activities.Ordinazione;
import com.restaurant.android.cameriere.activities.Table;
import com.restaurant.android.cameriere.activities.TableCardActivity;

/**
 * Activity per mostrare l'elenco delle prenotazioni. 
 * Attraverso questa Activity, un cameriere ha la possibilità di 
 * visualizzare in ordine alfabetico le prenotazioni. 
 * 
 * @author Fabio Pierazzi
 */
public class PrenotationsListActivity extends Activity {
	
	/* Variabile usata per il logging */
	private static final String TAG = "PrenotationsListActivity";
	
	/* Variabili necessarie alla ListView per mostrare
	 * l'elenco delle prenotazioni effettuate */
	private ListView prenotationListView = null;
	private ArrayList<Prenotazione> prenotationListView_arrayPrenotazioni = null;
	private PrenotationAdapter prenotationListView_adapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.cameriere_prenotations_list);

	      /** *****************************************
		   * Configuro ListView per le Prenotazioni
		   * *****************************************/
	      
		  this.prenotationListView = (ListView) findViewById(R.id.prenotationsListActivity_listView_prenotationsList);
		  
		  this.prenotationListView_arrayPrenotazioni = new ArrayList<Prenotazione>();
		  
		  this.prenotationListView_adapter = new PrenotationAdapter(getApplicationContext(), 
					R.layout.cameriere_prenotations_list_row, 
					prenotationListView_arrayPrenotazioni);
		  
		  this.prenotationListView.setAdapter(this.prenotationListView_adapter);
		  
		  /* *************************************************************
	       * Listener per il click su un elemento della lista dei tavoli 
	       **************************************************************/
	      prenotationListView.setOnItemClickListener(new OnItemClickListener() {
		  	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		  	    	 Log.i(TAG, "Hai cliccato su una prenotazione: " + prenotationListView_adapter.getItem(position).getNomeCliente());

		  	    	 /* Apro una nuova finestra con l'opzione di occupare il tavolo 
		  	    	  * secondo la prenotazione */
		  	    	 
//		  	   	Intent myIntent = new Intent(TableCardActivity.this, TableCardActivity.class);
//		  	    	  
//		  	     /* Creo un bundle per passare dei dati alla nuova activity */
//			  	     Bundle b = new Bundle();
//			  	     b.putSerializable("tableObject", (Table) m_adapter.getItem(position));
//			  	     b.putString("tableName", m_adapter.getItem(position).getTableName());
//			  	     myIntent.putExtras(b);
//			  	     startActivity(myIntent);
		  	    	 
		  	    } 
		  	    
      }); // fine itemClickListener
		  
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d("TableCardActivity","OnResume");
		new TableListAsyncTask().execute(null);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d("TableCardActivity","OnStart");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d("TableCardActivity","OnStop");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("TableCardActivity","OnDestroy");
		Log.d("TableCardActivity","Stoppo il thread di aggiornamento dei tavoli");
	}

	
	/**
     * Metodo per reperire l'elenco delle prenotazioni di tutti i tavoli
     * @author Fabio Pierazzi
     */
    private void getPrenotations(){
    	
    	Log.i(TAG, "Entrato in getPrenotations();");
          try{

//        	  RestaurantApplication restApplication = (RestaurantApplication)getApplication();
//        	  String url = ((RestaurantApplication)getApplication()).getHost();
//        	  String response = restApplication.makeHttpGetRequest(url + "ClientEJB/statoTavolo", new HashMap<String, String>());
//        	  
        	  prenotationListView_arrayPrenotazioni.clear();
        	  
        	  /**********************************************************
        	   * Decodifica della risposa del server contenente lo stato 
        	   * dei tavoli.
        	   **********************************************************/
        	  // JSONObject jsonObject = new JSONObject(response);
        	  
        	  // if(jsonObject.getBoolean("success") == true) {
        		  
        		 //  JSONArray jsonArray = jsonObject.getJSONArray("statoTavolo");
        		  
        		  // for(int i=0; i< jsonArray.length() ; i++) {
        		  for(int i=0; i< 15; i++) {
        			  Prenotazione p = new Prenotazione();
        			  
        			  if(i%3==0) {
        				  p.setNomeCliente("Turoldo");
        				  p.setNumPersone(10);
        				  p.setTimeAndDate("23/12/2011 22:30");
        			  } else {
        				  p.setNomeCliente("Bazinga");
        				  p.setNumPersone(5);
        				  p.setTimeAndDate("25/12/2011 21:30");
        			  }
        			  
//        			  o.setTableName(jsonArray.getJSONObject(i).getString("nomeTavolo"));
//        			  o.setTableStatus(jsonArray.getJSONObject(i).getString("statoTavolo"));
//        			  o.setTableId(Integer.parseInt(jsonArray.getJSONObject(i).getString("idTavolo")));
        			  
        			  prenotationListView_arrayPrenotazioni.add(p);
          		  }
        		  
        		 
//         	  }
        	  
        	  /**************************************************************************
        	   * Aggiornamento dell'interfaccia grafica. Solo l'UI thread può modificare
        	   * la view.
        	   **************************************************************************/
        	  runOnUiThread(new Runnable() {
        		  public void run() {
        			  prenotationListView_adapter.notifyDataSetChanged();
        			  
        			  Log.w(TAG, "Cambio altezza prenotationListView ");
            		  /* Modifico manualmente la nuova altezza della listView */
                      // Utility.setListViewHeightBasedOnChildren(prenotationListView);
        		  }
        	  });
        	  
        	  Log.i(	"TablesListService" + ": getPrenotations()", 
		        			  	"Number of Prenotations Loaded: " + 
		            		  	prenotationListView_arrayPrenotazioni.size());
              
        } catch (Exception e) {
        	Log.e("OdersListService" + ": BACKGROUND_PROC", e.getMessage());
        }
          
        Log.i(TAG, "Uscito da getPrenotations()");
    } // end of getPrenotations();

    
    /**
     * Adapter le prenotazioni. 
     * @author Fabio Pierazzi
     */
    private class PrenotationAdapter extends ArrayAdapter<Prenotazione> {

        private ArrayList<Prenotazione> items;

        public PrenotationAdapter(Context context, int textViewResourceId, ArrayList<Prenotazione> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	
        		/* Eseguo un inflate del Layout */
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.cameriere_prenotations_list_row, null);
                }
                
                /* Recupero l'oggetto Prenotazione e utilizzo i suoi campi per
                 * scriverli all'interno della lista delle prenotazioni */
                Prenotazione prenotazione = items.get(position);
                
                if (prenotazione != null) {
                	
                		TextView textView_dataPrenotazione = (TextView) v.findViewById(R.id.textView_prenotationList_data);
                		TextView textView_numPersone = (TextView) v.findViewById(R.id.textView_prenotationList_numPersone);
                		TextView textView_nomeCliente = (TextView) v.findViewById(R.id.textView_prenotationList_nomeCliente);
                		
                		if(textView_dataPrenotazione != null) {
                			textView_dataPrenotazione.setText(prenotazione.getTimeAndDate());                            
                        }
                		
                		if(textView_numPersone != null){
                			textView_numPersone.setText(Integer.toString(prenotazione.getNumPersone()));
                        }

                		if(textView_nomeCliente != null) {
                			textView_nomeCliente.setText(prenotazione.getNomeCliente());
                        }
                		
                }

                return v;
        }
    } // end of PrenotationAdapter

	
	
    /**
     * Async Task per l'aggiornamento delle informazioni dell'elenco 
     * prenotazioni. 
     * @author Fabio Pierazzi
     */
    class TableListAsyncTask extends AsyncTask<Object, Object, Error> {

    	private ProgressDialog progressDialog;
    	   	
    	@Override
    	protected void onPreExecute() {
    		progressDialog = ProgressDialog.show(PrenotationsListActivity.this, "Attendere", "Caricamento elenco prenotazioni");
    	}
    	
    	@Override
		protected Error doInBackground(Object... params) {
			
//			RestaurantApplication restApp = (RestaurantApplication)getApplication();
//			String response = "";
//					
//			try {
//				response = restApp.makeHttpGetRequest(restApp.getHost() + "ClientEJB/statoTavolo", new HashMap<String,String>());
//			} catch (ClientProtocolException e) {
//				
//			} catch (IOException e) {
//				return new Error("Richiesta effettuata correttamente", false );
//			}
			
//			Log.d("TableListAsyncTask" , response);
			
			/*****************************************************************
			 * Aggiornamento dell'interfaccia grafica dello stato del tavolo
			 *****************************************************************/
			try {
				
//				JSONObject jsonObject = new JSONObject(response);
//			
//						
//				if(jsonObject.getString("success").equals("true")) {
//				
//					JSONArray jsonArray = jsonObject.getJSONArray("statoTavolo");
//					
//					int i=0;
//					while(jsonArray.getJSONObject(i).getInt("idTavolo") != myTable.getTableId())
//						i++;
//					
//					if(i == jsonArray.length()) {
//						/* Non sono stati trovati tavoli che fanno match con l'id */
//						return new Error("Impossibile trovare il tavolo richiesto", true);
//					}
//					myTable.setArea(jsonArray.getJSONObject(i).getString("nomeArea"));
//					myTable.setTableName(jsonArray.getJSONObject(i).getString("nomeTavolo"));
//					myTable.setTableStatus(jsonArray.getJSONObject(i).getString("statoTavolo"));
//					myTable.setPiano(jsonArray.getJSONObject(i).getString("numeroPiano"));
//					myTable.setCameriere(jsonArray.getJSONObject(i).getString("cameriere"));
					runOnUiThread(new Runnable() {
						public void run() {
//							updateStatoTavolo();
//							getOrdersWaitingToBeConfirmed();
							getPrenotations();
						}
					});
//								
//					return new Error("Richiesta effettuata correttamente", false );
//			
//				} else {
//					return new Error("Errore durante l'aggiornamento del tavolo", true);
//				}
//				
//			} catch (JSONException e) {
				
		    // TODO: Rimetti JSONException come tipo di questo Catch
			} catch (Exception e) {
				return new Error("Errore durante l'aggiornamento dell'elenco prenotazioni (" + e.toString() + ")", true);
			}
			
			/* TODO: rimuovi questa riga! */
			return new Error("Richiesta effettuata correttamente", false );
		}
    	
    	@Override
    	protected void onPostExecute(Error error) {
     	   progressDialog.dismiss();
     	   if(error.errorOccurred()) 
     		   Toast.makeText(getApplicationContext(), error.getError(), 20).show();
     	 }
    }

}



