package Servlets;

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

import com.business.GestioneOrdinazioni;
import com.exceptions.DatabaseException;
import com.orb.StatoContoEnum;
import com.orb.gestioneOggetti.GestioneCategoria;
import com.orb.gestioneOggetti.GestioneConto;

import Utilita.JSONResponse;


@WebServlet("/gestioneConto")
public class gestioneOrdinazioni extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@EJB	
	private GestioneOrdinazioni gestioneOrdinazioni;
    
	public gestioneOrdinazioni() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Cameriere))
			return;

		int idTenant = (Integer) request.getSession().getAttribute("idTenant");
		int idTavolo = Integer.parseInt(request.getParameter("idTavolo"));
		
		// TODO Id provvisorio del cameriere
		int idCameriere = 1;
		
		String action = request.getParameter("action");
		
		if(action.equals("OCCUPA_TAVOLO")) {
			
				/**************************************************
				 * Occupazione del tavolo
				 **************************************************/
				try {
					gestioneOrdinazioni.occupaTavolo(idTavolo,  idTenant, idCameriere);
				} catch (DatabaseException e) {
					JSONResponse.WriteOutput(response, false, e.toString());
				}
				
				JSONResponse.WriteOutput(response, true, "");
		} 
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}

}
