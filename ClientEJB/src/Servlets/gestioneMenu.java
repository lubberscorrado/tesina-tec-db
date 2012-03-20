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
import com.orb.gestioneOggetti.*;
import com.restaurant.*;

import Utilita.JSONFromBean;
import Utilita.JSONResponse;



/**
 * Servlet implementation class gestioneMenu
 */
@WebServlet("/gestioneMenu")
public class gestioneMenu extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int idTenant = -1;
	@EJB	private GestioneCategoria	gestioneCategoria;
	@EJB	private GestioneVoceMenu	gestioneVoceMenu;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public gestioneMenu() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//Controllo dei privilegi di accesso
    	if( !JSONResponse.UserAccessControlAnyPrivs(request, response, new int[]{JSONResponse.PRIV_Cassiere,JSONResponse.PRIV_Cameriere,JSONResponse.PRIV_Cuoco}) ){
    		return;
    	}
    	
    	String node = request.getParameter("node");
		if(node == null){
			JSONResponse.WriteOutput(response, false, "Richiesta non valida");	return;
		}
		
		idTenant = (Integer) request.getSession().getAttribute("idTenant");
		
		try{
			if(node.equals("root")){
				List<TreeNodeCategoria> listaCategorie = gestioneCategoria.getCategorie(idTenant, 1, false);
				JSONArray json_array = new JSONArray();
				JSONObject json_tmp = null;
				if(listaCategorie != null){
					for(int i=0; i<listaCategorie.size(); i++){
						json_tmp = JSONFromBean.jsonFromOBJ(listaCategorie.get(i));
						json_tmp.put("parentId","root");
						json_tmp.put("tipo",1);	//Categoria
						json_array.put(json_tmp);
					}
				}
				JSONResponse.WriteOutput(response, true, "OK", "data", json_array); return;
			}else if(node.startsWith("C")){
				int parentId = Integer.parseInt(	node.substring(1)	);
				List<TreeNodeCategoria> listaCategorie = gestioneCategoria.getCategorie(idTenant, parentId, false);
				List<TreeNodeVoceMenu> listaVociMenu = gestioneVoceMenu.getVociMenuByCategoria(idTenant, parentId, false);
				JSONArray json_array = new JSONArray();
				JSONObject json_tmp = null;
				if(listaCategorie != null){
					for(int i=0; i<listaCategorie.size(); i++){
						json_tmp = JSONFromBean.jsonFromOBJ(listaCategorie.get(i));
						json_tmp.put("parentId", node);
						json_tmp.put("tipo",1);	//Categoria
						json_array.put(json_tmp);
					}
				}
				if(listaVociMenu != null){
					for(int i=0; i<listaVociMenu.size(); i++){
						json_tmp = JSONFromBean.jsonFromOBJ(listaVociMenu.get(i));
						json_tmp.put("parentId", node);
						json_tmp.put("tipo",2);	//Voce Menu
						json_array.put(json_tmp);
					}
				}
				JSONResponse.WriteOutput(response, true, "OK", "data", json_array); return;
			}
			
			
		}catch (DatabaseException e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, e.toString());			return;
		}catch(Exception e){
			JSONResponse.WriteOutput(response, false, "Eccezione generale");	return;
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//Controllo dei privilegi di accesso
    	if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Administrator) ){
    		return;
    	}
    	JSONObject json_tmp = null;
    	idTenant = (Integer) request.getSession().getAttribute("idTenant");
    	String action = request.getParameter("action");
    	
    	if(action == null || action.length() == 0){
    		JSONResponse.WriteOutput(response, true, "No action.");
    		return;
    	}
    	
    	
    	try{
			//CREATE
    		if(action.equals("create")){
    			
    			int tipo = Integer.parseInt(request.getParameter("tipo"));
    			int parentId = Integer.parseInt( request.getParameter("parentId").substring(1) );
    			String nome = request.getParameter("nome");
    			String descrizione = request.getParameter("descrizione");
    			JSONArray json_array = new JSONArray();
    			if( tipo == 1){	//Categoria
    				TreeNodeCategoria treeNodeCategoria = gestioneCategoria.aggiungiCategoria(idTenant, nome, descrizione, parentId);
    				json_tmp = JSONFromBean.jsonFromOBJ(treeNodeCategoria);
    				json_tmp.put("parentId", request.getParameter("parentId"));
    				json_tmp.put("tipo", 1);
    				json_array.put(json_tmp);
    			}else{
    				BigDecimal prezzo;
    				try{
    					prezzo = BigDecimal.valueOf(Double.parseDouble( request.getParameter("prezzo") ));
    				}catch(Exception e){
    					JSONResponse.WriteOutput(response, false, "Prezzo mancante.");	return;
    				}
    				TreeNodeVoceMenu treeNodeVoceMenu = gestioneVoceMenu.aggiungiVoceMenu(idTenant, parentId, nome, descrizione, prezzo);
    				json_tmp = JSONFromBean.jsonFromOBJ(treeNodeVoceMenu);
    				json_tmp.put("parentId", request.getParameter("parentId"));
    				json_tmp.put("tipo", 2);
    				json_array.put(json_tmp);
    			}
    			
    			
    			JSONResponse.WriteOutput(response, true, "Elemento creato correttamente", "create", "data", json_array);	return;
    			
    		//UPDATE
    		}else if(action.equals("update")){
    			JSONArray json_array = new JSONArray();
    			String idString = request.getParameter("id");
    			String nome = request.getParameter("nome");
    			String descrizione = request.getParameter("descrizione");
    			int id = Integer.parseInt( idString.substring(1));
    			if(idString.startsWith("C")){
    				TreeNodeCategoria treeNodeCategoria = gestioneCategoria.updateCategoria(id, nome, descrizione);
    				json_tmp = JSONFromBean.jsonFromOBJ(treeNodeCategoria);
    				json_tmp.put("parentId", request.getParameter("parentId"));
    				json_tmp.put("tipo", 1);
    				json_array.put(json_tmp);
    			}else if(idString.startsWith("V")){
    				BigDecimal prezzo;
    				try{
    					prezzo = BigDecimal.valueOf(Double.parseDouble( request.getParameter("prezzo") ));
    				}catch(Exception e){
    					JSONResponse.WriteOutput(response, false, "Prezzo mancante.");	return;
    				}
    				
    				TreeNodeVoceMenu treeNodeVoceMenu = gestioneVoceMenu.updateVoceMenu(id, nome, descrizione,prezzo);
    				json_tmp = JSONFromBean.jsonFromOBJ(treeNodeVoceMenu);
    				json_tmp.put("parentId", request.getParameter("parentId"));
    				json_tmp.put("tipo", 2);
    				json_array.put(json_tmp);
    			}
    			JSONResponse.WriteOutput(response, true, "Elemento modificato correttamente.", "update", "data", json_array);	return;
    		//DELETE
    		}else if(action.equals("delete")){
    			String idString = request.getParameter("id");
    			int id = Integer.parseInt( idString.substring(1));
    			if(idString.startsWith("C")){
    				gestioneCategoria.deleteCategoria(id);
    			}else if(idString.startsWith("V")){
    				gestioneVoceMenu.deleteVoceMenu(id);
    			}
    			JSONResponse.WriteOutput(response, true, "Elemento cancellato correttamente."); return;
    		}
			
    		//Caso del delete di default del datasource
    		JSONResponse.WriteOutput(response, true, "OK"); return;
			
    	}catch (DatabaseException e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, e.toString());			return;
		}catch(Exception e){
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, "Eccezione generale");	return;
		}
    	
	}
}
