package Servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.exceptions.DatabaseException;
import com.orb.gestioneOggetti.GestioneConto;
import com.orb.gestioneOggetti.GestioneStatoUtentePersonale;
import com.restaurant.WrapperUtentePersonale;
import com.restaurant.WrapperUtentePersonaleVisualizzazioneStato;

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
				List<WrapperUtentePersonale> list = gestioneStatoUtentePersonale.getLoggedCamerieri(idTenant);
				List<WrapperUtentePersonaleVisualizzazioneStato> listWUPVS = new ArrayList<WrapperUtentePersonaleVisualizzazioneStato>();
				
				JSONResponse.WriteOutput(response, true, "OK");
				return;
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response, false, "Errore reperimento informazioni INFO_CAMERIERE");
				e.printStackTrace();
				return;
			}
			
			
			
			
			
		}else if(action.equals("INFO_CUCINA")){
			try {
				gestioneStatoUtentePersonale.getLoggedCuochi(idTenant);
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response, false, "Errore reperimento informazioni INFO_CUOCO");
				//e.printStackTrace();
				return;
			}
			
			
		}
		
		
		
	}

}
