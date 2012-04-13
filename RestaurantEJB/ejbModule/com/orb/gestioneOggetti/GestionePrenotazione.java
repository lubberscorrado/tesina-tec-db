package com.orb.gestioneOggetti;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.exceptions.DatabaseException;
import com.orb.Prenotazione;
import com.orb.Tavolo;
import com.restaurant.WrapperPrenotazione;

@Stateless
public class GestionePrenotazione {

	@PersistenceContext(unitName="ejbrelationships") 
	private EntityManager em;
		
	public GestionePrenotazione() {}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public WrapperPrenotazione aggiungiPrenotazione(	int idTenant, 
												Date data, 
												Time ora,
												String nomecliente,
												int numpersone,
												String stato,
												int idTavolo) throws DatabaseException {
		
		Prenotazione prenotazione = new Prenotazione();
		prenotazione.setIdTenant(idTenant);
		prenotazione.setData(data);
		prenotazione.setOra(ora);
		prenotazione.setNomecliente(nomecliente);
		prenotazione.setNumpersone(numpersone);
		prenotazione.setStato(stato);
		
		Tavolo tavolo = em.find(Tavolo.class, idTavolo);
		
		if(tavolo == null)
			throw new DatabaseException("Impossibile trovare il tavolo associato alla prenotazione");
		
		prenotazione.setTavoloAppartenenza(tavolo);
		
		try {
			em.persist(prenotazione);
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'inserimento di una prenotazione");
		}
		return new WrapperPrenotazione(prenotazione);
		
	}
	
	/**
	 * Restituisce la lista delle prenotazioni 
	 * @param idTenant
	 * @return
	 * @author Fabio Pierazzi
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<WrapperPrenotazione> getListaPrenotazioni(int idTenant) throws DatabaseException {

		Query query = em.createQuery("SELECT p FROM Prenotazione p " +
									 "WHERE p.idTenant = :idTenant " + 
									 "ORDER BY data DESC, ora DESC");
		
		query.setParameter("idTenant", idTenant);

		List<WrapperPrenotazione> listPrenotazioni= new ArrayList<WrapperPrenotazione>();
		
		for(Prenotazione prenotazione :  ((ArrayList<Prenotazione>) query.getResultList())) 
			listPrenotazioni.add(new WrapperPrenotazione(prenotazione));
		
		return listPrenotazioni;
	}
	
	
		
}
