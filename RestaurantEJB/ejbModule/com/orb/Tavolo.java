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
	@NamedQuery(name = "getTavoli",
				query = "SELECT t FROM Tavolo t WHERE t.idTenant = :idTenant")})

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
	
	@ManyToOne
	@JoinColumn(name="idArea", referencedColumnName="idArea")
	private Area areaAppartenenza;
	
	

	public Tavolo(	String nome, 
					String descrizione,
					boolean enabled, 
					String stato, 
					int idTenant,
					Area areaAppartenenza) {
		super();
		this.nome = nome;
		this.descrizione = descrizione;
		this.enabled = enabled;
		this.stato = stato;
		this.idTenant = idTenant;
		this.areaAppartenenza = areaAppartenenza;
	}

	public Tavolo() {}

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
	
	public List<Prenotazione> getListPrenotazioni() {
		return listaPrenotazioni;
	}

	public void setListPrenotazioni(List<Prenotazione> listPrenotazioni) {
		this.listaPrenotazioni = listPrenotazioni;
	}

	public String getStato() {
		return stato;
	}
	
}