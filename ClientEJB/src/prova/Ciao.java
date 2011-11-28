package prova;


import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.persistence.PersistenceContext;

import com.orb.Area;
import com.orb.Piano;
import com.orb.Tavolo;
import com.orb.gestioneOggetti.GestioneArea;
import com.orb.gestioneOggetti.GestionePiano;
import com.orb.gestioneOggetti.GestioneTavolo;
import com.restaurant.EntityManagerCreator;
import com.restaurant.ProvaSessionRemote;
import com.restaurant.TenantSession;


@WebServlet("/Ciao")
public class Ciao extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB
 	private GestionePiano gestionePiano;
	
	@EJB
	private GestioneArea gestioneArea;
	
	@EJB
	private GestioneTavolo gestioneTavolo;

	
    public Ciao() {
        super();
        
        System.out.println("Creating servlet");
        
        
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
//		Piano piano = gestionePiano.aggiungiPiano(5, 0, "Piano terra", "Piano terra della Gallina d'oro", true);
//		System.out.println("id del piano è " + piano.getIdPiano());
//		Area area = gestioneArea.aggiungiArea(5, "Area cortile", "Area esterna sul cortile", true, piano);
//		Tavolo tavolo = gestioneTavolo.aggiungiTavolo(5, "Tavolo in marmo" , "Libero", "Tavolo grande", true, area);
		
		//List<Area> la = gestioneArea.getAreeTenant(3);
		response.getWriter().println("TROIE DI ALTRI TEMPI");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
