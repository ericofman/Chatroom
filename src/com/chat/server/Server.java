package com.chat.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import com.chat.server.packets.Packet;

public class Server {

	private DatagramSocket socket;
	private Map<Integer, User> clients;

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

		clients = new HashMap<Integer, User>();

		receive();
	}

	public void broadcastAll(final byte[] data) {
		Thread broadcast = new Thread("broadcast") {
			public void run() {

				DatagramPacket packet = null;

				try {
					for (User user : clients.values()) {
						packet = new DatagramPacket(data, data.length, user.getAddress());
						socket.send(packet);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		broadcast.start();
	}
	
	public void broadcastAllExcept(final byte[] data, InetSocketAddress ignore) {
		Thread broadcast = new Thread("broadcastExcept") {
			public void run() {

				DatagramPacket packet = null;

				try {
					for (User user : clients.values()) { 
						if(ignore.getPort() == user.getAddress().getPort()) {
							continue;
						} 
						packet = new DatagramPacket(data, data.length, user.getAddress());
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
						Object packetObj = oi.readObject();
						 
						InetSocketAddress address = new InetSocketAddress(packet.getAddress(), packet.getPort());
						
						if(packetObj instanceof Packet.LoginPacket) {  
							int clientUID = ((Packet.LoginPacket) packetObj).clientID;
							clients.put(clientUID, new User(address, ((Packet.LoginPacket) packetObj).username));
							
							System.out.println(clients.get(clientUID).getUsername() + " has connected! " + packet.getAddress()
							+ " | " + "Port: " + packet.getPort());
							broadcastAllExcept(data, address);
						} 
						if(packetObj instanceof Packet.LogoutPacket) {  
							clients.remove(((Packet.LogoutPacket) packetObj).clientID);
							System.out.println(((Packet.LogoutPacket) packetObj).username + " has disconnected!");
							broadcastAllExcept(data, address);
						} 
						if(packetObj instanceof Packet.MessagePacket) {
							String msg = ((Packet.MessagePacket) packetObj).message;
							int clientUID = ((Packet.MessagePacket) packetObj).clientID;
							System.out.println(clients.get(clientUID).getUsername() + ": " + msg);
							broadcastAllExcept(data, address);
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
