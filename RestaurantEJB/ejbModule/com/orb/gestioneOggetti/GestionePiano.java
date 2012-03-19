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
import com.orb.StatoTavoloEnum;
import com.orb.Tavolo;
import com.restaurant.TreeNodePiano;
import com.restaurant.TreeNodeTavolo;

@SuppressWarnings("unchecked")

@Stateless
public class GestionePiano {

	@PersistenceContext(unitName="ejbrelationships") 
	private EntityManager em;
	
	
	public GestionePiano() {}
	
	/**
	 * Aggiunge un piano nel database
	 * @param idTenant Id del client a cui appartiene il piano
	 * @param numero Numero di piano da inserire
	 * @param nome Nome del piano
	 * @param descrizione Descrizione del piano
	 * @param enabled Stato di attivazione del piano
	 * @return Oggetto TreeNodePiano che incapsula le informazioni sul piano per la
	 * presentation logic
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'errore che si è 
	 * verificato durante l'inserimento
	 */
	public TreeNodePiano aggiungiPiano(	int idTenant, 
										int numero, 
										String nome, 
										String descrizione, 
										boolean enabled) throws DatabaseException {
			
		Piano piano = new Piano();
		piano.setIdTenant(idTenant);
		piano.setNumero(numero);
		piano.setNome(nome);
		piano.setDescrizione(descrizione);
		piano.setEnabled(enabled);
		
		try {
			em.persist(piano);
		}catch(Exception e) {
			throw new DatabaseException("Errore durante l'inserimento del piano " +
										"(" + e.getMessage() + ")");
		}
		
		return new TreeNodePiano(piano);
	}
	
	/**
	 * Modifica un piano a partire dal suo id
	 * @param idPiano Id del piano da modificare
	 * @param nome Nome del piano modificato
	 * @param descrizione Descrizione del piano modificato
	 * @param numero Numero del piano modificato
	 * @param enabled Stato del piano modficato (attivato o disattivato)
	 * @return Oggetto TreeNodePiano che rappresenta il piano modificato
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'errore verificatosi
	 */
	
	public TreeNodePiano updatePiano(	int idPiano,
										String nome,
										String descrizione,
										int numero,
										boolean enabled) throws DatabaseException {
		try {
			
			Piano piano = em.find(Piano.class, idPiano);
			if(piano == null)
				throw new DatabaseException("Errore durante la ricerca del piano da aggiornare");
			
			piano.setNome(nome);
			piano.setDescrizione(descrizione);
			piano.setEnabled(enabled);
			piano.setNumero(numero);
			
			return new TreeNodePiano(piano);
			
		} catch (Exception e) {
			throw new DatabaseException("Errore durante la ricerca del piano da aggiornare (" + e.toString() +")");
			
		}
	}
	
	/**
	 * Elimina un piano dal database
	 * @param idPiano Id del piano da eliminare
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'errore verificatosi
	 */
	
	public void deletePiano(int idPiano) throws DatabaseException {
		
		try {
			Piano piano = em.find(Piano.class, idPiano);
			if(piano == null)
				throw new DatabaseException("Errore durante la ricerca del piano da eliminare");
			
//			em.remove(piano);
			piano.setRemoved(true);
		} catch (Exception e) {
			throw new DatabaseException("Errore durante la cancellazione del piano ("+ e.toString() + ")");
			
		}
	}
	
	/**
	 * Ritorna i piani appartenenti ad un cliente
	 * @param idTenant Id del cliente
	 * @param removed "true" per tornare i piani rimossi, "false" per tornare i piani non rimossi
	 * @return Lista di oggetti TreeNodePiano che incapsulano le informazioni
	 * sui piani.
	 * @throws DatabaseException 
	 */
	
	public List<TreeNodePiano> getPiani(int idTenant, boolean removed) throws DatabaseException {
		
		try {
			Query query = em.createNamedQuery("getPiani");
			query.setParameter("idTenant", idTenant);
			query.setParameter("removed", removed);
			
			List<Piano> listaPiano = (List<Piano>)query.getResultList();
			List<TreeNodePiano> listaTreeNodePiano = new ArrayList<TreeNodePiano>();
		
			for(Piano piano : listaPiano) 
				listaTreeNodePiano.add(new TreeNodePiano(piano));
					
			return listaTreeNodePiano;
		
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'acquisizione dei piani (" + e.toString() + ")");
		}
		
	}

}
