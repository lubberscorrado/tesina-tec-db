package com.orb;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries({
	@NamedQuery(name = "getTavoliByStato",
				query = "SELECT t FROM Tavolo t WHERE t.stato = :stato AND t.idTenant = :idTenant"),
	@NamedQuery(name = "getTavoli",
				query = "SELECT t FROM Tavolo t WHERE t.idTenant = :idTenant"),
	@NamedQuery(name = "getPrenotazioniByTavoloStato",
				query = "SELECT p FROM Prenotazione p INNER JOIN p.tavoloAppartenenza t " +
						"WHERE t.idTavolo = :idTavolo AND  p.stato = :stato")})

@Table(name="tavolo")
public class Tavolo {
		
	@Id
	@Column(name="idTavolo")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idTavolo;
		
	@Column(name="nome")
	private String nome;

	@Column(name="descrizione")
	private String descrizione;
	
	@Column(name="enabled")
	private boolean enabled;
	
	@Column(name="stato")
	private String stato;

	@Column(name="idTenant")
	private int idTenant;
	
	@OneToMany(mappedBy="tavoloAppartenenza")
	private List<Prenotazione> listaPrenotazioni;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idArea", referencedColumnName="idArea")
	private Area areaAppartenenza;
	
	public int getIdTavolo() {
		return idTavolo;
	}

	public void setIdTavolo(int idTavolo) {
		this.idTavolo = idTavolo;
	}


	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String isStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Area getAreaAppartenenza() {
		return areaAppartenenza;
	}

	public void setAreaAppartenenza(Area areaAppartenenza) {
		this.areaAppartenenza = areaAppartenenza;
	}

	public int getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(int idTenant) {
		this.idTenant = idTenant;
	}
	
	/* Metodi su oggetti Lazy fetched, non necessari in questo caso */
//	public List<Prenotazione> getListPrenotazioni() {
//		return listaPrenotazioni;
//	}
//
//	public void setListPrenotazioni(List<Prenotazione> listPrenotazioni) {
//		this.listaPrenotazioni = listPrenotazioni;
//	}

	public String getStato() {
		return stato;
	}
	
}