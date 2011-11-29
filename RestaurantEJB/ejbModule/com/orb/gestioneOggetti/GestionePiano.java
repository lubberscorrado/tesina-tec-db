package com.orb.gestioneOggetti;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.orb.Area;
import com.orb.Piano;
import com.orb.Tavolo;

@SuppressWarnings("unchecked")

@Stateless
public class GestionePiano {

	@PersistenceContext(unitName="ejbrelationships") 
	private EntityManager em;
	
	
	public GestionePiano() {}
	
	public Piano aggiungiPiano(	int idTenant, 
								int numero, 
								String nome, 
								String descrizione, 
								boolean enabled) {
		
		Piano piano = new Piano();
		piano.setIdTenant(idTenant);
		piano.setNumero(numero);
		piano.setNome(nome);
		piano.setDescrizione(descrizione);
		piano.setEnabled(enabled);
		
		try {
			em.persist(piano);
		}catch(Exception e) {
			System.out.println("Errore durante l'inserimento del piano (GestionePiano.aggiungiPiano): " + e.getMessage());
			return null;
		}
		
		return piano;
	
	}
	
	public List<Piano> getPiani(int idTenant) {
		Query query = em.createNamedQuery("getPiani");
		query.setParameter("idTenant", idTenant);
		List<Piano> listaPiano;
		
		try {
			listaPiano = (List<Piano>)query.getResultList();
		} catch (Exception e) {
			System.out.println("Errore durante l'acquisizione dei piani (GestionePiano.getPiani)" + e.getMessage());
			return null;
		}
		return listaPiano;
	}
	
}
