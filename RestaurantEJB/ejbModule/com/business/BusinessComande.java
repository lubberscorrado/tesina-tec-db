package com.business;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.exceptions.DatabaseException;

import com.orb.StatoComandaEnum;
import com.orb.gestioneOggetti.GestioneComanda;
import com.orb.gestioneOggetti.GestioneConto;
import com.orb.gestioneOggetti.GestioneVoceMenu;
import com.restaurant.TreeNodeVoceMenu;
import com.restaurant.WrapperComanda;
import com.restaurant.WrapperComandaCucina;
import com.restaurant.WrapperConto;

@Stateless
public class BusinessComande {
	
	private static String characters = "1234567890abcdefghijklmnopqrstuvwxyz";
	@EJB	
	private GestioneConto gestioneConto;
	@EJB
	private GestioneComanda gestioneComanda;
	@EJB
	private GestioneVoceMenu gestioneVoceMenu;
	
	/**
	 * Inserisce un gruppo di comande all'interno del database
	 * @param listaComande Lista di oggetti WrapperComanda che rappresentano le comande da 
	 * inserire
	 * @param idTavolo Id del tavolo a cui è associato il gruppo di comande
	 * @param idCameriere Id del cameriere che ha preso le comande
	 * @return Lista degli id assegnati a ciascuna comanda dal database. Serviranno al client 
	 * Android
	 * per la modifica delle comande
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo errore 
	 * verificatosi
	 */
	public List<Integer> inserisciComande(	List<WrapperComanda> listaComande, 
											int idTavolo, 
											int idCameriere) throws DatabaseException {
		
		
		List<Integer> listaIdRemotiComande = new ArrayList<Integer>();
		List<WrapperConto> listaContiAperti = gestioneConto.getContiAperti(idTavolo);
			
		if(listaContiAperti.size() != 1) 
			throw new DatabaseException("Errore durante la ricerca dei conti aperti (" +
											listaContiAperti.size() + " conti aperti)" + 
											idTavolo);
			
		/* Hash che identifica univocamente il gruppo di comande */
		String hashGruppo = generateString(new Random(), BusinessComande.characters, 64);
			
		for(WrapperComanda wrapperComanda : listaComande) {
				
			TreeNodeVoceMenu voceMenu = 
					gestioneVoceMenu.getVoceMenu(wrapperComanda.getIdVoceMenu());
				
			WrapperComanda newWrapperComanda = 
					gestioneComanda.aggiungiComanda(	wrapperComanda.getIdTenant(), 
														wrapperComanda.getIdVoceMenu(),
														listaContiAperti.get(0).getIdConto(),
														idCameriere,
														wrapperComanda.getNote(),
														hashGruppo,
														voceMenu.getPrezzo(),
														wrapperComanda.getQuantita(),
														wrapperComanda.getStato(),
														wrapperComanda.getListIdVariazioni());
			
			listaIdRemotiComande.add(new Integer(newWrapperComanda.getIdComanda()));
		}
		return listaIdRemotiComande;
			
	}
	
	/**
	 * Modifica una comanda
	 * @param wrapperComanda Oggetti che rappresenta la comanda modificata
	 * @throws DatabaseException
	 */
	
	public void modificaComanda(WrapperComanda wrapperComanda) throws DatabaseException{

		/* Verifica se la comanda è già in preparazione o in uno stato diverso
		 * da INVIATA. In caso affermativo rifiuta la cancellazione */
		
		StatoComandaEnum stato = gestioneComanda.getComandaById(wrapperComanda.getIdComanda())
												.getStato();
		
		if(!stato.equals(StatoComandaEnum.INVIATA)) 
				
			throw new DatabaseException("Impossibile modificare la comanda (stato: " + 
										stato.toString() + ")");
			
		gestioneComanda.updateComanda(	wrapperComanda.getIdComanda(),
										wrapperComanda.getNote(),
										wrapperComanda.getQuantita(),
										wrapperComanda.getListIdVariazioni());
	}
	
	/**
	 * Modifica lo stato di una comanda
	 * @param stato il nuovo stato 
	 * @throws DatabaseException
	 */
	public void modificaStatoComanda(int idComanda, int idCucina, String stato) throws DatabaseException{

		StatoComandaEnum statoAttuale = gestioneComanda.getComandaById(idComanda).getStato();
		
		try {
			gestioneComanda.updateStatoComanda(idComanda,idCucina, stato);
		} catch(Exception e) {
			throw new DatabaseException("Eccezione durante la modifica dello stato " +
					"della comanda con id " + idComanda + ". " + e.toString());
		}
			
	}
	
	
	/**
	 * Elimina una comanda
	 * @param idComanda Id della comanda da eliminare
	 * @throws DatabaseException 
	 */
	public void eliminaComanda(int idComanda) throws DatabaseException  {
		
		WrapperComanda wrapperComanda = gestioneComanda.getComandaById(idComanda);
		
		/* Impedisco l'eliminazione della comanda se è già in preparazione */
		if(!wrapperComanda.getStato().equals(StatoComandaEnum.INVIATA)) {
			throw new DatabaseException("Impossibile eliminare la comanda (stato: " + 
										wrapperComanda.getStato() + ")");
		}
			
		gestioneComanda.deleteComanda(idComanda);
	}
	
	/**
	 * Funzione per generare una stringa alfanumerica casuale
	 * @param rng Oggetto random per la generazione di una posizione casuale all'interno della stringa
	 * @param characters Caratteri accettati all'interno della stringa 
	 * @param length Lunghezza della stringa da generare
	 * @return String casuale
	 */
	public static String generateString(Random rng, String characters, int length)
	{
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(rng.nextInt(characters.length()));
	    }
	    return new String(text);
	}
	
	/**
	 * Funzione per ottenere un elenco delle comande in base al tipo
	 * @param type = CIBO, BEVANDA
	 */
	public List<WrapperComandaCucina> getElencoComandeByType(int idTenant, String type) {
		
		List<WrapperComandaCucina> lista = new ArrayList<WrapperComandaCucina>();
		
		try {
			lista = gestioneComanda.getElencoComandeByType(idTenant, type);
		} catch (DatabaseException e) {
			System.out.println("BusinessComande: eccezione invocando l'entity bean" );
			e.printStackTrace();
		}
		
		return lista;
	}
	
}
