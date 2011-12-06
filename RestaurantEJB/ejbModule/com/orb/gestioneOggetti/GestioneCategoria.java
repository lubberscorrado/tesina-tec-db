package com.orb.gestioneOggetti;

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
import com.restaurant.TreeNodeArea;
import com.restaurant.TreeNodeCategoria;
import com.restaurant.TreeNodePiano;

@SuppressWarnings("unchecked") 
@Stateless
public class GestioneCategoria {
	
	@PersistenceContext(unitName="ejbrelationships")
	private EntityManager em;
	
	public GestioneCategoria() {};
	
	/**
	 * Aggiunge una categoria come figlia di una categoria padre
	 * @param idTenant Id del cliente d cui appartiene la categoria
	 * @param nome Nome della categoria
	 * @param descrizione Descrizione della categoria
	 * @param idPadre Id della categoria padre
	 * @return Ogetto TreeNodeCategoria che rappresenta le informazioni sulla categoria 
	 * inserita
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'errore 
	 * verificatosi
	 */
	public TreeNodeCategoria aggiungiCategoria(	int idTenant,
												String nome,
												String descrizione,
												int idPadre) throws DatabaseException {
		
		try {
			Categoria categoriaPadre = em.find(Categoria.class, idPadre);
		
			if(categoriaPadre == null)
				throw new DatabaseException("Errore durante la ricerca della categoria padre");
			
			
			Categoria categoria = new Categoria();
			categoria.setIdTenant(idTenant);
			categoria.setNome(nome);
			categoria.setDescrizione(descrizione);
			categoria.setCategoriaPadre(categoriaPadre);
			
			em.persist(categoria);
			
			return new TreeNodeCategoria(categoria);
			
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'inserimento del piano +" +
										"("+ e.toString() +")" );
		}
		
	}
	/**
	 * Ritorna una lista delle categorie appartenenti ad un cliente e figlie di una determinata
	 * categoria padre.
	 * @param idTenant Id del cliente a cui appartengono le categorie
	 * @param idPadre Id della categoria padre
	 * @return Lista di oggetti TreeNodeCategoria che rappresentano le categorie ottenute dal 
	 * Database
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo errore 
	 * verificatosi
	 */
	
	public List<TreeNodeCategoria> getCategorie(int idTenant, int idPadre) throws DatabaseException {
		
		Query query = em.createNamedQuery("getCategorie");
		query.setParameter("idTenant", idTenant);
		query.setParameter("idCategoriaPadre", idPadre);
		
		List<Categoria> listaCategoria;
		
		try {
			listaCategoria = (List<Categoria>)query.getResultList();
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'acquisizione delle categorie " +
										"(" + e.toString() + ")");
		}
				
		List<TreeNodeCategoria> listaTreeNodeCategoria= new ArrayList<TreeNodeCategoria>();
		Iterator<Categoria> it = listaCategoria.iterator();
		
		while(it.hasNext()) {
			Categoria categoria = it.next();
			listaTreeNodeCategoria.add(new TreeNodeCategoria(categoria));
		}
		
		return listaTreeNodeCategoria;
	}
}

