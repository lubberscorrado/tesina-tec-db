package com.orb.gestioneOggetti;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.orb.Area;
import com.orb.Piano;
import com.orb.Prenotazione;
import com.orb.Tavolo;
import com.restaurant.StatoTavolo;

@SuppressWarnings("unchecked")
@Stateless
public class GestioneTavolo{

	@PersistenceContext(unitName="ejbrelationships") 
	private EntityManager em;
		
	public GestioneTavolo() {}
	
	public Tavolo aggiungiTavolo(	int idTenant, 
									String nome, 
									String stato,
									String descrizione, 
									boolean enabled,
									Area areaAssociata) {
		
		Tavolo tavolo = new Tavolo();
		tavolo.setIdTenant(idTenant);
		tavolo.setNome(nome);
		tavolo.setDescrizione(descrizione);
		tavolo.setEnabled(enabled);
		tavolo.setStato(stato);
		tavolo.setAreaAppartenenza(areaAssociata);
		em.persist(tavolo);
		return tavolo;
		
	}
	
	public List<StatoTavolo> getStatoTavoli(int idTenant) {
		
		Query query = em.createNamedQuery("getTavoli");
		query.setParameter("idTenant", idTenant);
		
		List<Tavolo> listTavoli = query.getResultList();
		Iterator<Tavolo> it = listTavoli.iterator();
				
		List<StatoTavolo> listaStatoTavolo = new ArrayList<StatoTavolo>();
		
		while(it.hasNext()) {
			
			Tavolo t = it.next();
			
			Area areaAppartenenza = t.getAreaAppartenenza();
			Piano pianoAssociato = areaAppartenenza.getPianoAppartenenza();
			
			listaStatoTavolo.add(new StatoTavolo(	t.getIdTavolo(),
													t.getNome(),
													pianoAssociato.getNumero(),
													areaAppartenenza.getNome(),
													0,
													t.getStato(),
													"TBD"));
		}
		return listaStatoTavolo;
	
	}
}