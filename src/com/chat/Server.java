package com.chat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Server {

	private DatagramSocket socket;
	private Map<User, InetSocketAddress> clients;

	public static void main(String[] args) {
		new Server(9000);
	}

	public Server(int port) {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		System.out.println("Server started on port: " + port);

		clients = new HashMap<User, InetSocketAddress>();

		receive();
	}

	public void broadcastAll(final byte[] data) {
		Thread broadcast = new Thread("broadcast") {
			public void run() {

				DatagramPacket packet = null;

				try {
					for (InetSocketAddress address : clients.values()) {
						packet = new DatagramPacket(data, data.length, address);
						socket.send(packet);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		broadcast.start();
	}

	private void receive() {
		Thread receiver = new Thread("Receiver") {
			public void run() {
				while (true) {
					byte[] data = new byte[6024];

					DatagramPacket packet = new DatagramPacket(data, data.length);

					try {
						socket.receive(packet);

						ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream(data));

						Message msgObj = (Message) oi.readObject();

						switch (msgObj.getMessageType()) {
						case Message.ID_LOGIN:
							System.out.println(msgObj.getUser().getUsername() + " has connected! " + packet.getAddress()
									+ " | " + "Port: " + packet.getPort());

							clients.put(msgObj.getUser(), new InetSocketAddress(packet.getAddress(), packet.getPort()));
							break;
						case Message.ID_MESSAGE:
							System.out.println(msgObj.getUser().getUsername() + ": " + msgObj.getMessage());
							broadcastAll(packet.getData());
							break;
						}

					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		};
		receiver.start();
	}
}
