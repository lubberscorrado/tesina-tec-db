package com.orb.gestioneOggetti;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.crypto.Data;

import com.exceptions.DatabaseException;
import com.orb.Area;
import com.orb.Piano;
import com.orb.Prenotazione;
import com.orb.Tavolo;
import com.restaurant.StatoTavolo;
import com.restaurant.TreeNodeArea;
import com.restaurant.TreeNodeTavolo;

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
	
	/** Funzione che ritorna lo stato di tutti i tavoli a
	 * partire dall'id del cliente
	 * @param idTenant Id del cliente
	 * @return Lista di oggetti StatoTavolo che incapsulano le informazioni
	 * sullo stato di un tavolo
	 * @throws DatabaseException 
	 */
	public List<StatoTavolo> getStatoTavoli(int idTenant) throws DatabaseException {
		
		Query query = em.createNamedQuery("getTavoli");
		query.setParameter("idTenant", idTenant);
		
		List<Tavolo> listTavoli = null;
		
		try {
			listTavoli = query.getResultList();
		} catch(Exception e) {
			/* NB: potrebbe anche essere un'eccezione data dalla violazione di un
			 * vincolo di integrità referenziale */
			throw new DatabaseException(DatabaseException.ERRORE_CONNESSIONE_DATABSE);
		}
		
	
		Iterator<Tavolo> it = listTavoli.iterator();
		List<StatoTavolo> listaStatoTavolo = new ArrayList<StatoTavolo>();
	
		Area areaAppartenenza;
		Piano pianoAssociato;
		
		while(it.hasNext()) {
			
			Tavolo t = it.next();
		
			try {
				
				areaAppartenenza = t.getAreaAppartenenza();
				pianoAssociato = areaAppartenenza.getPianoAppartenenza();
				
			} catch(Exception e) {
				
				throw new DatabaseException(DatabaseException.ERRORE_CONNESSIONE_DATABSE);
				
			}
			
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
	
	/** 
	 * Ritorna la lista dei tavoli associati ad una deteriminata area 
	 */
	
	public List<TreeNodeTavolo> getTavoloByArea(int idArea) throws DatabaseException {
		
		Area area = em.find(Area.class, idArea);
		
		if(area == null) {
			
			throw new DatabaseException(DatabaseException.OGGETTO_NON_TROVATO);
		}
		
		List<Tavolo> listaTavoli = area.getTavoli();	
		
			
		List<TreeNodeTavolo> listaTreeNodeTavolo = new ArrayList<TreeNodeTavolo>();
		
		Iterator<Tavolo> it = listaTavoli.iterator();
		
		while(it.hasNext()) {
			Tavolo t = it.next();
			listaTreeNodeTavolo.add(new TreeNodeTavolo(	t.getIdTavolo(),
														t.getNome(),
														t.getDescrizione(),
														t.isEnabled(),
														t.getStato(),
														t.getIdTenant()));
		}
		
		return listaTreeNodeTavolo;
	}
	
}