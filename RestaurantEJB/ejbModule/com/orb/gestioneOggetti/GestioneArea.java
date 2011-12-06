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
import com.orb.StatoTavoloEnum;
import com.orb.Tavolo;

import com.orb.Piano;
import com.restaurant.TreeNodeArea;
import com.restaurant.TreeNodeTavolo;

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
	
	public TreeNodeArea aggiungiArea(	int idTenant, 
										String nome, 
										String descrizione, 
										boolean enabled,
										int idPiano) throws DatabaseException {
		try {
			Area area = new Area();
			area.setIdTenant(idTenant);
			area.setNome(nome);
			area.setDescrizione(descrizione);
			area.setEnabled(enabled);
		
			Piano piano = em.find(Piano.class, idPiano);
					
			if(piano == null)
				throw new DatabaseException("Impossibile trovare il piano di appartenenza dell'area");
		
			area.setPianoAppartenenza(piano);
			em.persist(area);
			
			return new TreeNodeArea(area);
		
		} catch (Exception e) {
			
			throw new DatabaseException("Errore durante l'inserimento dell'area (" + e.toString() +")");
			
		}
		
		
	}
	
	/**
	 * Modifica un'area a partire dal suo id
	 * @param idArea Id dell'area da modificare
	 * @param nome Nome dell'area modificata
	 * @param descrizione Descrizione dell'area modificata
	 * @param enabled Stato dell'area modificata
	 * @return Oggetto TreeNodeArea che rappresenta la nuova area modificata
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'errore verificatosi
	 */
	 
	 public TreeNodeArea updateArea(	int idArea,
			 							String nome,
										String descrizione,
										boolean enabled) throws DatabaseException {
			
		
		try {
			
			Area area = em.find(Area.class, idArea);
			
			if(area == null)
				throw new DatabaseException("Errore durante la ricerca dell'area da aggiornare");
		
			area.setNome(nome);
			area.setDescrizione(descrizione);
			area.setEnabled(enabled);
			
			return new TreeNodeArea(area);
			
		}catch(Exception e) {
			
			throw new DatabaseException("Errore durante la modifica del tavolo (" +e.toString() +")");
			
		}
	 }
	 
	 /**
	  * Elimina un'are a partire dall'id.
	  * @param idArea id dell'area da eliminare
	  * @throws DatabaseException Eccezione che incapsula le informazioni sull'errore che si è verificato
	  */
	 public void deleteArea(int idArea) throws DatabaseException {
		 try {
			 Area area = em.find(Area.class, idArea);
			 if(area == null)
				 throw new DatabaseException("Errore durante la ricerca dell'area da rimuovere");
			 em.remove(area);
		 } catch (Exception e) {
			 throw new DatabaseException("Errore durante l'eliminazione dell'area ("+ e.toString() +")");
		 }
	  }
	 
	/** 
	 * Ritorna la lista delle aree associate ad un determinato piano
	 * @param idPiano id del piano del quale si vuole ottenere la lista delle aree
	 * @return Lista di oggetti TreeNodeArea che incapsulano di dati di un area
	 * @throws DatabaseException Eccezione di errore durante l'accesso al database
	 */
	
	// TODO Forzare il fetch con una query FETCH per ottenere le aree di un piano in un' unica query?
	
	public List<TreeNodeArea> getAreeByPiano(int idPiano) throws DatabaseException {
	
		try {
			
			Piano piano = em.find(Piano.class, idPiano);
			if(piano == null)
				throw new DatabaseException("Impossibile trovare il piano");
			
			List<Area> listaAree = piano.getAree();
			List<TreeNodeArea> listaTreeNodeArea = new ArrayList<TreeNodeArea>();
			Iterator<Area> it = listaAree.iterator();
			
			while(it.hasNext()) {
				Area area = it.next();
				listaTreeNodeArea.add(new TreeNodeArea(area));
			}
			
			return listaTreeNodeArea;
				
		} catch(Exception e) {
			throw new DatabaseException("Impossibile ottenere le aree associate al piano " +
										"(" + e.toString()+")");
		}
	}
}