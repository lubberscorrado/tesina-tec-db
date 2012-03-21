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

import Utilita.FieldChecker;
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
			List<WrapperUtentePersonale> listaWrapperUtentePersonale = gestioneUtentePersonale.getUtentePersonaleTenant(idTenant, false);
			
			for(int i=0; i<listaWrapperUtentePersonale.size(); i++){
				json_tmp = JSONFromBean.jsonFromOBJ(listaWrapperUtentePersonale.get(i));
				json_array.put(json_tmp);
			}
			
			JSONResponse.WriteOutput(response, true, "Caricamento effettuato correttamente.", "data", json_array); return;
			
		} catch (DatabaseException e) {
//			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, e.toString());		return;
		} catch (Exception e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, "Eccezione generale");	return;
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Controllo dei privilegi di accesso
		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Administrator) ){
			return;
		}
		//Controllo l'azione richiesta
		String action = request.getParameter("action");
		if(action == null || action.length() == 0){
			JSONResponse.WriteOutput(response, false, "No Action."); return;
		}
		
		idTenant = (Integer) request.getSession().getAttribute("idTenant");
		
		JSONArray json_array = new JSONArray();
		JSONObject json_tmp = null;
		
		
		int id;
		try {
			try{
				id = Integer.parseInt(request.getParameter("id"));
			}catch(Exception e){
				id = -1;
			}
			String nome = 		request.getParameter("nome");
			String cognome = 	request.getParameter("cognome");
			String username =	request.getParameter("username");
			String password = 	request.getParameter("passwd");
			String password2 = 	request.getParameter("passwd2");
			
			String isCameriereS = 	request.getParameter("isCameriere");
			String isCassiereS = 	request.getParameter("isCassiere");
			String isCucinaS = 		request.getParameter("isCucina");
			String isAdminS = 		request.getParameter("isAdmin");
			
			boolean isCameriere = 	FieldChecker.checkValue(isCameriereS);	//Boolean.parseBoolean(	isCameriereS);
			boolean isCassiere = 	FieldChecker.checkValue(isCassiereS);	//Boolean.parseBoolean(	isCassiereS);
			boolean isCucina = 		FieldChecker.checkValue(isCucinaS);	//Boolean.parseBoolean(	isCucinaS);
			boolean isAdmin = 		FieldChecker.checkValue(isAdminS);	//Boolean.parseBoolean(	isAdminS);
			
			if(action.equals("create")){
				
				if(password == null || password.length()==0){
					JSONResponse.WriteOutput(response, false, "Password mancante."); return;
				}
				if(password.length()<8){
					JSONResponse.WriteOutput(response, false, "La password deve esser di almeno 8 caratteri."); return;
				}
				if( !password.equals(password2)){
					JSONResponse.WriteOutput(response, false, "Le password inserite non coincidono. Riprovare."); return;
				}
				
				WrapperUtentePersonale wrapperUtentePersonale = gestioneUtentePersonale.aggiungiUtentePersonale(idTenant, nome, cognome, username, password, isCameriere, isCassiere, isCucina, isAdmin);
				json_tmp = JSONFromBean.jsonFromOBJ(	wrapperUtentePersonale	);
				json_array.put(json_tmp);
				
				JSONResponse.WriteOutput(response, true, "Inserimento effettuato correttamente.", "data", json_array); return;
			
			}else if(action.equals("delete")){
				gestioneUtentePersonale.deleteUtentePersonale(id);
				JSONResponse.WriteOutput(response, true, "Rimozione effettuata correttamente."); return;
			
			}else if(action.equals("update")){
				if(password.length() > 0 || password2.length() > 0){
					if(password.length()<8){
						JSONResponse.WriteOutput(response, false, "La password deve esser di almeno 8 caratteri."); return;
					}
					if( !password.equals(password2)){
						JSONResponse.WriteOutput(response, false, "Le password inserite non coincidono. Riprovare."); return;
					}
				}
				
				
				WrapperUtentePersonale wrapperUtentePersonale = gestioneUtentePersonale.updateUtentePersonale(id, nome, cognome, username, password, isCameriere, isCassiere, isCucina, isAdmin);
				json_tmp = JSONFromBean.jsonFromOBJ(	wrapperUtentePersonale	);
				json_array.put(json_tmp);
				
				JSONResponse.WriteOutput(response, true, "Caricamento effettuato correttamente.", "data", json_array); return;
			}
			
			
			
			
		} catch (DatabaseException e) {
//			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, e.toString());		return;
		} catch (Exception e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, "Eccezione generale");	return;
		}
	}

}
