package com.chat.utils;

import java.io.Serializable;

public class Message implements Serializable { 
	private static final long serialVersionUID = 1L;
	private int messageType = 0;
	
	public static final int ID_LOGIN = 0; 
	public static final int ID_LOGOUT = 1; 
	public static final int ID_MESSAGE = 2;

	private User user;
	private String sender = null; 
	private String message = null; 
	
	public Message(int messageType, User user, String sender, String message) {
		this.messageType = messageType; 
		this.user = user;
		this.sender = sender; 
		this.message = message;
	}  

	public int getMessageType() {
		return messageType;
	} 
	
	public User getUser() {
		return user;
	} 

	public String getSender() {
		return sender;
	} 

	public String getMessage() {
		return message;
	}
}
