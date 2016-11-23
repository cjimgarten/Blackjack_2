/*
 * MainApp.java
 * 
 * created: 10-01-2016
 * modified: 11-23-2016
 * 
 * main application entry point
 */

package main;

import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.game.frame.BlackjackFrame;
import main.login.frame.LoginFrame;

public class MainApp {
	
	// users' login status
	public static boolean loggedIn = false;
	public static String username = "";
	
	// DB credentials
	private final String dbms_username = ""; /* dbms_username */
	private final String dbms_password = ""; /* dbms_password */
	private final String db_name = ""; /* db_name */
	
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
	 * launch the application
	 */
	public static void main(String[] args) {
		MainApp app = new MainApp("Blackjack");
	}
	
	/**
	 * create an instance
	 */
	public MainApp(String title) {
		// establish a database connection
		this.conn =  this.connectToDatabase(
				dbms_username,
				dbms_password,
				db_name
			);
		this.applicationTitle = title;
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("png/jack_of_spades2.png"));
		this.logo = imageIcon.getImage();
		this.startApplication();
	}
	
	public MainApp() {
		// establish a database connection
		this.conn =  this.connectToDatabase(
				dbms_username,
				dbms_password,
				db_name
			);
		this.applicationTitle = "MainApp";
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("png/jack_of_spades2.png"));
		this.logo = imageIcon.getImage();
		this.startApplication();
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
			this.loginFrame = this.invokeLoginFrame(
					this.conn, 
					"Login or Register"
				);
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
			this.loginFrame.dispose();
			
			// start blackjack
			this.blackjackFrame = this.invokeBlackjackFrame(
					this.conn, 
					"Blackjack"
				);
			int option = JOptionPane.showConfirmDialog(
					this.blackjackFrame,
					"Are you ready to play?",
					"Welcome",
					JOptionPane.YES_NO_OPTION
				);
			if (option == 0) {
				this.blackjackFrame.getBlackjackPanel().startGame();
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
			
			this.blackjackFrame.dispose();
		}
	}
	
	/**
	 *  start a login frame
	 */
	public LoginFrame invokeLoginFrame(Connection conn, String title) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		LoginFrame frame = new LoginFrame(conn, title, this.logo);
		frame.setVisible(true);
		return frame;
	}
	
	/**
	 *  start blackjack
	 */
	public BlackjackFrame invokeBlackjackFrame(Connection conn, String title) {
		BlackjackFrame frame = new BlackjackFrame(conn, title, this.logo, MainApp.username);
		frame.setVisible(true);
		return frame;
	}
}
