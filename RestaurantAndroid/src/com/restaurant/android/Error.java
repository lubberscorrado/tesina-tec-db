package com.restaurant.android;

public class Error {
	private String error;
	private boolean errorOccurred;
	
	public Error(String error, boolean errorOccurred) {
		this.error = error;
		this.errorOccurred = errorOccurred;
		
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public boolean errorOccurred() {
		return errorOccurred;
	}
	public void setErrorOccurred(boolean errorOccurred) {
		this.errorOccurred = errorOccurred;
	}
	
	
}
