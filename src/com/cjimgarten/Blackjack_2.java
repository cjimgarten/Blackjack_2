/*
 * Blackjack_2.java
 * 
 * created: 12-02-2016
 * modified: 12-07-2016
 * 
 * blackjack desktop application
 */

package com.cjimgarten;

import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.cjimgarten.game.frames.BlackjackFrame;
import com.cjimgarten.login.frames.LoginFrame;

public class Blackjack_2 {
	
	// SQL connection
	private Connection conn;
	
	// GUI components
	private LoginFrame loginFrame;
	private BlackjackFrame blackjackFrame;
	
	// title of the application
	private String applicationTitle;
	
	// application logo
	private Image logo;
	
	/**
	 * create an instance
	 */
	public Blackjack_2(String title, String logoPath, String db_username, String db_password, String db_name) {
		// establish a database connection
		this.conn =  this.connectToDatabase(
				db_username,
				db_password,
				db_name
			);
		Main.logout(); // set loginStatus to false initially
		Main.setUsername(""); // set username to "" initially
		this.applicationTitle = title;
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(logoPath));
		this.logo = imageIcon.getImage();
		
		// set the look and feel of the application
		JFrame.setDefaultLookAndFeelDecorated(true);
	}
	
	/**
	 * start the application
	 */
	public void startApplication() {
		// ensure connection was successfully established
		if (conn == null) {
			System.err.println("Unable to establish database connection");
			return;
		}
		
		while (true) {
			// start application
			this.loginFrame = this.invokeLoginFrame();
			
			// monitor the users login status
			while (true) {
				// once the users login status is true, break the loop
				if (Main.getLoginStatus()) {
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
			this.loginFrame.dispose();
			
			// start blackjack
			this.blackjackFrame = this.invokeBlackjackFrame();
			int option = JOptionPane.showConfirmDialog(
					this.blackjackFrame,
					"Are you ready to play?",
					"Welcome",
					JOptionPane.YES_NO_OPTION
				);
			if (option == 0) { // if they user selects "Yes", start the game
				this.blackjackFrame.getBlackjackPanel().startGame();
			} else { // if the user selects "No", log them out
				Main.logout();
			}
			
			// monitor the users login status
			while (true) {
				// once the users login status is false, break the loop
				if (!(Main.getLoginStatus())) {
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
			
			this.blackjackFrame.dispose();
		}
	}
	
	/**
	 * establish and return a database connection
	 */
	public Connection connectToDatabase(String dbms_username, String dbms_password, String db_name) {
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
	public LoginFrame invokeLoginFrame() {
		LoginFrame frame = new LoginFrame(this.conn, this.applicationTitle, this.logo);
		frame.setVisible(true);
		return frame;
	}
	
	/**
	 *  start blackjack
	 */
	public BlackjackFrame invokeBlackjackFrame() {
		BlackjackFrame frame = new BlackjackFrame(this.conn, this.applicationTitle, this.logo, Main.getUsername());
		frame.setVisible(true);
		return frame;
	}
}
