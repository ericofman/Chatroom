package com.chat.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import com.chat.server.packets.Packet;

public class Client extends JFrame { 
	private static final long serialVersionUID = 1L;
	private JPanel contentPane; 
 
	private String username, password, ipaddress;
	private int serverPort;
	
	private JButton btnSend;
	private JTextArea textArea;
	private JTextField txtMessage;
	private JList<String> onlineUsers;
	
	private DefaultListModel<String> listModel;
	private DatagramSocket socket;  
	
	private ClientReceiverThread receiveThread;
	
	private int clientID = 0; 
	
	public Client(String username, String password, String ipaddress, int port) {
		this.username = username;
		this.password = password;
		this.ipaddress = ipaddress;
		this.serverPort = port;
		
		this.clientID = Math.abs(new Random().nextInt());
	 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(450, 300);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{575, 125};
		gbl_contentPane.rowHeights = new int[]{463, 37};
		gbl_contentPane.columnWeights = new double[]{0.5, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.5, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		txtMessage = new JTextField();
		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendObjUDP(new Packet.MessagePacket(username, clientID, txtMessage.getText()));
					pushMessage(username, txtMessage.getText());
				}
			}
		}); 
		
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
 
		gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMessage.insets = new Insets(0, 0, 0, 0);
		gbc_txtMessage.gridx = 0;
		gbc_txtMessage.gridy = 1;
		contentPane.add(txtMessage, gbc_txtMessage);
		txtMessage.setColumns(0);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(textArea);
		GridBagConstraints gbc_scroll = new GridBagConstraints();
		gbc_scroll.fill = GridBagConstraints.BOTH;
		gbc_scroll.insets = new Insets(0, 0, 0, 5);
		gbc_scroll.gridwidth = 1;
		gbc_scroll.gridx = 0;
		gbc_scroll.gridy = 0;
		contentPane.add(scroll, gbc_scroll);
		
		listModel = new DefaultListModel<String>();
		onlineUsers = new JList<String>(listModel);
		onlineUsers.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		onlineUsers.setLayoutOrientation(JList.VERTICAL_WRAP);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.insets = new Insets(0, 0, 0, 0); 
		gbc_list.gridx = 1;
		gbc_list.gridy = 0; 
		contentPane.add(onlineUsers, gbc_list);
		
		btnSend = new JButton("Send");
			btnSend.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) { 
					sendObjUDP(new Packet.MessagePacket(username, clientID, txtMessage.getText()));
					pushMessage(username, txtMessage.getText());
				}
			});
			GridBagConstraints gbc_btnSend = new GridBagConstraints();
			gbc_btnSend.gridx = 1;
			gbc_btnSend.gridy = 1;
		 
			contentPane.add(btnSend, gbc_btnSend);
		setVisible(true);
		
		txtMessage.requestFocusInWindow();
		
		boolean connected = openConnection();
	 
		if(connected) {  
			this.sendObjUDP(new Packet.LoginPacket(username, clientID));
			addUserList(username);
		} 
		
		receiveThread = new ClientReceiverThread(this);
		
		addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        sendObjUDP(new Packet.LogoutPacket(username, clientID));
		    }
		});
		
		setTitle("Chat Client" + " " + socket.getLocalPort());  
	} 
	
	private boolean openConnection() {
		try { 
			this.socket = new DatagramSocket();
		} catch (SocketException e1) { 
			e1.printStackTrace(); 
			return false;
		} 
		return true;
	}
	
	private void sendObjUDP(Object packetObj) { 
		ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
		try {
			ObjectOutput ou = new ObjectOutputStream(outBytes);
			
			ou.writeObject(packetObj);
			ou.close();
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
		sendUDP(outBytes.toByteArray(), this.ipaddress);
	}
	
	public void receiveUDP() {
		DatagramPacket packet;
		byte[] data = new byte[6024];
		packet = new DatagramPacket(data, data.length);
		 
		try {
			socket.receive(packet);
			ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream(data));
			
			Object packetObj = (Object) oi.readObject();
			
			if(packetObj instanceof Packet.LoginPacket) {
				this.addUserList(((Packet.LoginPacket) packetObj).username);
			} 
			if(packetObj instanceof Packet.LogoutPacket) {
				this.removeUserList(((Packet.LogoutPacket) packetObj).username);
			} 
			if(packetObj instanceof Packet.MessagePacket) {
				this.pushMessage(((Packet.MessagePacket) packetObj).username, 
						((Packet.MessagePacket) packetObj).message);
			} 
		} catch (IOException e) { 
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) { 
			cnfe.printStackTrace();
		} 
	}
	
	public void addUserList(String user) {
		listModel.addElement(user); 
	}
	
	public void removeUserList(String user) {
		listModel.removeElement(user);
	}
	
	private void sendUDP(byte data[], String address) { 
		Thread sender = new Thread("sender") {
			public void run() {
				DatagramPacket packet;
				try {
					InetAddress ipaddress = InetAddress.getByName(address);
					packet = new DatagramPacket(data, data.length, ipaddress, serverPort);
					socket.send(packet);
				} catch (UnknownHostException e1) { 
					e1.printStackTrace(); 
				} catch (IOException e) { 
					e.printStackTrace();
				} 
			}
		};
		sender.start();
	} 
	
	public void pushMessage(String msg) {
		pushMessage("", msg);
	}
	
	public void pushMessage(String user, String msg) {
		if(msg.isEmpty()) return;
		String seperator = System.getProperty("line.separator"); 
		
		String colonSeperator = ""; 
		if(!user.isEmpty()) {
			colonSeperator = ": ";
		}
		String message = user + colonSeperator + msg + seperator;
		textArea.append(message);
		textArea.setCaretPosition(textArea.getDocument().getLength());
		txtMessage.setText("");
	} 
}
