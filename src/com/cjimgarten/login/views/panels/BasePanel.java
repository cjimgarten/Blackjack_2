/*
 * BasePanel.java
 * 
 * created: 11-13-2016
 * modified: 12-10-2016
 * 
 * base class for shared GUI components of LoginPanel and RegisterPanel
 */

package com.cjimgarten.login.views.panels;

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
}
