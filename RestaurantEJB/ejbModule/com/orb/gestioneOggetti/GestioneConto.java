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
import com.orb.Conto;
import com.orb.StatoContoEnum;
import com.orb.Tavolo;
import com.orb.UtentePersonale;
import com.restaurant.WrapperConto;

@SuppressWarnings("unchecked") 
@Stateless
public class GestioneConto {
	
	@PersistenceContext(unitName="ejbrelationships")
	private EntityManager em;
	
	public GestioneConto() {};
	
	public WrapperConto aggiungiConto(	int idTenant,
										int idTavolo,
										int idCameriere,
										BigDecimal prezzo,
										Timestamp timestampApertura,
										Timestamp timestampChiusura,
										StatoContoEnum stato) throws DatabaseException {
		
		try {
							
			Tavolo tavolo = em.find(Tavolo.class, idTavolo);
			UtentePersonale cameriere = em.find(UtentePersonale.class, idCameriere);
			
			if(tavolo ==null)
				throw new DatabaseException("Errore durante la ricerca del tavolo associato al conto");
			else if(cameriere == null)
				throw new DatabaseException("Errore durante la ricerca del cameriere da associare al conto");
			
			Conto conto = new Conto();
			conto.setIdTenant(idTenant);
			conto.setPrezzo(prezzo);
			conto.setTimeStampApertura(timestampApertura);
			conto.setTimeStampChiusura(timestampChiusura);
			conto.setStato(stato);
			conto.setTavoloAppartenenza(tavolo);
			conto.setCameriereAssociato(cameriere);
			
			em.persist(conto);
			return new WrapperConto(conto);
			
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'inserimento del conto +" +
										"("+ e.toString() +")" );
		}
		
	}
	
	
	
	public List<WrapperConto> getContiAperti(int idTavolo) throws DatabaseException {
	
		Query query = em.createQuery("SELECT c FROM Conto c WHERE c.stato = 'APERTO' " +
									 "AND c.idTavolo = :idTavolo");
		query.setParameter("idTavolo", idTavolo);
		
		List<WrapperConto> listConti = new ArrayList<WrapperConto>();
		
		for(Conto conto :  ((List<Conto>) query.getResultList())) 
			listConti.add(new WrapperConto(conto));
			
		return listConti;
			
	}

	
}