package Servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.exceptions.DatabaseException;
import com.orb.gestioneOggetti.GestioneConto;
import com.orb.gestioneOggetti.GestioneStatoUtentePersonale;

import Utilita.JSONResponse;

/**
 * Servlet implementation class gestioneStatoUtenti
 */
@WebServlet("/gestioneStatoUtenti")
public class gestioneStatoUtenti extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB
	private GestioneStatoUtentePersonale gestioneStatoUtentePersonale;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public gestioneStatoUtenti() {
        super();
        // TODO Auto-generated constructor stub
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
		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Cassiere))
			return;
		
		String action = request.getParameter("action");
		int idTenant = (Integer) request.getSession().getAttribute("idTenant");
		
		
		if(action.equals("INFO_CAMERIERE")){
			try {
				gestioneStatoUtentePersonale.getLoggedCamerieri(idTenant);
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			
		}else if(action.equals("INFO_CUCINA")){
			try {
				gestioneStatoUtentePersonale.getLoggedCuochi(idTenant);
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
		
	}

}
