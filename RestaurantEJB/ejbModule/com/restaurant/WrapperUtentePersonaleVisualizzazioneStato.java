package com.restaurant;

import com.orb.StatoUtentePersonaleEnum;
import com.orb.UtentePersonale;

public class WrapperUtentePersonaleVisualizzazioneStato {
	
	private int idUtentePersonale;
	private String cognome;
	private String nome;
	private String username;
	private StatoUtentePersonaleEnum tipoAccesso;
	
	public WrapperUtentePersonaleVisualizzazioneStato() {
		
	}
	
	public WrapperUtentePersonaleVisualizzazioneStato(UtentePersonale utentePersonale, StatoUtentePersonaleEnum tipoAccesso) {
		this.setIdUtentePersonale(utentePersonale.getIdUtente());
		this.setNome(utentePersonale.getNome());
		this.setCognome(utentePersonale.getCognome());
		this.setUsername(utentePersonale.getUsername());
		this.setTipoAccesso(tipoAccesso);
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

	public StatoUtentePersonaleEnum getTipoAccesso() {
		return tipoAccesso;
	}

	public void setTipoAccesso(StatoUtentePersonaleEnum tipoAccesso) {
		this.tipoAccesso = tipoAccesso;
	}

}
