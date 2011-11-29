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

import com.orb.Piano;
import com.orb.gestioneOggetti.GestioneArea;
import com.orb.gestioneOggetti.GestionePiano;
import com.orb.gestioneOggetti.GestioneTavolo;

import DB.DBConnection;

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
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public gestioneTavolo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Piano> lista_piani = null;
		JSONArray json_array = new JSONArray();
		JSONObject json_tmp = null;
//		DBConnection db = new DBConnection();
//		JSONObject json_out = new JSONObject();
//		db.connect();
//		
//		String query = null;
//		
//		if(request.getParameter("node").equals("root")){
//			query = "SELECT idPiano AS realId,	nome AS text,	'root' AS realParentId,		1 AS tipo		FROM piano;";
//			json_out = db.executeJSONQuery(query, "data");
//			
//		}else if (request.getParameter("node").startsWith("P")){
//			query = "SELECT idArea AS realId,	nome AS text,	idPiano AS realParentId,	2 AS tipo	 	FROM area WHERE idPiano = '"+request.getParameter("node").substring(1)+"';";
//			json_out = db.executeJSONQuery(query, "data");
//			
//		}else if (request.getParameter("node").startsWith("A")){
//			query = "SELECT idTavolo AS realId,	nome AS text,	idArea AS realParentId,		3 AS tipo		FROM tavolo WHERE idArea = '"+request.getParameter("node").substring(1)+"';";
//			json_out = db.executeJSONQuery(query, "data");
//		}
//		
//		response.getWriter().println(	json_out	);
//		db.disconnect();
		
		
		
		if(request.getParameter("node").equals("root")){
			lista_piani = gestionePiano.getPiani(0);
			Piano piano = null;
			for(int i=0; i<lista_piani.size();i++){
				json_tmp = new JSONObject();
				piano = lista_piani.get(i);
				json_tmp.put("text", piano.getNome());
				json_tmp.put("id", piano.getIdPiano());
				json_tmp.put("parentId", "root");
				json_array.put(json_tmp);
				// fields: ['id','realId','parentId','realParentId','nome','descrizione','tipo','enabled','numPosti','stato','text'],
			}
			
			///query = "SELECT idPiano AS realId,	nome AS text,	'root' AS realParentId,		1 AS tipo		FROM piano;";
			//json_out = db.executeJSONQuery(query, "data");
			
		}else if (request.getParameter("node").startsWith("P")){
			//query = "SELECT idArea AS realId,	nome AS text,	idPiano AS realParentId,	2 AS tipo	 	FROM area WHERE idPiano = '"+request.getParameter("node").substring(1)+"';";
			//json_out = db.executeJSONQuery(query, "data");
			
		}else if (request.getParameter("node").startsWith("A")){
			//query = "SELECT idTavolo AS realId,	nome AS text,	idArea AS realParentId,		3 AS tipo		FROM tavolo WHERE idArea = '"+request.getParameter("node").substring(1)+"';";
			//json_out = db.executeJSONQuery(query, "data");
		}
		
		
		JSONObject json_out = new JSONObject();
		json_out.put("success", true);
		json_out.put("message","Forse va bene XD");
		json_out.put("data", json_array);
		response.getWriter().print(json_out);
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int tipo = Integer.parseInt( request.getParameter("tipo") );
		String query = null;
		switch(tipo){
			case 1: {//Aggiungi piano
				gestionePiano.aggiungiPiano(0, Integer.parseInt(request.getParameter("numeroPiano")), request.getParameter("nome"), request.getParameter("descrizione"), true);
				break;
			}
			case 2: {//Aggiungi area
				query = "INSERT INTO area () VALUES ();";
				break;
			}
			case 3: {//Aggiungi tavolo
				query = "INSERT INTO tavolo () VALUES ();";
				break;
			}
			default: return;
		}
		
		/*
		 * descrizione	
			enabled	on
			id	1
			nome	
			numPosti	
			numeroPiano	
			parentId	root
				1
		 */
		
		
		JSONObject tmp = new JSONObject();
		tmp.put("id", "9");
		if(request.getParameter("parentId").length()==0)tmp.put("parentId", "root");
		else tmp.put("parentId", request.getParameter("parentId"));
		
		tmp.put("tipo", request.getParameter("tipo"));
		tmp.put("text", request.getParameter("nome"));

		
		JSONArray json_array = new JSONArray();
		json_array.put(tmp);
		
		
		
		JSONObject json_out = new JSONObject();
		json_out.put("success", true);
		json_out.put("message", "Inserimento effettuato correttamente");
		json_out.put("id", 999);
		json_out.put("data", json_array);

		json_out.put("msg", "troie di altri tempi!!!");
		response.getWriter().println(	json_out	);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("PUTTA");
		
		
		
		
		
		JSONObject tmp = new JSONObject();
		tmp.put("parentId", "root");
		tmp.put("text", "newPiano");
		
		JSONArray json_array = new JSONArray();
		json_array.put(tmp);
		
		
		
		JSONObject json_out = new JSONObject();
		json_out.put("success", true);
		json_out.put("message", "shemale");
		json_out.put("root", json_array);
		json_out.put("msg", "troie di altri tempi!!!");
		
		response.getWriter().println(	json_out	);
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json_out = new JSONObject();
		json_out.put("success", true);
		json_out.put("message", "lalalala");
		
		response.getWriter().println(	json_out	);
	}

}
