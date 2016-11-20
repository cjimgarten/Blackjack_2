/*
 * BlackjackFrame.java
 * 
 * created: 10-30-2016
 * modified: 11-20-2016
 * 
 * graphical user interface for playing blackjack
 */

package main.game.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import main.MainApp;
import main.game.panels.BlackjackPanel;
import main.game.panels.TransactionsPanel;
import main.game.panels.OutcomesPanel;

public class BlackjackFrame extends JFrame implements ActionListener {

	// SQL connection
	private Connection conn;
	
	// users' id, username and cash balance
	private String id, username, balance;
	
	// GUI components
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem depositItem, withdrawItem, logoutItem;
	private JTabbedPane tabbedPane;
	private BlackjackPanel blackjackPanel;
	private TransactionsPanel transactionsPanel;
	private OutcomesPanel outcomesPanel;
	
	// array for frame bounds
	private int[] frameBounds = { 100, 100, 800, 600 };
	
	/**
	 * create the frame
	 */
	public BlackjackFrame(Connection conn, String username) {
		super("Blackjack (" + username + ")");
		this.conn = conn;
		this.username = MainApp.username;
		try {
			this.id = this.getUserData("id");
			this.balance = this.getUserData("balance");
		} catch (SQLException e){
			e.printStackTrace();
		}
		this.configureFrame();
	}
	
	public BlackjackFrame(Connection conn) {
		super("Blackjack");
		this.conn = conn;
		this.configureFrame();
	}
	
	public BlackjackFrame() {
		super("Blackjack");
		this.configureFrame();
	}
	
	/**
	 * configure frame components
	 */
	private void configureFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(this.frameBounds[0], this.frameBounds[1], this.frameBounds[2], this.frameBounds[3]);
		this.contentPane = new JPanel(new BorderLayout(0, 0));
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setBackground(new Color(200, 200, 200));
		this.setContentPane(this.contentPane);
		
		// set the icon image for this frame
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("../../png/jack_of_spades2.png"));
		Image image = imageIcon.getImage();
		this.setIconImage(image);
		
		// add a menu bar
		this.menuBar = new JMenuBar();
		this.menu = new JMenu("Menu");
		this.depositItem = new JMenuItem("Deposit");
		this.depositItem.addActionListener(this);
		this.withdrawItem = new JMenuItem("Withdrawal");
		this.withdrawItem.addActionListener(this);
		this.logoutItem = new JMenuItem("Logout");
		this.logoutItem.addActionListener(this);
		this.menuBar.add(this.menu);
		this.menu.add(this.depositItem);
		this.menu.add(this.withdrawItem);
		this.menu.add(this.logoutItem);
		this.setJMenuBar(this.menuBar);
		
		// create a tabbed pane
		this.tabbedPane = new JTabbedPane();
		
		// create an outcomes tab
		this.outcomesPanel = new OutcomesPanel(this.conn, this.id);
		
		// create a blackjack tab
		this.blackjackPanel = new BlackjackPanel(this.conn, this.id, this.balance, this.outcomesPanel);

		// create a transactions tab
		this.transactionsPanel = new TransactionsPanel(this.conn, this.id);
	
		// add tabs to tabbed pane
		this.tabbedPane.addTab("Blackjack", this.blackjackPanel);
		this.tabbedPane.addTab("Transactions", this.transactionsPanel);
		this.tabbedPane.addTab("Outcomes", this.outcomesPanel);
		
		// add tabbed pane to content pane
		this.contentPane.add(this.tabbedPane);
	}
	
	/**
	 * event handling method; implements ActionListener interface
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(this.depositItem)) { // allow the user to deposit money
			double deposit = this.getAmount();
			if (deposit == 0) { // if user cancels deposit; return
				return;
			}
			try {
				this.executeTransaction("deposit", deposit);
			} catch (SQLException exc) {
				System.err.println(exc.getMessage());
				exc.printStackTrace();
			}
		} else if (source.equals(this.withdrawItem)) { // allow the user to withdraw money
			double withdrawal = this.getAmount();
			if (withdrawal == 0) { // if user cancels withdrawal; return
				return;
			}
			try {
				this.executeTransaction("withdrawal", withdrawal);
			} catch (SQLException exc) {
				System.err.println(exc.getMessage());
				exc.printStackTrace();
			}
		} else { // change the users' login status
			MainApp.loggedIn = false;
		}
	}
	
	/**
	 * get methods
	 */
	public BlackjackPanel getBlackjackPanel() {
		return this.blackjackPanel;
	}
	
	/**
	 * get amount to be withdrawn or deposited
	 */
	public double getAmount() {
		String amountStr = JOptionPane.showInputDialog(this, "How much?");
		double amount = 0.00;
		try {
			while (amountStr.equals("") || Double.parseDouble(amountStr) < 0) {
				amountStr = JOptionPane.showInputDialog(this, "How much?");
			}
			amount = Double.parseDouble(amountStr);
		} catch (Exception e) {
			return amount;
		}
		return amount;
	}
	
	/**
	 * execute a deposit or withdrawal
	 */
	public void executeTransaction(String action, double amount) throws SQLException {	
		Statement stmt = null;
		try {
			// make sure cash variable is up to date
			stmt = this.conn.createStatement();
			String query = "SELECT balance FROM users WHERE id = " + this.id;
			ResultSet rs = stmt.executeQuery(query); // execute query statement
			while (rs.next()) {
				this.balance = rs.getString("balance");
			}
			
			// update cash variable
			double currentBalance = Double.parseDouble(this.balance);
			double previousBalance = currentBalance;
			if (action.equals("deposit")) {
				currentBalance += amount;
			} else if (action.equals("withdrawal")) {
				currentBalance -= amount;
			}
			this.balance = currentBalance + "";
			
			// update database
			String update = "UPDATE users SET balance = " + this.balance + "WHERE id = " + this.id;
			int updateResult = stmt.executeUpdate(update); // execute update statement
			String insert = "INSERT INTO transactions (user_id, date, time, transaction, prev_bal, amount, cur_bal, time_stamp)"
					+ " VALUES (" + this.id + ", CURDATE(), CURTIME(), '" + action + "', " + previousBalance + ", " + amount + ", " 
					+ this.balance + ", CURTIME())";
			int insertResult = stmt.executeUpdate(insert); // execute insert statement
			rs = stmt.executeQuery(query); // execute query statement
			while (rs.next()) {
				this.balance = rs.getString("balance");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		
		// update GUI
		this.update();
	}
	
	/**
	 * pull user data from database
	 */
	public String getUserData(String data) throws SQLException {
		String returnData = "";
		Statement stmt = null;
		try {
			String query = "SELECT " + data + " FROM users WHERE username = '" + this.username + "'";
			stmt = this.conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				returnData = rs.getString(data);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return returnData;
	}
	
	/**
	 * keep panels updated
	 */
	public void update() {
		this.blackjackPanel.updateBalance(this.balance);
		this.transactionsPanel.update(this.id);
	}
}
