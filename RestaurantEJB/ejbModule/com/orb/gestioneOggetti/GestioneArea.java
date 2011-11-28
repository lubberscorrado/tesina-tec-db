package com.orb.gestioneOggetti;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.orb.Area;

import com.orb.Piano;


@SuppressWarnings("unchecked") 
@Stateless
public class GestioneArea{

	@PersistenceContext(unitName="ejbrelationships") 
	private EntityManager em;
	
	
	public GestioneArea() {}
	
		
	/** Ritorna l'elenco di tutte le aree appartenenti ad un cliente */
	
	public List<Area> getAreeTenant(int idTenant) {
		
		Query query = em.createQuery("SELECT a FROM Area a WHERE a.idTenant = :idTenant");
		query.setParameter("idTenant", idTenant);
		return (List<Area>)query.getResultList();
		
	}
	
	public Area aggiungiArea(	int idTenant, 
								String nome, 
								String descrizione, 
								boolean enabled,
								Piano pianoAssociato) {
		
		
		Area area = new Area();
		area.setIdTenant(idTenant);
		area.setNome(nome);
		area.setDescrizione(descrizione);
		area.setEnabled(enabled);
		
		area.setPianoAppartenenza(pianoAssociato);
		
		em.persist(area);
		
		return area;
		
	}
	
	
	public Area updateArea(Area area) {
		em.merge(area);
		return area;
		
	}
	
	
	public void deleteArea(Area area) {
		em.remove(em.merge(area));
	}
	
	
	
	
}