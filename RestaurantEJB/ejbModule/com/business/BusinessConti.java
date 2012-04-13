package com.business;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.exceptions.DatabaseException;
import com.orb.gestioneOggetti.GestioneComanda;
import com.orb.gestioneOggetti.GestioneConto;
import com.restaurant.WrapperComanda;
import com.restaurant.WrapperConto;

@Stateless

public class BusinessConti {

	@EJB
	private GestioneComanda gestioneComanda;
	@EJB
	private GestioneConto gestioneConto;
	
	/**
	 *	Ritorna la lista con tutte le comande associate al conto aperto per il
	 *	tavolo idTavolo. Se non ci sono conti aperti ritorna una lista vuota.
	 *
	 * @param idTavolo Id del tavolo per il quale si vuole ottenere il conto
	 * @return Lista di oggetti WrapperComanda che rappresentano le comande
	 * appartenti al conto aperto
	 * @throws DatabaseException Eccezione che incapsula le informazioni sull'ultimo
	 * errore verificatosi
	 * @author Guerri Marco
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public List<WrapperComanda> getConto(int idTavolo) throws DatabaseException {
				
		List<WrapperConto> listaConti = gestioneConto.getContiAperti(idTavolo);
		if(listaConti.size() == 0) {
			
			return new ArrayList<WrapperComanda>();
		
		} else if(listaConti.size() == 1) {
			
			int idConto = listaConti.get(0).getIdConto();
			List<WrapperComanda> listaComande = gestioneComanda.getComandeByConto(idConto);
			return listaComande;
		
		} else 	{
			throw new DatabaseException("Errore durante la ricerca del conto aperto, " +
										"ci sono " +
										gestioneConto.getContiAperti(idTavolo).size() + 
										" associati al tavolo.");

		}
		
	}
}
