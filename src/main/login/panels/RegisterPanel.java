/*
 * RegisterPanel.java
 * 
 * created: 11-08-2016
 * modified: 11-20-2016
 * 
 * panel for user to register a new account
 */

package main.login.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import main.MainApp;
import main.login.demos.Demo;

public class RegisterPanel extends BasePanel implements ActionListener {

	private JPasswordField confirmationField;
	
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
		this.registrationAttempt();
	}
	
	/**
	 * attempt to register a new user account
	 */
	private void registrationAttempt() {
		
		// store username, password, and password confirmation as strings
		String username = this.usernameField.getText();
		String password = new String(this.passwordField.getPassword());
		String confirmation = new String(this.confirmationField.getPassword());
		
		// ensure username, password, and confirmation fields are not empty
		if (this.fieldsEmpty(username, password, confirmation)) {
			JOptionPane.showMessageDialog(this, "You must provide a username, password, and confirmation");
			return;
		}
		
		// ensure password matches confirmation
		if (!(password.equals(confirmation))) {
			JOptionPane.showMessageDialog(this, "Password does not match confirmation");
			return;
		}
		
		// insert new user into database
		try {
			int registered = this.insertNewUser(username, password);
			if (registered == 2) { // successful registration attempt
				Demo.loggedIn = true; // for main.login.Demo
				Demo.username = username;
				MainApp.loggedIn = true; // for main.Main
				MainApp.username = username;
			} else if (registered == 1) { // unsuccessful registration attempt
				JOptionPane.showMessageDialog(this, "Username is taken, please pick another");
			} else {
				JOptionPane.showMessageDialog(this, "Something went wrong");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * returns true if username, password or confirmation field is empty; else false
	 */
	private boolean fieldsEmpty(String username, String password, String confirmation) {
		boolean isEmpty = false;
		if (username.equals("") || password.equals("") || confirmation.equals("")) {
			isEmpty = true;
		}
		return isEmpty;
	}
	
	/**
	 * returns 2 if user is inserted into table; 1 if username is taken; else 0
	 */
	private int insertNewUser(String username, String password) throws SQLException {
		int registered = 0;
		double initialDeposit = 10000.00;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// initialize a Statement object
			stmt = this.conn.createStatement();
			
			// ensure username is not already taken
			String query = "SELECT id FROM users WHERE username = '" + username + "'";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				registered = 1;
				return registered;
			}
			
			// hash users password
			String hash = this.hashPassword(password);

			// insert username, hex string, and cash money into table
			String insert = "INSERT INTO users (username, hash, balance, time_stamp) VALUES " +
			 "('" + username + "', '" + hash + "', " + initialDeposit + ", CURTIME())";
			int result = stmt.executeUpdate(insert);
			if (result == 1) {
				registered = 2;
			}
			
			// get the new users id
			query = "SELECT id FROM users WHERE username = '" + username + "'";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				int id = rs.getInt("id");
				insert = "INSERT INTO transactions (user_id, date, time, transaction, amount, old_bal, new_bal, time_stamp) VALUES "
						+ "(" + id + ", CURDATE(), CURTIME(), 'deposit', " + initialDeposit + ", 0.00, " + initialDeposit + ", CURTIME())";
				result = stmt.executeUpdate(insert);
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return registered;
	}
}
