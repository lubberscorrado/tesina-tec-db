package com.restaurant.android.cameriere.activities;

public class VoceMenu {
	private String nome;
	private String descrizione;
	private int idCategoria;
	private int idCategoriaPadre;
	private boolean categoria;
	
	public VoceMenu(String nome, String descrizione, int idCategoria, boolean categoria) {
		super();
		this.nome = nome;
		this.descrizione = descrizione;
		this.idCategoria = idCategoria;
		this.categoria = categoria;
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
	
}
