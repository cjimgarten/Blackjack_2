/*
 * SessionData.java
 * 
 * created: 12-08-2016
 * modified: 12-08-2016
 * 
 * data object to store session data about the user
 */


package com.cjimgarten.data;

public class SessionData {
	
	// some attributes about the user
	private static String username;
	private static boolean loggedIn;
	
	/**
	 * get methods
	 */
	public static String getUsername() {
		return SessionData.username;
	}
	
	public static boolean isLoggedIn() {
		return SessionData.loggedIn;
	}
	
	/**
	 * methods to log the user in and log the user out
	 */
	public static void login(String username) {
		SessionData.username = username;
		SessionData.loggedIn = true;
	}
	
	public static void logout() {
		SessionData.username = "";
		SessionData.loggedIn = false;
	}
	
	/**
	 * print session data
	 */
	public static void printData() {
		System.out.println("[" + SessionData.username + ", " + SessionData.loggedIn + "]");
	}
}
