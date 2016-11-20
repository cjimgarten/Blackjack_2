/*
 * OutcomesPanel.java
 * 
 * created: 11-18-2016
 * modified: 11-20-2016
 * 
 * panel to display outcomes (wins, losses and ties)
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

public class OutcomesPanel extends JPanel {
	
	// SQL connection
	private Connection conn;
	
	// GUI components
	private JTable outcomesTable;
	private JScrollPane scrollPane;

	/**
	 * create the panel
	 */
	public OutcomesPanel(Connection conn, String id) {
		super();
		this.setLayout(new BorderLayout());
		this.conn = conn;
		ResultSet rs = this.getData(id);
		this.configurePanel(rs);
	}
	
	/**
	 * configure panel components
	 */
	private void configurePanel(ResultSet rs) {
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
		String[] columnNames = {"Entry #", "Date", "Outcome", "Previous Balance", "Wager", "Current Balance"};
		Object[][] data = new Object[rows][6];
		try {
			rs.beforeFirst(); // move cursor back to beginning
			for (int i = 0; rs.next(); i++) {
				String date = rs.getString("date");
				String outcome = rs.getString("outcome");
				String prev_bal = rs.getString("prev_bal");
				String wager = rs.getString("wager");
				String cur_bal = rs.getString("cur_bal");
				Object[] arr = {new Integer(i+1), date, outcome, prev_bal, wager, cur_bal};
				data[i] = arr;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		// create the table and the scroll pane
		this.outcomesTable = new JTable(data, columnNames);
		this.scrollPane = new JScrollPane(this.outcomesTable);
		this.add(this.scrollPane);
	}
	
	/**
	 * get outcome data from database
	 */
	public ResultSet getData(String id) {
		// pull and display transaction data from database
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String query = "SELECT date, outcome, prev_bal, wager, cur_bal FROM outcomes WHERE user_id = " + id;
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
