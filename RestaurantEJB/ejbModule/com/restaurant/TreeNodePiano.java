package com.restaurant;

import com.orb.Area;

public class TreeNodePiano {
	
	private int idPiano;
	private int idTenant;
	private int numero;
	private int numPersone;
	private String nome;
	private String descrizione;
	private boolean enabled;
	
	
	
	public TreeNodePiano(	int idPiano, 
							int idTenant, 
							int numero, 
							int numPersone,
							String nome, 
							String descrizione, 
							boolean enabled) {
		super();
		this.idPiano = idPiano;
		this.idTenant = idTenant;
		this.numero = numero;
		this.numPersone = numPersone;
		this.nome = nome;
		this.descrizione = descrizione;
		this.enabled = enabled;
	}
	
	public int getIdPiano() {
		return idPiano;
	}
	public void setIdPiano(int idPiano) {
		this.idPiano = idPiano;
	}
	public int getIdTenant() {
		return idTenant;
	}
	public void setIdTenant(int idTenant) {
		this.idTenant = idTenant;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public int getNumPersone() {
		return numPersone;
	}
	public void setNumPersone(int numPersone) {
		this.numPersone = numPersone;
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
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
