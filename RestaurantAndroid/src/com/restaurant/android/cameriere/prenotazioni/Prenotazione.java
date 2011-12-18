package com.restaurant.android.cameriere.prenotazioni;

/**
 * Classe che contiene i dati di una prenotazione
 * @author Fabio Pierazzi
 */
public class Prenotazione {
	
	/** Data della prenotazione */
	String timeAndDate;
	/** Numero delle persone che hanno prenotato il tavolo */
	int numPersone;
	/** Nome del cliente che ha effettuato la prenotazione */
	String nomeCliente;
	/** Id del tavolo a cui è riferita la prenotazione */
	int tableId; 
	
	
	public String getTimeAndDate() {
		return timeAndDate;
	}
	public void setTimeAndDate(String timeAndDate) {
		this.timeAndDate = timeAndDate;
	}
	
	public int getNumPersone() {
		return numPersone;
	}
	public void setNumPersone(int numPersone) {
		this.numPersone = numPersone;
	}
	
	public String getNomeCliente() {
		return nomeCliente;
	}
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	
	public int getTableId() {
		return tableId;
	}
	public void setTableId(int tableId) {
		this.tableId = tableId;
	}
	
}
