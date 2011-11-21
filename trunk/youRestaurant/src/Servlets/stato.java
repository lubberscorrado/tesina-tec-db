package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import DB.DBConnection;

/**
 * Servlet implementation class stato
 */
public class stato extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public stato() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		JSONObject t0 = new JSONObject();
		JSONObject t1 = new JSONObject();
		JSONObject t2 = new JSONObject();
		JSONObject t3 = new JSONObject();
		JSONObject t4 = new JSONObject();
		
		t0.put("id_tavolo", 0);
		t0.put("nome_tavolo", 0);
		t0.put("piano", 0);
		t0.put("area", "A0");
		t0.put("num_posti", 0);
		t0.put("stato", 0);
		t0.put("cameriere", 0);
		
		t1.put("id_tavolo", 1);
		t1.put("nome_tavolo", 0);
		t1.put("piano", 0);
		t1.put("area", "A0");
		t1.put("num_posti", 0);
		t1.put("stato", 0);
		t1.put("cameriere", 0);
		
		t2.put("id_tavolo", 2);
		t2.put("nome_tavolo", 0);
		t2.put("piano", 0);
		t2.put("area", "A0");
		t2.put("num_posti", 0);
		t2.put("stato", 0);
		t2.put("cameriere", 0);
		
		t3.put("id_tavolo", 3);
		t3.put("nome_tavolo", 0);
		t3.put("piano", 0);
		t3.put("area", "A0");
		t3.put("num_posti", 0);
		t3.put("stato", 0);
		t3.put("cameriere", 0);
		
		t4.put("id_tavolo", 4);
		t4.put("nome_tavolo", 0);
		t4.put("piano", 0);
		t4.put("area", "A0");
		t4.put("num_posti", 0);
		t4.put("stato", 0);
		t4.put("cameriere", 0);
	
		
		
		JSONArray array = new JSONArray();
		array.put(t0);	array.put(t1);	array.put(t2);	array.put(t3);	array.put(t4);
		JSONObject send = new JSONObject();
		send.put("items",array);
		
		response.getWriter().println(send);
		*/
		
		DBConnection db = new DBConnection();
		db.connect();
		
		String query = "SELECT * FROM statoTavolo;";
		JSONObject json_out = db.executeJSONQuery(query,"items");
		response.getWriter().println(	json_out	);
		
		db.disconnect();
	}

}
