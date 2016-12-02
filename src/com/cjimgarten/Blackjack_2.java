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
		String dbms_username = ""; /* username to access MySQL */
		String dbms_password = ""; /* password to access MySQL */
		String db_name = ""; /* name of the database */
		
		// path to the application logo
		String logoPath = "png/jack_of_spades2.png";
		
		BlackjackApplication app = new BlackjackApplication(
				"Blackjack",
				logoPath,
				dbms_username,
				dbms_password,
				db_name
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
