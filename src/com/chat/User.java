package com.chat;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String username;
	private int id;
	
	public User(int id, String username) {
		this.id = id;
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public int getId() {
		return id;
	}
}
