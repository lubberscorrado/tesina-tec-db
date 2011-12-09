package com.orb.gestioneOggetti;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.exceptions.DatabaseException;
import com.orb.Area;
import com.orb.Categoria;
import com.orb.Piano;
import com.orb.Variazione;
import com.restaurant.TreeNodeArea;
import com.restaurant.TreeNodePiano;
import com.restaurant.WrapperVariazione;



@SuppressWarnings("unchecked") 
@Stateless

public class GestioneVariazioni {
	@PersistenceContext(unitName="ejbrelationships") 
	private EntityManager em;
	
	public GestioneVariazioni(){};
	
	/**
	 * Aggiunge una variazione associata ad una categoria
	 * @param idTenant Id del cliente a cui appartiene la variazione
	 * @param nome Nome della variazione
	 * @param descrizione Descrizione della variazione
	 * @param prezzo Prezzo della variazione
	 * @param idCategoria Id della categoria a cui appartiene la variazione
	 * @return Oggetto _Variazione che rappresenta la nuova variazione inserita
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo
	 * errore verificatosi
	 */
	public WrapperVariazione aggiungiVariazione( int idTenant,
											String nome,
											String descrizione,
											BigDecimal prezzo,
											int idCategoria) throws DatabaseException {
		
		try {
			Variazione variazione = new Variazione();
			variazione.setIdTenant(idTenant);
			variazione.setNome(nome);
			variazione.setDescrizone(descrizione);
			variazione.setPrezzo(prezzo);
			
			Categoria categoria = em.find(Categoria.class, idCategoria);
			if(categoria == null) 
				throw new DatabaseException("Impossibile trovare la categoria di appartenenza della variazione");
			
			variazione.setCategoriaAppartenenza(categoria);
			em.persist(variazione);
		
			return new WrapperVariazione(variazione);
		
		} catch (Exception e) {
			
			throw new DatabaseException("Errore durante l'inserimento della variazione (" + e.toString() +")");
		}
		
	}
	
	
	/**
	 * Ritorna la lista delle variazioni associate ad una categoria
	 * @param idCategoria Id della categoria a cui appartengono le variazioni
	 * @param idTenant Id del cliente a cui appartengono le variazioni (non è sufficiente
	 * l'id della categoria poichè alcune categorie sono condivise tra i vari clienti)
	 * @return Lista di oggetti WrapperVariazione che rappresentano le variazioni disponibili
	 * per la categoria
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo errore verificatosi
	 */
	
	public List<WrapperVariazione> getVariazioniByCategoria(int idCategoria, int idTenant) throws DatabaseException {
		
		Query query = em.createNamedQuery("getVariazioniByCategoria");
		query.setParameter("idCategoria", idCategoria);
		
		/* Per le variazioni non è sufficiente l'id della categoria di appartenenza,
		 * poichè le categorie radice sono condivise da tutti gli utenti */
		query.setParameter("idTenant", idTenant);
		
		List<Variazione> listaVariazione;
		
		try {
			listaVariazione = (List<Variazione>)query.getResultList();
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'acquisizione delle variazioni (" + e.toString() + ")");
		}
				
		List<WrapperVariazione> listaWrapperVariazione = new ArrayList<WrapperVariazione>();
		Iterator<Variazione> it = listaVariazione.iterator();
		
		while(it.hasNext()) {
			Variazione variazione = it.next();
			listaWrapperVariazione.add(new WrapperVariazione(variazione));
		}
		
		return listaWrapperVariazione;
	}
	
	/**
	 * Cancella una variazione a partire dal suo id
	 * @param idVariazione Id della variazione da cancellare
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo errore
	 * verificatosi
	 */
	public void deleteVariazione(int idVariazione) throws DatabaseException {
		try {
			Variazione variazione = em.find(Variazione.class, idVariazione);
			if(variazione == null)
				throw new DatabaseException("Errore durante la ricerca della variazione");
			em.remove(variazione);
		} catch (Exception e) {
			throw new DatabaseException("Errore durante la cancellazione della variazione (" + e.toString() +")");
			
		}
	}
}
