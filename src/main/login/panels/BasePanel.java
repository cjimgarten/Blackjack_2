/*
 * BasePanel.java
 * 
 * created: 11-13-2016
 * modified: 11-13-2016
 * 
 * base class for shared functionality between LoginPanel and RegisterPanel
 */

package main.login.panels;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class BasePanel extends JPanel {

	// SQL connection
	protected Connection conn;
	
	// GUI components
	protected JTextField usernameField;
	protected JPasswordField passwordField;
	protected JButton submitButton;
		
	/**
	 * create the panel
	 */
	public BasePanel(Connection conn) {
		super();
		this.setLayout(null);
		this.conn = conn;
		this.configurePanel();
	}
	
	/**
	 * configure panel components
	 */
	private void configurePanel() {
		
		// add username field to the login tab
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setBounds(20,10,100,25);
		this.add(usernameLabel);
		this.usernameField = new JTextField();
		this.usernameField.setBounds(140,10,100,25);
		this.add(this.usernameField);
		
		// add a password field to the login tab
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(20,45,100,25);
		this.add(passwordLabel);
		this.passwordField = new JPasswordField();
		this.passwordField.setBounds(140,45,100,25);
		this.add(this.passwordField);
		
		// add a submit button
		this.submitButton = new JButton("Submit");
		this.add(this.submitButton);
	}
	
	/**
	 * returns a hashed password
	 */
	protected String hashPassword(String password) {
		String hash = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] passwordDigest = md.digest();
	
			// create a hex string of the password hash
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < passwordDigest.length; i++) {
				buffer.append(Integer.toHexString(0xff & passwordDigest[i]));
			}
			hash = buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash;
	}
}
