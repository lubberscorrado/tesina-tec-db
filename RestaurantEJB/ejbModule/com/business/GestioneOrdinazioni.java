package com.business;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.exceptions.DatabaseException;
import com.orb.StatoContoEnum;
import com.orb.StatoTavoloEnum;
import com.orb.gestioneOggetti.GestioneConto;
import com.orb.gestioneOggetti.GestioneTavolo;
import com.restaurant.TreeNodeTavolo;

@Stateless
public class GestioneOrdinazioni {
	
	@EJB	
	private GestioneConto gestioneConto;
	@EJB
	private GestioneTavolo gestioneTavolo;
	
	public void occupaTavolo(int idTavolo, int idTenant, int idCameriere) throws DatabaseException {
				
		/*****************************************************
		 * Verifica della correttezza dello stato del tavolo
		 *****************************************************/
		TreeNodeTavolo tavolo = gestioneTavolo.getTavoloById(idTavolo);
		
		if(tavolo.getStato() != StatoTavoloEnum.LIBERO)
			throw new DatabaseException("Il tavolo non è libero");
					
		
		/*****************************************************
		 * Verifico se esiste già un conto in stato aperto per
		 * il tavolo richiesto
		 *****************************************************/
		
		if(gestioneConto.getContiAperti(idTavolo).size() != 0) 
			throw new DatabaseException("Il tavolo è già associato ad un conto aperto");
				
		/*****************************************************
		 * Aggiunta (e apertura) del conto 
		 *****************************************************/
		Date currentDate = new Date();
	
		gestioneConto.aggiungiConto(idTenant, 
									idTavolo,  
									idCameriere, 
									new BigDecimal(0.0), 
									new Timestamp(currentDate.getTime()), 
									new Timestamp(currentDate.getTime()), 
									StatoContoEnum.APERTO);
	}
}
