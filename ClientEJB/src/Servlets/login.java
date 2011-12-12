package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

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
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		action = request.getParameter("action");
		
		
		if(action == null || action.length() == 0){	//LOGIN
			
			JSONObject json_out = new JSONObject();
			json_out.put("success", true);
			json_out.put("message", "Login effettuato correttamente.");
			
			// Setto i valori della sessione
			session.setAttribute("Logged", true);
			session.setAttribute("idTenant", 0);
			session.setAttribute("Privs", JSONResponse.PRIV_SuperAdministrator);
			
			System.out.println("Login from: "+request.getRemoteAddr());
			
			JSONResponse.WriteLoginPrivs(request, response, true, "Login effettuato correttamente.");	return;
			
		} else if(action.equals("logout")){//LOGOUT
			session.setAttribute("Logged", false);
			request.getSession().invalidate();
			JSONResponse.WriteOutput(response, true, "Logout effettuato correttamente");	return;
			
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
					json_obj.put("restaurant", "La tana delle scimmie");
					json_obj.put("user", "Gennaro lò pizzaiolo");
				}
			}catch(Exception e){
				json_obj.put("logged", false);
//				int user_privs = (Integer) session.getAttribute("Privs");
//				json_obj.put("logged", true);
//				json_obj.put("isCameriere", 	((user_privs&JSONResponse.PRIV_Cameriere)==JSONResponse.PRIV_Cameriere));
//				json_obj.put("isCuoco", 		((user_privs&JSONResponse.PRIV_Cuoco)==JSONResponse.PRIV_Cuoco));
//				json_obj.put("isCassiere", 		((user_privs&JSONResponse.PRIV_Cassiere)==JSONResponse.PRIV_Cassiere));
//				json_obj.put("isAdministrator", ((user_privs&JSONResponse.PRIV_Administrator)==JSONResponse.PRIV_Administrator));
//				json_obj.put("restaurant", "La tana delle scimmie");
//				json_obj.put("user", "Gennaro lò pizzaiolo");
			}
			json_obj.put("success", true);
			response.getWriter().println(json_obj);
			return;
		}

		
	}

}
