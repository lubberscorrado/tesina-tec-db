package com.orb.gestioneOggetti;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.exceptions.DatabaseException;
import com.orb.Area;

import com.orb.Piano;
import com.restaurant.TreeNodeArea;


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
								int idPiano) throws DatabaseException {
		
		Area area = new Area();
		area.setIdTenant(idTenant);
		area.setNome(nome);
		area.setDescrizione(descrizione);
		area.setEnabled(enabled);
		
		Piano piano = em.find(Piano.class, idPiano);
				
		if(piano == null)
			throw new DatabaseException("Impossibile trovare il piano di appartenenza dell'area");
		
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
	
	/** 
	 * Ritorna la lista delle aree associate ad un determinato piano
	 * @param idPiano id del piano del quale si vuole ottenere la lista delle aree
	 * @return Lista di oggetti TreeNodeArea che incapsulano di dati di un area
	 * @throws DatabaseException Eccezione di errore durante l'accesso al database
	 */
	
	public List<TreeNodeArea> getAreeByPiano(int idPiano) throws DatabaseException {
		
		Piano piano = em.find(Piano.class, idPiano);
		
		if(piano == null)
			throw new DatabaseException("Impossibile trovare il piano");
		
		List<Area> listaAree;
		
		try {
			listaAree = piano.getAree();
		} catch(Exception e) {
			throw new DatabaseException("Impossibile ottenere le aree associate al piano " +
										"(" + e.toString()+")");
		}
		
		List<TreeNodeArea> listaTreeNodeArea = new ArrayList<TreeNodeArea>();
		Iterator<Area> it = listaAree.iterator();
		
		while(it.hasNext()) {
			
			Area area = it.next();
			listaTreeNodeArea.add(new TreeNodeArea( area.getIdArea(), 
													area.getIdTenant(), 
													area.getNome(), 
													area.getDescrizione(), 
													area.isEnabled()));
		}
		
		return listaTreeNodeArea;
	}
	
	
}