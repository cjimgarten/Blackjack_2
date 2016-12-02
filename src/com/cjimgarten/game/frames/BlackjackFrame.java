/*
 * BlackjackFrame.java
 * 
 * created: 10-30-2016
 * modified: 12-02-2016
 * 
 * frame for blackjack
 */

package com.cjimgarten.game.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.FileOutputStream;

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
import java.util.ArrayList;

import com.cjimgarten.Blackjack_2;
import com.cjimgarten.game.panels.BlackjackPanel;
import com.cjimgarten.game.panels.TransactionsPanel;
import com.cjimgarten.game.panels.OutcomesPanel;

public class BlackjackFrame extends JFrame implements ActionListener {

	// SQL connection
	private Connection conn;
	
	// users' id, username and cash balance
	private String id, username, balance;
	
	// GUI components
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem depositItem, withdrawItem, exportItem, logoutItem;
	private JTabbedPane tabbedPane;
	private BlackjackPanel blackjackPanel;
	private TransactionsPanel transactionsPanel;
	private OutcomesPanel outcomesPanel;
	
	// array for frame bounds
	private int[] frameBounds = { 100, 100, 800, 600 };
	
	// define a few constants
	private final String DEPOSIT = "deposit";
	private final String WITHDRAWAL = "withdrawal";
	
	/**
	 * create the frame
	 */
	public BlackjackFrame(Connection conn, String title, Image logo, String username) {
		super(title + " (" + username + ")");
		this.conn = conn;
		this.setIconImage(logo);
		this.username = username;
		try {
			String query = "SELECT id, balance FROM users WHERE username = '" + this.username + "'";
			ResultSet userData = this.getData(query);
			while (userData.next()) {
				this.id = userData.getString("id");
				this.balance = userData.getString("balance");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		this.configureFrame();
	}
	
	public BlackjackFrame(Connection conn, String title, String username) {
		super(title + " (" + username + ")");
		this.conn = conn;
		this.username = username;
		try {
			String query = "SELECT id, balance FROM users WHERE username = '" + this.username + "'";
			ResultSet userData = this.getData(query);
			while (userData.next()) {
				this.id = userData.getString("id");
				this.balance = userData.getString("balance");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		this.configureFrame();
	}
	
	public BlackjackFrame(Connection conn, String username) {
		super("Blackjack (" + username + ")");
		this.conn = conn;
		this.username = username;
		try {
			String query = "SELECT id, balance FROM users WHERE username = '" + this.username + "'";
			ResultSet userData = this.getData(query);
			while (userData.next()) {
				this.id = userData.getString("id");
				this.balance = userData.getString("balance");
			}
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
		
		// add a menu bar
		this.menuBar = new JMenuBar();
		this.menu = new JMenu("Menu");
		this.depositItem = new JMenuItem("Deposit");
		this.depositItem.addActionListener(this);
		this.withdrawItem = new JMenuItem("Withdrawal");
		this.withdrawItem.addActionListener(this);
		this.exportItem = new JMenuItem("Export");
		this.exportItem.addActionListener(this);
		this.logoutItem = new JMenuItem("Logout");
		this.logoutItem.addActionListener(this);
		this.menuBar.add(this.menu);
		this.menu.add(this.depositItem);
		this.menu.add(this.withdrawItem);
		this.menu.add(this.exportItem);
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
				this.executeTransaction(this.DEPOSIT, deposit);
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
				this.executeTransaction(this.WITHDRAWAL, withdrawal);
			} catch (SQLException exc) {
				System.err.println(exc.getMessage());
				exc.printStackTrace();
			}
		} else if (source.equals(this.exportItem)) { // allow the user to export data
			this.exportTransactions();
			this.exportOutcomes();
			JOptionPane.showMessageDialog(this, "Exported successfully");
		} else { // change the users' login status
			Blackjack_2.logout();
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
	 * get data from database
	 */
	public ResultSet getData(String query) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = this.conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} 
		return rs;
	}
	
	/**
	 * export users transactions to a file
	 */
	public void exportTransactions() {
		// get users transaction data
		String query = "SELECT * FROM transactions WHERE user_id = " + this.id;
		ResultSet transactions = this.getData(query);
		
		// specify a file path
		String path = "exported/transactions.csv";
		
		// store table column names
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("id");
		columns.add("user_id");
		columns.add("date");
		columns.add("time");
		columns.add("transaction");
		columns.add("amount");
		columns.add("old_bal");
		columns.add("new_bal");
		columns.add("time_stamp");
			
		// write transactions to file
		this.writeToFile(path, transactions, columns);
	}
	
	/**
	 * export users outcomes to a file
	 */
	public void exportOutcomes() {
		// get users outcome data
		String query = "SELECT * FROM outcomes WHERE user_id = " + this.id;
		ResultSet outcomes = this.getData(query);
		
		// specify a file path
		String path = "exported/outcomes.csv";
		
		// store table column names
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("id");
		columns.add("user_id");
		columns.add("date");
		columns.add("time");
		columns.add("outcome");
		columns.add("wager");
		columns.add("old_bal");
		columns.add("new_bal");
		columns.add("time_stamp");
		
		// write outcomes to file
		this.writeToFile(path, outcomes, columns);
	}
	
	/**
	 * write users data (rs) to a file (path)
	 */
	private void writeToFile(String path, ResultSet rs, ArrayList<String> columns) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(path);
			while (rs.next()) { // iterate over rows
				for (int i = 0; i < columns.size(); i++) { // iterate over columns
					String column = rs.getString(columns.get(i));
					for (int j = 0; j < column.length(); j++) { // iterate over characters
						out.write(column.charAt(j));
					}
					out.write(',');
				}
				out.write('\n');
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
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
			double newBalance = Double.parseDouble(this.balance);
			double oldBalance = newBalance;
			if (action.equals("deposit")) {
				newBalance += amount;
			} else if (action.equals("withdrawal")) {
				newBalance -= amount;
			}
			this.balance = newBalance + "";
			
			// update database
			String update = "UPDATE users SET balance = " + this.balance + "WHERE id = " + this.id;
			int updateResult = stmt.executeUpdate(update); // execute update statement
			String insert = "INSERT INTO transactions (user_id, date, time, transaction, amount, old_bal, new_bal, time_stamp)"
					+ " VALUES (" + this.id + ", CURDATE(), CURTIME(), '" + action + "', " + amount + ", " + oldBalance + ", " 
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
	 * keep panels updated
	 */
	public void update() {
		this.blackjackPanel.updateBalance(this.balance);
		this.transactionsPanel.update(this.id);
	}
}
