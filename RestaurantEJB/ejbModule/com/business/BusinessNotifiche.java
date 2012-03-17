package com.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;



import com.exceptions.DatabaseException;
import com.orb.StatoComandaEnum;
import com.orb.gestioneOggetti.GestioneComanda;
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
	
	/**
	 * Recupera tutte le notifiche relative all'attività di un determinato utente.
	 * @param idUtente Id dell'utente per il quale si vogliono recuperare le notifiche.
	 * @return Lista di oggetti Notifica che rappresentano tutte le notifiche disponibili
	 * per l'utente.
	 */
	public List<Notifica> getNotifiche(int idUtente, String lastDateSqlFormatted) throws DatabaseException {
		
		
		List<Notifica> listaNotifiche = new ArrayList<Notifica>();
		
		
		/* ***********************************************************
		 * Recupero delle notifiche relative alle comande pronte 
		 *************************************************************/
		
		List<WrapperComanda> listaComande = 
				gestioneComanda.getComandeByStatoAndCameriere(	StatoComandaEnum.PRONTA, 
																idUtente,
																lastDateSqlFormatted);
		
		for(WrapperComanda wrapperComanda : listaComande) {
			
			Notifica notifica = new Notifica();
			TreeNodeTavolo tavolo = gestioneComanda.getTavoloByIdComanda(wrapperComanda.getIdComanda());
			
			notifica.setTipoNotifica(TipoNotificaEnum.COMANDA_PRONTA);
			
			notifica.setIdTavolo(tavolo.getIdTavolo());
			notifica.setNomeTavolo(tavolo.getNome());
			
			notifica.setIdVoceMenu(wrapperComanda.getIdVoceMenu());
			notifica.setData(new Date());
			
			listaNotifiche.add(notifica);
		}
		
		
		/* *********************************************************
		 * Recupero delle notifiche relative ai tavoli ceduti 
		 ***********************************************************/
		
		
		/* *********************************************************
		 * Recupero delle notifiche relative ai tavoli da pulire 
		 ***********************************************************/
		
		return listaNotifiche;
	}
	
}
