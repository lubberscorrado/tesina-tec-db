package com.business;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.exceptions.DatabaseException;
import com.orb.StatoContoEnum;
import com.orb.StatoTavoloEnum;
import com.orb.gestioneOggetti.GestioneConto;
import com.orb.gestioneOggetti.GestioneTavolo;
import com.restaurant.TreeNodeTavolo;
import com.restaurant.WrapperConto;

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
			throw new DatabaseException("Impossibile occupare un tavolo non libero");
					
		/*****************************************************
		 * Verifico se esiste già un conto in stato aperto per
		 * il tavolo richiesto
		 *****************************************************/
		
		if(gestioneConto.getContiAperti(idTavolo).size() != 0) 
			throw new DatabaseException("Il tavolo è già associato ad un conto aperto");
				
		/*****************************************************
		 * Apertura del conto e occupazione del tavolo
		 *****************************************************/
		Date currentDate = new Date();
	
		gestioneConto.aggiungiConto(idTenant, 
									idTavolo,  
									idCameriere, 
									new BigDecimal(0.0), 
									new Timestamp(currentDate.getTime()), 
									new Timestamp(currentDate.getTime()), 
									StatoContoEnum.APERTO);
		
		gestioneTavolo.updateTavolo(tavolo.getIdTavolo(), 
									tavolo.getNumposti(), 
									tavolo.getNome(),
									tavolo.getDescrizione(), 
									StatoTavoloEnum.OCCUPATO, 
									false);
	}
	
	public void liberaTavolo(int idTavolo) throws DatabaseException {
		
		/*****************************************************
		 * Verifica della correttezza dello stato del tavolo
		 *****************************************************/
		TreeNodeTavolo tavolo = gestioneTavolo.getTavoloById(idTavolo);
		
		if(tavolo.getStato() != StatoTavoloEnum.OCCUPATO) 
			throw new DatabaseException("Impossibile liberare un tavolo che non è occupato");
		
		/******************************************************
		 * Verifico la presenza di un conto aperto associato al
		 * tavolo
		 ******************************************************/
		List<WrapperConto> listConti = gestioneConto.getContiAperti(idTavolo);
		if(listConti.size() == 0) 
			throw new DatabaseException("Impossibile trovare un conto aperto associato al tavolo");
		else if (listConti.size() > 1) 
			throw new DatabaseException("Sono presenti più conti aperti associati al tavolo");
		
		WrapperConto conto = listConti.get(0);
		
		/*****************************************************
		 * Aggiorno lo stato del conto e del tavolo
		 *****************************************************/
		gestioneTavolo.updateTavolo(tavolo.getIdTavolo(),
									tavolo.getNumposti(),
									tavolo.getNome(),
									tavolo.getDescrizione(),
									StatoTavoloEnum.LIBERO,
									tavolo.isEnabled());
		
		gestioneConto.updateConto(	conto.getIdConto(),
									conto.getPrezzo(), 
									conto.getTimestampChiusura(),
									StatoContoEnum.DAPAGARE);
		
	}
	
}
