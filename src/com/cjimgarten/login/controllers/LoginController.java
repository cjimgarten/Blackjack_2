/*
 * LoginController.java
 * 
 * created: 12-10-2016
 * modified: 12-10-2016
 * 
 * controls login events
 */

package com.cjimgarten.login.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginController extends BaseController {
	
	/**
	 * create an instance
	 */
	public LoginController(Connection conn, String username, String password) {
		super(conn, username, password);
	}
	
	/**
	 * attempt to log the user in to an existing account
	 */
	public int loginAttempt() {
		int result = 2; // unsuccessful attempt
		
		// ensure username and password fields are not empty; else return
		if (this.fieldsEmpty()) {
			result = 1; // unsuccessful attempt
			return result;
		}
		
		// validate the users credentials
		try {
			if (this.validateUser()) {
				result = 0; // successful attempt
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * returns true if username or password field is empty; else false
	 */
	private boolean fieldsEmpty() {
		boolean isEmpty = false;
		if (this.username.equals("") || this.password.equals("")) {
			isEmpty = true;
		}
		return isEmpty;
	}
	
	/**
	 * returns true if user exists and is valid; else false
	 */
	private boolean validateUser() throws SQLException {
		boolean validated = false;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// get hashed password stored in table
			stmt = this.conn.createStatement();
			String query = "SELECT hash FROM users WHERE username = '" + this.username + "'";
			rs = stmt.executeQuery(query);
			String db_hash = "";
			if(rs.next()) {
				db_hash = rs.getString("hash");
			}
			
			// hash the password that the user has given
			String hash = this.hashPassword(this.password);
			
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
