package Servlets;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
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

import com.exceptions.DatabaseException;
import com.orb.gestioneOggetti.GestionePrenotazione;
import com.restaurant.WrapperPrenotazione;

/**
 * Servlet implementation class gestionePrenotazioni
 */
@WebServlet("/gestionePrenotazioni")
public class gestionePrenotazioni extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB
	private GestionePrenotazione gestionePrenotazioni;
	
	
    public gestionePrenotazioni() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Controllo che l'utente abbia i privilegi di cameriere */
		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Cameriere))
			return;
		
		int idTenant = 1; // (Integer) request.getSession().getAttribute("idTenant");
		// int idTavolo = Integer.parseInt(request.getParameter("idPrenotazione"));
		
		String action = request.getParameter("action");
		 
		if(action.equals("GET_LISTA_PRENOTAZIONI")) {
			
			JSONArray json_array = new JSONArray(); 
			JSONObject json_tmp; 
			
			/******************************************************
			 * Viene restituita la lista delle prenotazioni
			 ******************************************************/
			try {
				
				List<WrapperPrenotazione> lista_prenotazioni =  gestionePrenotazioni.getListaPrenotazioni(idTenant);
				
				for(int i=0; i<lista_prenotazioni.size(); ++i) {
					
					
					/* Inserisco man mano le informazioni della lista delle prenotazioni in un array */
					json_tmp = new JSONObject();
					json_tmp.put("idPrenotazione", lista_prenotazioni.get(i).getIdPrenotazione());
					json_tmp.put("idTenant", lista_prenotazioni.get(i).getIdTenant());
					json_tmp.put("idTavolo", lista_prenotazioni.get(i).getTavoloAppartenenza().getIdTavolo());
					json_tmp.put("nomeCliente", lista_prenotazioni.get(i).getNomecliente());
					json_tmp.put("numPersone", lista_prenotazioni.get(i).getNumpersone());
					
						/* ****************************************************************************** 
						 * Reperisco la data e l'ora (che su database sono separate)
						 * e le unisco in una stringa con questo tipo di formato: "22/12/2011 21:30"
						 * ****************************************************************************** */
		
						// reperisco la data, e la trasformo in una stringa
						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				        Date prenotationDate = lista_prenotazioni.get(i).getData();  
				        String string_prenotationDate = dateFormat.format(prenotationDate);
	
				        // reperisco l'ora, e la trasformo in una stringa
				        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
				        Time prenotationTime = lista_prenotazioni.get(i).getOra();
				        String string_prenotationTime = timeFormat.format(prenotationTime);
				        
				        // metto nel json la stringa risultante
				        String string_dateTime = string_prenotationDate + " " + string_prenotationTime;
				        json_tmp.put("data_e_ora", string_dateTime);
				        
				        /* ****************************************************************************** */

					json_array.put(json_tmp);
					
				} // fine ciclo for
				
				
			} catch (DatabaseException e) {
				JSONResponse.WriteOutput(response, false, e.toString());
				return;
			} // fine catch
			
			// TODO Acquisire il nome dell'utente 
			
			System.out.println("FINE SCRITTURA JSON?");
			
			JSONObject json_output = new JSONObject();
			json_output.put("success", true);
			json_output.put("listaPrenotazioni", json_array);
			response.getWriter().print(json_output);
		
		} // fine di: if(action.equals("GET_LISTA_PRENOTAZIONI")) 
	
	} // fine doPost

}


