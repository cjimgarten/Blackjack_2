/*
 * LoginPanel.java
 * 
 * created: 11-08-2016
 * modified: 12-10-2016
 * 
 * panel for user to login to an existing account
 */

package com.cjimgarten.views.login.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;

import javax.swing.JOptionPane;

import com.cjimgarten.data.SessionData;
import com.cjimgarten.controllers.login.LoginController;

public class LoginPanel extends BasePanel implements ActionListener {

	// controls login events
	private LoginController lc;

	/**
	 * create the panel
	 */
	public LoginPanel(Connection conn) {
		super(conn);
		this.configurePanel();
	}

	/**
	 * configure panel components
	 */
	private void configurePanel() {

		// position the submit button
		this.submitButton.setBounds(20,80,100,25);

		// initialize controller to null
		this.lc = null;

		// add action listeners to panel components
		this.usernameField.addActionListener(this);
		this.passwordField.addActionListener(this);
		this.submitButton.addActionListener(this);
	}

	/**
	 * event handling method
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String username = this.usernameField.getText();
		String password = new String(this.passwordField.getPassword());
		this.lc = new LoginController(this.conn,  username, password);
		int result = this.lc.loginAttempt();
		if (result == 0) { // successful login
			SessionData.login(username);
		} else if (result == 1) { // unsuccessful login
			JOptionPane.showMessageDialog(this, "You must provide a username and password");
		} else { // unsuccessful login
			JOptionPane.showMessageDialog(this, "Incorrect credentials");
		}
	}
}
