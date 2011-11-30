package com.restaurant;

public class TreeNodeArea {

	
	private int idArea;
	private int idTenant;
	private String nome;
	private String descrizione;
	private boolean enabled;
	
	
	
	public TreeNodeArea(	int idArea,
							int idTenant, 
							String nome,
							String descrizione, 
							boolean enabled) {
		super();
		this.idArea = idArea;
		this.idTenant = idTenant;
		this.nome = nome;
		this.descrizione = descrizione;
		this.enabled = enabled;
	}
	
	public int getIdArea() {
		return idArea;
	}
	public void setIdArea(int idArea) {
		this.idArea = idArea;
	}
	public int getIdTenant() {
		return idTenant;
	}
	public void setIdTenant(int idTenant) {
		this.idTenant = idTenant;
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
