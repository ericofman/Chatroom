package com.chat;

public class ClientReceiverThread extends Thread {

	private Client client;
	
	public ClientReceiverThread(Client client) {
		this.client = client;
		this.start();
	}
	
	public void run() {
		while(true) {
			client.receiveUDP();
		}
	}
}
