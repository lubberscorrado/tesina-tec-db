package com.restaurant;

import com.orb.Area;
import com.orb.Piano;

public class TreeNodePiano {
	
	private int idPiano;
	private int idTenant;
	private int numero;
	private String nome;
	private String descrizione;
	private boolean enabled;
	
	
	
	public TreeNodePiano(Piano piano) {
		super();
		this.idPiano = piano.getIdPiano();
		this.idTenant = piano.getIdTenant();
		this.numero = piano.getNumero();
		this.nome = piano.getNome();
		this.descrizione = piano.getDescrizione();
		this.enabled = piano.isEnabled();
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
