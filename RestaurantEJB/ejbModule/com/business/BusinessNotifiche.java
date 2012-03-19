package com.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;



import com.exceptions.DatabaseException;
import com.orb.StatoComandaEnum;
import com.orb.StatoTavoloEnum;
import com.orb.gestioneOggetti.GestioneComanda;
import com.orb.gestioneOggetti.GestioneTavolo;
import com.orb.gestioneOggetti.GestioneVoceMenu;
import com.restaurant.Notifica;
import com.restaurant.TipoNotificaEnum;
import com.restaurant.TreeNodeTavolo;
import com.restaurant.WrapperComanda;

@Stateless
public class BusinessNotifiche {

	@EJB
	private GestioneComanda gestioneComanda;
	@EJB
	private GestioneVoceMenu gestioneVoceMenu;
	@EJB
	private GestioneTavolo gestioneTavolo;
	
	
	/**
	 * Recupera tutte le notifiche relative all'attività di un determinato utente.
	 * @param idUtente Id dell'utente per il quale si vogliono recuperare le notifiche.
	 * @return Lista di oggetti Notifica che rappresentano tutte le notifiche disponibili
	 * per l'utente.
	 * @author Guerri Marco
	 */
	public List<Notifica> getNotifiche(int idUtente, String lastDateSqlFormatted) throws DatabaseException {
		
		/* Lista delle notifiche da ritornare alla logica di presentazione.
		 * Vengono controllate prima le notifiche per le comande pronte e 
		 * solo successivamente le notifiche per i tavoli da pulire. 
		 * L'ordine di presentazione al client non rispetta quindi
		 * la data ma la tipologia */
		
		List<Notifica> listaNotifiche = new ArrayList<Notifica>();
		
		/* ***********************************************************
		 * Recupero delle notifiche relative alle comande pronte 
		 *************************************************************/
		
		List<WrapperComanda> listaComande = 
				gestioneComanda.getComandeByStatoAndCameriere(	StatoComandaEnum.PRONTA, 
																idUtente,
																lastDateSqlFormatted);
		
		for(WrapperComanda wrapperComanda : listaComande) {
			
			TreeNodeTavolo tavolo = 
					gestioneComanda.getTavoloByIdComanda(wrapperComanda.getIdComanda());
			
			Notifica notifica = new Notifica();
			notifica.setTipoNotifica(TipoNotificaEnum.COMANDA_PRONTA);
			notifica.setIdComanda(wrapperComanda.getIdComanda());
			notifica.setIdTavolo(tavolo.getIdTavolo());
			notifica.setNomeTavolo(tavolo.getNome());
			notifica.setIdVoceMenu(wrapperComanda.getIdVoceMenu());
			
			/* La data associata alla notifica corrisponde alla data di
			 *  ultima modifica sul server */
			notifica.setLastModfied(wrapperComanda.getLastModified());
			
			listaNotifiche.add(notifica);
		}
		
		
		/* *********************************************************
		 * Recupero delle notifiche relative ai tavoli da pulire 
		 ***********************************************************/
		
		List<TreeNodeTavolo> listaTreeNodeTavolo =
				gestioneTavolo.getTavoliByStatoAndCameriere(StatoTavoloEnum.PULIRE,
															idUtente,
															lastDateSqlFormatted);
		
		for(TreeNodeTavolo tavolo : listaTreeNodeTavolo) {
			Notifica notifica = new Notifica();
		
			notifica.setTipoNotifica(TipoNotificaEnum.TAVOLO_DA_PULIRE);
			notifica.setIdComanda(0);
			notifica.setIdVoceMenu(0);
			notifica.setLastModfied(tavolo.getLastModified());
			notifica.setIdComanda(tavolo.getIdTavolo());
			notifica.setIdTavolo(tavolo.getIdTavolo());
			notifica.setNomeTavolo(tavolo.getNome());
			
			listaNotifiche.add(notifica);
		}
				
		

		
		
		return listaNotifiche;
	}
	
}
