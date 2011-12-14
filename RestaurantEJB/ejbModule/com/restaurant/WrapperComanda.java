package com.restaurant;

import java.math.BigDecimal;

import com.orb.Comanda;
import com.orb.StatoComandaEnum;

public class WrapperComanda {
	private int idTenant;
	private int idComanda;
	private String note;
	private BigDecimal prezzo;
	private int quantita;
	private StatoComandaEnum stato;
	
	public WrapperComanda(Comanda comanda) {
		this.idTenant = comanda.getIdTenant();
		this.idComanda = comanda.getIdComanda();
		this.note = comanda.getNote();
		this.prezzo = comanda.getPrezzo();
		this.quantita = comanda.getQuantita();
		this.stato = comanda.getStato();
	}
	
	
	public int getIdTenant() {
		return idTenant;
	}
	public void setIdTenant(int idTenant) {
		this.idTenant = idTenant;
	}
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
	public StatoComandaEnum getStato() {
		return stato;
	}
	public void setStato(StatoComandaEnum stato) {
		this.stato = stato;
	}
	
	
}
