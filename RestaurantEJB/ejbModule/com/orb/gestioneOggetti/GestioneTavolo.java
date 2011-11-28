package com.orb.gestioneOggetti;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.orb.Area;
import com.orb.Piano;
import com.orb.Tavolo;

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
	
	
public List<Tavolo> getTavoliTenant(int idTenant) {
		
		Query query = em.createQuery("SELECT t FROM Tavolo t WHERE t.idTenant = :idTenant");
		query.setParameter("idTenant", idTenant);
		return (List<Tavolo>)query.getResultList();
		
	}
	
		
}