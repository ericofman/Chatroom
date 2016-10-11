package com.chat.server.packets;

import java.io.Serializable;

public class Packet implements Serializable { 
	private static final long serialVersionUID = 1L;

	public String username;
	public int clientID;
	
	public Packet(String username, int clientID)  {
		this.username = username;
		this.clientID = clientID;
	}
	
	public static class LoginPacket extends Packet {   
		private static final long serialVersionUID = 1L;

		public LoginPacket(String username, int clientID) {
			super(username, clientID);
		}
	}
	
	public static class LogoutPacket extends Packet { 
		private static final long serialVersionUID = 1L;

		public LogoutPacket(String username, int clientID) {
			super(username, clientID);
		}
	}
	
	public static class MessagePacket extends Packet { 
		private static final long serialVersionUID = 1L;
		public String message = null;
		
		public MessagePacket(String username, int clientID, String message) {
			super(username, clientID);
			this.message = message;
		}
	}
}
