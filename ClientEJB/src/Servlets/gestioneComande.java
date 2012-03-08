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
import com.orb.Comanda;
import com.orb.StatoComandaEnum;
import com.orb.StatoContoEnum;
import com.orb.gestioneOggetti.GestioneCategoria;
import com.orb.gestioneOggetti.GestioneComanda;
import com.orb.gestioneOggetti.GestioneConto;
import com.orb.gestioneOggetti.GestioneUtentePersonale;
import com.orb.gestioneOggetti.GestioneVoceMenu;
import com.restaurant.TreeNodeVoceMenu;
import com.restaurant.WrapperComanda;
import com.restaurant.WrapperConto;
import com.restaurant.WrapperUtentePersonale;

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
    @EJB
    private GestioneUtentePersonale gestioneUtentePersonale;
 
    
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
		
		WrapperUtentePersonale utentePersonale;
		try {
			utentePersonale = gestioneUtentePersonale.getUtentePersonaleById(	(Integer)request.
																				getSession().
																				getAttribute("idUtente"));
		} catch (DatabaseException e) {
			JSONResponse.WriteOutput(response, false, e.toString());
			return;
		}
		
		if(request.getParameter("action").equals("OCCUPA_TAVOLO")) {
			
			/*****************************************************************************
			 * Viene occupato il tavolo e aperto il conto associato
			 *****************************************************************************/
			try {
				gestioneOrdinazioni.occupaTavolo(idTavolo,  idTenant, utentePersonale.getIdUtentePersonale());
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("cameriere", utentePersonale.getNome());
			jsonObject.put("success", true);
			
			response.getWriter().print(jsonObject);
		
		} else if(request.getParameter("action").equals("LIBERA_TAVOLO")) {
			
			/*****************************************************************************
			 * Viene liberato il tavolo e settato il conto come 
			 * DAPAGARE
			 *****************************************************************************/
			try {
				gestioneOrdinazioni.liberaTavolo(idTavolo);
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
			JSONResponse.WriteOutput(response, true, "");
			
		} else if(request.getParameter("action").equals("INSERISCI_COMANDE")){
			
			/*****************************************************************************
			 * Riceve un gruppo di ordinazioni destinate ad un 
			 * determinato tavolo
			 *****************************************************************************/
			
			int length = request.getContentLength();
			
			BufferedReader reader = request.getReader();
			char[] httpBody = new char[length];
			
			try {
				reader.read(httpBody, 0, length);
				
				JSONObject jsonObjectOrdinazioni = new JSONObject(new String(httpBody));
				JSONArray jsonArrayOrdinazioni = jsonObjectOrdinazioni.getJSONArray("comande");
				
				int idTavoloPrenotazione = jsonObjectOrdinazioni.getInt("idTavolo");
				
				/* Acquisisco il conto aperto associato al tavolo per il quale è stata inviata 
				 * l'ordinazione. Se sono presenti più conti aperti viene sollevata un'eccezione */					
				List<WrapperConto> listContiAperti = gestioneConto.getContiAperti(idTavoloPrenotazione);
				
				if(listContiAperti.size() != 1) 
					throw new DatabaseException("Errore durante la ricerca dei conti aperti associati " +
												"al tavolo (>1 conto aperto) " + idTavoloPrenotazione); 
				
				/* Hash che identifica univocamente il gruppo di comande */
				String hashGruppo = generateString(new Random(), gestioneComande.characters, 64);
				
				
				/* Array JSON che contiene gli id associati alle comande sul
				 * database. Vengono ritornati al client in modo che sucessive 
				 * operazioni di modifica possano essere fatte con riferimento 
				 * a quegli id */
				
				JSONArray jsonArrayIdComande = new JSONArray();
				
				/*****************************************************************************
				 *  Inserisco ogni comanda associata al gruppo di ordinazioni all'interno del
				 *  database.
				 *****************************************************************************/
				
				for(int i=0; i<jsonArrayOrdinazioni.length(); i++) {
					
					JSONObject jsonObjectOrdinazione = jsonArrayOrdinazioni.getJSONObject(i);
					
					/* Acquisisco l'oggetto voceMenu relativo alla comanda che sto considerando */
					TreeNodeVoceMenu voceMenu = gestioneVoceMenu.getVoceMenu(jsonObjectOrdinazione.getInt("idVoceMenu"));
					System.out.println("Ordinazione per " + voceMenu.getNome());		
					
					/* Estraggo gli id di tutte le variazioni associate all'ordinazione */
					JSONArray jsonArrayVariazioni = jsonObjectOrdinazione.getJSONArray("variazioni");
					List<Integer> listIdVariazioni = new ArrayList<Integer>();
					
					for(int j=0; j<jsonArrayVariazioni.length(); j++) 
						listIdVariazioni.add(jsonArrayVariazioni.getJSONObject(j).getInt("idVariazione"));
					
									
					WrapperComanda wrapperComanda = gestioneComanda.aggiungiComanda((Integer)request.getSession().getAttribute("idTenant"), 
																					jsonObjectOrdinazione.getInt("idVoceMenu"),
																					listContiAperti.get(0).getIdConto(),
																					1,
																					jsonObjectOrdinazione.getString("note"),
																					hashGruppo,
																					voceMenu.getPrezzo(),
																					jsonObjectOrdinazione.getInt("quantita"),
																					StatoComandaEnum.INVIATA,
																					listIdVariazioni);
					
					JSONObject jsonObjectId = new JSONObject();
					jsonObjectId.put("id", wrapperComanda.getIdComanda());
					
					jsonArrayIdComande.put(jsonObjectId);
				}
				
				JSONResponse.WriteOutput(response, true, "","idComande", jsonArrayIdComande);
				
			} catch (Exception e) {
				e.printStackTrace();
				JSONResponse.WriteOutput(response, false, e.toString());
				System.out.println("Errore durante l'inserimento delle comande nel database: " + e.toString());
				return;
			}
			
		} else if(request.getParameter("action").equals("MODIFICA_COMANDA")) {
			
			
			/*****************************************************************************
			 * Modifica le informazioni associate ad una comanda
			 *****************************************************************************/
			
			int length = request.getContentLength();
			
			BufferedReader reader = request.getReader();
			char[] httpBody = new char[length];
			
			try {
				reader.read(httpBody, 0, length);
				JSONObject jsonObjectComanda = new JSONObject(new String(httpBody));
				
				int idComanda = jsonObjectComanda.getInt("idRemotoComanda");
				WrapperComanda wrapperComanda = gestioneComanda.getComandaById(idComanda);
				
				/* Se la comanda è in uno stato diverso da INVIATA significa che è in preparazione o è già
				 * stata consegnata. Non può essere quindi modificata */
				
				if(!wrapperComanda.getStato().equals(StatoComandaEnum.INVIATA)) {
					JSONResponse.WriteOutput(response, false, "Impossibile modificare la comanda (" + wrapperComanda.getStato() +")");
					return;
				}
			
				/* Costruisco la lista delle variazioni associate alla comanda */
				List<Integer> listVariazioni = new ArrayList<Integer>();
				JSONArray jsonArrayVariazioni = jsonObjectComanda.getJSONArray("variazioni");
				
				for(int i=0; i<jsonArrayVariazioni.length(); i++) 
					listVariazioni.add(new Integer(jsonArrayVariazioni.getJSONObject(i).getInt("idVariazione")));
				
				gestioneComanda.updateComanda(	jsonObjectComanda.getInt("idRemotoComanda"),
												jsonObjectComanda.getString("note"),
												jsonObjectComanda.getInt("quantita"),
												listVariazioni);
				JSONResponse.WriteOutput(response, true, "Comanda modificata");
			
			
			} catch (Exception e) {
				e.printStackTrace();
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
			
		} else if (request.getParameter("action").equals("ELIMINA_COMANDA")) {
			
			try {
				
				
				int idRemotoComanda = new Integer(request.getParameter("idRemotoComanda"));
				WrapperComanda wrapperComanda = gestioneComanda.getComandaById(idRemotoComanda);
				
				/* Impedisco l'eliminazione della comanda se è già in preparazione */
				if(!wrapperComanda.getStato().equals(StatoComandaEnum.INVIATA)) {
					JSONResponse.WriteOutput(response, false, "Impossibile eliminare la comanda (" + wrapperComanda.getStato() +")");
					return;
				}
				gestioneComanda.deleteComanda(idRemotoComanda);
				JSONResponse.WriteOutput(response, true, "Comanda eliminata");
				
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response, false, e.toString());
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
