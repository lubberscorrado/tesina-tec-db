package com.restaurant.android.cucina.comanda;

import java.io.Serializable;

public class Variazione implements Serializable {
	private String nomeVariazione;
	private int idVariazione;
	
	public String getNomeVariazione() {
		return nomeVariazione;
	}
	public void setNomeVariazione(String nomeVariazione) {
		this.nomeVariazione = nomeVariazione;
	}
	public int getIdVariazione() {
		return idVariazione;
	}
	public void setIdVariazione(int idVariazione) {
		this.idVariazione = idVariazione;
	}
	
	
}
