package com.orb.gestioneOggetti;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
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
			
			if(password.length() < 8){
				throw new DatabaseException("Password troppo breve! La password deve esser di almeno 8 caratteri.)");
			}//altri eventuali controlli
			
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
		} catch (DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'inserimento dell'utente (" + e.toString() +")");
		}
	}

	/**
	 * Modifica un'area a partire dal suo id
	 * @param idUtentePersonale
	 * @param nome 
	 * @param cognome
	 * @param username
	 * @param password
	 * @param isCameriere
	 * @param isCassiere
	 * @param isCucina
	 * @param isAdmin
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'errore verificatosi
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
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
		
			if(password == null || password.length() == 0){	
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
			
			utentePersonale.setNome(nome);
			utentePersonale.setCognome(cognome);
			utentePersonale.setUsername(username);
			utentePersonale.setCameriere(isCameriere);
			utentePersonale.setCassiere(isCassiere);
			utentePersonale.setCucina(isCucina);
			utentePersonale.setAdmin(isAdmin);

			
			
			return new WrapperUtentePersonale(utentePersonale);
		} catch (DatabaseException e) {
			throw e;
		}catch(Exception e) {
			throw new DatabaseException("Errore durante la modifica dell'utente (" +e.toString() +")");
		}
	 }
	 
	 /**
	  * Elimina un utente a partire dall'id.
	  * @param idUtentePersonale id dell'utente da eliminare
	  * @throws DatabaseException Eccezione che incapsula le informazioni sull'errore che si è verificato
	  */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteUtentePersonale(int idUtentePersonale) throws DatabaseException {
		 try {
			 UtentePersonale utentePersonale = em.find(UtentePersonale.class, idUtentePersonale);
			 if(utentePersonale == null)
				 throw new DatabaseException("Errore durante la ricerca dell'utente da rimuovere");
//			 em.remove(utentePersonale);
			 utentePersonale.setRemoved(true);
		 } catch (DatabaseException e) {
				throw e;
		 } catch (Exception e) {
			 throw new DatabaseException("Errore durante l'eliminazione dell'utente ("+ e.toString() +")");
		 }
	  }
		
	 
	/** 
	 * Ritorna l'elenco di tutti gli utenti appartenenti ad un cliente 
	 * @param idTenant Id del cliente
	 * @param removed "false" per restituire gli utenti personale non eliminati dal DB
	 * @return Oggetto WrapperUtentePersonale che rappresenta un utente
	 * @throws DatabaseException Eccezione che incapsula le informazioni
	 * sull'ultimo errore verificatosi
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<WrapperUtentePersonale> getUtentePersonaleTenant(int idTenant, boolean removed) throws DatabaseException {
		try {
			
			Query query = em.createQuery(	"SELECT a FROM UtentePersonale a " +
											"WHERE a.idTenant = :idTenant " +
											"AND a.removed = :removed ");
			
			query.setParameter("idTenant", idTenant);
			query.setParameter("removed", removed);
			
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
	 * Ritorna i dati sull'utente a parire dall'id
	 * @param idUtentePersonale Id dell'utente
	 * @return Oggetto WrapperUtentePersonale che rappresenta l'utente trovato
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo errore
	 * verificatosi
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public WrapperUtentePersonale getUtentePersonaleById(int idUtentePersonale) throws DatabaseException {
		
		try {
			UtentePersonale utentePersonale = em.find(UtentePersonale.class, idUtentePersonale);
			if(utentePersonale == null)
				throw new DatabaseException("Impossibile trovare l'utente");
			return new WrapperUtentePersonale(utentePersonale);
		} catch (DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Errore durante la ricerca dell'utente (" + e.toString() + ")");
		}
	}

	
}