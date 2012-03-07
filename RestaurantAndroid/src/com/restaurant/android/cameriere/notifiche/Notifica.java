package com.restaurant.android.cameriere.notifiche;

public class Notifica {
	
	private String notificationText = "";
	private String notificationType = "";
	private String dateTime = "";
	private int idTavolo = -1;
	private String nomeTavolo = "";
	private int idOrdinazione = - 1;
	private String nomeOrdinazione = "";
	private int idConto = -1;
	
	public String getNotificationText() {
		return notificationText;
	}
	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public int getIdTavolo() {
		return idTavolo;
	}
	public void setIdTavolo(int idTavolo) {
		this.idTavolo = idTavolo;
	}
	public int getIdOrdinazione() {
		return idOrdinazione;
	}
	public void setIdOrdinazione(int idOrdinazione) {
		this.idOrdinazione = idOrdinazione;
	}
	public int getIdConto() {
		return idConto;
	}
	public void setIdConto(int idConto) {
		this.idConto = idConto;
	}
	public String getNomeTavolo() {
		return nomeTavolo;
	}
	public void setNomeTavolo(String nomeTavolo) {
		this.nomeTavolo = nomeTavolo;
	}
	public String getNomeOrdinazione() {
		return nomeOrdinazione;
	}
	public void setNomeOrdinazione(String nomeOrdinazione) {
		this.nomeOrdinazione = nomeOrdinazione;
	}
	
}