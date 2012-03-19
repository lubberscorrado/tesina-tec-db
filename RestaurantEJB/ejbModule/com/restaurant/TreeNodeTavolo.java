package com.restaurant;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.orb.Area;
import com.orb.Prenotazione;
import com.orb.StatoTavoloEnum;
import com.orb.Tavolo;

public class TreeNodeTavolo {
	
	private int idTavolo;
	private String nome;
	private String descrizione;
	private boolean enabled;
	private StatoTavoloEnum stato;
	private Date lastModified;
	private int idTenant;
	private int numposti;
	
	public TreeNodeTavolo(Tavolo tavolo) {
		super();
		this.idTavolo = tavolo.getIdTavolo();
		this.nome = tavolo.getNome();
		this.descrizione = tavolo.getDescrizione();
		this.enabled = tavolo.isEnabled();
		this.stato = tavolo.getStato();
		this.idTenant = tavolo.getIdTenant();
		this.numposti = tavolo.getNumposti();
		this.lastModified = tavolo.getLastModified();
	}
	
	
	public TreeNodeTavolo(	int idTavolo, 
							String nome, 
							String descrizione,
							boolean enabled, 
							StatoTavoloEnum stato, 
							int idTenant, 
							int numposti) {
		super();
		this.idTavolo = idTavolo;
		this.nome = nome;
		this.descrizione = descrizione;
		this.enabled = enabled;
		this.stato = stato;
		this.idTenant = idTenant;
		this.numposti = numposti;
		this.lastModified = new Date();
	}


	public int getNumposti() {
		return numposti;
	}


	public void setNumposti(int numposti) {
		this.numposti = numposti;
	}


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
	public StatoTavoloEnum getStato() {
		return stato;
	}
	public void setStato(StatoTavoloEnum stato) {
		this.stato = stato;
	}
	public int getIdTenant() {
		return idTenant;
	}
	public void setIdTenant(int idTenant) {
		this.idTenant = idTenant;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
}
