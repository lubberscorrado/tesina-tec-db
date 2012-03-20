package Servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
		
		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Cameriere) ||
			(Integer) request.getSession().getAttribute("idUtente") == null) {
			return;
		}
		
		int idUtente = (Integer) request.getSession().getAttribute("idUtente");
		
		try {
	
			if(	request.getParameter("action") != null && 
				request.getParameter("action").equals("CHECK_NOTIFICHE")) {
			
				/* **********************************************************
				 * Verifica la presenza di notifiche senza mandarle in
				 * output. Utilizzato dal service per notificare la 
				 * necessità di aggiornamento all'activity. La data di ultima
				 * ricerca viene acquisita da android.
				 ************************************************************/
				
				String lastDateSqlFormatted = 
						request.getParameter("lastDate") == null ? 
						new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date()) :
						request.getParameter("lastDate");
				
				List<Notifica> listaNotifiche = 
						businessNotifiche.getNotifiche(	idUtente,lastDateSqlFormatted);
				
				if(listaNotifiche.size() > 0)
					JSONResponse.WriteOutput(response, true, "CHECK_NOTIFICHE_PRESENTI");
				else 
					JSONResponse.WriteOutput(response, true, "CHECK_NOTIFICHE_NON_PRESENTI");
				
				return;
				
			} else if(	request.getParameter("action") != null && 
						request.getParameter("action").equals("GET_NOTIFICHE")) {
				
				/* *********************************************************
				 * Verifica la presenza di notifiche e le manda in output
				 * formattandole in un oggetto JSON. La data di ricerca
				 * deve essere generata sul server in modo da essere 
				 * allineata con la data di aggiornamento di MySQL (nel
				 * caso in cui entrambi girino sulla stessa macchina). La
				 * data di Android potrebbe essere non sincronizzata con
				 * il server creando così dei problemi nella gestione
				 * deglle notifiche.
				 ***********************************************************/
				
				/* Acquisisco dalla richiesta la data dell'ultima verifica della
				 * presenza di notifiche */
				
				String lastDateSqlFormatted = 
						request.getParameter("lastDate") == null ? 
						new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date()) :
						request.getParameter("lastDate");
			
				List<Notifica> listaNotifiche = 
						businessNotifiche.getNotifiche(idUtente, lastDateSqlFormatted);
				
				JSONArray jsonArrayNotifiche = new JSONArray();
				
				for(Notifica notifica : listaNotifiche) {
					
					JSONObject jsonObjectNotifica = new JSONObject();
						
					jsonObjectNotifica.put("tipo", notifica.getTipoNotifica().toString());
					jsonObjectNotifica.put("idTavolo", notifica.getIdTavolo());
					jsonObjectNotifica.put("nomeTavolo", notifica.getNomeTavolo());
					jsonObjectNotifica.put("idVoceMenu", notifica.getIdVoceMenu());
						
					/* Ritorno anche l'id della comanda a cui è associata la notifica 
					 * (nel caso di comanda pronta) così che possa essere identificata
					 * la comanda per il passaggio allo stato 'CONSEGNATA' */
					jsonObjectNotifica.put("idComanda", notifica.getIdComanda());
						
					String lastModified = 
							new SimpleDateFormat("HH:mm").format(notifica.getLastModfied());
					
					jsonObjectNotifica.put("lastModified", lastModified);
					jsonArrayNotifiche.put(jsonObjectNotifica);
				}
				
				/* Invio in output al client la data generata dal server per la ricerca di
				 * notifiche come "message" */
				JSONResponse.WriteOutput(	response, 
											true, 
											new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), 
											"notifiche", 
											jsonArrayNotifiche);
			}
			
		} catch (DatabaseException e) {
			JSONResponse.WriteOutput(	response, false, 
										"Errore durante la ricerca delle notifiche (" + 
										e.toString() + ")");
		}
	}

}
