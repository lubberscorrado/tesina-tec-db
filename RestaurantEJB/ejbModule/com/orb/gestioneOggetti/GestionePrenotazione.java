package com.orb.gestioneOggetti;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
												Tavolo tavolo) {
		
		
		Prenotazione prenotazione = new Prenotazione();
		prenotazione.setIdTenant(idTenant);
		prenotazione.setData(data);
		prenotazione.setOra(ora);
		prenotazione.setNomecliente(nomecliente);
		prenotazione.setNumpersone(numpersone);
		prenotazione.setStato(stato);
		prenotazione.setTavoloAppartenenza(tavolo);
		em.persist(prenotazione);
		return prenotazione;
		
	}
	
		
}
