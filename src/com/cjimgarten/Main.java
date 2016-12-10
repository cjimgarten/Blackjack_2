/*
 * Main.java
 * 
 * created: 10-01-2016
 * modified: 12-07-2016
 * 
 * launches Blackjack_2 application
 */

package com.cjimgarten;

public class Main {
	
	/**
	 * launch the application
	 */
	public static void main(String[] args) {
		// path to the application logo
		String logoPath = "png/jack_of_spades2.png";
		
		// DB credentials
		String dbms_username = ""; /* username to access MySQL */
		String dbms_password = ""; /* password to access MySQL */
		String db_name = ""; /* name of the database */
		
		Blackjack_2 app = new Blackjack_2(
				"Blackjack",
				logoPath,
				dbms_username,
				dbms_password,
				db_name
			);
		app.startApplication();
	}
}
