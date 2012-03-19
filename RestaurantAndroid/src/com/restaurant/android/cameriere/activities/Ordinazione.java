package com.restaurant.android.cameriere.activities;

import java.io.Serializable;
import java.util.ArrayList;

public class Ordinazione implements Serializable {
	
	private String nome;
	private String note;
	private int quantita;
	private int idTavolo;
	private int idOrdinazione;
	private int idRemotoOrdinazione;
	private int idVoceMenu;
	private String stato;
	private boolean selezionata;
	private ArrayList<Integer> idVariazioni;
	
	
	public Ordinazione() {
		this.nome="";
		this.note="";
		this.quantita = 0;
		this.idVoceMenu = 0;
		this.idOrdinazione = 0;
		this.idRemotoOrdinazione = 0;
		this.stato ="";
		selezionata = true;
		idVariazioni = new ArrayList<Integer>();
	}
	
	public int getIdRemotoOrdinazione() {
		return idRemotoOrdinazione;
	}

	public void setIdRemotoOrdinazione(int idRemotoOrdinazione) {
		this.idRemotoOrdinazione = idRemotoOrdinazione;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getIdTavolo() {
		return idTavolo;
	}
	public void setIdTavolo(int idTavolo) {
		this.idTavolo = idTavolo;
	}
	public int getIdVoceMenu() {
		return idVoceMenu;
	}
	public void setIdVoceMenu(int idVoceMenu) {
		this.idVoceMenu = idVoceMenu;
	}
	public int getIdOrdinazione() {
		return idOrdinazione;
	}
	public void setIdOrdinazione(int idOrdinazione) {
		this.idOrdinazione = idOrdinazione;
	}
	
	public ArrayList<Integer> getListVariazioni() {
		return idVariazioni;
	}

	public boolean isSelezionata() {
		return selezionata;
	}

	public void setSelezionata(boolean selezionata) {
		this.selezionata = selezionata;
	}
	
	
}
