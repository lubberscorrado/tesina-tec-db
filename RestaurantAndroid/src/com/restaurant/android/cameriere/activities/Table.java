package com.restaurant.android.cameriere.activities;

import java.io.Serializable;


/** 
 * Classe che rappresenta un tavolo normale
 * @author Fabio Pierazzi
 */
public class Table implements Serializable {
	
		private static final long serialVersionUID = 1L;
		private int id = -1;
	 	private String name = "";
	    private String status = "";
	    private String piano = "";
	    private String area = "" ;
	    private int numPosti = -1;
	    
	    /* Statistiche dipendenti dallo stato */
	    // prenotazioni
	    // numero posti occupati
	    // id del conto associato
	    
		public int getTableId() {
			return id;
		}
		public void setTableId(int tableId) {
			this.id = tableId;
		}
		
		public String getTableName() {
			return name;
		}
		public void setTableName(String tableName) {
			this.name = tableName;
		}
		
		public String getTableStatus() {
			return status;
		}
		public void setTableStatus(String tableStatus) {
			this.status = tableStatus;
		}
		
		public int getNumPosti() {
			return numPosti;
		}
		public void setNumPosti(int numPosti) {
			this.numPosti = numPosti;
		}
		public String getPiano() {
			return piano;
		}
		public void setPiano(String piano) {
			this.piano = piano;
		}
		public String getArea() {
			return area;
		}
		public void setArea(String area) {
			this.area = area;
		}
		
		
		
}
