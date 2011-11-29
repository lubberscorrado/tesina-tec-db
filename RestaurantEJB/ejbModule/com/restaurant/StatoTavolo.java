package com.restaurant;

public class StatoTavolo {
	
	private int idTavolo;
	private String nomeTavolo;
	private int numeroPiano;
	private String nomeArea;
	private int numPosti;
	private String statoTavolo;
	private String cameriere;
	
	
	
	public StatoTavolo(	int idTavolo, 
						String nomeTavolo, 
						int numeroPiano,
						String nomeArea, 
						int numPosti, 
						String statoTavolo, 
						String cameriere) {
		
		super();
		this.idTavolo = idTavolo;
		this.nomeTavolo = nomeTavolo;
		this.numeroPiano = numeroPiano;
		this.nomeArea = nomeArea;
		this.numPosti = numPosti;
		this.statoTavolo = statoTavolo;
		this.cameriere = cameriere;
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
