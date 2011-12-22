package Servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
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
import com.orb.StatoContoEnum;
import com.orb.gestioneOggetti.GestioneCategoria;
import com.orb.gestioneOggetti.GestioneConto;

import Utilita.JSONResponse;


@WebServlet("/gestioneOrdinazioni")
public class gestioneOrdinazioni extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@EJB	
	private GestioneOrdinazioni gestioneOrdinazioni;
    
	public gestioneOrdinazioni() {
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
			
			
			int length = request.getContentLength();
			
			BufferedReader reader = request.getReader();
			char[] httpBody = new char[length];
			
			try {
				reader.read(httpBody, 0, length);
				JSONObject jsonObjectOrdinazioni = new JSONObject(new String(httpBody));
				int idTavoloPrenotazione = jsonObjectOrdinazioni.getInt("idTavolo");
				
				JSONArray jsonArrayOrdinazioni = jsonObjectOrdinazioni.getJSONArray("ordinazioni");
				
				System.out.println("Prenotazione per tavol " + idTavoloPrenotazione);
				
				for(int i=0; i<jsonArrayOrdinazioni.length(); i++) {
					JSONObject jsonObjectOrdinazione = jsonArrayOrdinazioni.getJSONObject(i);
					
					System.out.println("Ordinazione per id " + jsonObjectOrdinazione.get("idVoceMenu"));
					
					JSONArray jsonArrayVariazioni = jsonObjectOrdinazione.getJSONArray("variazioni");
					
					for(int j=0; j<jsonArrayVariazioni.length(); j++) {
						
						JSONObject jsonObjectVariazione = jsonArrayVariazioni.getJSONObject(j);
						System.out.println("Variazione per l'ordinazione: " + jsonObjectVariazione.getInt("idVariazione"));
						
					}
					
				}
				
				
			} catch (Exception e) {
				System.out.println("Errore durante la lettura della richiesta post per la comanda: " + e.toString());
				return;
			}
			
			
			
			

		}
	}

}
