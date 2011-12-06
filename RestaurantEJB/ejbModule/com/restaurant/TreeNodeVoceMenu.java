package com.restaurant;

import java.math.BigDecimal;

import com.orb.VoceMenu;

public class TreeNodeVoceMenu {
	
	private int idVoceMenu;
	private String nome;
	private String descrizione;
	private BigDecimal prezzo;
	
	public TreeNodeVoceMenu(VoceMenu voceMenu) {
		this.idVoceMenu = voceMenu.getIdVoceMenu();
		this.nome = voceMenu.getNome();
		this.descrizione = voceMenu.getDescrizione();
		this.prezzo = voceMenu.getPrezzo();
		
	}
	
	public int getIdVoceMenu() {
		return idVoceMenu;
	}
	public void setIdVoceMenu(int idVoceMenu) {
		this.idVoceMenu = idVoceMenu;
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
	public BigDecimal getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}
	
	

}
