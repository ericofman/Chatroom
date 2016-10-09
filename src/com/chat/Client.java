package com.chat;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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

public class Client extends JFrame { 
	private static final long serialVersionUID = 1L;
	private JPanel contentPane; 
 
	private String name, password, ipaddress;
	private int port;
	
	private JButton btnSend;
	private JTextArea textArea;
	private JTextField txtMessage;
	private JList<String> onlineUsers;
	
	private DefaultListModel<String> listModel;
	
	public Client(String name, String password, String ipaddress, int port) {
		this.name = name;
		this.password = password;
		this.ipaddress = ipaddress;
		this.port = port;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 500);
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
					pushMessage(name, txtMessage.getText());
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
					pushMessage(name, txtMessage.getText());
				}
			});
			GridBagConstraints gbc_btnSend = new GridBagConstraints();
			gbc_btnSend.gridx = 1;
			gbc_btnSend.gridy = 1;
		 
			contentPane.add(btnSend, gbc_btnSend);
		setVisible(true);
		setTitle("Chat Client");
		
		txtMessage.requestFocusInWindow();
		
		userConnected(name);
	}
	
	private void userConnected(String user) {
		listModel.addElement(user);
	}
	
	public void pushMessage(String user, String msg) {
		if(msg.isEmpty()) return;
		String seperator = System.getProperty("line.separator"); 
		textArea.append(user + ": " + msg + seperator);
		textArea.setCaretPosition(textArea.getDocument().getLength());
		txtMessage.setText("");
	}
}
