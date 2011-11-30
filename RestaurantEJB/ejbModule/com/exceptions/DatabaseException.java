package com.exceptions;



public class DatabaseException extends Exception  {

	private static final long serialVersionUID = 1L;
	
	public static int ERRORE_CONNESSIONE_DATABSE = 1;
	public static int OGGETTO_NON_TROVATO = 2;
	
	/* Errori di integrità referenziale non dovrebbero verificarsi
	 * con foreign key impostate correttamente durante la creazione
	 * del database */
	public static int ERRORE_INTEGRITA_REFERENZIALE = 3;
		
	private int lastError = 0;

	
	public DatabaseException(int lastError) {
		super();
		this.lastError = lastError;
	}

	public int getLastError() {
		return lastError;
	}

	public void setLastError(int lastError) {
		this.lastError = lastError;
	}
	
	
	public String toString() {
		
		if(lastError == DatabaseException.ERRORE_CONNESSIONE_DATABSE)
			return "Errore di connessione al database";
		else if(lastError == DatabaseException.OGGETTO_NON_TROVATO) 
			return "Nessun oggetto trovato";
		else if(lastError == DatabaseException.ERRORE_INTEGRITA_REFERENZIALE)
			return "Errore di integrità referenziale";
		
		return "Nessun errore riscontrato";
	}
	
}
