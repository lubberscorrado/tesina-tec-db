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
		JSONObject json_tmp = null;
		String parentNode = request.getParameter("node");
		
		if(parentNode == null || parentNode.length() == 0){
			JSONResponse.WriteOutput(response, false, "Nodo non valido.");
			return;
		}
		
		
		
		try {
		
			//PIANI
			if(request.getParameter("node").equals("root")){
			
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
			
			
			//AREE
			}else if (request.getParameter("node").startsWith("P")){
			
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
			
			
			//TAVOLI
			}else if (request.getParameter("node").startsWith("A")){
			
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
				
			}
		
			//Mando in output i dati
			JSONResponse.WriteOutput(response, true, "OK","data",json_array);
		
		} catch (DatabaseException e) {
			JSONResponse.WriteOutput(response, false, e.toString());
			e.printStackTrace();
			return;
		} catch (Exception e){
			JSONResponse.WriteOutput(response, false, "Parsing error.");
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Controllo dei privilegi di accesso
		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Administrator) ){
			return;
		}
		//Controllo dell'azione richiesta
		String action = request.getParameter("action");
		if(action == null || action.length() == 0){
			JSONResponse.WriteOutput(response, true, "No Action");
			return;
		}

		idTenant = (Integer) request.getSession().getAttribute("idTenant");
		JSONArray json_array = new JSONArray();
		JSONObject json_tmp = null;
		
		//Parametri di input
		String nome = null;
		String descrizione = null;
		String enabledS = request.getParameter("enabled");
		String numPostiS = request.getParameter("numPosti");
		String numeroPianoS = request.getParameter("numeroPiano");
		int numPosti;
		int numeroPiano;
		boolean enabled;
		int tipo = 0;
		try{
			tipo = Integer.parseInt( request.getParameter("tipo") );
			
			try{
				if(enabledS.equals("on"))
					enabled = true;
				else
					enabled = Boolean.parseBoolean( enabledS );
			}catch(Exception e){
				enabled = false;
			}
			
			try{	numPosti = Integer.parseInt(numPostiS);			}catch(Exception e){	numPosti = 0;	}
			try{	numeroPiano = Integer.parseInt(numeroPianoS);	}catch(Exception e){	numeroPiano = 0;	}
			
			
			nome = request.getParameter("nome");
			descrizione = request.getParameter("descrizione");
		}catch(Exception e){
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, "ERRORE PARSING DEI PARAMETRI");
			return;
		}
		
		
		
		try{
			
		//UPDATE
		if(action.equals("update")){
			int id = Integer.parseInt( request.getParameter("id").substring(1) );
			
			switch(tipo){
				case 1:{
					TreeNodePiano treeNodePiano = gestionePiano.updatePiano(id, nome, descrizione, Integer.parseInt(request.getParameter("numeroPiano")), enabled);
					json_tmp = JSONFromBean.jsonFromTreeNodePiano(treeNodePiano);
					json_tmp.put("parentId", request.getParameter("parentId"));
					json_array.put(json_tmp);

					JSONResponse.WriteOutput(response, true, "Piano modificato correttamente.","data",json_array);
					return;
				}
				case 2:{
					TreeNodeArea treeNodeArea = gestioneArea.updateArea(id, nome, descrizione, enabled);
					json_tmp = JSONFromBean.jsonFromTreeNodeArea(treeNodeArea);
					json_tmp.put("parentId", request.getParameter("parentId"));
					json_array.put(json_tmp);

					JSONResponse.WriteOutput(response, true, "Area modificata correttamente.","data",json_array);
					return;
				}
				case 3:{
					TreeNodeTavolo treeNodeTavolo = gestioneTavolo.updateTavolo(id, numPosti, nome, descrizione, StatoTavoloEnum.LIBERO, enabled);
					json_tmp = JSONFromBean.jsonFromTreeNodeTavolo(treeNodeTavolo);
					json_tmp.put("parentId", request.getParameter("parentId"));
					json_array.put(json_tmp);
					
					JSONResponse.WriteOutput(response, true, "Tavolo modificato correttamente.","data",json_array);
					return;
				}
				default: {
					JSONResponse.WriteOutput(response, false, "Parametri errati.","data",json_array);
					return;
				}
			}
		
		//DELETE
		}else if(action.equals("delete")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt( sid.substring(1) );
			
			if(sid.startsWith("P")){
				gestionePiano.deletePiano(id);
			}else if(sid.startsWith("A")){
				gestioneArea.deleteArea(id);
			}else if(sid.startsWith("T")){
				gestioneTavolo.deleteTavolo(id);
			}
			
			JSONResponse.WriteOutput(response, true, "Elemento cancellato correttamente!","data",json_array);
			return;
			
		//CREATE
		}else if(action.equals("create")){
			//ADD
			
			
			String query = null;
			TreeNodePiano piano = null;
			TreeNodeArea area = null;
			TreeNodeTavolo tavolo = null;
			switch(tipo){
				case 1: {//Aggiungi piano
					piano = gestionePiano.aggiungiPiano(idTenant, numeroPiano, nome, descrizione, enabled);
					json_tmp	=	JSONFromBean.jsonFromTreeNodePiano(piano);
					//json_tmp.put("parentId, );
					json_array.put( json_tmp );
					JSONResponse.WriteOutput(response, true, "Piano aggiunto correttamente.","data",json_array);
					return;
				}
				case 2: {//Aggiungi area
					area = gestioneArea.aggiungiArea(idTenant, nome, descrizione, enabled, Integer.parseInt(request.getParameter("parentId").substring(1)));
					json_tmp	=	JSONFromBean.jsonFromTreeNodeArea(area);
					json_tmp.put("parentId", request.getParameter("parentId"));
					json_array.put(	json_tmp );
					JSONResponse.WriteOutput(response, true, "Area aggiunta correttamente.","data",json_array);
					return;
				}
				case 3: {//Aggiungi tavolo
					tavolo = gestioneTavolo.aggiungiTavolo(idTenant, nome, StatoTavoloEnum.LIBERO, descrizione, numPosti, enabled, Integer.parseInt(request.getParameter("parentId").substring(1)));
					json_tmp = JSONFromBean.jsonFromTreeNodeTavolo(tavolo);
					json_tmp.put("parentId", request.getParameter("parentId"));
					json_array.put(	json_tmp );
					JSONResponse.WriteOutput(response, true, "Tavolo aggiunto correttamente.","data",json_array);
					return;
				}
				default: return;
			}
		}
		
		//Caso del delete di default del datasource
		JSONResponse.WriteOutput(response, true, "OK");
		
		} catch (NumberFormatException e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, e.toString()); return;
		} catch (DatabaseException e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, e.toString()); return;
		} catch (Exception e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, "ECCEZIONE GENERICA"); return;
		}
		
		
	}

}
