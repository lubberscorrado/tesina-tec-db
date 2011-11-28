package com.orb;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tavolo")
public class Tavolo implements Serializable {
		
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
	
	@ManyToOne
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
	
	
	
}