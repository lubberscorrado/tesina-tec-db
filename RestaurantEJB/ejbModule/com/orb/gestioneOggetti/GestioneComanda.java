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
import com.orb.Area;
import com.orb.Comanda;
import com.orb.Conto;
import com.orb.StatoComandaEnum;
import com.orb.StatoContoEnum;
import com.orb.Tavolo;
import com.orb.UtentePersonale;
import com.orb.Variazione;
import com.orb.VoceMenu;
import com.restaurant.TreeNodeArea;
import com.restaurant.TreeNodeTavolo;
import com.restaurant.TreeNodeVoceMenu;
import com.restaurant.WrapperComanda;
import com.restaurant.WrapperConto;

@SuppressWarnings("unchecked") 
@Stateless
public class GestioneComanda {
	
	@PersistenceContext(unitName="ejbrelationships")
	private EntityManager em;
	
	public GestioneComanda() {};
	
	/**
	 * Aggiunge una comanda al database
	 * @param idTenant Id del tenant a cui appartiene la comanda
	 * @param idVoceMenu Id della voce di menu per cui è stata richiesta la comanda
	 * @param idConto Id del conto a cui appartiene la comanda
	 * @param idCameriere Id del cameriere che ha preso la comanda
	 * @param note Note associate alla comanda
	 * @param prezzo Prezzo della comanda la momento della registrazione
	 * @param quantita Quantità richiesta per la voce di menu associata alla comanda
	 * @param stato Stato della comanda
	 * @param listIdVariazioniAssociate Lista di id di variazioni richiesta per la
	 * comanda
	 * @return Oggetto WrapperComanda che rappresenta le comanda appena inserita
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo
	 * errore verificatosi.
	 */
	public WrapperComanda aggiungiComanda(	int idTenant,
											int idVoceMenu,
											int idConto,
											int idCameriere,
											String note,
											String hashGruppo,
											BigDecimal prezzo,
											int quantita,
											StatoComandaEnum stato,
											List<Integer> listIdVariazioniAssociate) throws DatabaseException {
		
		try {
			Comanda comanda = new Comanda() ;
			comanda.setIdTenant(idTenant);
			comanda.setNote(note);
			comanda.setPrezzo(prezzo);
			comanda.setQuantita(quantita);
			comanda.setStato(stato);
			comanda.setHashGruppo(hashGruppo);
			
			/******************************************************************
			 * Associo ad ogni comanda le variazioni richieste
			 ******************************************************************/
			
			List<Variazione> listVariazioniAssociate = new ArrayList<Variazione>();
			
			for(Integer idVariazione : listIdVariazioniAssociate) {
				Variazione variazione = em.find(Variazione.class, idVariazione);
				if(variazione == null)
					throw new DatabaseException("Errore durante la ricerca della variazione " + idVariazione);
				listVariazioniAssociate.add(variazione);
			}
			comanda.setVariazioniAssociate(listVariazioniAssociate);
			
			/******************************************************************
			 * Associo la comanda al conto di cui fa parte 
			 ******************************************************************/

			Conto conto = em.find(Conto.class, idConto);
			if(conto == null)
				throw new DatabaseException("Errore durante la ricerca del conto a cui appartiene la comanda");
			comanda.setContoAppartenenza(conto);
				
			/******************************************************************
			 * Associo alla comanda la voce di menu relativa
			 ******************************************************************/
			
			VoceMenu voceMenu = em.find(VoceMenu.class, idVoceMenu);
			if(voceMenu == null)
				throw new DatabaseException("Errore durante la ricerca della voce di menu associata alla comanda");
			comanda.setVoceMenuAssociata(voceMenu);
			
			/******************************************************************
			 * Associo la comanda al cameriere che l'ha presa in carico
			 ******************************************************************/
			
			UtentePersonale cameriere = em.find(UtentePersonale.class, idCameriere);
			if(cameriere == null)
				throw new DatabaseException("Errore durante la ricerca del cameriere responsabile per la comanda");
			comanda.setCameriereAssociato(cameriere);
			
			em.persist(comanda);
			return new WrapperComanda(comanda);
			
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'inserimento della comanda +" +
										"("+ e.toString() +")" );
		}
	}
	
	/**
	 * Modifica le informazioni su una comanda. Non tutte le informazioni
	 * possono essere modificate
	 * @param idComanda Id della comanda da modificare
	 * @param note Note della comanda modificata
	 * @param quantita Quantità relativa alla comanda modificata
	 * @param listIdVariazioniAssociate Lista delle variazioni associate alla comanda
	 * @return
	 */
	public WrapperComanda updateComanda(int idComanda,
										String note,
										int quantita,
										List<Integer> listIdVariazioniAssociate) throws DatabaseException {
			
		try {
			
			Comanda comanda = em.find(Comanda.class, idComanda);
			if(comanda == null)
				throw new DatabaseException("Errore durante la ricerca della comanda da aggiornare");
				
			comanda.setNote(note);
			comanda.setQuantita(quantita);
			
			/* Rimuovo le variazioni non più necessarie */
			
			for(int i=0; i<comanda.getVariazioniAssociate().size(); i++ ) {
				if(!listIdVariazioniAssociate.contains(comanda.getVariazioniAssociate().get(i)));
					comanda.getVariazioniAssociate().remove(i);
			}
			
			/* Aggiungo le nuove variazioni se non sono già presenti */
				
			for(int i=0; i< listIdVariazioniAssociate.size(); i++) {
				if(comanda.getVariazioniAssociate().contains(listIdVariazioniAssociate.get(i))) {
					continue;
				} else {
					Variazione variazione = em.find(Variazione.class, listIdVariazioniAssociate.get(i));
					comanda.getVariazioniAssociate().add(variazione);
				}
					
			}
			
			return new WrapperComanda(comanda);
			
		}catch(Exception e) {
			throw new DatabaseException("Errore durante la modifica della comanda (" +e.toString() +")");
		}
	 }
	 
	 
	
	/**
	 * Ritorna le comande che fanno parte del conto aperto associato al tavolo
	 * @param idTavolo Id del tavolo per il quale si vogliono ottenere le voci di menu
	 * @return Lista delle comande associate al conto aperto del tavolo
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo
	 * errore verificatosi
	 */
	public List<WrapperComanda> getComandeByTavolo(int idTavolo) throws DatabaseException {
		
		try {
			
			/* Ottengo la lista dei conti in stato aperto per il tavolo richiesto */
			Query query = em.createQuery(	"SELECT c FROM Tavolo t JOIN t.conti c WHERE " +
					"c.stato = 'APERTO' AND t.idTavolo = :idTavolo");
			query.setParameter("idTavolo", idTavolo);
			List<Conto> listaContiAperti =  query.getResultList();
			
			/* Se c'è più di un conto aperto associato al tavolo si è verificato qualche
			 * errore */
			if(listaContiAperti.size() > 1 ) 
				throw new DatabaseException("Errora durante la ricerca del conto aperto, " +
											"ci sono più conti aperti associati al tavolo");
			
			if(listaContiAperti.size() == 0)
				return new ArrayList<WrapperComanda>();
			
			/* Vengono recuperate dal database le comande associate al conto aperto */
			List<Comanda> listaComande = listaContiAperti.get(0).getComande();
			
			List<WrapperComanda> listaWrapperComande = new ArrayList<WrapperComanda>();
			
			for(Comanda comanda: listaComande) 
				listaWrapperComande.add(new WrapperComanda(comanda));
					
			return listaWrapperComande;
		
		} catch (Exception e) {
			throw new DatabaseException("Errora durante la ricerca delle comande  ("+ e.toString() +")");
			
		}
	}
	
	
	/**
	 * Ritorna la comanda a partire dall'id
	 * @param idComanda Id della comanda da ricercare
	 * @return Oggetto WrapperComanda che rappresenta la comanda trovata 
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo errore
	 * verificatosi
	 */
	public WrapperComanda getComandaById(int idComanda) throws DatabaseException {
		try {
			Comanda comanda = em.find(Comanda.class, idComanda);
			if(comanda == null) 
				throw new DatabaseException("Impossibile trovare la comanda");
			
			return new WrapperComanda(comanda);
			
		} catch(Exception e) {
			throw new DatabaseException("Errore durante la ricerca della comanda (" + e.toString() +")");
		}
		
	}
	
	/**
	 * Ritorna la voce di menu per cui è stata creata una determinata comanda
	 * @param idComanda Id della comanda per la quale si vuole ottenere la voce di menu
	 * @return Oggetto TreeNodeVoceMenu che rappresenta la voce di menu trovata
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo
	 * errore verificatosi
	 */
	public TreeNodeVoceMenu getVoceMenuByComanda(int idComanda) throws DatabaseException {
		try {
			Comanda comanda = em.find(Comanda.class, idComanda);
			
			if(comanda == null)
				throw new DatabaseException("Errore duranta la ricerca della comanda");
			return new TreeNodeVoceMenu(comanda.getVoceMenuAssociata());
		} catch (Exception e) {
			throw new DatabaseException("Errora durante la ricerca della voce di menu (" + e.toString() + ")");
		}
	}
}