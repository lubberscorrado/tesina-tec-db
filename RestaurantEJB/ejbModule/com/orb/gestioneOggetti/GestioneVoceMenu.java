package com.orb.gestioneOggetti;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.exceptions.DatabaseException;
import com.orb.Area;
import com.orb.Categoria;
import com.orb.Piano;
import com.orb.VoceMenu;
import com.restaurant.TreeNodeArea;
import com.restaurant.TreeNodeCategoria;
import com.restaurant.TreeNodePiano;
import com.restaurant.TreeNodeVoceMenu;

@SuppressWarnings("unchecked") 
@Stateless
public class GestioneVoceMenu{
	
	@PersistenceContext(unitName="ejbrelationships")
	private EntityManager em;
	
	public GestioneVoceMenu() {};
	
	/**
	 * Aggiunge una voce del menu associandola a una determinata categoria
	 * @param idTenant Id del cliente a cui appartiene la voce del menu
	 * @param idCategoria Id della categoria a cui appartiene la voce del menu
	 * @param nome Nome della voce di menu
	 * @param descrizione Descrizione della voce di menu
	 * @param prezzo Prezzo della voce di menu
	 * @return Oggetto TreeNodeVoceMenu che rappresenta la voce del menu
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo errore verificatosi
	 */

	public TreeNodeVoceMenu aggiungiVoceMenu(	int idTenant,
												int idCategoria,
												String nome,
												String descrizione,
												BigDecimal prezzo) throws DatabaseException {
				
		try {
						
			Categoria categoria = em.find(Categoria.class, idCategoria);
			
			if(categoria == null)
				throw new DatabaseException("Impossibile trovare la categoria associata alla voce del menu");
						
			VoceMenu voceMenu = new VoceMenu();
			voceMenu.setCategoriaAppartenenza(categoria);
			voceMenu.setNome(nome);
			voceMenu.setIdTenant(idTenant);
			voceMenu.setDescrizione(descrizione);
			voceMenu.setPrezzo(prezzo);
			
			em.persist(voceMenu);
			
			return new TreeNodeVoceMenu(null);
		
		}catch (Exception e) {
			throw new DatabaseException("Errore durante l'inserimento della voce di menu (" + e.toString() +")");
			
		}
	}
		
	/**
	 * Ritorna tutte le voci di menu associate ad una categoria. L'id del cliente è necessario
	 * poichè le categorie radice sono comuni a tutti i clienti.
	 * @param idTenant Id del client a cui appartengono le voci del menu
	 * @param idCategoria Categoria della quale si vogliono ottenere le voci di menu
	 * @return Lista di oggetti TreeNodeVoceMenu che rappresentano le voci del menu 
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo errore verificatosi
	 */
		
	public List<TreeNodeVoceMenu> getVociMenuByCategoria(int idTenant, int idCategoria) throws DatabaseException {
			
		try {
			Query query = em.createNamedQuery("getVociMenuByCategoria");
			query.setParameter("idTenant", idTenant);
			query.setParameter("idCategoria", idCategoria);
			
			List<VoceMenu> listaVociMenu = query.getResultList();
			List<TreeNodeVoceMenu> listaTreeNodeVoceMenu = new ArrayList<TreeNodeVoceMenu>();
			
			Iterator<VoceMenu> it = listaVociMenu.iterator();
			
			while(it.hasNext()) {
				VoceMenu voceMenu = it.next();
				listaTreeNodeVoceMenu.add(new TreeNodeVoceMenu(voceMenu));
			}
			
			return listaTreeNodeVoceMenu;
			
		} catch (Exception e) {
			throw new DatabaseException("Errore durante la ricerca delle voci di menu (" + e.toString() +")");
			
		}
	}
			
}