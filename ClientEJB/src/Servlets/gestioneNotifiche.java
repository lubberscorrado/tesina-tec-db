package Servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import Utilita.JSONResponse;

import com.business.BusinessNotifiche;
import com.exceptions.DatabaseException;
import com.restaurant.Notifica;
import com.restaurant.TipoNotificaEnum;


@WebServlet("/gestioneNotifiche")
public class gestioneNotifiche extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	@EJB
	private BusinessNotifiche businessNotifiche;
  
    public gestioneNotifiche() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Cameriere))
			return;
		int idUtente = (Integer) request.getSession().getAttribute("idUtente");
		
		try {
			if(request.getParameter("action").equals("CHECK_NOTIFICHE")) {
				
				/* *********************************************************
				 * Verifica la presenza di notifiche senza mandarle in
				 * output. Utilizzato dal service per notificare la 
				 * necessità di aggiornamento all'activity.
				 ***********************************************************/
				
				/* Acquisisco dalla richiesta la data dell'ultima verifica della
				 * presenza di notifiche. */
				String lastDateSqlFormatted = request.getParameter("lastDate");
				
				List<Notifica> listaNotifiche = 
						businessNotifiche.getNotifiche(	idUtente,lastDateSqlFormatted);
				
				if(listaNotifiche.size() > 0) {
					JSONResponse.WriteOutput(response, true, "CHECK_NOTIFICHE_PRESENTI");
					return;
				} else {
					JSONResponse.WriteOutput(response, true, "CHECK_NOTIFICHE_NON_PRESENTI");
					return;
				}
				
			} else if(request.getParameter("action").equals("GET_NOTIFICHE")) {
				
				/* *********************************************************
				 * Verifica la presenza di notifiche e le manda in output
				 * formattandole in un oggetto JSON.
				 ***********************************************************/
				
				/* Acquisisco dalla richiesta la data dell'ultima verifica della
				 * presenza di notifiche */
				String lastDateSqlFormatted = request.getParameter("lastDate");
				
			
				List<Notifica> listaNotifiche = 
						businessNotifiche.getNotifiche(idUtente, lastDateSqlFormatted);
				
				
				JSONArray jsonArrayNotifiche = new JSONArray();
				
				for(Notifica notifica : listaNotifiche) {
					
					if(notifica.getTipoNotifica().equals(TipoNotificaEnum.COMANDA_PRONTA)) {
						
						JSONObject jsonObjectNotifica = new JSONObject();
						jsonObjectNotifica.put("tipo", notifica.getTipoNotifica().toString());
						jsonObjectNotifica.put("idTavolo", notifica.getIdTavolo());
						jsonObjectNotifica.put("nomeTavolo", notifica.getNomeTavolo());
							
						jsonArrayNotifiche.put(jsonObjectNotifica);
					
					}else if(notifica.getTipoNotifica().equals(TipoNotificaEnum.TAVOLO_ASSEGNATO)) {
						
					}
				}
				
				JSONResponse.WriteOutput(response, true, "", "notifiche", jsonArrayNotifiche);
				
				
				
				
			}
			
		} catch (DatabaseException e) {
			
			
			JSONResponse.WriteOutput(	response, false, 
										"Errore durante la ricerca delle notifiche (" + 
										e.toString() + ")");
			
		}
	}

}
