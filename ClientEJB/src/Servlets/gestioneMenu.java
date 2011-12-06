package Servlets;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

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
    	if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Administrator) ){
    		return;
    	}
    	
    	String node = request.getParameter("node");
		if(node == null){
			JSONResponse.WriteOutput(response, false, "Richiesta non valida");	return;
		}
		
		idTenant = (Integer) request.getSession().getAttribute("idTenant");
		
		try{
			if(node.equals("root")){
				List<TreeNodeCategoria> listaCategorie = gestioneCategoria.getCategorie(idTenant, 0);
				JSONArray json_array = new JSONArray();
				JSONObject json_tmp = null;
				if(listaCategorie != null){
					for(int i=0; i<listaCategorie.size(); i++){
						json_tmp = JSONFromBean.jsonFromOBJ(listaCategorie.get(i));
						json_tmp.put("parentId","root");
						json_array.put(json_tmp);
					}
				}
				JSONResponse.WriteOutput(response, true, "OK", "data", json_array); return;
			}else if(node.startsWith("C")){
				List<TreeNodeCategoria> listaCategorie = gestioneCategoria.getCategorie(idTenant, Integer.parseInt(request.getParameter("parentId").substring(1)));
			}else if(node.equals("V")){
				
			}
			
			
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
    	
    	
    	try{
			//CREATE
    		if(request.getParameter("action").equals("create")){
    			
    		//UPDATE
    		}else if(request.getParameter("action").equals("update")){
    			
    		//DELETE
    		}else if(request.getParameter("action").equals("delete")){
    			
    		}
			
    		//Caso del delete di default del datasource
    		JSONResponse.WriteOutput(response, true, "OK"); return;
			
		}catch(Exception e){
			JSONResponse.WriteOutput(response, false, "Eccezione generale");	return;
		}
    	
	}
}
