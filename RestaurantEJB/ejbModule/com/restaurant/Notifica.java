package com.restaurant;

import java.util.Date;

public class Notifica {
	
	private TipoNotificaEnum tipoNotifica;
	private String nomeTavolo;
	private String voceMenu;
	private int idTavolo;
	private Date data;
	
	public TipoNotificaEnum getTipoNotifica() {
		return tipoNotifica;
	}
	public void setTipoNotifica(TipoNotificaEnum tipoNotifica) {
		this.tipoNotifica = tipoNotifica;
	}
	public String getNomeTavolo() {
		return nomeTavolo;
	}
	public void setNomeTavolo(String nomeTavolo) {
		this.nomeTavolo = nomeTavolo;
	}
	public String getVoceMenu() {
		return voceMenu;
	}
	public void setVoceMenu(String voceMenu) {
		this.voceMenu = voceMenu;
	}
	public int getIdTavolo() {
		return idTavolo;
	}
	public void setIdTavolo(int idTavolo) {
		this.idTavolo = idTavolo;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	
	
}
