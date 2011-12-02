package Servlets;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import Utilita.JSONFromBean;
import Utilita.JSONResponse;

import com.exceptions.DatabaseException;
import com.orb.Area;
import com.orb.Piano;
import com.orb.Tavolo;
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
	@EJB
 	private GestionePiano gestionePiano;
	@EJB
	private GestioneArea gestioneArea;
	@EJB
	private GestioneTavolo gestioneTavolo;
	
	private int idTenant = -1;
       
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
			List<TreeNodePiano> lista_piani = gestionePiano.getPiani(idTenant);
			TreeNodePiano piano = null;
			if(lista_piani != null){
				for(int i=0; i<lista_piani.size();i++){
					piano = lista_piani.get(i);
					json_array.put(	JSONFromBean.jsonFromTreeNodePiano(piano) );
				}
			}
			
		//AREE
		}else if (request.getParameter("node").startsWith("P")){
			int idPiano = Integer.parseInt( request.getParameter("node").substring(1) );
			List<TreeNodeArea> lista_aree = null;
			try {
				lista_aree = gestioneArea.getAreeByPiano( idPiano );
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TreeNodeArea area = null;
			if(lista_aree != null){
				for(int i=0; i<lista_aree.size();i++){
					area = lista_aree.get(i);
					json_tmp = JSONFromBean.jsonFromTreeNodeArea(area);
					json_tmp.put("parentId","P"+idPiano);
					json_array.put( json_tmp );
				}
			}
			
		//TAVOLI
		}else if (request.getParameter("node").startsWith("A")){
			int idArea = Integer.parseInt( request.getParameter("node").substring(1) );
			List<TreeNodeTavolo> lista_tavoli = null;
			try {
				lista_tavoli = gestioneTavolo.getTavoloByArea(idArea);
			} catch (DatabaseException e) {
				json_out.put("success", false);
				json_out.put("message","Eccezione caricamento tavoli");
				response.getWriter().print(json_out);
				e.printStackTrace();
				System.err.println("ECCEZIONE: "+e.toString());
			}
			TreeNodeTavolo tavolo = null;
			if(lista_tavoli != null){
				for(int i=0; i<lista_tavoli.size();i++){
					tavolo = lista_tavoli.get(i);
					json_tmp = JSONFromBean.jsonFromTreeNodeTavolo(tavolo);
					json_tmp.put("parentId","A"+idArea);
					json_array.put( json_tmp );
				}
			}

		}
		
		json_out.put("success", true);
		json_out.put("message","OK");
		json_out.put("data", json_array);
		response.getWriter().print(json_out);
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Controllo dei privilegi di accesso
		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Administrator) ){
			return;
		}
		
		String nome = request.getParameter("nome");
		String enabledS = request.getParameter("enabled");
		boolean enabled;
		if(enabledS.equals("on")) enabled = true; else enabled = false;
		String numPosti = request.getParameter("numPosti");
		String numeroPiano = request.getParameter("numeroPiano");
		String descrizione = request.getParameter("descrizione");
		
		idTenant = (Integer) request.getSession().getAttribute("idTenant");
		JSONArray json_array = new JSONArray();
		JSONObject json_tmp = null;
		int tipo = Integer.parseInt( request.getParameter("tipo") );
		String query = null;
		TreeNodePiano piano = null;
		TreeNodeArea area = null;
		TreeNodeTavolo tavolo = null;
		switch(tipo){
			case 1: {//Aggiungi piano
				try {
					piano = gestionePiano.aggiungiPiano(idTenant, Integer.parseInt(numeroPiano), nome, descrizione, enabled);
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
					tavolo = gestioneTavolo.aggiungiTavolo(idTenant, request.getParameter("nome"), request.getParameter("stato"), request.getParameter("descrizione"), Integer.parseInt(request.getParameter("numPosti")), Boolean.parseBoolean(request.getParameter("enabled")), Integer.parseInt(request.getParameter("parentId").substring(1)));
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
		
		JSONObject json_out = new JSONObject();
		json_out.put("success", true);
		json_out.put("message", "Inserimento effettuato correttamente");
		json_out.put("data", json_array);
		response.getWriter().println(	json_out	);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Controllo dei privilegi di accesso
		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Administrator) ){
			return;
		}
		//Modifico i dati
		int tipo = Integer.parseInt(request.getParameter("tipo"));
		switch(tipo){
			case 1:{
				break;
			}
			case 2:{
				break;
			}
			case 3:{
				break;
			}
		}
		
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Controllo dei privilegi di accesso
		if( !JSONResponse.UserAccessControl(request, response, JSONResponse.PRIV_Administrator) ){
			return;
		}
	}

}
