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

import com.business.BusinessComande;
import com.business.BusinessTavolo;
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
    
	@EJB	
	private BusinessTavolo businessTavolo;
	@EJB
	private BusinessComande businessComande;
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
			
		WrapperUtentePersonale utentePersonale;
		
		try {
			utentePersonale = gestioneUtentePersonale.getUtentePersonaleById(	(Integer)request
																				.getSession()
																				.getAttribute("idUtente"));
		} catch (Exception e) {
			JSONResponse.WriteOutput(response, false, e.toString());
			return;
		}
		
		if(request.getParameter("action").equals("OCCUPA_TAVOLO")) {
			
			/* ****************************************************************************
			 * Viene occupato il tavolo e aperto il conto associato
			 *****************************************************************************/
			try {
				int idTavolo = 0;
				if(request.getParameter("idTavolo")!= null)
					idTavolo = Integer.parseInt(request.getParameter("idTavolo"));
			
			
				businessTavolo.occupaTavolo(idTavolo, idTenant, utentePersonale.getIdUtentePersonale());
			
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("cameriere", utentePersonale.getNome());
				jsonObject.put("success", true);
				
				response.getWriter().print(jsonObject);
				
			} catch (Exception e) {
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
		
		} else if(request.getParameter("action").equals("LIBERA_TAVOLO")) {
			
			/* ****************************************************************************
			 * Viene liberato il tavolo e settato il conto come 
			 * DAPAGARE
			 *****************************************************************************/
			
			try {
				
				int idTavolo = 0;
			
				if(request.getParameter("idTavolo")!= null)
					idTavolo = Integer.parseInt(request.getParameter("idTavolo"));
				businessTavolo.liberaTavolo(idTavolo);
				
				
				JSONResponse.WriteOutput(response, true, "");
			
			} catch (Exception e) {
				
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
			
		} else if(request.getParameter("action").equals("INSERISCI_COMANDE")){
			
			/* ****************************************************************************
			 * Riceve un gruppo di ordinazioni destinate ad un determinato tavolo.
			 * Decodifica la richiesta e passa alla logica di business la lista
			 * delle comande.
			 *****************************************************************************/
			
			int length = request.getContentLength();
			
			BufferedReader reader = request.getReader();
			char[] httpBody = new char[length];
			
			/* Lista delle comande estratte dalla richiesta */
			List<WrapperComanda> listaComande = new ArrayList<WrapperComanda>();
			
			try {
				reader.read(httpBody, 0, length);
				JSONObject jsonObjectOrdinazioni = new JSONObject(new String(httpBody));
				JSONArray jsonArrayOrdinazioni = jsonObjectOrdinazioni.getJSONArray("comande");
				
				int idTavolo = jsonObjectOrdinazioni.getInt("idTavolo");
				
				for(int i=0; i<jsonArrayOrdinazioni.length(); i++) {
					
					JSONObject jsonObjectOrdinazione = jsonArrayOrdinazioni.getJSONObject(i);
				
					/* Estraggo gli id di tutte le variazioni richieste per l'ordinazione
					 * e le associto alla comanda da passare alla logica di business */
					
					JSONArray jsonArrayVariazioni = jsonObjectOrdinazione.getJSONArray("variazioni");
					
					WrapperComanda wrapperComanda = new WrapperComanda();
					
					for(int j=0; j<jsonArrayVariazioni.length(); j++) {
						wrapperComanda.addVariazione	(jsonArrayVariazioni
														.getJSONObject(j)
														.getInt("idVariazione"));
					}
				
					wrapperComanda.setIdTenant((Integer)request.getSession().getAttribute("idTenant"));
					wrapperComanda.setNote(jsonObjectOrdinazione.getString("note"));
					wrapperComanda.setQuantita(jsonObjectOrdinazione.getInt("quantita"));
					wrapperComanda.setIdVoceMenu(jsonObjectOrdinazione.getInt("idVoceMenu"));
					wrapperComanda.setStato(StatoComandaEnum.INVIATA);
					
					listaComande.add(wrapperComanda);
				}
				
				// TODO Sistemare id cameriere che è settato staticamente a 1
				List<Integer> listaIdRemoti = businessComande.inserisciComande(	listaComande, 
																				idTavolo, 
																				1);
					
					
				/* Array JSON che contiene gli id associati alle comande sul
				 * database. Vengono ritornati al client in modo che sucessive 
				 * operazioni di modifica possano essere fatte con riferimento 
				 * a quegli id */
					
					
				JSONArray jsonArrayIdComande = new JSONArray();
				for(int j=0; j< listaIdRemoti.size(); j++) {
					JSONObject jsonObjectId = new JSONObject();
					jsonObjectId.put("id", listaIdRemoti.get(j));
					jsonArrayIdComande.put(jsonObjectId);
				}
			
				JSONResponse.WriteOutput(response, true, "","idComande", jsonArrayIdComande);
				
				
			} catch (Exception e) {
			
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
			
		} else if(request.getParameter("action").equals("MODIFICA_COMANDA")) {
			
			/* ****************************************************************************
			 * Decodifica la richiesta di modifica di una comanda e la passa alla logica
			 * di business.
			 *****************************************************************************/
		
			int length = request.getContentLength();
			
			BufferedReader reader = request.getReader();
			char[] httpBody = new char[length];
			
			try {
				reader.read(httpBody, 0, length);
				JSONObject jsonObjectComanda = new JSONObject(new String(httpBody));
				
				WrapperComanda wrapperComanda = new WrapperComanda();
				
				wrapperComanda.setIdComanda(jsonObjectComanda.getInt("idRemotoComanda"));
				wrapperComanda.setNote(jsonObjectComanda.getString("note"));
				wrapperComanda.setQuantita(jsonObjectComanda.getInt("quantita"));
				
				/* *************************************************************************
				 * Associo le variazioni richieste alla comanda
				 ***************************************************************************/
				JSONArray jsonArrayVariazioni = jsonObjectComanda.getJSONArray("variazioni");
				
				for(int i=0; i<jsonArrayVariazioni.length(); i++) 
					wrapperComanda.getListIdVariazioni().add(new Integer(	jsonArrayVariazioni
																			.getJSONObject(i)
																			.getInt("idVariazione")));
				
				
				businessComande.modificaComanda(wrapperComanda);
				JSONResponse.WriteOutput(response, true, "Comanda modificata");
			
			} catch (Exception e) {
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
			
		} else if (request.getParameter("action").equals("ELIMINA_COMANDA")) {
			
			/* ****************************************************************************
			 * Acquisisce l'id della comanda da eliminare e passa la richiesta alla logica
			 * di business
			 *****************************************************************************/
			
			try {
				int idRemotoComanda = new Integer(request.getParameter("idRemotoComanda"));
				businessComande.eliminaComanda(idRemotoComanda);
				JSONResponse.WriteOutput(response, true, "Comanda eliminata");
			} catch (Exception e) {
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			}
			
		}
	}
	

}
