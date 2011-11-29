package com.orb.gestioneOggetti;

import java.util.List;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.orb.Area;
import com.orb.Piano;
import com.orb.Prenotazione;
import com.orb.Tavolo;

@SuppressWarnings("unchecked")
@Stateless
public class GestioneTavolo{

	@PersistenceContext(unitName="ejbrelationships") 
	private EntityManager em;
		
	public GestioneTavolo() {}
	
	public Tavolo aggiungiTavolo(	int idTenant, 
									String nome, 
									String stato,
									String descrizione, 
									boolean enabled,
									Area areaAssociata) {
		
		Tavolo tavolo = new Tavolo();
		tavolo.setIdTenant(idTenant);
		tavolo.setNome(nome);
		tavolo.setDescrizione(descrizione);
		tavolo.setEnabled(enabled);
		tavolo.setStato(stato);
		tavolo.setAreaAppartenenza(areaAssociata);
		em.persist(tavolo);
		return tavolo;
		
	}
	
	/** Ritorna le prenotazioni associate ad un tavolo in un determinato stato */
	public List<Prenotazione>  getPrenotazioniAssociate(int idTavolo, String stato){
		
		Query query = em.createNamedQuery("getPrenotazioniByTavoloStato");
		query.setParameter("idTavolo", idTavolo);
		query.setParameter("stato", stato);
		
		return (List<Prenotazione>)query.getResultList();

	}
	
	
	/** Ritorna un tavolo tramite chiave primaria */
	public Tavolo getTavolo(int idTavolo){
		return em.find(Tavolo.class, idTavolo);
	}
	
	/** Ritorna la lista dei tavoli associati ad un tenant */
	public List<Tavolo> getTavoliByTenant(int idTenant) {
		
		Query query = em.createNamedQuery("getTavoli");
		query.setParameter("idTenant", idTenant);
		return (List<Tavolo>)query.getResultList();
	}

	/** Ritorna i tavoli di un tenant in un determinato stato */
	public List<Tavolo> getTavoliByStato(int idTenant, String stato) {
		Query query;
		query = em.createNamedQuery("getTavoliByStato");
		query.setParameter("stato", stato);
		query.setParameter("idTenant", idTenant);
		return (List<Tavolo>)query.getResultList();
	}
		
}