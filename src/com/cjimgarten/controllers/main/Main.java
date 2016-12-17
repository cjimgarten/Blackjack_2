/*
 * Main.java
 * 
 * created: 10-01-2016
 * modified: 12-16-2016
 * 
 * launches Blackjack_2 application
 */

package com.cjimgarten.controllers.main;

public class Main {
	
	/**
	 * launch the application
	 */
	public static void main(String[] args) {
		// ensure correct number of arguments
		if (args.length != 3) {
			System.out.println("Usage: java <class> <username> <password> <database>");
			System.exit(1);
		}

		// path to the application logo
		String logoPath = "../../images/png/jack_of_spades2.png";
		
		// DB credentials
		String dbms_username = args[0]; /* username to access MySQL */
		String dbms_password = args[1]; /* password to access MySQL */
		String db_name = args[2]; /* name of the database */
		
		// initialize and start the application
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
