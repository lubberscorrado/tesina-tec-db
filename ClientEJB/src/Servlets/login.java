package Servlets;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.exceptions.DatabaseException;
import com.orb.StatoUtentePersonaleEnum;
import com.orb.gestioneOggetti.GestionePrenotazione;
import com.orb.gestioneOggetti.GestioneStatoUtentePersonale;
import com.orb.gestioneOggetti.GestioneTenant;
import com.orb.gestioneOggetti.GestioneUtentePersonale;
import com.restaurant.WrapperTenant;
import com.restaurant.WrapperUtentePersonale;
import com.sun.mail.iap.Response;

import Utilita.JSONResponse;

/**
 * Servlet implementation class login
 */
@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HttpSession session = null;
	private String action = null;
	
	@EJB
	private GestioneTenant gestioneTenant;
	@EJB
	private GestioneUtentePersonale gestioneUtentePersonale;
	@EJB
	private GestioneStatoUtentePersonale gestioneStatoUtentePersonale;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public login() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		action = request.getParameter("action");
		

		if(action == null || action.length() == 0){	//LOGIN
			
			int idTenant = 0;
			int idUtente = 1;

			
//
//			String ristorante = request.getParameter("ristorante");
//			String username = request.getParameter("username");
//			String password = request.getParameter("password");
//			int idTenant = Integer.valueOf(ristorante);
//			
//			try {
//				//Estraggo la lista degli utenti
//				WrapperUtentePersonale tmp;
//				try{
//					tmp = gestioneTenant.getWrapperUtentePersonaleByTenantId(idTenant);
//				}catch(DatabaseException e){
//					JSONResponse.WriteOutput(response, false, "Il codice del ristorante non è valido.");
//					return;
//				}
//				List<WrapperUtentePersonale> listaUtentiPersonale = gestioneUtentePersonale.getUtentePersonaleTenant(idTenant);
//				listaUtentiPersonale.add( tmp );	//Aggiungo il superutente alla lista
//				
//				//Cerco l'utente che ha sta cercando di loggare
//				for(int i=0;i<listaUtentiPersonale.size(); i++){
//					tmp = listaUtentiPersonale.get(i);
//					if(tmp.getUsername().equals(username) && tmp.getPassword().equals(password)){	//Utente trovato
//						int privilegi = 0;
//						if(tmp.isSuperAdmin()){
//							privilegi = privilegi|JSONResponse.PRIV_SuperAdministrator;
//						}else if(tmp.isAdmin()){
//							privilegi = privilegi|JSONResponse.PRIV_Administrator;
//						}else{
//							if(tmp.isCameriere()){
//								privilegi = privilegi|JSONResponse.PRIV_Cameriere;
//							}
//							if(tmp.isCassiere()){
//								privilegi = privilegi|JSONResponse.PRIV_Cassiere;
//							}
//							if(tmp.isCucina()){
//								privilegi = privilegi|JSONResponse.PRIV_Cuoco;
//							}
//						}
//						// Setto i valori della sessione
//						session.setAttribute("Logged", true);
//						session.setAttribute("idTenant", idTenant);
//						session.setAttribute("Privs", privilegi);
//						
//						WrapperTenant wrapperTenant = gestioneTenant.getTenantById(idTenant);
//						
//						session.setAttribute("Ristorante", wrapperTenant.getRagioneSociale());
//						session.setAttribute("Username", tmp.getUsername());
//						JSONResponse.WriteLoginPrivs(request, response, true, "Login effettuato correttamente.");
//						return;
//					}
//				}
//				JSONResponse.WriteOutput(response, false, "Username o password non corretti.");
//				return;
//				
//			} catch (DatabaseException e1) {
////				e1.printStackTrace();
//				JSONResponse.WriteOutput(response, false, "Errore durante il login.");
//				return;
//			}
			
			
			
			//VERSIONE SENZA LOGIN
			// Setto i valori della sessione
			session.setAttribute("Logged", true);
			session.setAttribute("idTenant", idTenant);
			session.setAttribute("idUtente", idUtente);
			session.setAttribute("Privs", JSONResponse.PRIV_SuperAdministrator);
			
			
			
			System.out.println("Login from: "+request.getRemoteAddr()+" Tenant: "+idTenant+" User: "+idUtente);
			try {
				gestioneStatoUtentePersonale.aggiungiStatoUtentePersonale(idUtente, idTenant, StatoUtentePersonaleEnum.CAMERIERE);
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			JSONResponse.WriteLoginPrivs(request, response, true, "Login effettuato correttamente.");	return;
			
		} else if(action.equals("logout")){//LOGOUT
			session.setAttribute("Logged", false);
			try {
				gestioneStatoUtentePersonale.deleteStatoUtentePersonale(Integer.parseInt((String) session.getAttribute("idUtente")), Integer.parseInt((String) session.getAttribute("idTenant")));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.getSession().invalidate();
			JSONResponse.WriteOutput(response, true, "Logout effettuato correttamente");
			return;
			
		}else if(action.equals("login_info")){//LOGIN INFO
			JSONObject json_obj = new JSONObject();
			try{
				if(session == null || session.isNew() || session.getAttribute("Logged").equals(false)){
					json_obj.put("logged", false);
				}else{
					int user_privs = (Integer) session.getAttribute("Privs");
					json_obj.put("logged", true);
					json_obj.put("isCameriere", 	((user_privs&JSONResponse.PRIV_Cameriere)==JSONResponse.PRIV_Cameriere));
					json_obj.put("isCuoco", 		((user_privs&JSONResponse.PRIV_Cuoco)==JSONResponse.PRIV_Cuoco));
					json_obj.put("isCassiere", 		((user_privs&JSONResponse.PRIV_Cassiere)==JSONResponse.PRIV_Cassiere));
					json_obj.put("isAdministrator", ((user_privs&JSONResponse.PRIV_Administrator)==JSONResponse.PRIV_Administrator));
				
					json_obj.put("restaurant", 	session.getAttribute("Ristorante"));
					json_obj.put("user", 		session.getAttribute("Username"));
				}
			}catch(Exception e){
				json_obj.put("logged", false);
			}
			json_obj.put("success", true);
			response.getWriter().println(json_obj);
			return;
		}

		
	}

}
