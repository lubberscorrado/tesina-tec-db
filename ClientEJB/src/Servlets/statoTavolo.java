package Servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import Utilita.JSONFromBean;

import com.exceptions.DatabaseException;
import com.orb.gestioneOggetti.GestionePiano;
import com.orb.gestioneOggetti.GestioneTavolo;
import com.restaurant.StatoTavolo;


/**
 * Servlet implementation class stato
 */
@WebServlet("/statoTavolo")
public class statoTavolo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int idTenant = -1;
	@EJB
 	private GestioneTavolo gestioneTavolo;

    /**
     * Default constructor. 
     */
    public statoTavolo(){}

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	idTenant = (Integer) request.getSession().getAttribute("idTenant");
    	List<StatoTavolo> lista_statoTavolo = null;
    	JSONObject json_out = new JSONObject();
		
    	try {
			lista_statoTavolo = gestioneTavolo.getStatoTavoli( idTenant );
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json_out.put("success",false);
	    	json_out.put("message","NO");
	    	response.getWriter().println(json_out);
			return;
		} 
    	
//    	StatoTavolo stato_tavolo1 = new StatoTavolo(1, "nomeTavolo", 0, "nomearea", 5, "libero", "Troiaccio");
//    	StatoTavolo stato_tavolo2 = new StatoTavolo(2, "nomeTavolo", 1, "nomearea", 566, "occupato", "Puzzolo");
//    	StatoTavolo stato_tavolo3 = new StatoTavolo(3, "nomeTavolo", 2, "nomearea", 56, "Prenotato", "Magnaccio");
//    	lista_statoTavolo.add(stato_tavolo1);
//    	lista_statoTavolo.add(stato_tavolo2);
//    	lista_statoTavolo.add(stato_tavolo3);
		
		JSONArray json_array_statoTavolo = new JSONArray();
    	StatoTavolo st_tmp = null;
    	
    	for(int i=0; i<lista_statoTavolo.size();i++){
    		st_tmp = lista_statoTavolo.get(i);
    		json_array_statoTavolo.put(		JSONFromBean.jsonFromOBJ(st_tmp)	);
    	}

    	json_out.put("statoTavolo", json_array_statoTavolo);
    	json_out.put("success",true);
    	json_out.put("message","OK");
    	response.getWriter().println(json_out);
	}

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		System.out.println("POST");
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		System.out.println("PUT");
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		System.out.println("DELETE");
	}

	

}
