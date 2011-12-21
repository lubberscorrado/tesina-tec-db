package Servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.security.*;

import com.Ostermiller.util.RandPass;
import com.business.GestioneOrdinazioni;
import com.exceptions.DatabaseException;
import com.orb.gestioneOggetti.GestioneTenant;
import com.restaurant.WrapperTenant;
import com.sun.net.ssl.internal.ssl.*;
import Mail.SendMail;
import Utilita.JSONResponse;

/**
 * Servlet implementation class registrazione
 */
@WebServlet("/registrazione")
public class registrazione extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB	private GestioneTenant gestioneTenant;
       
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
		String nomeLocale;
		String cap;
		int CAP;
		String citta;
		String email;
		String fax;
		String indirizzo;
		String nazione;
		String civico;
		int numCivico;
		String piva;
		String provincia;
		String ragioneSociale;
		String url;
		String telefono;
		String superadmin;
		String passwd;
				
		try {
			
			nomeLocale = request.getParameter("nomeLocale");
			cap = request.getParameter("cap");
			CAP = Integer.valueOf(cap);
			citta = request.getParameter("citta");
			email = request.getParameter("email");
			fax = request.getParameter("fax");
			indirizzo = request.getParameter("indirizzo");
			nazione = request.getParameter("nazione");
			civico = request.getParameter("numCivico");
			numCivico = Integer.valueOf(civico);
			piva = request.getParameter("piva");
			provincia = request.getParameter("provincia");
			ragioneSociale = request.getParameter("ragioneSociale");
			url = request.getParameter("sitoWeb");
			telefono = request.getParameter("telefono");
			superadmin = request.getParameter("username");
			
			//GENERARE LA PASSWORD RANDOM
			char[] passwordAlphabet = RandPass.NUMBERS_AND_LETTERS_ALPHABET;
			passwd = new RandPass(passwordAlphabet).getPass(12);		//generazione password randomica
			
		} catch (Exception e){
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, "Possibile errore nei parametri.");
			return;
		}
		
		
		
		try{
			WrapperTenant wrapperTenant = gestioneTenant.aggiungiTenant(ragioneSociale, piva, indirizzo, numCivico, CAP, citta, provincia, nazione, telefono, fax, email, url, superadmin,passwd);
			
			String[] sendTo = { email };
			String emailFromAddress = "YouRestaurant";
			String emailSubjectTxt = 	"Registrazione al servizio youRestaurant";
			String emailMsgTxt = 		"Grazie per avere scelto il nostro servizio!\n\n" +
										"Qui di seguito saranno fornite le tue credenziali di accesso all'area di amministrazione.\n\n" +
										"Codice ristorante: "+wrapperTenant.getIdTenant()+"\n" +
										"Username:\t\t"+wrapperTenant.getSuperadmin()+"\n" +
										"Password:\t\t"+wrapperTenant.getPasswd()+"\n\n" +
										"Mandaci i tuoi feedback per un servizio sempre migliore!";
		
		
			
			
			
			
			
			
		} catch (DatabaseException e) {
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, e.toString());
			return;
		} catch (Exception e){
			e.printStackTrace();
			JSONResponse.WriteOutput(response, false, "Errore inserimento.");
			return;
		}
		
		
		
		

		
		

//		try {
//			SendMail.sendSSLMessage(sendTo, emailSubjectTxt,emailMsgTxt, emailFromAddress);
//		} catch (MessagingException e) {
//			System.out.println("Exception");
//			e.printStackTrace();
//		}
//		System.out.println("Sucessfully Sent mail to All Users");
//		System.out.println("MAIL INVIATA");
		
		
		
		
		String message = "Registrazione effettuata correttamente\n" +
				"Ti sarà inviata al più presto una mail con i tuoi dati di accesso.";
		JSONResponse.WriteOutput(response, true, message);
	}

}
