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
									int idArea) throws DatabaseException {
		
		Tavolo tavolo = new Tavolo();
		tavolo.setIdTenant(idTenant);
		tavolo.setNome(nome);
		tavolo.setDescrizione(descrizione);
		tavolo.setEnabled(enabled);
		tavolo.setStato(stato);
		
		Area area = em.find(Area.class, idArea);
		if(area == null)
			throw new DatabaseException("Impossibile trovare l'area di appartenenza del tavolo");
		
		tavolo.setAreaAppartenenza(area);
		
		try {
			em.persist(tavolo);
		} catch (Exception e) {
			throw new DatabaseException("Errore durante l'inserimento del tavolo + " +
										"("+ e.toString()+")");
			
		}
		return tavolo;
	}
	
	/** 
	 * Ritorna lo stato di tutti i tavoli a partire dall'id del cliente
	 * @param idTenant Id del cliente
	 * @return Lista di oggetti StatoTavolo che incapsulano le informazioni
	 *  sullo stato di un tavolo
	 * @throws DatabaseException Oggetto di eccezione che incapsula le informazioni
	 * sull'errore che si è verificato.
	 */
	
	public List<StatoTavolo> getStatoTavoli(int idTenant) throws DatabaseException {
		
		Query query = em.createNamedQuery("getTavoli");
		query.setParameter("idTenant", idTenant);
		
		List<Tavolo> listTavoli = null;
		
		try {
			listTavoli = query.getResultList();
		} catch(Exception e) {
			throw new DatabaseException("Errore durante la ricerca dei tavoli ("+ e.toString()+")");
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
				throw new DatabaseException("Errore durante la ricerca dei piani e delle aree " +
											"(" + e.toString() + ")");
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
	 * Ritorna la lista dei tavoli associati ad una certa area
	 * @param idArea Id dell'area della quale si vogliono ottenere i tavoli
	 * @return Lista di oggetti TreeNodeTavolo che incapsulano le informazioni su
	 * un tavolo
	 * @throws DatabaseException Generica eccezione durante le operazioni sul database
	 */
	
	public List<TreeNodeTavolo> getTavoloByArea(int idArea) throws DatabaseException {
		
	
		Area area = em.find(Area.class, idArea);
		
		if(area == null) 
			throw new DatabaseException("Area non trovata");
		
		List<Tavolo> listaTavoli;
		
		try {
			listaTavoli = area.getTavoli();	
		}catch(Exception e) {
			throw new DatabaseException("Errore durante la ricerca dei tavoli associati ad un area +" +
										"(" + e.getMessage() +")");
		}
		
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