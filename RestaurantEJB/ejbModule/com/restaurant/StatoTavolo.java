package com.restaurant;

import com.orb.Area;
import com.orb.Conto;
import com.orb.Piano;
import com.orb.Tavolo;
import com.orb.UtentePersonale;

public class StatoTavolo {
	
	private int idTavolo;
	private String nomeTavolo;
	private int numeroPiano;
	private String nomeArea;
	private int numeroPersone;
	private int numPosti;
	private String statoTavolo;
	private String cameriere;
		
	
	public StatoTavolo(Tavolo t, Area a, Piano p, UtentePersonale u, Conto c) {
		
		super();
		this.idTavolo = t.getIdTavolo();
		this.nomeTavolo = t.getNome();
		this.numeroPiano = p.getNumero();
		this.nomeArea = a.getNome();
		this.numPosti = t.getNumposti();
		this.statoTavolo = t.getStato().toString();
		this.numeroPersone = 0;
		
		/* Setto il numero di persone assegnato al tavolo solo se il conto passato come parametro non è nullo */
		if(c != null) 
			this.numeroPersone = c.getNumeroPersone();
		else
			this.numeroPersone = 0;
		
		/* Setto il cameriere associato al tavolo solo se non è nullo */
		if(u != null)
			this.cameriere = u.getNome() + " " + u.getCognome();
		else
			this.cameriere = "Non definito";
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

	public int getNumeroPersone() {
		return numeroPersone;
	}

	public void setNumeroPersone(int numeroPersone) {
		this.numeroPersone = numeroPersone;
	}
}
