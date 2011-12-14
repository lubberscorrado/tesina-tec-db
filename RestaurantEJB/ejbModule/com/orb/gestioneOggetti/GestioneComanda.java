package com.orb.gestioneOggetti;

import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.exceptions.DatabaseException;
import com.orb.Comanda;
import com.orb.Conto;
import com.orb.StatoComandaEnum;
import com.orb.StatoContoEnum;
import com.orb.Tavolo;
import com.orb.UtentePersonale;
import com.orb.VoceMenu;
import com.restaurant.WrapperComanda;
import com.restaurant.WrapperConto;

@SuppressWarnings("unchecked") 
@Stateless
public class GestioneComanda {
	
	@PersistenceContext(unitName="ejbrelationships")
	private EntityManager em;
	
	public GestioneComanda() {};
	
	public WrapperComanda aggiungiComanda(	int idTenant,
											int idVoceMenu,
											int idConto,
											int idCameriere,
											String note,
											BigDecimal prezzo,
											int quantita,
											StatoComandaEnum stato) throws DatabaseException {
		
		try {
					
			Comanda comanda = new Comanda() ;
			comanda.setIdTenant(idTenant);
			comanda.setNote(note);
			comanda.setPrezzo(prezzo);
			comanda.setQuantita(quantita);
			comanda.setStato(stato);
			
			Conto conto = em.find(Conto.class, idConto);
			if(conto == null)
				throw new DatabaseException("Errore durante la ricerca del conto a cui appartiene la comanda");
			comanda.setContoAppartenenza(conto);
						
			VoceMenu voceMenu = em.find(VoceMenu.class, idVoceMenu);
			if(voceMenu == null)
				throw new DatabaseException("Errore durante la ricerca della voce di menu associata alla comanda");
			comanda.setVoceMenuAssociata(voceMenu);
			
			UtentePersonale cameriere = em.find(UtentePersonale.class, idCameriere);
			if(cameriere == null)
				throw new DatabaseException("Errore durante la ricerca del cameriere responsabile per la comanda");
			comanda.setCameriereAssociato(cameriere);
			
			em.persist(conto);
			return new WrapperComanda(comanda);
			
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'inserimento della comanda +" +
										"("+ e.toString() +")" );
		}
		
	}
}