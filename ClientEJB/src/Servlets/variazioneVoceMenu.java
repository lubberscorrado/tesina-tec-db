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
import javax.swing.tree.TreeNode;

import org.json.JSONArray;
import org.json.JSONObject;

import com.exceptions.DatabaseException;
import com.orb.gestioneOggetti.GestioneCategoria;
import com.orb.gestioneOggetti.GestioneVariazioni;
import com.restaurant.TreeNodeCategoria;
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
	@EJB	private GestioneCategoria	gestioneCategorie;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public variazioneVoceMenu() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		idTenant = (Integer) request.getSession().getAttribute("idTenant");
		String idCategoriaString = request.getParameter("idCategoria");
		int idCategoria = 0;
		try{
			idCategoria = Integer.parseInt(idCategoriaString);
		}catch(Exception e){
			JSONResponse.WriteOutput(response, false, "Categoria non valida."); return;
		}
		JSONArray json_array = new JSONArray();
		JSONObject json_tmp = null;
		TreeNodeCategoria treeNodeCategoria = null;
		List<WrapperVariazione> listaVariazioni = null;
		//List<TreeNodeCategoria> listaCategorie = gestioneCategorie.getCategorie(idTenant, 5);
		boolean flagIsEreditata = false;
		try {
			while(idCategoria > 0){
				treeNodeCategoria = gestioneCategorie.getCategoriaById(idCategoria);
				listaVariazioni = gestioneVariazioni.getVariazioniByCategoria(idCategoria, idTenant);
				if(listaVariazioni != null){
					for(int i=0; i<listaVariazioni.size(); i++){
						json_tmp = JSONFromBean.jsonFromOBJ( listaVariazioni.get(i) );
						json_tmp.put("isEreditata", flagIsEreditata);
						json_tmp.put("idCategoria", idCategoria);
						json_tmp.put("categoriaDiAppartenenza", treeNodeCategoria.getNome());
						json_array.put(json_tmp);
					}
				}
				idCategoria = treeNodeCategoria.getIdCategoriaPadre();
				flagIsEreditata = true;
			}
			
			
			JSONResponse.WriteOutput(response, true, "Caricamento effettuato correttamente", "data", json_array); return;
		} catch (DatabaseException e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, e.toString()); return;
		} catch (Exception e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, e.toString()); return;
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		idTenant = (Integer) request.getSession().getAttribute("idTenant");
		String action = request.getParameter("action");
		String idCategoriaString = request.getParameter("idCategoria");
		
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
			int idCategoria = Integer.parseInt(idCategoriaString);
			
			if(action != null && action.equals("create")){
				if(nome.length() == 0 && descrizione.length() == 0){
					JSONResponse.WriteOutput(response, false, "Campi vuoti"); return;
				}
				WrapperVariazione wrapperVariazione = gestioneVariazioni.aggiungiVariazione(idTenant, nome, descrizione, prezzo, idCategoria);
				json_tmp = JSONFromBean.jsonFromOBJ(wrapperVariazione);
				json_tmp.put("isEreditata", false);
				json_tmp.put("idCategoria", idCategoria);
				TreeNodeCategoria treeNodeCategoria = gestioneCategorie.getCategoriaById(idCategoria);
				json_tmp.put("categoriaDiAppartenenza", treeNodeCategoria.getNome());
				json_array.put(json_tmp);
				JSONResponse.WriteOutput(response, true, "Inserimento effettuato correttamente!","data",json_array); return;
			
			}else if(action != null && action.equals("delete")){
				gestioneVariazioni.deleteVariazione(Integer.parseInt(id));
				JSONResponse.WriteOutput(response, true, "Rimozione effettuata correttamente!","data",json_array); return;
			
			}else{
				WrapperVariazione wrapperVariazione = gestioneVariazioni.updateVariazione(Integer.parseInt(id), nome, descrizione, prezzo);
				json_tmp = JSONFromBean.jsonFromOBJ(wrapperVariazione);
				json_tmp.put("isEreditata", false);
				json_tmp.put("idCategoria", idCategoria);
				TreeNodeCategoria treeNodeCategoria = gestioneCategorie.getCategoriaById(idCategoria);
				json_tmp.put("categoriaDiAppartenenza", treeNodeCategoria.getNome());
				json_array.put(json_tmp);
				JSONResponse.WriteOutput(response, true, "Modifiche effettuate correttamente!","data",json_array); return;
			
			}
			
			
			
			
		} catch (DatabaseException e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, e.toString()); return;
		} catch (Exception e) {
			//e.printStackTrace();
			JSONResponse.WriteOutput(response, false, "Errore generale."); return;
		}
		
	}

}
