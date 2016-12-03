/*
 * Main.java
 * 
 * created: 10-01-2016
 * modified: 12-03-2016
 * 
 * launches Blackjack_2 application
 */

package com.cjimgarten;

public class Main {
	
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
		
		Blackjack_2 app = new Blackjack_2(
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
		Main.loginStatus = true;
	}
	
	/**
	 * log the user out
	 */
	public static void logout() {
		Main.loginStatus = false;
	}
	
	/**
	 * get users login status
	 */
	public static boolean getLoginStatus() {
		return Main.loginStatus;
	}
	
	/**
	 * get the users username
	 */
	public static String getUsername() {
		return Main.username;
	}
	
	/**
	 * set the users username
	 */
	public static void setUsername(String username) {
		Main.username = username;
	}
}
