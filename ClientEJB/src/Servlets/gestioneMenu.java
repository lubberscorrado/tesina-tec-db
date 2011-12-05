package Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Utilita.JSONResponse;



/**
 * Servlet implementation class gestioneMenu
 */
@WebServlet("/gestioneMenu")
public class gestioneMenu extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
		//AGGIUNGERE CONTROLLO PRIVILEGI
    	
    	
    	String node = request.getParameter("node");
		if(node == null){
			JSONResponse.WriteOutput(response, false, "Richiesta non valida");	return;
		}
		
		try{
			
			
			
		}catch(Exception e){
			JSONResponse.WriteOutput(response, false, "Eccezione generale");	return;
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}
