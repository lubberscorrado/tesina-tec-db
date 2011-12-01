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
import com.orb.Tavolo;
import com.restaurant.TreeNodePiano;

@SuppressWarnings("unchecked")

@Stateless
public class GestionePiano {

	@PersistenceContext(unitName="ejbrelationships") 
	private EntityManager em;
	
	
	public GestionePiano() {}
	
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
	 * Ritorna i piani associati ad un cliente
	 * @param idTenant Id del cliente
	 * @return Lista di oggetti TreeNodePiano che incapsulano le informazioni
	 * sui piani.
	 */
	
	public List<TreeNodePiano> getPiani(int idTenant) {
		
		Query query = em.createNamedQuery("getPiani");
		query.setParameter("idTenant", idTenant);
		
		List<Piano> listaPiano;
		
		try {
			listaPiano = (List<Piano>)query.getResultList();
		} catch (Exception e) {
			System.out.println("Errore durante l'acquisizione dei piani (" + e.getMessage() +")");
			return null;
		}
		
		List<TreeNodePiano> listaTreeNodePiano = new ArrayList<TreeNodePiano>();
		
		
		Iterator<Piano> it = listaPiano.iterator();
		
		while(it.hasNext()) {
			Piano piano = it.next();
			
			listaTreeNodePiano.add(new TreeNodePiano(piano));
	
		}
		
		try {
			listaPiano = (List<Piano>)query.getResultList();
		} catch (Exception e) {
			System.out.println("Errore durante l'acquisizione dei piani (GestionePiano.getPiani)" + e.getMessage());
			return null;
		}
		
		return listaTreeNodePiano;
	}

}
