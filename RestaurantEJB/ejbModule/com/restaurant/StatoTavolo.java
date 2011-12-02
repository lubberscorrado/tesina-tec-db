package com.restaurant;

import com.orb.Area;
import com.orb.Piano;
import com.orb.Tavolo;

public class StatoTavolo {
	
	private int idTavolo;
	private String nomeTavolo;
	private int numeroPiano;
	private String nomeArea;
	private int numPosti;
	private String statoTavolo;
	private String cameriere;
	
	
	
	public StatoTavolo(Tavolo t, Area a, Piano p) {
		
		super();
		this.idTavolo = t.getIdTavolo();
		this.nomeTavolo = t.getNome();
		this.numeroPiano = p.getNumero();
		this.nomeArea = a.getNome();
		this.numPosti = t.getNumposti();
		this.statoTavolo = t.getStato();
		this.cameriere = "TODO";
	}
	
	public int getIdTavolo() {
		return idTavolo;
	}
	public void setIdTavolo(int idTavolo) {
		this.idTavolo = idTavolo;
	}
	public String getNomeTavolo() {
		return nomeTavolo;
	}
	public void setNomeTavolo(String nomeTavolo) {
		this.nomeTavolo = nomeTavolo;
	}
	public int getNumeroPiano() {
		return numeroPiano;
	}
	public void setNumeroPiano(int numeroPiano) {
		this.numeroPiano = numeroPiano;
	}
	public String getNomeArea() {
		return nomeArea;
	}
	public void setNomeArea(String nomeArea) {
		this.nomeArea = nomeArea;
	}
	public int getNumPosti() {
		return numPosti;
	}
	public void setNumPosti(int numPosti) {
		this.numPosti = numPosti;
	}
	public String getStatoTavolo() {
		return statoTavolo;
	}
	public void setStatoTavolo(String statoTavolo) {
		this.statoTavolo = statoTavolo;
	}
	public String getCameriere() {
		return cameriere;
	}
	public void setCameriere(String cameriere) {
		this.cameriere = cameriere;
	}
}
