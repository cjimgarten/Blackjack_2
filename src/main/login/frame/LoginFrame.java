/*
 * LoginFrame.java
 * 
 * created: 10-01-2016
 * modified: 11-23-2016
 * 
 * existing user login and new user registration frame
 */

package main.login.frame;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import java.sql.Connection;

import main.login.panels.LoginPanel;
import main.login.panels.RegisterPanel;

public class LoginFrame extends JFrame {
	
	// SQL connection
	private Connection conn;
	
	// GUI components
	private JPanel contentPane, loginPanel, registerPanel;
	private JTabbedPane tabbedPane;
	
	/**
	 * create the frame
	 */
	public LoginFrame(Connection conn, String title) {
		super(title);
		this.conn = conn;
		this.configureFrame();
	}
	
	public LoginFrame(Connection conn) {
		super("LoginFrame");
		this.conn = conn;
		this.configureFrame();
	}
	
	/**
	 * configure frame components
	 */
	private void configureFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100,100,400,350);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(10,10,10,10));
		this.contentPane.setLayout(new BorderLayout());
		this.setContentPane(this.contentPane);
		
		// set the icon image for this frame
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("../../png/jack_of_spades2.png"));
		Image image = imageIcon.getImage();
		this.setIconImage(image);
		
		// create a tabbed pane
		this.tabbedPane = new JTabbedPane();
		
		// create a login tab
		this.loginPanel = new LoginPanel(this.conn);
		
		// create a register tab
		this.registerPanel = new RegisterPanel(this.conn);
		
		// add tabs to tabbed pane
		this.tabbedPane.addTab("Login", this.loginPanel);
		this.tabbedPane.addTab("Register", this.registerPanel);
		
		// add tabbed pane to content pane
		this.contentPane.add(this.tabbedPane);
	}
}
