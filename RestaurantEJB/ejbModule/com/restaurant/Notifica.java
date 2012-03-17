package com.restaurant;

import java.util.Date;

public class Notifica {
	
	private TipoNotificaEnum tipoNotifica;
	private int idVoceMenu;
	private int idTavolo;
	private String nomeTavolo;
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
	public int getIdVoceMenu() {
		return idVoceMenu;
	}
	public void setIdVoceMenu(int idVoceMenu) {
		this.idVoceMenu = idVoceMenu;
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
