package com.restaurant;
import javax.ejb.Local;
import javax.ejb.Remote;

@Remote
public interface ProvaSessionRemote {
	public String greeting();
	public void initialize();
	public int returnFirstId();
}
