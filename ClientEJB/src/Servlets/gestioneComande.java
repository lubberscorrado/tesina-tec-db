package Servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.business.GestioneOrdinazioni;
import com.exceptions.DatabaseException;
import com.orb.StatoComandaEnum;
import com.orb.StatoContoEnum;
import com.orb.gestioneOggetti.GestioneCategoria;
import com.orb.gestioneOggetti.GestioneComanda;
import com.orb.gestioneOggetti.GestioneConto;
import com.orb.gestioneOggetti.GestioneVoceMenu;
import com.restaurant.TreeNodeVoceMenu;
import com.restaurant.WrapperConto;

import Utilita.JSONResponse;


@WebServlet("/gestioneComande")
public class gestioneComande extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private static String characters = "1234567890abcdefghijklmnopqrstuvwxyz";
	@EJB	
	private GestioneComanda gestioneComanda;
	@EJB	
	private GestioneOrdinazioni gestioneOrdinazioni;
	@EJB	
	private GestioneConto gestioneConto;
    @EJB
    private GestioneVoceMenu gestioneVoceMenu;
    
	public gestioneComande() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Cameriere))
			return;

		int idTenant = (Integer) request.getSession().getAttribute("idTenant");
		
		int idTavolo = 0;
		if(request.getParameter("idTavolo")!= null)
			idTavolo = Integer.parseInt(request.getParameter("idTavolo"));
		
		// TODO Id provvisorio del cameriere
		int idCameriere = 1;
		
		String action = request.getParameter("action");
		
		System.out.println("GESTIONE_COMANDE");
	
		if(action.equals("OCCUPA_TAVOLO")) {
			
			/******************************************************
			 * Viene occupato il tavolo e aperto il conto associato
			 ******************************************************/
			try {
				gestioneOrdinazioni.occupaTavolo(idTavolo,  idTenant, idCameriere);
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
			
			// TODO Acquisire il nome dell'utente 
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("cameriere", "Marco Guerri");
			jsonObject.put("success", true);
			
			response.getWriter().print(jsonObject);
		
		} else if(action.equals("LIBERA_TAVOLO")) {
			
			/******************************************************
			 * Viene liberato il tavolo e settato il conto come 
			 * DAPAGARE
			 ******************************************************/
			try {
				gestioneOrdinazioni.liberaTavolo(idTavolo);
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
			JSONResponse.WriteOutput(response, true, "");
			
		} else if(action.equals("COMANDE")){
			
			
			System.out.println("GESTIONE DELLE COMANDE");
			/******************************************************
			 * Riceve un gruppo di ordinazioni destinate ad un 
			 * determinato tavolo
			 ******************************************************/
			
			int length = request.getContentLength();
			
			BufferedReader reader = request.getReader();
			char[] httpBody = new char[length];
			
			try {
				reader.read(httpBody, 0, length);
				
				/* Ascuisisco l'oggetto globale con tutte le ordinazioni. Dall'oggetto 
				 * globale si può ricavare subito l'id del tavolo */
				
				JSONObject jsonObjectOrdinazioni = new JSONObject(new String(httpBody));
				int idTavoloPrenotazione = jsonObjectOrdinazioni.getInt("idTavolo");
				
				/* Hash che identifica univocamente il gruppo di comande */
				String hashGruppo = generateString(new Random(), gestioneComande.characters, 64);
				
				/* Acquisico l'array degli oggetti ordinazionie */
				JSONArray jsonArrayOrdinazioni = jsonObjectOrdinazioni.getJSONArray("comande");
				
				/********************************************************************************
				 *  Inserisco ogni comanda associata al gruppo di ordinazioni all'interno del
				 *  database.
				 ********************************************************************************/
				
				for(int i=0; i<jsonArrayOrdinazioni.length(); i++) {
					
					/* Acquisisco l'i-esimo oggetto ordinazione ed estraggo da esso i campi di interesse */
					JSONObject jsonObjectOrdinazione = jsonArrayOrdinazioni.getJSONObject(i);
					
					/* Estraggo gli id di tutte le variazioni associate all'ordinazione e li raggruppo
					 * in una lista */
					JSONArray jsonArrayVariazioni = jsonObjectOrdinazione.getJSONArray("variazioni");
					List<Integer> listIdVariazioni = new ArrayList<Integer>();
					
					for(int j=0; j<jsonArrayVariazioni.length(); j++) {
						JSONObject jsonObjectVariazione = jsonArrayVariazioni.getJSONObject(j);
						listIdVariazioni.add(jsonObjectVariazione.getInt("idVariazione"));
						
						System.out.println("Associo alla voce di menu la variazione " + listIdVariazioni.get(j));
					}
					
					/* Acquisisco il conto aperto associato al tavolo per il quale è stata inviata 
					 * l'ordinazione. Se sono presenti più conti aperti viene sollevata un'eccezione */					
					List<WrapperConto> listContiAperti = gestioneConto.getContiAperti(idTavoloPrenotazione);
					if(listContiAperti.size() != 1) 
						throw new DatabaseException("Errore durante la ricerca dei conti aperti associati " +
													"al tavolo (>1 conto aperto) " + idTavoloPrenotazione); 
					
					/* Acquisisco l'oggetto voceMenu relativo alla comanda considerata attualmente */
					TreeNodeVoceMenu voceMenu = gestioneVoceMenu.getVoceMenu(jsonObjectOrdinazione.getInt("idVoceMenu"));
					System.out.println("Ordinazione per " + voceMenu.getNome());			
					
					gestioneComanda.aggiungiComanda((Integer)request.getSession().getAttribute("idTenant"), 
													jsonObjectOrdinazione.getInt("idVoceMenu"),
													listContiAperti.get(0).getIdConto(),
													1,
													jsonObjectOrdinazione.getString("note"),
													hashGruppo,
													voceMenu.getPrezzo(),
													jsonObjectOrdinazione.getInt("quantita"),
													StatoComandaEnum.INVIATA,
													listIdVariazioni);
				}
				
				JSONResponse.WriteOutput(response,  true, "Comande inserite correttamente");
				
			} catch (Exception e) {
				
				JSONResponse.WriteOutput(response, false, e.toString());
				
				System.out.println("Errore durante la lettura della richiesta post per la comanda: " + e.toString());
				return;
			}
		}
	}
	
	/**
	 * Funzione per generare una stringa alfanumerica casuale
	 * @param rng Oggetto random per la generazione di una posizione casuale all'interno della stringa
	 * @param characters Caratteri accettati all'interno della stringa 
	 * @param length Lunghezza della stringa da generare
	 * @return String casuale
	 */
	public static String generateString(Random rng, String characters, int length)
	{
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(rng.nextInt(characters.length()));
	    }
	    return new String(text);
	}
}
