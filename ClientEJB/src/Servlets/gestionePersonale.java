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
import com.orb.gestioneOggetti.GestioneCategoria;
import com.orb.gestioneOggetti.GestioneUtentePersonale;
import com.restaurant.WrapperUtentePersonale;

import Utilita.JSONFromBean;
import Utilita.JSONResponse;

/**
 * Servlet implementation class gestionePersonale
 */
@WebServlet("/gestionePersonale")
public class gestionePersonale extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int idTenant = -1;
	@EJB	private GestioneUtentePersonale	gestioneUtentePersonale;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public gestionePersonale() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Controllo dei privilegi di accesso
		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Administrator) ){
			return;
		}
		
		JSONArray json_array = new JSONArray();
		JSONObject json_tmp = null;
		idTenant = (Integer) request.getSession().getAttribute("idTenant");

		try {
			List<WrapperUtentePersonale> listaWrapperUtentePersonale = gestioneUtentePersonale.getUtentePersonaleTenant(idTenant);
			
			for(int i=0; i<listaWrapperUtentePersonale.size(); i++){
				json_tmp = JSONFromBean.jsonFromOBJ(listaWrapperUtentePersonale.get(i));
				json_array.put(json_tmp);
			}
			
			JSONResponse.WriteOutput(response, true, "Caricamento effettuato correttamente.", "data", json_array); return;
			
		} catch (DatabaseException e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, e.toString());		return;
		} catch (Exception e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, "Eccezione generale");	return;
		}
		
//		// fields: ['id','username','passwd','nome','cognome','isCameriere','isCassiere','isCucina','isAdmin'],
//		json_tmp = new JSONObject();
//		json_tmp.put("id", 1);
//		json_tmp.put("username", "kissalino");
//		json_tmp.put("passwd", "asd");
//		json_tmp.put("nome", "gino");
//		json_tmp.put("cognome", "capra");
//		json_tmp.put("isCameriere", true);
//		json_tmp.put("isCassiere", true);
//		json_tmp.put("isCucina", true);
//		json_tmp.put("isAdmin", true);
////		json_tmp.put("isCameriere", "on");
////		json_tmp.put("isCassiere", "on");
////		json_tmp.put("isCucina", "on");
////		json_tmp.put("isAdmin", "on");
//		json_array.put(json_tmp);
//		
//		
//		JSONResponse.WriteOutput(response, true, "Caricamento effettuato correttamente.", "data", json_array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Controllo dei privilegi di accesso
		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Administrator) ){
			return;
		}
		JSONArray json_array = new JSONArray();
		JSONObject json_tmp = null;
		idTenant = (Integer) request.getSession().getAttribute("idTenant");
		String action = request.getParameter("action");
		System.out.println("AZIONEEEEEEEEEEE: "+action);
		int id;
		try {
			try{
				id = Integer.parseInt(request.getParameter("id"));
			}catch(Exception e){
				id = -1;
			}
			String nome = request.getParameter("nome");
			String cognome = request.getParameter("cognome");
			String username = request.getParameter("username");
			String password = request.getParameter("passwd");
			boolean isCameriere = Boolean.parseBoolean(request.getParameter("isCameriere"));
			boolean isCassiere = Boolean.parseBoolean(request.getParameter("isCassiere"));
			boolean isCucina = Boolean.parseBoolean(request.getParameter("isCucina"));
			boolean isAdmin = Boolean.parseBoolean(request.getParameter("isAdmin"));
			
			if(action.equals("create")){
				WrapperUtentePersonale wrapperUtentePersonale = gestioneUtentePersonale.aggiungiUtentePersonale(idTenant, nome, cognome, username, password, isCameriere, isCassiere, isCucina, isAdmin);
				json_tmp = JSONFromBean.jsonFromOBJ(	wrapperUtentePersonale	);
				json_array.put(json_tmp);
				
				JSONResponse.WriteOutput(response, true, "Inserimento effettuato correttamente.", "data", json_array); return;
			
			}else if(action.equals("delete")){
				gestioneUtentePersonale.deleteUtentePersonale(id);
				JSONResponse.WriteOutput(response, true, "Rimozione effettuata correttamente."); return;
			
			}else if(action.equals("update")){
				WrapperUtentePersonale wrapperUtentePersonale = gestioneUtentePersonale.updateUtentePersonale(id, nome, cognome, username, password, isCameriere, isCassiere, isCucina, isAdmin);
				json_tmp = JSONFromBean.jsonFromOBJ(	wrapperUtentePersonale	);
				json_array.put(json_tmp);
				
				JSONResponse.WriteOutput(response, true, "Caricamento effettuato correttamente.", "data", json_array); return;
			}
			
			
			JSONResponse.WriteOutput(response, false, "No Action."); return;
			
		} catch (DatabaseException e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, e.toString());		return;
		} catch (Exception e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, "Eccezione generale");	return;
		}
	}

}
