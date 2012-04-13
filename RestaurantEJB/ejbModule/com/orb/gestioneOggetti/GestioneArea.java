package com.orb.gestioneOggetti;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
	
	/**
	 * Aggiunge un area per un determinato cliente
	 * @param idTenant Id del cliente a cui appartiene l'area
	 * @param nome Nome dell'area
	 * @param descrizione Descrizione dell'area
	 * @param enabled Stato dell'area, abilitata o disabilitata
	 * @param idPiano Id del piano a cui appartiene l'area
	 * @return Oggetto TreeNodeArea che rappresenta la nuova area 
	 * inserita
	 * @throws DatabaseException Eccezione che incapsula le informazioni
	 * sull'ultimo errore verificatosi
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
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
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TreeNodeArea updateArea(int idArea,
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
	  * Elimina un'area a partire dall'id.
	  * @param idArea id dell'area da eliminare
	  * @throws DatabaseException Eccezione che incapsula le informazioni sull'errore che si è verificato
	  */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteArea(int idArea) throws DatabaseException {
		 try {
			 Area area = em.find(Area.class, idArea);
			 if(area == null)
				 throw new DatabaseException("Errore durante la ricerca dell'area da rimuovere");
//			 em.remove(area);
			 area.setRemoved(true);
		 } catch (Exception e) {
			 throw new DatabaseException("Errore durante l'eliminazione dell'area ("+ e.toString() +")");
		 }
	  }
		
	/** 
	 * Ritorna l'elenco di tutte le aree appartenenti ad un cliente 
	 * @param idTenant Id del cliente
	 * @param removed "false" per ottenere le aree non eliminate dal DB
	 * @return Oggetto TreeNodeArea che rappresenta un area di un cliente
	 * @throws DatabaseException Eccezione che incapsula le informazioni
	 * sull'ultimo errore verificatosi
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS) 
	public List<TreeNodeArea> getAreeTenant(int idTenant, boolean removed) throws DatabaseException {
		try {
			
			Query query = em.createQuery("SELECT a FROM Area a " +
										 "WHERE a.idTenant = :idTenant " +
										 "AND a.removed = :removed ");
			query.setParameter("idTenant", idTenant);
			query.setParameter("removed", removed);
			
			List<Area> listaAree = (List<Area>)query.getResultList();
			List<TreeNodeArea> listaTreeNodeArea = new ArrayList<TreeNodeArea>();
			
			for(Area area : listaAree) 
				listaTreeNodeArea.add(new TreeNodeArea(area));
					
			return listaTreeNodeArea;
					
		}catch(Exception e) {
			throw new DatabaseException("Errore durante la ricerca delle aree (" + e.toString() +")");
		}
	}
	
	
	/** 
	 * Ritorna la lista delle aree associate ad un determinato piano
	 * @param idPiano id del piano del quale si vuole ottenere la lista delle aree
	 * @param removed "false" per tornare le aree non eliminate, "true" otherwise
	 * @return Lista di oggetti TreeNodeArea che incapsulano di dati di un area
	 * @throws DatabaseException Eccezione di errore durante l'accesso al database
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<TreeNodeArea> getAreeByPiano(int idPiano, boolean removed) throws DatabaseException {
	
		try {
			
			Piano piano = em.find(Piano.class, idPiano);
			if(piano == null)
				throw new DatabaseException("Impossibile trovare il piano");
			
			List<Area> listaAree = piano.getAree();
			List<TreeNodeArea> listaTreeNodeArea = new ArrayList<TreeNodeArea>();
			
			for(Area area : listaAree)  {
				if(area.getRemoved() == removed)
					listaTreeNodeArea.add(new TreeNodeArea(area));
			}
			return listaTreeNodeArea;
				
		} catch(Exception e) {
			throw new DatabaseException("Impossibile ottenere le aree associate al piano " +
										"(" + e.toString()+")");
		}
	}
}