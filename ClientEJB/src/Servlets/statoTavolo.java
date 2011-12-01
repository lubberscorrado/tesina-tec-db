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

import com.restaurant.StatoTavolo;

import DB.DBConnection;

/**
 * Servlet implementation class stato
 */
@WebServlet("/statoTavolo")
public class statoTavolo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public statoTavolo(){}

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	JSONArray json_array_statoTavolo = new JSONArray();

    	//AGGIUNGERE GLI STATI DEI TAVOLI
    	
    	JSONObject json_out = new JSONObject();
    	json_out.put("statoTavolo", json_array_statoTavolo);
    	json_out.put("success",true);
    	json_out.put("message","OK");
    	response.getWriter().println(json_out);
	}

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		System.out.println("POST");
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
