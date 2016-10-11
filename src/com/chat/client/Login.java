package com.chat.client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame { 
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JLabel lblIpAddress;
	private JTextField txtAddress;
	private JTextField txtPort;
	private JLabel lblPort;
	private JButton btnHost;
	private JLabel lblLogin_1;
 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	} 
	
	public void login(String name, String password, String address, int port) { 
		dispose();
		new Client(name, password, address, port);
	}
	
	public Login() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) { 
			e.printStackTrace();
		} catch (InstantiationException e) { 
			e.printStackTrace();
		} catch (IllegalAccessException e) { 
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) { 
			e.printStackTrace();
		}
		setResizable(false);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setSize(366, 343);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtUsername = new JTextField();
		txtUsername.setText("User");
		txtUsername.setBounds(160, 49, 130, 26);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setText("Password"); 
		txtPassword.setBounds(160, 77, 130, 26);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);
		
		JLabel lblLogin = new JLabel("Username:");
		lblLogin.setBounds(77, 54, 71, 16);
		contentPane.add(lblLogin);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(77, 82, 71, 16);
		contentPane.add(lblPassword);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = txtUsername.getText();
				String password = String.valueOf(txtPassword.getPassword());
				String ipaddress = txtAddress.getText();
				int port = -1;
				try {
					port = Integer.parseInt(txtPort.getText());
				} catch(NumberFormatException nfe) {
					port = -1;
				}
				login(username, password, ipaddress, port); 
			}
		});
		btnConnect.setBounds(77, 268, 96, 29);
		contentPane.add(btnConnect);
		
		lblIpAddress = new JLabel("IP Address:");
		lblIpAddress.setBounds(147, 135, 71, 16);
		contentPane.add(lblIpAddress);
		
		txtAddress = new JTextField();
		txtAddress.setText("localhost");
		txtAddress.setBounds(121, 154, 130, 26);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);
		
		txtPort = new JTextField();
		txtPort.setText("9000");
		txtPort.setBounds(121, 211, 130, 26);
		contentPane.add(txtPort);
		txtPort.setColumns(10);
		
		lblPort = new JLabel("Port:");
		lblPort.setBounds(168, 192, 29, 16);
		contentPane.add(lblPort);
		
		btnHost = new JButton("Host");
		btnHost.setBounds(201, 268, 89, 29);
		contentPane.add(btnHost);
		
		lblLogin_1 = new JLabel("LOGIN");
		lblLogin_1.setBounds(163, 21, 40, 16);
		contentPane.add(lblLogin_1);
	}
}
