package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import Utilita.JSONResponse;

/**
 * Servlet implementation class login
 */
@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public login() {
        super();
        // TODO Auto-generated constructor stub
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
		JSONObject json_out = new JSONObject();
		json_out.put("success", true);
		json_out.put("message", "Login effettuato correttamente.");
		
		// Setto i valori della sessione
		HttpSession session = request.getSession();
		session.setAttribute("Logged", true);
		session.setAttribute("idTenant", 0);
		session.setAttribute("Privs", JSONResponse.PRIV_SuperAdministrator);
		
		System.out.println("Login from: "+request.getRemoteAddr());
		
		JSONResponse.WriteLoginPrivs(request, response, true, "Login effettuato correttamente.");
	}

}
