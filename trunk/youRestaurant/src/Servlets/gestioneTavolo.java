package Servlets;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import DB.DBConnection;

/**
 * Servlet implementation class gestioneTavolo
 */
@WebServlet("/gestioneTavolo")
public class gestioneTavolo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
		DBConnection db = new DBConnection();
		JSONObject json_out = new JSONObject();
		db.connect();
		
		String query = null;
		
		if(request.getParameter("node").equals("root")){
			query = "SELECT CONCAT('P',idPiano) AS id,	idPiano AS realId,	nome AS text, 'root' AS parentId				FROM piano;";
			json_out = db.executeJSONQuery(query, "data");
			
		}else if (request.getParameter("node").startsWith("P")){
			query = "SELECT CONCAT('A',idArea) AS id,	idArea AS realId,	nome AS text, CONCAT('P',idPiano) AS parentId 	FROM area WHERE idPiano = '"+request.getParameter("node").substring(1)+"';";
			json_out = db.executeJSONQuery(query, "data");
			
		}else if (request.getParameter("node").startsWith("A")){
			query = "SELECT CONCAT('T',idTavolo) AS id,	idTavolo AS realId,	nome AS text, CONCAT('A',idArea) AS parentId, true AS leaf	FROM tavolo WHERE idArea = '"+request.getParameter("node").substring(1)+"';";
			json_out = db.executeJSONQuery(query, "data");
		}
		
		response.getWriter().println(	json_out	);
		db.disconnect();
	}
	
	/*
	 * `idPiano` int(11) NOT NULL AUTO_INCREMENT,
  `numero` int(11) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `enabled
	 */

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("PUTTA");
		
		
		
		
		
		JSONObject tmp = new JSONObject();
		tmp.put("parentId", "root");
		tmp.put("text", "newPiano");
		
		JSONArray json_array = new JSONArray();
		json_array.put(tmp);
		
		
		
		JSONObject json_out = new JSONObject();
		json_out.put("success", true);
		json_out.put("message", "shemale");
		json_out.put("id", 999);
		json_out.put("root", json_array);
		
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
	
	private JSONArray getAree(String idPiano){
		DBConnection db = new DBConnection();
		JSONArray json_array = new JSONArray();
		JSONObject json_tmp = new JSONObject();
		db.connect();
		String query = "SELECT * FROM area WHERE idPiano = '"+idPiano+"';";
		/**/
		int colonne;
		try {
			Statement stmt = db.getConnection().createStatement();     // Creo lo Statement per l'esecuzione della query
	        ResultSet rs = stmt.executeQuery(query);   // Ottengo il ResultSet dell'esecuzione della query
	        ResultSetMetaData rsmd = rs.getMetaData();
	        colonne = rsmd.getColumnCount();
	        boolean first = true;
	        while(rs.next()) {
	        	json_tmp = new JSONObject();
	        	json_tmp.put("123", rs.getString(1));
	        	json_tmp.put("qwe", rs.getString(2));
	        	json_tmp.put("text", rs.getString(3));
	        	json_tmp.put("asd", rs.getString(4));
	        	json_tmp.put("tipo", "piano");
	        	json_tmp.put("parentNode", idPiano);
	        	json_tmp.put("leaf", "false");
	        	json_tmp.put("depth", 2);
	        	json_tmp.put("childNodes", getAree(rs.getString(1)));
	        	
	        	json_array.put(json_tmp);
	        	
	        }
	        rs.close();     // Chiudo il ResultSet
	        stmt.close();   // Chiudo lo Statement
     	} catch (Exception e) { e.printStackTrace(); e.getMessage(); }
		db.disconnect();
		return json_array;
	}
	
	private JSONArray getTavoli(String idArea){
		DBConnection db = new DBConnection();
		JSONArray json_array = new JSONArray();
		JSONObject json_tmp = new JSONObject();
		db.connect();
		String query = "SELECT * FROM tavolo WHERE idArea = '"+idArea+"';";
		/**/
		int colonne;
		try {
			Statement stmt = db.getConnection().createStatement();     // Creo lo Statement per l'esecuzione della query
	        ResultSet rs = stmt.executeQuery(query);   // Ottengo il ResultSet dell'esecuzione della query
	        ResultSetMetaData rsmd = rs.getMetaData();
	        colonne = rsmd.getColumnCount();
	        boolean first = true;
	        while(rs.next()) {
	        	json_tmp = new JSONObject();
	        	json_tmp.put("123", rs.getString(1));
	        	json_tmp.put("qwe", rs.getString(2));
	        	json_tmp.put("text", rs.getString(3));
	        	json_tmp.put("asd", rs.getString(4));
	        	json_tmp.put("tipo", "piano");
	        	json_tmp.put("parentNode", idArea);
	        	json_tmp.put("leaf", "true");
	        	json_tmp.put("depth", 3);
	        	
	        	json_array.put(json_tmp);
	        	
	        }
	        rs.close();     // Chiudo il ResultSet
	        stmt.close();   // Chiudo lo Statement
     	} catch (Exception e) { e.printStackTrace(); e.getMessage(); }
		db.disconnect();
		return json_array;
	}

}