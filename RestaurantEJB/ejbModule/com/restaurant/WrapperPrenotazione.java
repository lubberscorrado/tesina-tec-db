package com.restaurant;

import java.sql.Time;
import java.util.Date;

import com.orb.Prenotazione;
import com.orb.Tavolo;

public class WrapperPrenotazione {
	
	private int idPrenotazione;
	private Time ora;
	private Date data;
	private String stato;
	private int idTenant;
	private int numpersone;
	private String nomecliente;
	private Tavolo tavoloAppartenenza;
	
	public WrapperPrenotazione(Prenotazione prenotazione) {
		this.setIdPrenotazione(prenotazione.getIdPrenotazione());
		this.setOra(prenotazione.getOra());
		this.setData(prenotazione.getData());
		this.setStato(prenotazione.getStato());
		this.setIdTenant(prenotazione.getIdTenant());
		this.setNumpersone(prenotazione.getNumpersone());
		this.setNomecliente(prenotazione.getNomecliente());
		this.setTavoloAppartenenza(prenotazione.getTavoloAppartenenza());
	}

	
	public int getIdPrenotazione() {
		return idPrenotazione;
	}
	public void setIdPrenotazione(int idPrenotazione) {
		this.idPrenotazione = idPrenotazione;
	}
	
	public Time getOra() {
		return ora;
	}
	public void setOra(Time ora) {
		this.ora = ora;
	}
	
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	
	public int getIdTenant() {
		return idTenant;
	}
	public void setIdTenant(int idTenant) {
		this.idTenant = idTenant;
	}
	
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	
	public int getNumpersone() {
		return numpersone;
	}
	public void setNumpersone(int numpersone) {
		this.numpersone = numpersone;
	}
	
	public String getNomecliente() {
		return nomecliente;
	}
	public void setNomecliente(String nomecliente) {
		this.nomecliente = nomecliente;
	}
	
	public Tavolo getTavoloAppartenenza() {
		return tavoloAppartenenza;
	}
	public void setTavoloAppartenenza(Tavolo tavoloAppartenenza) {
		this.tavoloAppartenenza = tavoloAppartenenza;
	}

}