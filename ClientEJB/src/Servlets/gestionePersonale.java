package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import Utilita.JSONResponse;

/**
 * Servlet implementation class gestionePersonale
 */
@WebServlet("/gestionePersonale")
public class gestionePersonale extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public gestionePersonale() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray json_array = new JSONArray();
		JSONObject json_tmp = null;
		// fields: ['id','username','passwd','nome','cognome','isCameriere','isCassiere','isCucina','isAdmin'],
		json_tmp = new JSONObject();
		json_tmp.put("id", 1);
		json_tmp.put("username", "kissalino");
		json_tmp.put("passwd", "asd");
		json_tmp.put("nome", "gino");
		json_tmp.put("cognome", "capra");
		json_tmp.put("isCameriere", true);
		json_tmp.put("isCassiere", true);
		json_tmp.put("isCucina", true);
		json_tmp.put("isAdmin", true);
//		json_tmp.put("isCameriere", "on");
//		json_tmp.put("isCassiere", "on");
//		json_tmp.put("isCucina", "on");
//		json_tmp.put("isAdmin", "on");
		json_array.put(json_tmp);
		
		
		JSONResponse.WriteOutput(response, true, "Caricamento effettuato correttamente.", "data", json_array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
