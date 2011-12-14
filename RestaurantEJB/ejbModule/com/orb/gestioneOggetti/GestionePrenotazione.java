package com.orb.gestioneOggetti;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sun.org.mozilla.javascript.internal.ast.ThrowStatement;

import com.exceptions.DatabaseException;
import com.orb.Area;
import com.orb.Piano;
import com.orb.Prenotazione;
import com.orb.Tavolo;

@Stateless
public class GestionePrenotazione {

	@PersistenceContext(unitName="ejbrelationships") 
	private EntityManager em;
		
	public GestionePrenotazione() {}
	
	public Prenotazione aggiungiPrenotazione(	int idTenant, 
												Date data, 
												String ora,
												String nomecliente,
												int numpersone,
												String stato,
												int idTavolo) throws DatabaseException {
		
		Prenotazione prenotazione = new Prenotazione();
		prenotazione.setIdTenant(idTenant);
		prenotazione.setData(data);
		prenotazione.setOra(ora);
		prenotazione.setNomecliente(nomecliente);
		prenotazione.setNumpersone(numpersone);
		prenotazione.setStato(stato);
		
		Tavolo tavolo = em.find(Tavolo.class, idTavolo);
		
		if(tavolo == null)
			throw new DatabaseException("Impossibile trovare il tavolo associato alla prenotazione");
		
		prenotazione.setTavoloAppartenenza(tavolo);
		
		try {
			em.persist(prenotazione);
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'inserimento di una prenotazione");
		}
		return prenotazione;
		
	}
	
		
}
