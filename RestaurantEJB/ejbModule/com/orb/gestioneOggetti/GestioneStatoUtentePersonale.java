package com.orb.gestioneOggetti;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
import com.orb.Comanda;
import com.orb.Conto;
import com.orb.StatoComandaEnum;
import com.orb.StatoUtentePersonale;
import com.orb.StatoUtentePersonaleEnum;
import com.orb.Tavolo;
import com.orb.TipoCategoriaEnum;
import com.orb.UtentePersonale;
import com.orb.Variazione;
import com.orb.VoceMenu;
import com.restaurant.TreeNodeVoceMenu;
import com.restaurant.WrapperComanda;
import com.restaurant.WrapperComandaCucina;
import com.restaurant.WrapperConto;
import com.restaurant.WrapperStatoUtentePersonale;
import com.restaurant.WrapperUtentePersonale;
import com.restaurant.WrapperUtentePersonaleVisualizzazioneStato;
import com.restaurant.WrapperVariazione;

@SuppressWarnings("unchecked") 
@Stateless
public class GestioneStatoUtentePersonale {
	
	@PersistenceContext(unitName="ejbrelationships")
	private EntityManager em;
	
	public GestioneStatoUtentePersonale() {};
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public WrapperStatoUtentePersonale aggiungiStatoUtentePersonale(int idUtente,
																	int idTenant,
																	StatoUtentePersonaleEnum tipoAccesso	)	throws DatabaseException {
		
		//Se esiste già una sessione su db
		try {
			StatoUtentePersonale statoUtentePersonale = em.find(StatoUtentePersonale.class, idUtente);
			if(statoUtentePersonale != null){
				statoUtentePersonale.setTipoAccesso(tipoAccesso);
				statoUtentePersonale.setLoginTime(null);
				return new WrapperStatoUtentePersonale(statoUtentePersonale);
			}
		}catch(Exception e) {
			//Non faccio niente
		}
		
		//Se non esiste già una sessione su db
		try {
			StatoUtentePersonale statoUtentePersonale = new StatoUtentePersonale();
			statoUtentePersonale.setIdTenant(idTenant);
			statoUtentePersonale.setIdUtente(idUtente);
			statoUtentePersonale.setTipoAccesso(tipoAccesso);
			em.persist(statoUtentePersonale);
			return new WrapperStatoUtentePersonale(statoUtentePersonale);
		
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'inserimento della sessione " +
										"("+ e.toString()+")" );
		} 
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteStatoUtentePersonale(int idUtente, int idTenant) throws DatabaseException {
		try {
			StatoUtentePersonale statoUtentePersonale = em.find(StatoUtentePersonale.class, idUtente);
			if(statoUtentePersonale == null) 
				throw new DatabaseException("Errore durante la ricerca della sessione da rimuovere");
			em.remove(statoUtentePersonale);
			
		} catch (DatabaseException e) {
			/* Rilancia l'eccezione */
			throw e;
		}catch(Exception e) {
			throw new DatabaseException("Errore durante l'eliminazione della sessione ("+ e.toString() + ")");
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<WrapperUtentePersonaleVisualizzazioneStato> getLoggedCamerieri(int idTenant) throws DatabaseException {
		try {
			Query query = em.createQuery("SELECT u FROM StatoUtentePersonale s JOIN s.utentePersonale u WHERE s.idTenant = :idTenant AND s.tipoAccesso = :tipoAccesso");
			query.setParameter("idTenant", idTenant);
			query.setParameter("tipoAccesso", StatoUtentePersonaleEnum.CAMERIERE);
			
			List<WrapperUtentePersonaleVisualizzazioneStato> listWrapperUtentePersonale = new ArrayList<WrapperUtentePersonaleVisualizzazioneStato>();

			for(UtentePersonale utentePersonale :  ((List<UtentePersonale>) query.getResultList())) {
				listWrapperUtentePersonale.add(	new WrapperUtentePersonaleVisualizzazioneStato(utentePersonale, StatoUtentePersonaleEnum.CAMERIERE)	);
			}
			
			return listWrapperUtentePersonale;
			
		}catch(Exception e) {
			throw new DatabaseException("Errore durante la ricerca delle sessioni dei camerieri loggati ("+ e.toString() + ")");
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<WrapperUtentePersonaleVisualizzazioneStato> getLoggedCuochi(int idTenant) throws DatabaseException {
		try {
			Query query = em.createQuery("SELECT u FROM StatoUtentePersonale s JOIN s.utentePersonale u WHERE s.idTenant = :idTenant AND s.tipoAccesso = :tipoAccesso");
			query.setParameter("idTenant", idTenant);
			query.setParameter("tipoAccesso", StatoUtentePersonaleEnum.CUOCO);
			
			List<WrapperUtentePersonaleVisualizzazioneStato> listWrapperUtentePersonale = new ArrayList<WrapperUtentePersonaleVisualizzazioneStato>();

			for(UtentePersonale utentePersonale :  ((List<UtentePersonale>) query.getResultList())) {
				listWrapperUtentePersonale.add(	new WrapperUtentePersonaleVisualizzazioneStato(utentePersonale, StatoUtentePersonaleEnum.CAMERIERE)	);
			}
			
			return listWrapperUtentePersonale;
			
		}catch(Exception e) {
			throw new DatabaseException("Errore durante la ricerca delle sessioni dei cuochi loggati ("+ e.toString() + ")");
		}
	}
	 
}