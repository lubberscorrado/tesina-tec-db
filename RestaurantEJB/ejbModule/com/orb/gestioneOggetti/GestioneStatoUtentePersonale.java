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
import com.restaurant.WrapperStatoUtentePersonale;
import com.restaurant.WrapperVariazione;

@SuppressWarnings("unchecked") 
@Stateless
public class GestioneStatoUtentePersonale {
	
	@PersistenceContext(unitName="ejbrelationships")
	private EntityManager em;
	
	public GestioneStatoUtentePersonale() {};
	

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
			
			/* *********************************************************************
			 * Non verifica il numero di conti aperti associati al tavolo. In ogni
			 * momento dovrebbe esserci comunque al più 1 conto aperto.
			 * Il controllo è compito della logica di business.
			 ***********************************************************************/
			
			if(listaContiAperti.size() == 0) {
				return new ArrayList<WrapperComanda>();
			} else {
			
				List<Comanda> listaComande = listaContiAperti.get(0).getComande();
				List<WrapperComanda> listaWrapperComande = new ArrayList<WrapperComanda>();
				
				for(Comanda comanda: listaComande) 
					listaWrapperComande.add(new WrapperComanda(comanda));
						
				return listaWrapperComande;
			}
	
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
			
		} catch (DatabaseException e) {
			/* Rilancia l'eccezione */
			throw e;
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
		
		} catch (DatabaseException e) {
			/* Rilancia l'eccezione */
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Errora durante la ricerca della voce di menu (" + e.toString() + ")");
		}
	}
	
	/**
	 * Ritorna l'elenco delle comande di un certo tipo (CIBO o BEVANDE) 
	 * che sono in stato "INVIATA" o "INPREPARAZIONE". 
	 * Viene utilizzata per mostrare l'elenco delle comande nell'interfaccia
	 * cucina di Android. 
	 * @param type: può essumere i valori "CIBO", "BEVANDA"
	 * @return Lista di comande
	 */
	public List<WrapperComandaCucina> getElencoComandeByType(String type) throws DatabaseException {
		
		List<WrapperComandaCucina> listComandeCucina = new ArrayList<WrapperComandaCucina>();
		
		try {
			
				System.out.println("Bean GestioneComanda: sono entrato!");
			
				List<Comanda> listComande;
				
				// Costruisco la stringa con la query
				String stringaQuery = "SELECT c " +
						"FROM Comanda c " +
						"LEFT JOIN c.voceMenuAssociata v " +
						"LEFT JOIN v.categoriaAppartenenza cat " +
						"WHERE c.stato IN ('INVIATA','INPREPARAZIONE') " +
						"and cat.tipo=:tipo " +
						"order by c.lastModified DESC, c.hashGruppo";
				
				Query query = em.createQuery(stringaQuery);
				
				// TODO: imposta il parametro. Attualmente dà un problema il TipoCategoriaEnum
				if(type.equals("CIBO")) {
					System.out.println("Scelto elenco cibi (enum)");
					query.setParameter("tipo", TipoCategoriaEnum.CIBO);
				} else /* if(type.equals("BEVANDA"))*/ {
					System.out.println("Scelta elenco bevande (enum)");
					query.setParameter("tipo", TipoCategoriaEnum.BEVANDA);
				}
				
				/* Imposto il parametro per selezionare se ritornare la lista 
				 * dei cibi o delle bevande */
				
				listComande = query.getResultList();
				
				System.out.println("Bean GestioneComanda: ho eseguito la query!");
				
				/************************************************
				 * Recupero tutte le informazioni relative alle comande
				 ************************************************/
				
				
				List<Variazione> listVariazioni;
				List<WrapperVariazione> listWrapperVariazione = new ArrayList<WrapperVariazione>(); 
				
				/** Per tutte le comande presenti nella lista */
				for(Comanda comanda : listComande) {
					
					
					/** Creo la comanda cucina che andrò ad inserire nella lista */
					WrapperComandaCucina wrapperComandaCucina = new WrapperComandaCucina();
					
					/** Recupero le variazioni */
					try {
						// ottengo Variazioni (orb)
						listVariazioni = comanda.getVariazioniAssociate();
					} catch( Exception e) {
						throw new DatabaseException("Errore durante la ricerca dei delle variazioni associate ad una comanda " +
								"(" + e.toString() + ")");
					}
					
					/* Copio le Variazioni (orb) nell'oggetto Wrapper */
					for(Variazione variazione : listVariazioni) {
						listWrapperVariazione.add(new WrapperVariazione(variazione));
					}
					
					
					// TODO: CHECK!!!
					
					/* Copio la lista variazioni */
					wrapperComandaCucina.setListVariazioni(listWrapperVariazione);
					
					/** Fine recupero variazioni */
					
					/** IdTenant? */
					wrapperComandaCucina.setIdTenant(comanda.getIdTenant());

					/** Info Comanda */
					// id
					wrapperComandaCucina.setIdComanda(comanda.getIdComanda());
					// stato 
					wrapperComandaCucina.setStato(comanda.getStato());
					// quantita
					wrapperComandaCucina.setQuantita(comanda.getQuantita());
					// prezzo
					wrapperComandaCucina.setPrezzo(comanda.getPrezzo());
					// note
					wrapperComandaCucina.setNote(comanda.getNote());
					// lastModified
					wrapperComandaCucina.setLastModified(comanda.getLastModified());
					// hashgroup
					wrapperComandaCucina.setHashGruppo(comanda.getHashGruppo());
					
					/** Info Conto */
					Conto conto = comanda.getContoAppartenenza();
					wrapperComandaCucina.setIdConto(conto.getIdConto());
					
					/** Info Voce Menu */
					VoceMenu voceMenu = comanda.getVoceMenuAssociata();
					
					wrapperComandaCucina.setIdVoceMenu(voceMenu.getIdVoceMenu());
					wrapperComandaCucina.setNomeVoceMenu(voceMenu.getNome());
					
					/** Info Categoria */
					Categoria categoria = voceMenu.getCategoriaAppartenenza();
					
					wrapperComandaCucina.setIdCategoria(categoria.getIdCategoria());
					wrapperComandaCucina.setNomeCategoria(categoria.getNome());
					
					// TODO: Check!!!
//					wrapperComandaCucina.setTipoCategoria(categoria.getTipo().toString());
					
					/** Info Tavolo */
					Tavolo tavolo = conto.getTavoloAppartenenza();
					wrapperComandaCucina.setIdTavolo(tavolo.getIdTavolo());
					wrapperComandaCucina.setNomeTavolo(tavolo.getNome());
					
						
					/** Aggiungo il wrapper con la comanda cucina alla lista globale */
					listComandeCucina.add(wrapperComandaCucina);
				}
				
				
				
			/** Cancella da qui in poi */
				
		} catch (DatabaseException e) {
			/* Rilancia l'eccezione */
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Errora durante la ricerca delle comande  ("+ e.toString() +")");
			
		}
		return listComandeCucina;
		
	}
	
}