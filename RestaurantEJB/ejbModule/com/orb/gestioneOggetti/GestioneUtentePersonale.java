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
import com.orb.UtentePersonale;

import com.orb.Piano;
import com.restaurant.TreeNodeArea;
import com.restaurant.TreeNodeTavolo;
import com.restaurant.WrapperUtentePersonale;

@SuppressWarnings("unchecked") 
@Stateless
public class GestioneUtentePersonale{

	@PersistenceContext(unitName="ejbrelationships") 
	private EntityManager em;
	
	public GestioneUtentePersonale() {}
	
	/**
	 * Aggiunge un nuovo utente del personale per un determinato cliente
	 * @param idTenant Id del cliente a cui appartiene l'area
	 * @param nome 
	 * @param cognome
	 * @param username
	 * @param password
	 * @param isCameriere
	 * @param isCassiere
	 * @param isCucina
	 * @param isAdmin
	 * @return Oggetto WrapperUtentePersonale che rappresenta il nuovo utente inserito
	 * @throws DatabaseException Eccezione che incapsula le informazioni
	 * sull'ultimo errore verificatosi
	 */
	public WrapperUtentePersonale aggiungiUtentePersonale(	int idTenant, 
															String nome,
															String cognome,
															String username,
															String password,
															boolean isCameriere,
															boolean isCassiere,
															boolean isCucina,
															boolean isAdmin
															) throws DatabaseException {
		try {
			UtentePersonale utentePersonale = new UtentePersonale();
			
			utentePersonale.setIdTenant(idTenant);
//			utentePersonale.setIdUtente(idUtente)
			utentePersonale.setNome(nome);
			utentePersonale.setCognome(cognome);
			
			utentePersonale.setUsername(username);
			utentePersonale.setPassword(password);
			
			utentePersonale.setAdmin(isAdmin);
			utentePersonale.setCameriere(isCameriere);
			utentePersonale.setCassiere(isCassiere);
			utentePersonale.setCucina(isCucina);
			
			em.persist(utentePersonale);
			
			return new WrapperUtentePersonale(utentePersonale);
		
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'inserimento dell'utente (" + e.toString() +")");
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
	 public WrapperUtentePersonale updateUtentePersonale(int idUtentePersonale,
														String nome,
														String cognome,
														String username,
														String password,
														boolean isCameriere,
														boolean isCassiere,
														boolean isCucina,
														boolean isAdmin
														) throws DatabaseException {
			
		try {
			
			UtentePersonale utentePersonale = em.find(UtentePersonale.class, idUtentePersonale);
			if(utentePersonale == null)
				throw new DatabaseException("Errore durante la ricerca dell'utente da aggiornare");
		
			utentePersonale.setNome(nome);
			utentePersonale.setCognome(cognome);
			utentePersonale.setUsername(username);
			utentePersonale.setCameriere(isCameriere);
			utentePersonale.setCassiere(isCassiere);
			utentePersonale.setCucina(isCucina);
			utentePersonale.setAdmin(isAdmin);

			if(password == null){	
				//La password non viene modificata
			}else{
				if(password.length() < 8){
					throw new DatabaseException("Password troppo breve! La password deve esser di almeno 8 caratteri.)");
				}//altri eventuali controlli
				else{
					//Modifico la password
					utentePersonale.setPassword(password);
				}
			}
			
			return new WrapperUtentePersonale(utentePersonale);
			
		}catch(Exception e) {
			throw new DatabaseException("Errore durante la modifica del tavolo (" +e.toString() +")");
		}
	 }
	 
	 /**
	  * Elimina un utente a partire dall'id.
	  * @param idUtentePersonale id dell'utente da eliminare
	  * @throws DatabaseException Eccezione che incapsula le informazioni sull'errore che si è verificato
	  */
	 
	 public void deleteUtentePersonale(int idUtentePersonale) throws DatabaseException {
		 try {
			 UtentePersonale utentePersonale = em.find(UtentePersonale.class, idUtentePersonale);
			 if(utentePersonale == null)
				 throw new DatabaseException("Errore durante la ricerca dell'utente da rimuovere");
			 em.remove(utentePersonale);
		 } catch (Exception e) {
			 throw new DatabaseException("Errore durante l'eliminazione dell'utente ("+ e.toString() +")");
		 }
	  }
		
	/** 
	 * Ritorna l'elenco di tutti gli utenti appartenenti ad un cliente 
	 * @param idTenant Id del cliente
	 * @return Oggetto WrapperUtentePersonale che rappresenta un utente
	 * @throws DatabaseException Eccezione che incapsula le informazioni
	 * sull'ultimo errore verificatosi
	 */
	 
	public List<WrapperUtentePersonale> getUtentePersonaleTenant(int idTenant) throws DatabaseException {
		try {
			
			Query query = em.createQuery("SELECT a FROM UtentePersonale a WHERE a.idTenant = :idTenant");
			query.setParameter("idTenant", idTenant);
			List<UtentePersonale> listaUtentePersonale = (List<UtentePersonale>)query.getResultList();
			List<WrapperUtentePersonale> listaWrapperUtentePersonale = new ArrayList<WrapperUtentePersonale>();
			
			for(UtentePersonale utentePersonale : listaUtentePersonale) 
				listaWrapperUtentePersonale.add(new WrapperUtentePersonale(utentePersonale));
					
			return listaWrapperUtentePersonale;
					
		}catch(Exception e) {
			throw new DatabaseException("Errore durante la ricerca degli utenti (" + e.toString() +")");
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
			
			/* TODO Impostare l'associazione con piano di ogni area come LAZY per evitare
			il fetch di oggetti inutili? */
			List<Area> listaAree = piano.getAree();
			List<TreeNodeArea> listaTreeNodeArea = new ArrayList<TreeNodeArea>();
			
			for(Area area : listaAree) 
				listaTreeNodeArea.add(new TreeNodeArea(area));
				
			return listaTreeNodeArea;
				
		} catch(Exception e) {
			throw new DatabaseException("Impossibile ottenere le aree associate al piano " +
										"(" + e.toString()+")");
		}
	}
}