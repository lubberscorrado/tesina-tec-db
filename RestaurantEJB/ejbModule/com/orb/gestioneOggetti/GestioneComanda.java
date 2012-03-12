package com.orb.gestioneOggetti;

import java.math.BigDecimal;
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
import com.orb.Tavolo;
import com.orb.TipoCategoriaEnum;
import com.orb.UtentePersonale;
import com.orb.Variazione;
import com.orb.VoceMenu;
import com.restaurant.TreeNodeVoceMenu;
import com.restaurant.WrapperComanda;
import com.restaurant.WrapperComandaCucina;
import com.restaurant.WrapperVariazione;

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
											List<Integer> listIdVariazioniAssociate) 
											throws DatabaseException {
		
		try {
			Comanda comanda = new Comanda() ;
			comanda.setIdTenant(idTenant);
			comanda.setNote(note);
			comanda.setPrezzo(prezzo);
			comanda.setQuantita(quantita);
			comanda.setStato(stato);
			comanda.setHashGruppo(hashGruppo);
			
			/* *****************************************************************
			 * Associo ad ogni comanda le variazioni richieste
			 ******************************************************************/
		
			for(Integer idVariazione : listIdVariazioniAssociate) {
				Variazione variazione = em.find(Variazione.class, idVariazione);
				if(variazione == null)
					throw new DatabaseException("Errore durante la ricerca della variazione " +
												idVariazione);
				comanda.getVariazioniAssociate().add(variazione);
			}
			
			/* *****************************************************************
			 * Associo la comanda al conto di cui fa parte 
			 ******************************************************************/

			Conto conto = em.find(Conto.class, idConto);
			if(conto == null)
				throw new DatabaseException("Errore durante la ricerca del conto a cui appartiene la comanda");
			comanda.setContoAppartenenza(conto);
				
			/* *****************************************************************
			 * Associo alla comanda la voce di menu relativa
			 ******************************************************************/
			
			VoceMenu voceMenu = em.find(VoceMenu.class, idVoceMenu);
			if(voceMenu == null)
				throw new DatabaseException("Errore durante la ricerca della voce di menu associata alla comanda");
			comanda.setVoceMenuAssociata(voceMenu);
			
			/* *****************************************************************
			 * Associo la comanda al cameriere che l'ha presa in carico
			 ******************************************************************/
			
			UtentePersonale cameriere = em.find(UtentePersonale.class, idCameriere);
			if(cameriere == null)
				throw new DatabaseException("Errore durante la ricerca del cameriere responsabile per la comanda");
			comanda.setCameriereAssociato(cameriere);
			
			em.persist(comanda);
			return new WrapperComanda(comanda);
		
		} catch (DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'inserimento della comanda " +
										"("+ e.toString()+")" );
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
			
			/* Rimuovo tutte le variazioni associate alla comanda */
			comanda.getVariazioniAssociate().clear();
			
			/* Aggiungo le nuove variazioni */
			for(int i=0; i< listIdVariazioniAssociate.size(); i++) {
					Variazione variazione = em.find(Variazione.class, listIdVariazioniAssociate.get(i));
					comanda.getVariazioniAssociate().add(variazione);
			}
			
			return new WrapperComanda(comanda);
			
		} catch (DatabaseException e) {
			/* Rilancia l'eccezione */
			throw e;
		} catch (Exception e) {
			throw new DatabaseException("Errore durante la modifica della comanda (" +e.toString() +")");
		}
	 }
		
	/**
	 * Elimina una comanda a partire dall'id
	 * @param idComanda Id della comanda da eliminare
	 * @throws DatabaseException
	 */
	
	public void deleteComanda(int idComanda) throws DatabaseException {
		try {
			Comanda comanda = em.find(Comanda.class, idComanda);
			if(comanda == null) 
				throw new DatabaseException("Errore durante la ricerca della comanda da rimuovere");
			em.remove(comanda);
			
		} catch (DatabaseException e) {
			/* Rilancia l'eccezione */
			throw e;
		}catch(Exception e) {
			throw new DatabaseException("Errore durante l'eliminazione dell'area ("+ e.toString() + ")");
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