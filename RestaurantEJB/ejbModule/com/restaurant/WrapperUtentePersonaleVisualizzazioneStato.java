package com.restaurant;

import com.orb.UtentePersonale;

public class WrapperUtentePersonaleVisualizzazioneStato {
	
	private int idUtentePersonale;
	private String cognome;
	private String nome;
	private String username;
	private String password;
	private boolean isSuperAdmin = false;
	private boolean isAdmin;
	private boolean isCameriere;
	private boolean isCucina;
	private boolean isCassiere;
	
	public WrapperUtentePersonaleVisualizzazioneStato() {
		
	}
	
	public WrapperUtentePersonaleVisualizzazioneStato(UtentePersonale utentePersonale) {
		this.setIdUtentePersonale(utentePersonale.getIdUtente());
		this.setNome(utentePersonale.getNome());
		this.setCognome(utentePersonale.getCognome());
		this.setUsername(utentePersonale.getUsername());
		this.setPassword(utentePersonale.getPassword());
		this.setAdmin(utentePersonale.isAdmin());
		this.setCameriere(utentePersonale.isCameriere());
		this.setCucina(utentePersonale.isCucina());
		this.setCassiere(utentePersonale.isCassiere());
	}


	public boolean isCassiere() {
		return isCassiere;
	}


	public void setCassiere(boolean isCassiere) {
		this.isCassiere = isCassiere;
	}


	public boolean isCucina() {
		return isCucina;
	}


	public void setCucina(boolean isCucina) {
		this.isCucina = isCucina;
	}


	public boolean isCameriere() {
		return isCameriere;
	}


	public void setCameriere(boolean isCameriere) {
		this.isCameriere = isCameriere;
	}


	public boolean isAdmin() {
		return isAdmin;
	}


	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getCognome() {
		return cognome;
	}


	public void setCognome(String cognome) {
		this.cognome = cognome;
	}


	public int getIdUtentePersonale() {
		return idUtentePersonale;
	}


	public void setIdUtentePersonale(int idUtentePersonale) {
		this.idUtentePersonale = idUtentePersonale;
	}

	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}

	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
