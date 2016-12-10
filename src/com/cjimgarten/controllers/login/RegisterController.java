/*
 * RegisterController.java
 * 
 * created: 12-10-2016
 * modified: 12-10-2016
 * 
 * controls registration events
 */

package com.cjimgarten.controllers.login;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterController extends BaseController {
	
	// additional field for registration
	private String confirmation;
	
	/**
	 * create an instance
	 */
	public RegisterController(Connection conn, String username, String password, String confirmation) {
		super(conn, username, password);
		this.confirmation = confirmation;
	}

	/**
	 * attempt to register a new user account
	 */
	public int registrationAttempt() {
		int result = 4; // unsuccessful attempt
		
		// ensure username, password, and confirmation fields are not empty; else return
		if (this.fieldsEmpty()) {
			result = 1; // unsuccessful attempt
			return result;
		}
		
		// ensure password field matches confirmation field; else return
		if (!(this.password.equals(this.confirmation))) {
			result = 2; // unsuccessful attempt
			return result;
		}
		
		// attempt to insert the new user into the database
		try {
			int registered = this.insertNewUser();
			if (registered == 2) {
				result = 0; // successful attempt
			} else if (registered == 1) {
				result = 3; // unsuccessful attempt
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * returns true if username, password or confirmation field is empty; else false
	 */
	private boolean fieldsEmpty() {
		boolean isEmpty = false;
		if (this.username.equals("") || this.password.equals("") || this.confirmation.equals("")) {
			isEmpty = true;
		}
		return isEmpty;
	}
	
	/**
	 * returns 2 if user is inserted into table; 1 if username is taken; else 0
	 */
	private int insertNewUser() throws SQLException {
		int registered = 0;
		double initialDeposit = 10000.00;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// initialize a Statement object
			stmt = this.conn.createStatement();
			
			// ensure username is not already taken
			String query = "SELECT id FROM users WHERE username = '" + this.username + "'";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				registered = 1;
				return registered;
			}
			
			// hash users password
			String hash = this.hashPassword(this.password);

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
