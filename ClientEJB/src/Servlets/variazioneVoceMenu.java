package Servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.exceptions.DatabaseException;
import com.orb.gestioneOggetti.GestioneCategoria;
import com.orb.gestioneOggetti.GestioneVariazioni;
import com.restaurant.WrapperVariazione;

import Utilita.JSONFromBean;
import Utilita.JSONResponse;

/**
 * Servlet implementation class variazioneVoceMenu
 */
@WebServlet("/variazioneVoceMenu")
public class variazioneVoceMenu extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int idTenant = -1;
	@EJB	private GestioneVariazioni	gestioneVariazioni;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public variazioneVoceMenu() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		idTenant = (Integer) request.getSession().getAttribute("idTenant");
		int idCategoria = 2;
		JSONArray json_array = new JSONArray();
		JSONObject json_tmp = null;
		try {
			List<WrapperVariazione> listaVariazioni = gestioneVariazioni.getVariazioniByCategoria(idCategoria, idTenant);
			if(listaVariazioni != null){
				for(int i=0; i<listaVariazioni.size(); i++){
					json_tmp = JSONFromBean.jsonFromOBJ( listaVariazioni.get(i) );
					json_tmp.put("isEreditata", false);
					json_tmp.put("categoriaDiAppartenenza", "asderella");
					json_array.put(json_tmp);
				}
			}
			
			
		} catch (DatabaseException e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, e.toString()); return;
		} catch (Exception e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, e.toString()); return;
		}
		
		
		
//		JSONObject json_obj = new JSONObject();
//		json_obj.put("id", "1");
//		json_obj.put("nome", "shemalee");
//		json_obj.put("descrizione", "shemalee");
//		json_obj.put("prezzo", "5.5");
//		json_obj.put("isEreditata", true);
//		json_array.put(json_obj);
//		
//		json_obj = new JSONObject();
//		json_obj.put("id", "2");
//		json_obj.put("nome", "asder");
//		json_obj.put("descrizione", "shemalee");
//		json_obj.put("prezzo", "5.5");
//		json_obj.put("isEreditata", true);
//		json_array.put(json_obj);
//		
//		json_obj = new JSONObject();
//		json_obj.put("id", "3");
//		json_obj.put("nome", "loller");
//		json_obj.put("descrizione", "shemalee");
//		json_obj.put("categoriaDiAppartenenza", "shemalee");
//		json_obj.put("prezzo", "5.5");
//		json_obj.put("isEreditata", false);
//		json_array.put(json_obj);
		
		JSONResponse.WriteOutput(response, true, "Caricamento effettuato correttamente", "data", json_array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		idTenant = (Integer) request.getSession().getAttribute("idTenant");
		JSONArray json_array = new JSONArray();
		JSONObject json_tmp = null;
		String id = request.getParameter("id");
		BigDecimal prezzo;
		try{
			prezzo = BigDecimal.valueOf( Double.parseDouble(request.getParameter("prezzo")));
		}catch (Exception e){
			prezzo = BigDecimal.valueOf(0);
		}
		try {
			String nome = request.getParameter("nome");
			String descrizione = request.getParameter("descrizione");
			int idCategoria = 2;
			String action = request.getParameter("action");
			
			if(action != null && action.equals("delete")){
				gestioneVariazioni.deleteVariazione(Integer.parseInt(id));
				JSONResponse.WriteOutput(response, true, "Rimozione effettuata correttamente!","data",json_array); return;
			}
			
			
			if(id == null || id.length() == 0){	//New
				JSONResponse.WriteOutput(response, true, "Kissalini a me!!");
			}else if(id.equals("new")){
					if(nome.length() == 0 && descrizione.length() == 0){
						JSONResponse.WriteOutput(response, false, "Campi vuoti"); return;
					}
				WrapperVariazione wrapperVariazione = gestioneVariazioni.aggiungiVariazione(idTenant, nome, descrizione, prezzo, idCategoria);
				json_tmp = JSONFromBean.jsonFromOBJ(wrapperVariazione);
				json_tmp.put("isEreditata", false);
				json_array.put(json_tmp);
			}else{
				JSONObject json_obj = new JSONObject();
				json_obj.put("id", "1");
				json_obj.put("nome", "shemalee");
				json_array.put(json_obj);
				JSONResponse.WriteOutput(response, true, "Kissalini a me!!", "data", json_array);
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, e.toString()); return;
		} catch (Exception e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, e.toString()); return;
		}
		
	}

}
