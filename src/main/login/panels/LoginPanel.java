/*
 * LoginPanel.java
 * 
 * created: 11-08-2016
 * modified: 11-20-2016
 * 
 * panel for user to login to an existing account
 */

package main.login.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import main.MainApp;
import main.login.demos.Demo;

public class LoginPanel extends BasePanel implements ActionListener {
		
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
		this.loginAttempt();
	}
	
	/**
	 * attempt to log the user in to an existing account
	 */
	private void loginAttempt() {
		
		// store username and password as strings
		String username = this.usernameField.getText();
		String password = new String(this.passwordField.getPassword());
		
		// ensure username and password fields are not empty
		if (this.fieldsEmpty(username, password)) {
			JOptionPane.showMessageDialog(this, "You must provide a username and password");
			return;
		}
		
		// attempt to log the user in
		try {
			if (this.validateUser(username, password)) { // successful login attempt
				Demo.loggedIn = true; // for main.login.Demo
				Demo.username = username;
				MainApp.loggedIn = true; // for main.Main
				MainApp.username = username;
			} else { // unsuccessful login attempt
				JOptionPane.showMessageDialog(this, "Incorrect credentials");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * returns true if username or password field is empty; else false
	 */
	private boolean fieldsEmpty(String username, String password) {
		boolean isEmpty = false;
		if (username.equals("") || password.equals("")) {
			isEmpty = true;
		}
		return isEmpty;
	}
	
	/**
	 * returns true if user exists and is valid; else false
	 */
	private boolean validateUser(String username, String password) throws SQLException {
		boolean validated = false;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// get hashed password stored in table
			stmt = this.conn.createStatement();
			String query = "SELECT hash FROM users WHERE username = '" + username + "'";
			rs = stmt.executeQuery(query);
			String db_hash = "";
			if(rs.next()) {
				db_hash = rs.getString("hash");
			}
			
			// hash the password that the user has given
			String hash = this.hashPassword(password);
			
			// ensure that the hashes match
			if (hash.equals(db_hash)) {
				validated = true;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return validated;
	}
}
