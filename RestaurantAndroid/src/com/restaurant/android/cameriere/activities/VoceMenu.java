package com.restaurant.android.cameriere.activities;

import java.math.BigDecimal;

public class VoceMenu {
	private int idVoceMenu;
	private String nome;
	private String descrizione;
	private int idCategoria;
	private BigDecimal prezzo;
	private boolean categoria;
	
	public VoceMenu(int idVoceMenu, String nome, String descrizione, int idCategoria, BigDecimal prezzo, boolean categoria) {
		super();
		this.setIdVoceMenu(idVoceMenu);
		this.nome = nome;
		this.descrizione = descrizione;
		this.idCategoria = idCategoria;
		this.categoria = categoria;
		this.prezzo = prezzo;
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
	public boolean isCategoria() {
		return categoria;
	}

	public void setCategoria(boolean categoria) {
		this.categoria = categoria;
	}
	public int getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}
	public BigDecimal getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}

}
