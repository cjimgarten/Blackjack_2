/*
 * Blackjack_2.java
 * 
 * created: 10-01-2016
 * modified: 12-02-2016
 * 
 * blackjack desktop application
 */

package com.cjimgarten;

public class Blackjack_2 {
	
	// users' login status
	private static boolean loginStatus;
	private static String username;
	
	/**
	 * launch the application
	 */
	public static void main(String[] args) {
		
		// DB credentials
		final String DBMS_USERNAME = ""; /* dbms_username */
		final String DBMS_PASSWORD = ""; /* dbms_password */
		final String DB_NAME = ""; /* db_name */
		
		BlackjackApplication app = new BlackjackApplication(
				"Blackjack",
				DBMS_USERNAME,
				DBMS_PASSWORD,
				DB_NAME
			);
		app.startApplication();
	}
	
	/**
	 * log the user in
	 */
	public static void login() {
		Blackjack_2.loginStatus = true;
	}
	
	/**
	 * log the user out
	 */
	public static void logout() {
		Blackjack_2.loginStatus = false;
	}
	
	/**
	 * get users login status
	 */
	public static boolean getLoginStatus() {
		return Blackjack_2.loginStatus;
	}
	
	/**
	 * get the users username
	 */
	public static String getUsername() {
		return Blackjack_2.username;
	}
	
	/**
	 * set the users username
	 */
	public static void setUsername(String username) {
		Blackjack_2.username = username;
	}
}
