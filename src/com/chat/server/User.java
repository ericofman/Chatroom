package com.chat.server;

import java.net.InetSocketAddress;

public class User {
	private InetSocketAddress address;
	private String username;
	
	public User(InetSocketAddress address, String username) {
		this.address = address;
		this.username = username;
	}
	
	public InetSocketAddress getAddress() {
		return address;
	}
	
	public String getUsername() {
		return username;
	}
}
