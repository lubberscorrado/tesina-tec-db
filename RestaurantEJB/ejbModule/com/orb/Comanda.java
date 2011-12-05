package com.orb;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="comanda")
public class Comanda {
	
	@Id
	@Column(name="idComanda")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idComanda;
	
	@Column(name="note")
	private String note;
	
	@Column(name="prezzo")
	private BigDecimal prezzo;
	
	@Column(name="quantita")
	private int quantita;
	
	@Column(name="stato")
	private String stato;
	
	@Column(name="idTenant")
	private int idTenant;
	
	@ManyToOne
	@JoinColumn(name="idCucina", referencedColumnName="idUtente")
	private UtentePersonale cucinaAssociata;
	
	@ManyToOne
	@JoinColumn(name="idCameriere", referencedColumnName="idUtente")
	private UtentePersonale cameriereAssociato;
	
	@ManyToOne
	@JoinColumn(name="idVoceMenu", referencedColumnName="idVoceMenu")
	private VoceMenu voceMenuAssociata;
	
	@ManyToMany
	@JoinTable(	name="comandavariazione",
				joinColumns = @JoinColumn(name="idComanda", referencedColumnName="idComanda"),
				inverseJoinColumns = @JoinColumn(name="idVariazione", referencedColumnName="idVariazione"))
	
	private List<Variazione> variazioniAssociate;

	public int getIdComanda() {
		return idComanda;
	}

	public void setIdComanda(int idComanda) {
		this.idComanda = idComanda;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public int getIdTenant() {
		return idTenant;
	}

	public void setIdTenant(int idTenant) {
		this.idTenant = idTenant;
	}

	public UtentePersonale getCucinaAssociata() {
		return cucinaAssociata;
	}

	public void setCucinaAssociata(UtentePersonale cucinaAssociata) {
		this.cucinaAssociata = cucinaAssociata;
	}

	public UtentePersonale getCameriereAssociato() {
		return cameriereAssociato;
	}

	public void setCameriereAssociato(UtentePersonale cameriereAssociato) {
		this.cameriereAssociato = cameriereAssociato;
	}

	public VoceMenu getVoceMenuAssociata() {
		return voceMenuAssociata;
	}

	public void setVoceMenuAssociata(VoceMenu voceMenuAssociata) {
		this.voceMenuAssociata = voceMenuAssociata;
	}

	public List<Variazione> getVariazioniAssociate() {
		return variazioniAssociate;
	}

	public void setVariazioniAssociate(List<Variazione> variazioniAssociate) {
		this.variazioniAssociate = variazioniAssociate;
	}
	
}