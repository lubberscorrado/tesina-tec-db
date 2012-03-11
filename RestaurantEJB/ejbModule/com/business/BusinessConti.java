package com.business;


import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.exceptions.DatabaseException;
import com.orb.gestioneOggetti.GestioneComanda;
import com.restaurant.WrapperComanda;

@Stateless

public class BusinessConti {

	@EJB
	private GestioneComanda gestioneComanda;
	
	public List<WrapperComanda> getConto(int idTavolo) throws DatabaseException {
		
		/* *****************************************************************************
		 * Ritorna la lista con tutte le comande associate al conto aperto per il
		 * tavolo idTavolo
		 *******************************************************************************/
		
		List<WrapperComanda> listaComande = gestioneComanda.getComandeByTavolo(idTavolo);
		return listaComande;
		
	}
}
