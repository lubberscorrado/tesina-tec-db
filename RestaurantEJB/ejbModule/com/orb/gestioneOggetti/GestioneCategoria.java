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
import com.orb.Categoria;
import com.restaurant.TreeNodeCategoria;

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
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
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
			categoria.setTipo(categoriaPadre.getTipo());
			
			em.persist(categoria);
			
			return new TreeNodeCategoria(categoria);
	
		} catch (DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'inserimento della categoria +" +
										"("+ e.toString() +")" );
		}
		
	}
			
	/**
	 * Modifica una categoria a partire dal suo id
	 * @param idCategoria Id della categoria da modificare
	 * @param nome Nome della categoria modificata
	 * @param descrizione Descrizione della cateogoria modificata
	 * @return Oggetto TreeNodeCategoria che rappresenta la categoria modificata
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'errore verificatosi
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	 public TreeNodeCategoria updateCategoria(	int idCategoria,
			 									String nome,
			 									String descrizione) throws DatabaseException {
			
		
		try {
			
			Categoria categoria = em.find(Categoria.class, idCategoria);
			
			if(categoria == null)
				throw new DatabaseException("Errore durante la ricerca della categoria da aggiornare");
		
			categoria.setNome(nome);
			categoria.setDescrizione(descrizione);
			
			return new TreeNodeCategoria(categoria);
			
		} catch (DatabaseException e) {
			throw e;
		}catch(Exception e) {
			throw new DatabaseException("Errore durante la modifica della categoria (" +e.toString() +")");
		}
	 }
	 
	 /**
	  * Elimina una categoria a partire dall'id
	  * @param idCategoria Id della categoria da elimiare
	  * @throws DatabaseException Eccezione che incapsula le informazioni sull'errore che si è verificato
	  */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	 public void deleteCategoria(int idCategoria) throws DatabaseException {
		 try {
			 Categoria categoria = em.find(Categoria.class, idCategoria);
			 if(categoria == null)
				 throw new DatabaseException("Errore durante la ricerca della categoria da eliminare");
//			 em.remove(categoria);
			 categoria.setRemoved(true);
		
		 } catch (DatabaseException e) {
				throw e;
		 } catch (Exception e) {
			 throw new DatabaseException("Errore durante l'eliminazione della categoria ("+ e.toString() +")");
		 }
	  }
	 
	 
	
	/**
	 * Ritorna una lista delle categorie appartenenti ad un cliente e figlie di una determinata
	 * categoria padre.
	 * @param idTenant Id del cliente a cui appartengono le categorie. Necessario poichè alcune
	 * categorie sono condivise.
	 * @param idPadre Id della categoria padre
	 * @param removed True per restituire le categorie rimosse (flag removed=true), False per restituire le categorie non rimosse (flag removed=false)
	 * @return Lista di oggetti TreeNodeCategoria che rappresentano le categorie ottenute dal 
	 * Database
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo errore 
	 * verificatosi
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<TreeNodeCategoria> getCategorie(int idTenant, int idPadre, boolean removed) 
												throws DatabaseException {
		
		try {
			
			if(idPadre <= 0) {
				/* Ritorna una lista vuota, nessun elemento può avere id <= 0 */
				return new ArrayList<TreeNodeCategoria>();
			}
			
			/* Acquisisco le categorie figlie dei nodi radici comuni limitando la selezione
			 * al cliente di interesse  */
			Query query= em.createNamedQuery("getCategorieFiglieDi");
			query.setParameter("idCategoriaPadre", idPadre);
			query.setParameter("idTenant", idTenant);
			query.setParameter("removed", removed);
						
			List<Categoria> listaCategoria = (List<Categoria>)query.getResultList();
			List<TreeNodeCategoria> listaTreeNodeCategoria= new ArrayList<TreeNodeCategoria>();
			
			for(Categoria categoria : listaCategoria) {
				TreeNodeCategoria temp = new TreeNodeCategoria(categoria);
				temp.setIdCategoriaPadre(idPadre);
				listaTreeNodeCategoria.add(temp);
			}
	
			return listaTreeNodeCategoria;
			
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'acquisizione delle categorie " +
										"(" + e.toString() + ")");
		}
	}
	
	/**
	 * Ritorna una categoria a partire dal suo id
	 * @param idCategoria Id della categoria da cercare
	 * @return Oggetto TreeNodeCategoria che rappresenta la categoria trovata
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo errore che si
	 * è verificato
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TreeNodeCategoria getCategoriaById(int idCategoria) throws DatabaseException {
		
		try {
			Categoria categoria = em.find(Categoria.class, idCategoria);
			if(categoria == null)
				throw new DatabaseException("Errore durante la ricerca della categoria");
			
			TreeNodeCategoria treeNodeCategoria = new TreeNodeCategoria(categoria);
			
			/* *****************************************************
			 * Ricerco l'id della categoria padre. Se la categoria
			 * padre è null significa che è una categoria radice.
			 ******************************************************/
			
			Categoria categoriaPadre = categoria.getCategoriaPadre();
			
			if(categoriaPadre == null)
				treeNodeCategoria.setIdCategoriaPadre(0);
			else
				treeNodeCategoria.setIdCategoriaPadre(categoriaPadre.getIdCategoria());
			
			return treeNodeCategoria;
	
		} catch (DatabaseException e) {
			throw e;
		}catch(Exception e) {
			throw new DatabaseException("Errore durante la ricerca della categoria (" + e.toString() + ")");
		}
	}
}

