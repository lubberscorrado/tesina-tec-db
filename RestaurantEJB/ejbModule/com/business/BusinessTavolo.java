package com.business;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.exceptions.DatabaseException;
import com.orb.StatoContoEnum;
import com.orb.StatoTavoloEnum;
import com.orb.gestioneOggetti.GestioneConto;
import com.orb.gestioneOggetti.GestioneTavolo;
import com.restaurant.TreeNodeTavolo;
import com.restaurant.WrapperConto;

@Stateless
public class BusinessTavolo {
	
	@EJB	
	private GestioneConto gestioneConto;
	@EJB
	private GestioneTavolo gestioneTavolo;
	@Resource
	private SessionContext context;
	
	/**
	 * Occupa un tavolo associandolo a un determinato cameriere.
	 * In ordine:
	 * - Verifica lo stato del tavolo
	 * - Verifica la presenza di conti aperti
	 * - Aggiunge un conto
	 * - Aggiorna lo stato del tavolo
	 * Se anche una sola delle operazioni fallisce effettua il rollback della
	 * transazione.
	 * @param idTavolo Id del tavolo da occupare
	 * @param idTenant Id del cliente a cui appartiene al tavolo
	 * @param idCameriere Id del cameriere che vuole occupare il tavolo
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo
	 * errore verificatosi
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void occupaTavolo(int idTavolo, int idTenant, int idCameriere, int numeroPersone) throws DatabaseException {
		
		try {
			/* ****************************************************
			 * Verifica della correttezza dello stato del tavolo
			 *****************************************************/
			
			TreeNodeTavolo tavolo = gestioneTavolo.getTavoloById(idTavolo);
				
			if(tavolo.getStato() != StatoTavoloEnum.LIBERO)
				throw new DatabaseException("Impossibile occupare il tavolo (stato " + 
											tavolo.getStato() + ")");
							
			/* ****************************************************
			 * Verifico se esiste già un conto in stato aperto per
			 * il tavolo richiesto
			 *****************************************************/
				
			if(gestioneConto.getContiAperti(idTavolo).size() != 0) 
				throw new DatabaseException("Il tavolo è già associato ad un conto aperto");
						
			/* ****************************************************
			 * Apertura del conto e occupazione del tavolo
			 *****************************************************/
			Date currentDate = new Date();
			
			gestioneConto.aggiungiConto(idTenant, 
										idTavolo,  
										idCameriere, 
										new BigDecimal(0.0), 
										numeroPersone,
										new Timestamp(currentDate.getTime()), 
										new Timestamp(currentDate.getTime()), 
										StatoContoEnum.APERTO);
				
			gestioneTavolo.updateTavolo(tavolo.getIdTavolo(), 
										tavolo.getNumposti(), 
										tavolo.getNome(),
										tavolo.getDescrizione(), 
										StatoTavoloEnum.OCCUPATO, 
										false);
		}catch(DatabaseException e) {
			context.setRollbackOnly();
			 /* Rilancio l'eccezione per la logica di presentazione */
			 throw e;
		}
	}
	
	/**
	 * Libera un tavolo precedentemente occupato. In ordine effettua le seguenti operazioni:
	 * - Verifica il corretto stato del tavolo (OCCUPATO)
	 * - Verifica la presenza di unico conto aperto associato al tavolo
	 * - Aggiorna lo stato del tavolo
	 * - Aggiorna lo stato del conto
	 * Se anche solo una di queste operazioni fallisce viene fatto il rollback della 
	 * transazione
	 * @param idTavolo
	 * @throws DatabaseException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void liberaTavolo(int idTavolo) throws DatabaseException {
		
		try {
			
			/* ****************************************************
			 * Verifica della correttezza dello stato del tavolo
			 *****************************************************/
			
			TreeNodeTavolo tavolo = gestioneTavolo.getTavoloById(idTavolo);
				
			if(tavolo.getStato() != StatoTavoloEnum.OCCUPATO) 
				throw new DatabaseException("Impossibile liberare un tavolo che non è occupato");
				
			/* *****************************************************
			 * Verifico la presenza di un conto aperto associato al
			 * tavolo
			 ******************************************************/
			
			List<WrapperConto> listConti = gestioneConto.getContiAperti(idTavolo);
			if(listConti.size() == 0) 
				throw new DatabaseException("Impossibile trovare un conto aperto associato al tavolo");
			else if (listConti.size() > 1) 
				throw new DatabaseException("Sono presenti più conti aperti associati al tavolo");
				
			WrapperConto conto = listConti.get(0);
				
			/* ****************************************************
			 * Aggiorno lo stato del conto e del tavolo
			 *****************************************************/
				
			gestioneTavolo.updateTavolo(tavolo.getIdTavolo(),
										tavolo.getNumposti(),
										tavolo.getNome(),
										tavolo.getDescrizione(),
										StatoTavoloEnum.PULIRE,
										tavolo.isEnabled());
			
			gestioneConto.updateConto(	conto.getIdConto(),
										conto.getPrezzo(), 
										conto.getTimestampChiusura(),
										StatoContoEnum.DAPAGARE);
		
		}catch(DatabaseException e) {
			context.setRollbackOnly();
			/* Rilancio l'eccezione per la logica di presentazione */
			throw e;
		}
		
	}
	
	/**
	 * Funzione che setta lo stato di un tavolo da pulire in "LIBERO
	 * @param idTavolo Id del tavolo da liberare
	 * @throws DatabaseException Eccezione che incapsula le informazioni
	 * sull'ultimo errore verificatosi
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void pulisciTavolo(int idTavolo) throws DatabaseException {
		try {
			
			/* ****************************************************
			 * Verifica la correttezza dello stato del tavolo
			 ******************************************************/
			
			TreeNodeTavolo tavolo = gestioneTavolo.getTavoloById(idTavolo);
			if(tavolo.getStato() != StatoTavoloEnum.PULIRE) 
				throw new DatabaseException("Impossibile pulire il tavolo (stato: " + 
											tavolo.getStato() + ")");
			
			gestioneTavolo.updateTavolo(tavolo.getIdTavolo(),
										tavolo.getNumposti(),
										tavolo.getNome(),
										tavolo.getDescrizione(),
										StatoTavoloEnum.LIBERO,
										tavolo.isEnabled());
			
		}catch(DatabaseException e) {
			throw e;
		}
	}
}
