package com.orb.gestioneOggetti;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.orb.Area;
import com.orb.Piano;

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
		
		em.persist(piano);
		
		return piano;
		
	}
	
		
}
