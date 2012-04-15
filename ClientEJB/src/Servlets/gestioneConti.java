package Servlets;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.business.BusinessConti;
import com.exceptions.DatabaseException;
import com.orb.Conto;
import com.orb.VoceMenu;
import com.orb.gestioneOggetti.GestioneComanda;
import com.orb.gestioneOggetti.GestioneConto;
import com.restaurant.TreeNodeVoceMenu;
import com.restaurant.WrapperComanda;
import com.restaurant.WrapperConto;
import com.restaurant.WrapperVariazione;

import Utilita.JSONResponse;


@WebServlet("/gestioneConti")
public class gestioneConti extends HttpServlet {
	
	@EJB
	private BusinessConti businessConti;
	@EJB
	private GestioneComanda gestioneComanda;
	@EJB
	private GestioneConto gestioneConto;
	private static final long serialVersionUID = 1L;
       
    
    public gestioneConti() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Cameriere))
			return;

		int idTavolo = 0;
		int idConto = 0;
		
		try {
			if(request.getParameter("idTavolo")!= null)
				idTavolo = Integer.parseInt(request.getParameter("idTavolo"));
			
			if(request.getParameter("idConto")!= null)
				idConto = Integer.parseInt(request.getParameter("idConto"));
		
		}catch(NumberFormatException e) {
			/* Prosegue, solleverà un'eccezione per l'id nullo */
		}
		
	
		if(	request.getParameter("action")!= null &&
			request.getParameter("action").equals("GET_CONTO")) {
			
			/* ******************************************************************
			 * Richiedo la lista di tutte le ordinazioni alla logica di business 
			 ********************************************************************/
			try {
				
				List<WrapperComanda> listaComande = businessConti.getConto(idTavolo);
				
				JSONArray jsonArrayComande = new JSONArray();
				
				if(listaComande != null) {
					for(WrapperComanda comanda : listaComande) {
						TreeNodeVoceMenu voceMenu = 
								gestioneComanda.getVoceMenuByComanda(comanda.getIdComanda());
			
						JSONObject jsonObjectComanda = new JSONObject();
						
						/* ********************************************************
						 * Informazioni necessarie relative al conto da passare
						 * al client:
						 * - idRemoto comanda
						 * - idVoceMenu associata 
						 * - note
						 * - stato 
						 * - array degli id delle variazioni (le variazioni hanno gli
						 * 	stessi id sia su client che su server
						 * - quantità
						 ***********************************************************/
						
						jsonObjectComanda.put("idRemoto", comanda.getIdComanda());
						jsonObjectComanda.put("idVoceMenu", voceMenu.getIdVoceMenu());
						jsonObjectComanda.put("note", comanda.getNote());
						jsonObjectComanda.put("quantita", comanda.getQuantita());
						jsonObjectComanda.put("stato", comanda.getStato());
						
						/* **********************************************************
						 * Costruisco l'array degli id delle variazioni associate 
						 * alla comanda che sto considerando
						 ************************************************************/
						JSONArray jsonArrayVariazioni = new JSONArray();
						
						for(Integer idVariazione : comanda.getListIdVariazioni()) {
							JSONObject jsonObjectIdVariazione = new JSONObject();
							jsonObjectIdVariazione.put("id", idVariazione);
							jsonArrayVariazioni.put(jsonObjectIdVariazione);
						}
						
						jsonObjectComanda.put("variazioni", jsonArrayVariazioni);
						jsonArrayComande.put(jsonObjectComanda);
					}
				}
				
				JSONResponse.WriteOutput(response,true, "", "comande", jsonArrayComande);
				
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response,  false, e.toString());
				
			}
		}else if(request.getParameter("action").equals("GET_ELENCO_CONTI")) {
			
			/* **********************************************************************************
			 * Richiedo la lista dei conti associati ad un determinato tavolo
			 ************************************************************************************/
			
			try {
			
				List<WrapperConto> listaConti = gestioneConto.getConti(idTavolo);
				JSONArray jsonArrayConti = new JSONArray();
				
				for(WrapperConto conto : listaConti) {
					JSONObject jsonObjectConto = new JSONObject();
					jsonObjectConto.put("idConto", conto.getIdConto());
					jsonObjectConto.put("idTavolo", conto.getIdTavolo());
					jsonObjectConto.put("prezzo", conto.getPrezzo());
					jsonObjectConto.put("stato", conto.getStato().toString());
					jsonObjectConto.put("timestampApertura", conto.getTimestampApertura().toString());
					jsonObjectConto.put("timestampChiusura", conto.getTimestampChiusura().toString());
					jsonObjectConto.put("numeroPersone", conto.getNumeroPersone());
					jsonArrayConti.put(jsonObjectConto);
				}
				
				JSONResponse.WriteOutput(response,true, "OK", "conti", jsonArrayConti);
				return;
				
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response,  false, e.toString());
				
			}
			
		}else if(request.getParameter("action").equals("GET_STORICO_CONTI")) {
			
			/* **********************************************************************************
			 * Richiedo la lista dei conti associati ad un determinato tavolo
			 ************************************************************************************/
			int idTenant = (Integer) request.getSession().getAttribute("idTenant");
			int start;
			int limit;
			
			try{
				start = Integer.parseInt(request.getParameter("start"));
				limit = Integer.parseInt(request.getParameter("limit"));
			}catch(Exception e){
				start = 0;
				limit = 25;
			}
			JSONArray jsonArrayConti = new JSONArray();
			
			try {
				List<WrapperConto> listaConti = gestioneConto.getContoByIdTenant(idTenant,start,limit);
				
				for(WrapperConto conto : listaConti) {
					
					JSONObject jsonObjectConto = new JSONObject();
					jsonObjectConto.put("idConto", conto.getIdConto());
					jsonObjectConto.put("idTavolo", conto.getIdTavolo());
					jsonObjectConto.put("prezzo", conto.getPrezzo());
					jsonObjectConto.put("stato", conto.getStato().toString());
					jsonObjectConto.put("timestampApertura", conto.getTimestampApertura().toString());
					jsonObjectConto.put("timestampChiusura", conto.getTimestampChiusura().toString());
					jsonObjectConto.put("numeroPersone", conto.getNumeroPersone());
					jsonArrayConti.put(jsonObjectConto);
				}
				
								
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response,  false, e.toString());
				return;
			}
			
			long results = 0;
			try {
				results = gestioneConto.getNumContiByIdTenant(idTenant);
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response,  false, e.toString());
				return;
			}
			
			//Gestione output
			JSONObject json_out = new JSONObject();
			json_out.put("success", true);
			json_out.put("message", "OK");
			json_out.put("conti", jsonArrayConti);
			json_out.put("results", results);
			response.getWriter().println(json_out);
			
		}else if(request.getParameter("action").equals("VISUALIZZA_CONTO")) {
			
			/* **********************************************************************************
			 * Richiedo il conto per la visualizzazione lato desktop alla logica di business 
			 ************************************************************************************/
			
			try {
				List<WrapperComanda> listaComande = gestioneComanda.getComandeByIdConto(idConto);
				JSONArray jsonArrayComande = new JSONArray();
				
				for(WrapperComanda comanda : listaComande) {
					
					TreeNodeVoceMenu voceMenu = gestioneComanda.getVoceMenuByComanda(comanda.getIdComanda());
					WrapperVariazione wrapperVariazione;
					
					
					JSONObject jsonObjectComanda = new JSONObject();
					
					/* ********************************************************
					 * Informazioni necessarie relative al conto da passare
					 * al client:
					 * - idRemoto comanda
					 * - nome della voce di menu associata
					 * - note
					 * - stato 
					 * - array degli id delle variazioni (le variazioni hanno gli
					 * 	stessi id sia su client che su server
					 * - quantità
					 ***********************************************************/
					
					jsonObjectComanda.put("idComanda", comanda.getIdComanda());
					jsonObjectComanda.put("quantita", comanda.getQuantita());
					jsonObjectComanda.put("nomeVoceMenu", voceMenu.getNome());
					jsonObjectComanda.put("prezzoVoceMenu", voceMenu.getPrezzo()+"€");
					
					jsonObjectComanda.put("note", comanda.getNote());
					jsonObjectComanda.put("quantita", comanda.getQuantita());
					jsonObjectComanda.put("stato", comanda.getStato().toString());
					
					String variazioni = "";
					
					WrapperVariazione[] arrayWrapperVariazione = gestioneComanda.getVariazioniByComanda(comanda.getIdComanda());

					for(int i=0; i<arrayWrapperVariazione.length; i++){
						variazioni+=arrayWrapperVariazione[i].getNome()+" ["+arrayWrapperVariazione[i].getPrezzoVariazione()+"€]<br>";
					}
					
					jsonObjectComanda.put("variazioni", variazioni);
					jsonArrayComande.put(jsonObjectComanda);
				}
				
				JSONResponse.WriteOutput(response,true, "OK", "comande", jsonArrayComande);
				return;
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response,  false, e.toString());
				
			}
		}else if(request.getParameter("action").equals("INFO_CONTO")) {
			/* **********************************************************************************
			 * Richiedo le informazioni del conto
			 ************************************************************************************/
			JSONArray jsonArray = new JSONArray();
			
			
			try {
				//int idConto = Integer.parseInt(request.getParameter("idConto"));
				WrapperConto wrapperConto = gestioneConto.getContoById(idConto);
				
				JSONObject jsonobj = new JSONObject();
				jsonArray.put(jsonobj);
				
				jsonobj.put("idConto", wrapperConto.getIdConto());
				jsonobj.put("prezzo", wrapperConto.getPrezzo());
				jsonobj.put("stato", wrapperConto.getStato().toString());
				jsonobj.put("timestampApertura", wrapperConto.getTimestampApertura().toString());
				jsonobj.put("timestampChiusura", wrapperConto.getTimestampChiusura().toString());
				jsonobj.put("numeroPersone", wrapperConto.getNumeroPersone());
				
				JSONResponse.WriteOutput(response,true, "OK", "conto", jsonArray);
				return;
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response,  false, e.toString());
				
			}
		}else if(request.getParameter("action").equals("CHIUDI_CONTO")) {
			/* **********************************************************************************
			 * Richiede la chiusura del conto
			 ************************************************************************************/
			try {
				//int idConto = Integer.parseInt(request.getParameter("idConto"));
				boolean contoChiuso = gestioneConto.chiudiContoById(idConto);
				
				if(contoChiuso){
					JSONResponse.WriteOutput(response,true, "Il conto è stato chiuso correttamente");
				}else{
					JSONResponse.WriteOutput(response,false, "Il conto non è stato chiuso");
				}
				
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response,  false, e.toString());
			}
		}
		
		
	}
}
