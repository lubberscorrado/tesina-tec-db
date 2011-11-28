package Servlets;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

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

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection db = new DBConnection();
		db.connect();
		
		String query = "SELECT * FROM statoTavolo;";
		JSONObject json_out = db.executeJSONQuery(query,"items");
		response.getWriter().println(	json_out	);
		
		db.disconnect();
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		System.out.println("POST");
	
		JSONObject json_out = new JSONObject();
		json_out.put("success", true);
		json_out.put("message", "lalalala");
		
		response.getWriter().println(	json_out	);
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		System.out.println("PUT");
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		System.out.println("DELETE");
	}

	

}
