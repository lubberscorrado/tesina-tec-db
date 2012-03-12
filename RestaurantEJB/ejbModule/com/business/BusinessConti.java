package com.business;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.exceptions.DatabaseException;
import com.orb.gestioneOggetti.GestioneComanda;
import com.orb.gestioneOggetti.GestioneConto;
import com.restaurant.WrapperComanda;

@Stateless

public class BusinessConti {

	@EJB
	private GestioneComanda gestioneComanda;
	@EJB
	private GestioneConto gestioneConto;
	
	public List<WrapperComanda> getConto(int idTavolo) throws DatabaseException {
		
		/* *****************************************************************************
		 * Ritorna la lista con tutte le comande associate al conto aperto per il
		 * tavolo idTavolo. Se non ci sono conti aperti ritorna una lista vuota.
		 *******************************************************************************/
		
		if(gestioneConto.getContiAperti(idTavolo).size() == 0) {
			return new ArrayList<WrapperComanda>();
		
		} else if( gestioneConto.getContiAperti(idTavolo).size() == 1) {
			List<WrapperComanda> listaComande = gestioneComanda.getComandeByTavolo(idTavolo);
			return listaComande;
		} else 	{
			throw new DatabaseException("Errora durante la ricerca del conto aperto, " +
										"ci sono " +
										gestioneConto.getContiAperti(idTavolo).size() + 
										" associati al tavolo.");

		}
		
	}
}
