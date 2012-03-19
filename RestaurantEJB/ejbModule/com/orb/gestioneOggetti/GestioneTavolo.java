package com.orb.gestioneOggetti;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.exceptions.DatabaseException;
import com.orb.Area;
import com.orb.Comanda;
import com.orb.Conto;
import com.orb.Piano;
import com.orb.StatoContoEnum;
import com.orb.StatoTavoloEnum;
import com.orb.Tavolo;
import com.orb.UtentePersonale;
import com.restaurant.StatoTavolo;
import com.restaurant.TreeNodeTavolo;
import com.restaurant.WrapperComanda;


@SuppressWarnings("unchecked")
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GestioneTavolo{

	@PersistenceContext(unitName="ejbrelationships") 
	private EntityManager em;
		
	public GestioneTavolo() {}
	
	
	/**
	 * Aggiunge un tavolo per un determinato cliente
	 * @param idTenant Id del cliente
	 * @param nome Nome del tavolo
	 * @param stato Stato del tavolo
	 * @param descrizione Descrizione del tavolo
	 * @param numposti Numero di posti
	 * @param enabled Flag che indica se il tavolo è attivo
	 * @param idArea Id dell'area a cui appartiene il tavolo
	 * @return Oggetto TreeNodeTavolo che rappresenta il tavolo appena creato
	 * @throws DatabaseException
	 */
	public TreeNodeTavolo aggiungiTavolo(	int idTenant, 
											String nome, 
											StatoTavoloEnum stato,
											String descrizione, 
											int numposti,
											boolean enabled,
											int idArea) throws DatabaseException {
		
		try {
			// TODO Controllare che i valori passati non siano null
			Tavolo tavolo = new Tavolo();
			tavolo.setIdTenant(idTenant);
			tavolo.setNome(nome);
			tavolo.setDescrizione(descrizione);
			tavolo.setEnabled(enabled);
			tavolo.setStato(StatoTavoloEnum.LIBERO);
			tavolo.setNumposti(numposti);
			
			Area area = em.find(Area.class, idArea);
			if(area == null)
				throw new DatabaseException("Impossibile trovare l'area di appartenenza del tavolo");
			
			tavolo.setAreaAppartenenza(area);
		
			em.persist(tavolo);
			
			return new TreeNodeTavolo(tavolo);
			
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'inserimento del tavolo " +
										"("+ e.toString()+")");
		}		
	}
	
	/**
	 * Modifica le informazioni di un tavolo a partire dal suo id.
	 * @param idTavolo Id del tavolo da modificare
	 * @param numposti Numero dei posti del tavolo
	 * @param nome Nome del tavolo
	 * @param descrizione Descrizione del tavolo
	 * @param stato Stato del tavolo
	 * @param enabled Flag di attivazione 
	 * @return Oggetto TreeNodeTavolo che rappresenta il tavolo modificato
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'errore verificatosi
	 */
	
	public TreeNodeTavolo updateTavolo(	int idTavolo,
										int numposti,
										String nome,
										String descrizione,
										StatoTavoloEnum stato,
										boolean enabled) throws DatabaseException {
		
		try {
			
			Tavolo tavolo = em.find(Tavolo.class, idTavolo);
			
			if(tavolo == null)
				throw new DatabaseException("Errore durante la ricerca del tavolo da aggiornare");
		
			tavolo.setNome(nome);
			tavolo.setDescrizione(descrizione);
			tavolo.setStato(stato);
			tavolo.setEnabled(enabled);
			tavolo.setNumposti(numposti);
						
			return new TreeNodeTavolo(tavolo);
			
		}catch(Exception e) {
			
			throw new DatabaseException("Errore durante la modifica del tavolo (" +e.toString() +")");
			
		}
	}
	
	/**
	 * Elimina una taovlo a partire dall'id
	 * @param idTavolo Id del tavolo da eliminare dal database
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'errore che si è
	 * verificato
	 */
	public void deleteTavolo(int idTavolo) throws DatabaseException {
		
		try {
			Tavolo tavolo = em.find(Tavolo.class, idTavolo);
			if(tavolo == null)
				throw new DatabaseException("Errore durante la ricerca del tavolo");
//			em.remove(tavolo);
			tavolo.setRemoved(true);
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'eliminazione del tavolo ("+ e.toString() +")" );
		}
	}
	
	
	/** 
	 * Ritorna lo stato di tutti i tavoli a partire dall'id del cliente. 
	 * poichè tutte le entity sono eagerly fetched.
	 * @param idTenant Id del cliente
	 * @param "false" per ottenere i tavoli non eliminati, "true" per ottenere i tavoli eliminati
	 * @return Lista di oggetti StatoTavolo che incapsulano le informazioni
	 *  sullo stato di un tavolo
	 * @throws DatabaseException Eccezione che incapsula le informazioni
	 * sull'errore che si è verificato.
	 */
	public List<StatoTavolo> getStatoTavoli(int idTenant, boolean removed) throws DatabaseException {
		
		/* Ottengo tutti i tavoli associato al cliente, forzando l'acquisizione delle aree e 
		 * dei piani in un'unica query. Senza il FETCH JOIN il metodo di fetch delle enitità 
		 * (eager) è a discrezione  del persistence framework */
		
		List<Tavolo> listTavoli = null;
		
		try {
			Query query = em.createQuery(	"SELECT t FROM Tavolo t " +
											"LEFT JOIN FETCH t.areaAppartenenza a " +
											"LEFT JOIN FETCH a.pianoAppartenenza WHERE " +
											"t.idTenant = :idTenant AND " +
											"t.removed = :removed ");	
			
			query.setParameter("idTenant", idTenant);
			query.setParameter("removed", removed);
			listTavoli = query.getResultList();
			
		} catch(Exception e) {
			throw new DatabaseException("Errore durante la ricerca dei tavoli (" + 
										e.toString()+")");
		}
		
		/* ****************************************************************************
		 * Recupero tutte le informazioni associate al tavolo (area, piano, cameriere)
		 *****************************************************************************/
		
		List<StatoTavolo> listaStatoTavolo = new ArrayList<StatoTavolo>();
		
		Area area;
		Piano piano;
		
		for(Tavolo tavolo : listTavoli) {
			try {
				area = tavolo.getAreaAppartenenza();
				piano = area.getPianoAppartenenza();
			} catch(Exception e) {
				throw new DatabaseException("Errore durante la ricerca dei piani e delle aree " +
											"(" + e.toString() + ")");
			}
			
			try {
			
				/* *********************************************************
				 * Recupera il conto correntemente associato al tavolo
				 * ********************************************************/
				Query queryConto = em.createQuery( 	"SELECT c FROM Conto c " +
													"LEFT JOIN c.tavoloAppartenenza t " +
													"WHERE c.stato = :stato AND t.idTavolo = :idTavolo ");
				
				queryConto.setParameter("stato", StatoContoEnum.APERTO);
				queryConto.setParameter("idTavolo", tavolo.getIdTavolo());
				
				List<Conto> listaConti = queryConto.getResultList();
				
				/* *********************************************************
				 * Recupero il cameriere correntemente associato al tavolo
				 ***********************************************************/
				Query queryCameriere = em.createQuery(	"SELECT u FROM UtentePersonale u " +
														"JOIN u.conti c " +
														"JOIN c.tavoloAppartenenza t " +
														"WHERE c.stato = :stato AND t.idTavolo = :idTavolo");
				
				queryCameriere.setParameter("stato", StatoContoEnum.APERTO);
				queryCameriere.setParameter("idTavolo", tavolo.getIdTavolo());
		
				List<UtentePersonale> listaCamerieri = queryCameriere.getResultList();
						
				
				if(listaCamerieri.size() == 0) {
					/* Al tavolo non è associato alcun cameriere, probabilmente poichè non c'è 
					 * alcun conto aperto */
					listaStatoTavolo.add(new StatoTavolo(tavolo, area, piano, null, null));
				}else {
					
					if(listaCamerieri.size()> 1)
						System.out.println(	"WARNING: Ad un unico tavolo sono associati più camerieri, " +
											"non sono stati probabilmente chiusi conti precedenti");
				
					/* Se ci sono più camerieri associati allo stesso tavolo riporto l'ultimo cameriere 
					 * in ordine temporale  */
					listaStatoTavolo.add(new StatoTavolo(	tavolo, 
															area, 
															piano, 
															listaCamerieri.get(listaCamerieri.size() - 1),
															listaConti.get(0)
															));
				}
		
			}catch(Exception e) {
				throw new DatabaseException("Errore durante la ricerca del cameriere associato " +
											"al tavolo (" + e.toString() + ")");
			}
		}
		
		return listaStatoTavolo;
	
	}
	
	/** 
	 * Ritorna la lista dei tavoli associati ad una certa area. I tavoli sono 
	 * lazy fetched quindi è necessario che l'entità area rimanda attached 
	 * al persistence contex (l'entity manager è transaction scoped). Se una transazione
	 * JTA non è avviata, l'entità area diventa subito detached.
	 * @param idArea Id dell'area della quale si vogliono ottenere i tavoli
	 * @param removed : "false" se voglio ottenere i tavoli non eliminati; "true" se voglio ottenere gli eliminati
	 * @return Lista di oggetti TreeNodeTavolo che incapsulano le informazioni su
	 * un tavolo
	 * @throws DatabaseException Generica eccezione durante le operazioni sul database
	 */
	public List<TreeNodeTavolo> getTavoloByArea(int idArea, boolean removed) throws DatabaseException {
		
		Area area;
		
		List<Tavolo> listaTavoli;
		List<TreeNodeTavolo> listaTreeNodeTavolo;
		
		try {
			area = em.find(Area.class, idArea);
			if(area == null) 
				throw new DatabaseException("Errore durante la ricerca dell'area");
		} catch(Exception e) {
			throw new DatabaseException("Errore durante la ricerca dell'area +("+ e.toString() +")");
		}
		
		try {
			
			listaTavoli = area.getTavoli();	
			listaTreeNodeTavolo = new ArrayList<TreeNodeTavolo>();
		
			for(Tavolo tavolo : listaTavoli) {
				/* Aggiungo al wrapper solo i tavoli che m'interessano */
				if(tavolo.getRemoved() == removed) {
					listaTreeNodeTavolo.add(new TreeNodeTavolo(tavolo));
				}
				
			}
				
			
		}catch(Exception e) {
			throw new DatabaseException("Errore durante la ricerca dei tavoli associati ad un area " +
										"(" + e.getMessage() +")");
		}
		
		return listaTreeNodeTavolo;
	}
	
	/**
	 * Ritorna un tavolo a partire dal suo Id
	 * @param idTavolo Id del tavolo
	 * @return Oggetto TreeNodeTavolo, wrapper per le informazioni sul tavolo
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo
	 * errore verificatosi
	 */
	public TreeNodeTavolo getTavoloById(int idTavolo) throws DatabaseException {
		
		try {
			Tavolo tavolo = em.find(Tavolo.class, idTavolo);
			if(tavolo == null) 
				throw new DatabaseException("Impossibile trovare il tavolo");
			return new TreeNodeTavolo(tavolo);
			
		}catch(Exception e) {
			throw new DatabaseException("Errore durante la ricerca del tavolo (" + e.toString() + ")");
		}
	}
	
	/**
	 * Ritorna i tavoli associati ad un determinato cameriere che si trovano in un determinato
	 * stato. Viene utilizzato per la gestione delle notifiche.
	 * @param stato Stato dei tavoli che si vogliono recuperare
	 * @param idUtente Id del cameriere al quale sono associati i tavoli
	 * @return Lista dei tavoli
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo errore
	 * verificatosi
	 */
	
	
	/**
	 * Ritorna i tavoli associati ad un cameriere che si trovano in un determinato stato.
	 * Questo metodo viene utilizzato per la gestione delle notifiche per ottenere
	 * l'elenco dei tavoli da pulire
	 * @param stato Stato dei tavoli che si vogliono recuperare
	 * @param idUtente Id del cameriere al quale sono associati i tavoli
	 * @param lastCheckDate Data dell'ultima ricerca. Ritorna solo le tuple che 
	 * sono state modificate
	 * @return Lista dei tavoli recuperati
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo errore 
	 * verificatosi
	 */
	public List<TreeNodeTavolo> getTavoliByStatoAndCameriere(	StatoTavoloEnum stato, 
																int idUtente,
																String lastCheckDate)
																throws DatabaseException{
		
		List<Tavolo> listaTavoli = new ArrayList<Tavolo>();
		List<TreeNodeTavolo> listaTreeNodeTavolo = new ArrayList<TreeNodeTavolo>();
		
		try {
			Query query = em.createQuery(	"SELECT DISTINCT t FROM Tavolo t "+
											"JOIN t.conti co " +
											"JOIN co.cameriereAssociato ca WHERE " +
											"t.stato = :stato AND " +
											"ca.idUtente = :idUtente AND " +
											"t.lastModified > :lastCheckDate");
					
			
			query.setParameter("idUtente", idUtente);
			query.setParameter("stato", stato);
			query.setParameter(	"lastCheckDate", 
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.parse(lastCheckDate));
			
			listaTavoli = query.getResultList();
			
			for(Tavolo tavolo : listaTavoli) {
				listaTreeNodeTavolo.add(new TreeNodeTavolo(tavolo));
			}
			return listaTreeNodeTavolo;
			
		}catch(Exception e) {
			throw new DatabaseException("Errore durante la ricerca dei tavoli nello stato " +
										stato.toString() + " ("+ e.toString() +")");
		}
		
	}
	
}