package com.exceptions;



public class DatabaseException extends Exception  {

	private static final long serialVersionUID = 1L;
		
	private String error;
	
		
	public DatabaseException(String error) {
		super();
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String toString() {
		
		return error;
	
	}
	
}
