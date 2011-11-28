package com.utils;

import java.io.Serializable;

public class UserInfo implements Serializable {
	private String name;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
		
	}
}
