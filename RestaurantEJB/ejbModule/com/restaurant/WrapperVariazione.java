package com.restaurant;

import java.math.BigDecimal;

import com.orb.Variazione;

public class WrapperVariazione {
	
	private int idVariazione;
	private String nome;
	private String descrizione;
	private BigDecimal prezzoVariazione;
	private boolean removed;
	
	public WrapperVariazione(Variazione variazione) {
		this.idVariazione = variazione.getIdVariazione();
		this.nome = variazione.getNome();
		this.descrizione = variazione.getDescrizone();
		this.prezzoVariazione = variazione.getPrezzo();
		this.removed = variazione.getRemoved();
	}
	
	public WrapperVariazione(int id, String nome, String descrizione, BigDecimal prezzo) {
		this.idVariazione = id;
		this.nome = nome;
		this.descrizione = descrizione;
		this.prezzoVariazione = prezzo;
	}
	
	public int getIdVariazione() {
		return idVariazione;
	}
	public void setIdVariazione(int idVariazione) {
		this.idVariazione = idVariazione;
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
	public BigDecimal getPrezzoVariazione() {
		return prezzoVariazione;
	}
	public void setPrezzoVariazione(BigDecimal prezzoVariazione) {
		this.prezzoVariazione = prezzoVariazione;
	}

	public boolean getRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
	
	

}
