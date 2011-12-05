package Servlets;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import sun.org.mozilla.javascript.internal.IdScriptableObject;

import Utilita.JSONFromBean;
import Utilita.JSONResponse;

import com.exceptions.DatabaseException;
import com.orb.StatoTavoloEnum;
import com.orb.gestioneOggetti.GestioneArea;
import com.orb.gestioneOggetti.GestionePiano;
import com.orb.gestioneOggetti.GestioneTavolo;
import com.restaurant.TreeNodeArea;
import com.restaurant.TreeNodePiano;
import com.restaurant.TreeNodeTavolo;

/**
 * Servlet implementation class gestioneTavolo
 */
@WebServlet("/gestioneTavolo")
public class gestioneTavolo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int idTenant = -1;
	
	@EJB	private GestionePiano	gestionePiano;
	@EJB	private GestioneArea	gestioneArea;
	@EJB	private GestioneTavolo	gestioneTavolo;
	
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public gestioneTavolo() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Controllo dei privilegi di accesso
		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Administrator) ){
			return;
		}
		
		idTenant = (Integer) request.getSession().getAttribute("idTenant");
		JSONArray json_array = new JSONArray();
		JSONObject json_out = new JSONObject();
		JSONObject json_tmp = null;
		
		//PIANI
		if(request.getParameter("node").equals("root")){
			try {
				List<TreeNodePiano> lista_piani = gestionePiano.getPiani(idTenant);
				TreeNodePiano piano = null;
				if(lista_piani != null){
					for(int i=0; i<lista_piani.size();i++){
						piano = lista_piani.get(i);
						json_array.put(	JSONFromBean.jsonFromTreeNodePiano(piano) );
					}
				}
				JSONResponse.WriteOutput(response, true, "OK","data",json_array);
				return;
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			} catch (Exception e){
				JSONResponse.WriteOutput(response, false, "Parsing error.");
				return;
			}
			
		//AREE
		}else if (request.getParameter("node").startsWith("P")){
			
			try {
				int idPiano = Integer.parseInt( request.getParameter("node").substring(1) );
				List<TreeNodeArea> lista_aree = gestioneArea.getAreeByPiano( idPiano );
				TreeNodeArea area = null;
				if(lista_aree != null){
					for(int i=0; i<lista_aree.size();i++){
						area = lista_aree.get(i);
						json_tmp = JSONFromBean.jsonFromTreeNodeArea(area);
						json_tmp.put("parentId","P"+idPiano);
						json_array.put( json_tmp );
					}
				}
				JSONResponse.WriteOutput(response, true, "OK","data",json_array);
				return;
			} catch (DatabaseException e) {
				e.printStackTrace();
				return;
			} catch (Exception e){
				JSONResponse.WriteOutput(response, false, "Parsing error.");
				return;
			}
			
			
		//TAVOLI
		}else if (request.getParameter("node").startsWith("A")){
			try {
				int idArea = Integer.parseInt( request.getParameter("node").substring(1) );
				List<TreeNodeTavolo> lista_tavoli = gestioneTavolo.getTavoloByArea(idArea);
				TreeNodeTavolo tavolo = null;
				if(lista_tavoli != null){
					for(int i=0; i<lista_tavoli.size();i++){
						tavolo = lista_tavoli.get(i);
						json_tmp = JSONFromBean.jsonFromTreeNodeTavolo(tavolo);
						json_tmp.put("parentId","A"+idArea);
						json_array.put( json_tmp );
					}
				}
				JSONResponse.WriteOutput(response, true, "OK","data",json_array);
				return;
				
			} catch (DatabaseException e) {
				json_out.put("success", false);
				json_out.put("message","Eccezione caricamento tavoli");
				response.getWriter().print(json_out);
				e.printStackTrace();
				System.err.println("ECCEZIONE: "+e.toString());
				return;
			} catch (Exception e){
				JSONResponse.WriteOutput(response, false, "Parsing error.");
				return;
			}
		}
		
		//Mando in output i dati
		JSONResponse.WriteOutput(response, true, "OK","data",json_array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Controllo dei privilegi di accesso
		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Administrator) ){
			return;
		}
		
		idTenant = (Integer) request.getSession().getAttribute("idTenant");
		JSONArray json_array = new JSONArray();
		JSONObject json_tmp = null;
		int tipo = Integer.parseInt( request.getParameter("tipo") );
		boolean enabled = Boolean.parseBoolean( request.getParameter("enabled") );
		String nome = request.getParameter("nome");
		String descrizione = request.getParameter("descrizione");
		
		if(request.getParameter("action").equals("update")){
			int id = Integer.parseInt( request.getParameter("id").substring(1) );
			switch(tipo){
				case 1:{
					
					break;
				}
				case 2:{
					break;
				}
				case 3:{
					try {
						TreeNodeTavolo treeNodeTavolo = gestioneTavolo.updateTavolo(id, Integer.parseInt(request.getParameter("numPosti")), nome, descrizione, StatoTavoloEnum.LIBERO.toString(), enabled);
						json_tmp = JSONFromBean.jsonFromTreeNodeTavolo(treeNodeTavolo);
						json_tmp.put("parentId", request.getParameter("parentId"));
						json_array.put(json_tmp);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DatabaseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;
				}
				default: return;
			}
			JSONResponse.WriteOutput(response, true, "Modifiche effettuate correttamente!","data",json_array);
			return;
			
		}else if(request.getParameter("action").equals("delete")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt( sid.substring(1) );
			
			if(sid.startsWith("P")){
				
			}else if(sid.startsWith("A")){
				
			}else if(sid.startsWith("T")){
				
			}
			
			JSONResponse.WriteOutput(response, true, "Elemento cancellato correttamente!","data",json_array);
			return;
			
		}else if(request.getParameter("action").equals("create")){
			//ADD
			
			String numPosti = request.getParameter("numPosti");
			String numeroPiano = request.getParameter("numeroPiano");
			
			
			String query = null;
			TreeNodePiano piano = null;
			TreeNodeArea area = null;
			TreeNodeTavolo tavolo = null;
			switch(tipo){
				case 1: {//Aggiungi piano
					int intNumeroPiano = 0;
					try {
						try{
							intNumeroPiano = Integer.parseInt(numeroPiano);
						}catch(Exception e){
							intNumeroPiano = 0;
						}
						
						piano = gestionePiano.aggiungiPiano(idTenant, intNumeroPiano, nome, descrizione, enabled);
						json_tmp	=	JSONFromBean.jsonFromTreeNodePiano(piano);
						//json_tmp.put("parentId, );
						json_array.put( json_tmp );
						
						
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DatabaseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;
				}
				case 2: {//Aggiungi area
					try {
						area = gestioneArea.aggiungiArea(idTenant, request.getParameter("nome"), request.getParameter("descrizione"), Boolean.parseBoolean(request.getParameter("enabled")), Integer.parseInt(request.getParameter("parentId").substring(1)));
						json_tmp	=	JSONFromBean.jsonFromTreeNodeArea(area);
						json_tmp.put("parentId", request.getParameter("parentId").substring(1));
						json_array.put(	json_tmp );
					
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DatabaseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				case 3: {//Aggiungi tavolo
					try {
						tavolo = gestioneTavolo.aggiungiTavolo(idTenant, request.getParameter("nome"), StatoTavoloEnum.LIBERO.toString(), request.getParameter("descrizione"), Integer.parseInt(request.getParameter("numPosti")), Boolean.parseBoolean(request.getParameter("enabled")), Integer.parseInt(request.getParameter("parentId").substring(1)));
						json_tmp = JSONFromBean.jsonFromTreeNodeTavolo(tavolo);
						json_tmp.put("parentId", request.getParameter("parentId").substring(1));
						json_array.put(	json_tmp );
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DatabaseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				default: return;
			}
			//Mando in output i dati
			JSONResponse.WriteOutput(response, true, "Inserimento effettuato correttamente","data",json_array);
			return;
		}
		
		//Caso del delete di default del datasource
		JSONResponse.WriteOutput(response, true, "OK");
	}

}
