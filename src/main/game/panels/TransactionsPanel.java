/*
 * TransactionsPanel.java
 * 
 * created: 11-08-2016
 * modified: 11-20-2016
 * 
 * panel to display transactions (withdrawals and deposits)
 */

package main.game.panels;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TransactionsPanel extends JPanel {

	// SQL connection
	private Connection conn;
	
	// GUI components
	private JTable transactionsTable;
	private JScrollPane scrollPane;
	
	/**
	 * create the panel
	 */
	public TransactionsPanel(Connection conn, String id) {
		super();
		this.setLayout(new BorderLayout());
		this.conn = conn;
		ResultSet rs = this.getData(id);
		this.configurePanel(rs);
	}
	
	/**
	 * configure panel components
	 */
	public void configurePanel(ResultSet rs) {
		// count the rows in the ResultSet
		int rows = 0;
		try {
			while (rs.next()) {
				rows++;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		// store the ResultSet in a two-dimensional Object array
		String[] columnNames = {"Entry #", "Date", "Transaction", "Amount", "Previous Balance", "Current Balance"};
		Object[][] data = new Object[rows][6];
		try {
			rs.beforeFirst(); // move cursor back to beginning
			for (int i = 0; rs.next(); i++) {
				String date = rs.getString("date");
				String transaction = rs.getString("transaction");
				String amount = rs.getString("amount");
				String prev_bal = rs.getString("prev_bal");
				String cur_bal = rs.getString("cur_bal");
				Object[] arr = {new Integer(i+1), date, transaction, amount, prev_bal, cur_bal};
				data[i] = arr;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		// create the table and the scroll pane
		this.transactionsTable = new JTable(data, columnNames);
		this.scrollPane = new JScrollPane(this.transactionsTable);
		this.add(this.scrollPane);
	}
	
	/**
	 * get transaction data from database
	 */
	public ResultSet getData(String id) {
		// pull and display transaction data from database
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String query = "SELECT date, amount, transaction, prev_bal, cur_bal FROM transactions WHERE user_id = " + id;
			stmt = this.conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 * update the panel
	 */
	public void update(String id) {
		this.remove(this.scrollPane);
		ResultSet rs = this.getData(id);
		this.configurePanel(rs);
	}
}
