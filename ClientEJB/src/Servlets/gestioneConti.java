package Servlets;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.exceptions.DatabaseException;
import com.orb.Conto;
import com.orb.VoceMenu;
import com.orb.gestioneOggetti.GestioneComanda;
import com.orb.gestioneOggetti.GestioneConto;
import com.restaurant.TreeNodeVoceMenu;
import com.restaurant.WrapperComanda;
import com.restaurant.WrapperConto;

import Utilita.JSONResponse;


@WebServlet("/gestioneConti")
public class gestioneConti extends HttpServlet {
	
	@EJB
	private GestioneConto gestioneConto;
	@EJB
	private GestioneComanda gestioneComanda;
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
		if(request.getParameter("idTavolo")!= null)
			idTavolo = Integer.parseInt(request.getParameter("idTavolo"));
		
		if(request.getParameter("action").equals("GET_CONTO")) {
			
			/******************************************************
			 * Richiesta del conto (quindi di tutte le ordinazioni)
			 * per il tavolo idTavolo
			 ******************************************************/
			
			try {
				List<WrapperComanda> listaComande = gestioneComanda.getComandeByTavolo(idTavolo);
				
				/* Genero l'array di oggetti JSON che rappresentano le comande */
				JSONArray jsonArrayVociMenu = new JSONArray();
				
				for(WrapperComanda comanda : listaComande) {
					TreeNodeVoceMenu voceMenu = gestioneComanda.getVoceMenuByComanda(comanda.getIdComanda());
					JSONObject jsonObjectVoceMenu = new JSONObject();
					jsonObjectVoceMenu.put("idComanda", comanda.getIdComanda());
					jsonObjectVoceMenu.put("nome", voceMenu.getNome());
					jsonObjectVoceMenu.put("quantita", comanda.getQuantita());
					jsonObjectVoceMenu.put("stato", comanda.getStato());
					jsonArrayVociMenu.put(jsonObjectVoceMenu);
				}
				
				JSONResponse.WriteOutput(response,true, "", "vocimenu", jsonArrayVociMenu);
				
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response,  false, e.toString());
				
			}
		}
	}
}
