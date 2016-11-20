/*
 * MainApp.java
 * 
 * created: 10-01-2016
 * modified: 11-20-2016
 * 
 * main application entry point
 */

package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.game.frame.BlackjackFrame;
import main.login.frame.LoginFrame;

public class MainApp {
	
	// users' login status
	public static boolean loggedIn = false;
	public static String username = "";
	
	/**
	 * application entry point
	 */
	public static void main(String[] args) {
		
		// establish a database connection
		Connection conn = connectToDatabase(
			"", /* dbms username */
			"", /* dbms password */
			"" /* database name */
		);
		
		// ensure connection was successfully established
		if (conn == null) {
			System.err.println("Unable to establish database connection");
			return;
		}
		
		while (true) {
			// start application
			LoginFrame loginFrame = MainApp.openLogin(conn);
			
			// monitor the users' login status
			while (true) {
				// once the user is logged in, break the loop
				if (MainApp.loggedIn) {
					break;
				}
				
				// WON'T WORK WITHOUT THIS... DON'T KNOW WHY!!!
				try {
					Thread.sleep(0);
				} catch(Exception e) {
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
			}
			
			// dispose of login frame once logged in
			loginFrame.dispose();
			
			// start blackjack
			BlackjackFrame blackjackFrame = MainApp.openBlackjack(conn);
			int option = JOptionPane.showConfirmDialog(
					blackjackFrame,
					"Are you ready to play?",
					"Welcome",
					JOptionPane.YES_NO_OPTION
				);
			if (option == 0) {
				blackjackFrame.getBlackjackPanel().startGame();
			} else {
				MainApp.loggedIn = false;
			}
			
			// monitor the users' login status
			while (true) {
				// once the user is logged in, break the loop
				if (!(MainApp.loggedIn)) {
					break;
				}
				
				// WON'T WORK WITHOUT THIS... DON'T KNOW WHY!!!
				try {
					Thread.sleep(0);
				} catch(Exception e) {
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
			}
			
			blackjackFrame.dispose();
		}
	}
	
	/**
	 * establish and return a database connection
	 */
	public static Connection connectToDatabase(String dbms_username, String dbms_password, String db_name) {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", dbms_username);
		connectionProps.put("password", dbms_password);
		String db_url = "jdbc:mysql://localhost:3306/" + db_name;
		try {
			conn = DriverManager.getConnection(db_url, connectionProps);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return conn;
	}
	
	/**
	 *  start a login frame
	 */
	public static LoginFrame openLogin(Connection conn) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		LoginFrame frame = new LoginFrame(conn);
		frame.setVisible(true);
		return frame;
	}
	
	/**
	 *  start blackjack
	 */
	public static BlackjackFrame openBlackjack(Connection conn) {
		BlackjackFrame frame = new BlackjackFrame(conn, MainApp.username);
		frame.setVisible(true);
		return frame;
	}
}
