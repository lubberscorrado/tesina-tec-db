package com.orb.gestioneOggetti;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.exceptions.DatabaseException;
import com.orb.Conto;
import com.orb.StatoContoEnum;
import com.orb.StatoTavoloEnum;
import com.orb.Tavolo;
import com.orb.UtentePersonale;
import com.restaurant.TreeNodeTavolo;
import com.restaurant.WrapperConto;

@SuppressWarnings("unchecked") 
@Stateless
public class GestioneConto {
	
	@PersistenceContext(unitName="ejbrelationships")
	private EntityManager em;
	
	public GestioneConto() {};
	
	/**
	 * Aggiunge un conto al database
	 * @param idTenant Id del tenant a cui appartiene il conto
	 * @param idTavolo Id del tavolo a cui è associato il conto
	 * @param idCameriere Id del cameriere responsabile del conto
	 * @param prezzo Prezzo del conto
	 * @param timestampApertura Timestamp di apertura del conto
	 * @param timestampChiusura Timestamp di chiusura del conto
	 * @param stato Stato attuale del conto
	 * @return Oggetto WrapperConto che rappresenta il conto modificato
	 * @throws DatabaseException Eccezione che incapsula le informazioni
	 * sull'ultimo errore verificatosi
	 */
	public WrapperConto aggiungiConto(	int idTenant,
										int idTavolo,
										int idCameriere,
										BigDecimal prezzo,
										int numeroPersone, 
										Timestamp timestampApertura,
										Timestamp timestampChiusura,
										StatoContoEnum stato) throws DatabaseException {
		
		try {
							
			Tavolo tavolo = em.find(Tavolo.class, idTavolo);
			if(tavolo ==null)
				throw new DatabaseException("Errore durante la ricerca del tavolo associato al conto");
			
			UtentePersonale cameriere = em.find(UtentePersonale.class, idCameriere);
			if(cameriere == null)
				throw new DatabaseException("Errore durante la ricerca del cameriere da associare al conto");
			
			Conto conto = new Conto();
			conto.setIdTenant(idTenant);
			conto.setPrezzo(prezzo);
			conto.setTimeStampApertura(timestampApertura);
			conto.setTimeStampChiusura(timestampChiusura);
			conto.setStato(stato);
			conto.setTavoloAppartenenza(tavolo);
			conto.setCameriereAssociato(cameriere);
			conto.setNumeroPersone(numeroPersone);
			
			em.persist(conto);
			return new WrapperConto(conto);
			
		} catch (DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'inserimento del conto " +
										"("+ e.toString() +")" );
		}
	}
	
	/**
	 * Aggiorna le informazioni su un conto. Alcune informazioni
	 * non sono previste nella funzione di aggiornamento poichè
	 * non è necessario che siano modificate
	 * @param idConto Id del conto da modificare
	 * @param prezzo Prezzo del conto modificato
	 * @param timestampChiusura Timestamp di chisura del conto
	 * @param stato Stato del conto modificato
	 * @return Oggetto WrapperConto che rappresenta il conto
	 * modificato
	 * @throws DatabaseException Eccezione che incapsula le 
	 * informazioni sull'ultimo errore verificatosi
	 */
	
	public WrapperConto updateConto(int idConto,
									BigDecimal prezzo, 
									Timestamp timestampChiusura, 
									StatoContoEnum stato) throws DatabaseException {
		
		try {
			
			Conto conto = em.find(Conto.class, idConto);
			if(conto == null)
				throw new DatabaseException("Errore durante la ricerca del conto  " + 
											"(conto non esistente)");
			conto.setPrezzo(prezzo);
			conto.setTimeStampChiusura(timestampChiusura);
			conto.setStato(stato);
			em.persist(conto);
		
			return new WrapperConto(conto);
			
		} catch (DatabaseException e) {
			throw e;
		} catch(Exception e) {
			throw new DatabaseException("Errore durante l'aggiornamento del conto (" + 
										e.toString() + ")");
		}
	}
	
	/**
	 * Ritorna la lista dei conti in stato APERTO associati ad un tavolo
	 * @param idTavolo Id del tavolo per il quale si vuole ottenere la lista
	 * @return Lista di oggetti WrapperConto
	 * @throws DatabaseException Eccezione che incapsula le informazioni
	 * sull'ultimo errore verificatosi
	 */
	public List<WrapperConto> getContiAperti(int idTavolo) throws DatabaseException {
	
		try {
			
			Query query = em.createQuery(	"SELECT c FROM Tavolo t JOIN t.conti c WHERE " +
											"c.stato = 'APERTO' AND t.idTavolo = :idTavolo");
			
			query.setParameter("idTavolo", idTavolo);
			List<WrapperConto> listConti = new ArrayList<WrapperConto>();

			for(Conto conto :  ((List<Conto>) query.getResultList())) 
				listConti.add(new WrapperConto(conto));
			
			return listConti;
					
		}catch(Exception e) {
			throw new DatabaseException(	"Errore durante la ricerca dei conti aperti " +
											" associati al tavolo (" + e.toString() +")");
		}
	}
	
	
	/**
	 * Ritorna la lista dei conti associati ad un tavolo
	 * @param idTavolo Id del tavolo per il quale si vuole ottenere la lista
	 * @return Lista di oggetti WrapperConto
	 * @throws DatabaseException Eccezione che incapsula le informazioni
	 * sull'ultimo errore verificatosi
	 */
	public List<WrapperConto> getConti(int idTavolo) throws DatabaseException {
	
		try {
			
			Query query = em.createQuery(	"SELECT c FROM Tavolo t JOIN t.conti c WHERE t.idTavolo = :idTavolo ORDER BY c.idConto DESC");
			
			query.setParameter("idTavolo", idTavolo);
			List<WrapperConto> listConti = new ArrayList<WrapperConto>();
			
			for(Conto conto :  ((List<Conto>) query.getResultList())) 
				listConti.add(new WrapperConto(conto));
			
			return listConti;
					
		}catch(Exception e) {
			throw new DatabaseException(	"Errore durante la ricerca dei conti aperti " +
											" associati al tavolo (" + e.toString() +")");
		}
	}
	
	
	public WrapperConto getContoById(int idConto) throws DatabaseException {
		
		try {
			
			Query query = em.createQuery(	"SELECT c FROM Conto c WHERE c.idConto = :idConto");
			query.setParameter("idConto", idConto);
			List<Conto> listConto = query.getResultList();
			WrapperConto wrapperConto = new WrapperConto(listConto.get(0));
			
			return wrapperConto;
					
		}catch(Exception e) {
			throw new DatabaseException(	"Errore durante la ricerca dei conti aperti " +
											" associati al tavolo (" + e.toString() +")");
		}
	}
	
	/**
	 * Permette di ottenere lo storico dei conti di un Tenant con funzionalità di paging.
	 * @param idTenant
	 * @param start
	 * @param limit
	 * @return
	 * @throws DatabaseException
	 */
	public List<WrapperConto> getContoByIdTenant(int idTenant, int start, int limit) throws DatabaseException {

		try {
			Query query = em.createQuery(	"SELECT c FROM Conto c WHERE c.idTenant = :idTenant ORDER BY idConto DESC");//ORDER BY timeStampChiusura DESC
			query.setFirstResult(start);
			query.setMaxResults(limit);
			query.setParameter("idTenant", idTenant);
			List<WrapperConto> listConti = new ArrayList<WrapperConto>();
			
			for(Conto conto :  ((List<Conto>) query.getResultList())) 
				listConti.add(new WrapperConto(conto));
			
			return listConti;
					
		}catch(Exception e) {
			throw new DatabaseException(	"Errore durante la ricerca dei conti del tenant richiesto (" + e.toString() +")");
		}
	}
	
	/**
	 * Ritorna il numero totali di conti appartenenti ad un Tenant.
	 * @param idTenant
	 * @return
	 * @throws DatabaseException
	 */
	public Long getNumContiByIdTenant(int idTenant) throws DatabaseException {
		try {
			
			Query query = em.createQuery(	"SELECT COUNT(*) FROM Conto c WHERE c.idTenant = :idTenant");
			query.setParameter("idTenant", idTenant);
			int results = 0;
			return (Long) query.getSingleResult();
					
		}catch(Exception e) {
			throw new DatabaseException(	"Errore durante il calcolo del numero dei conti del tenant (" + e.toString() +")");
		}
	}
	
	/**
	 * Permette di settare come CHIUSO lo stato di un conto.
	 * @param idConto
	 * @return
	 * @throws DatabaseException
	 */
	public boolean chiudiContoById(int idConto) throws DatabaseException {
		try {
			
			Query query = em.createQuery(	"SELECT c FROM Conto c WHERE c.idConto = :idConto");
			query.setParameter("idConto", idConto);
			List<Conto> listConto = query.getResultList();
			
			Conto conto = listConto.get(0);
			Tavolo tavolo =	conto.getTavoloAppartenenza();
			
			StatoContoEnum statoConto = conto.getStato();
			StatoTavoloEnum statoTavolo = tavolo.getStato();
			
			if(statoConto == StatoContoEnum.APERTO && ( statoTavolo == StatoTavoloEnum.OCCUPATO /*|| statoTavolo == StatoTavoloEnum.ASSEGNATO*/)){
				conto.setStato(StatoContoEnum.CHIUSO);
				tavolo.setStato(StatoTavoloEnum.PULIRE);
				/*CREDO SERVA TRANSAZIONE*/
				em.persist(conto);
				em.persist(tavolo);
				/*FINE CREDO SERVA TRANSAZIONE*/
				return true;
			}else if(statoConto == StatoContoEnum.DAPAGARE){
				conto.setStato(StatoContoEnum.CHIUSO);
				em.persist(conto);
				return true;
			}
			
			return false;
					
		}catch(Exception e) {
			throw new DatabaseException(	"Errore durante la ricerca dei conti aperti " +
											" associati al tavolo (" + e.toString() +")");
		}

	}
	

}