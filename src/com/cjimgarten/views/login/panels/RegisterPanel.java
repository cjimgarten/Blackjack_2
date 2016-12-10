/*
 * RegisterPanel.java
 * 
 * created: 11-08-2016
 * modified: 12-10-2016
 * 
 * panel for user to register a new account
 */

package com.cjimgarten.views.login.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.cjimgarten.data.SessionData;
import com.cjimgarten.controllers.login.RegisterController;

public class RegisterPanel extends BasePanel implements ActionListener {

	// additional GUI component
	private JPasswordField confirmationField;

	// controls registration events
	private RegisterController rc;

	/**
	 * create the panel
	 */
	public RegisterPanel(Connection conn) {
		super(conn);
		this.configurePanel();
	}

	/**
	 * configure panel components
	 */
	private void configurePanel() {

		// add a password confirmation field to the register tab
		JLabel confirmationLabel = new JLabel("Confirmation:");
		confirmationLabel.setBounds(20,80,100,25);
		this.add(confirmationLabel);
		this.confirmationField = new JPasswordField();
		this.confirmationField.setBounds(140,80,100,25);
		this.confirmationField.addActionListener(this);
		this.add(this.confirmationField);

		// position the submit button
		this.submitButton.setBounds(20,115,100,25);

		// initialize controller to null
		this.rc = null;

		// add action listeners to panel components
		this.usernameField.addActionListener(this);
		this.passwordField.addActionListener(this);
		this.confirmationField.addActionListener(this);
		this.submitButton.addActionListener(this);
	}

	/**
	 * event handling method
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String username = this.usernameField.getText();
		String password = new String(this.passwordField.getPassword());
		String confirmation = new String(this.confirmationField.getPassword());
		this.rc = new RegisterController(this.conn, username, password, confirmation);
		int result = this.rc.registrationAttempt();
		if (result == 0) { // successful registration
			SessionData.login(username);
		} else if (result == 1) { // unsuccessful registration
			JOptionPane.showMessageDialog(this, "You must provide a username, password, and confirmation");
		} else if (result == 2) { // unsuccessful registration
			JOptionPane.showMessageDialog(this, "Password does not match confirmation");
		} else if (result == 3) { // unsuccessful registration
			JOptionPane.showMessageDialog(this, "Username is taken, please pick another");
		} else {
			JOptionPane.showMessageDialog(this, "Something went wrong");
		}
	}
}
