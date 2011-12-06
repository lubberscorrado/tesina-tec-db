package com.restaurant;

import com.orb.Categoria;

public class TreeNodeCategoria {
	
	private int idCategoria;
	private String nome;
	private String descrizione;
	
	public TreeNodeCategoria(Categoria categoria) {
		this.idCategoria = categoria.getIdCategoria();
		this.nome = categoria.getNome();
		this.descrizione = categoria.getDescrizione();
	}
	
	public int getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
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
	
	
	

}
