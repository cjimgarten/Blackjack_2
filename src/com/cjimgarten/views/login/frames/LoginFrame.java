/*
 * LoginFrame.java
 * 
 * created: 10-01-2016
 * modified: 12-10-2016
 * 
 * existing user login and new user registration frame
 */

package com.cjimgarten.views.login.frames;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import java.sql.Connection;

import com.cjimgarten.views.login.panels.LoginPanel;
import com.cjimgarten.views.login.panels.RegisterPanel;

public class LoginFrame extends JFrame {
	
	// SQL connection
	private Connection conn;
	
	// GUI components
	private JPanel contentPane, loginPanel, registerPanel;
	private JTabbedPane tabbedPane;
	
	/**
	 * create the frame
	 */
	public LoginFrame(Connection conn, String title, Image logo) {
		super(title + " (Login or Register)");
		this.conn = conn;
		this.setIconImage(logo);
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
