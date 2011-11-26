package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class registrazione
 */
@WebServlet("/registrazione")
public class registrazione extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public registrazione() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cap = request.getParameter("cap");
		String citta = request.getParameter("citta");
		String email = request.getParameter("email");
		String fax = request.getParameter("fax");
		String indirizzo = request.getParameter("indirizzo");
		String nazione = request.getParameter("nazione");
		String numCivico = request.getParameter("numCivico");
		String piva = request.getParameter("piva");
		String provincia = request.getParameter("provincia");
		String ragioneSociale = request.getParameter("ragioneSociale");
		String sitoWeb = request.getParameter("sitoWeb");
		String telefono = request.getParameter("telefono");
		String username = request.getParameter("username");
		
		JSONObject json_out = new JSONObject();
		json_out.put("success", true);
		json_out.put("message", "Registrazione effettuata correttamente\n" +
				"Ti sarà inviata al più presto una mail con i tuoi dati di accesso.");
		
		response.getWriter().println(json_out);
	}

}
