/*
 * BlackjackPanel.java
 * 
 * created: 11-08-2016
 * modified: 11-27-2016
 * 
 * panel for blackjack
 */

package com.cjimgarten.game.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.cjimgarten.MainApp;
import com.cjimgarten.game.blackjack.Blackjack;
import com.cjimgarten.game.blackjack.Card;
import com.cjimgarten.game.blackjack.Player;

public class BlackjackPanel extends JPanel implements ActionListener {
	
	// keep track of the users' id and cash balance
	private String id, balance;

	// SQL connection
	private Connection conn;
	
	// GUI components
	private JPanel centerPane, southPane;
	private JButton hitBtn, stayBtn, dealBtn;
	private JLabel balanceLabel, wagerLabel, userValueLabel, dealerValueLabel;
	private OutcomesPanel outcomesPanel;
	
	// blackjack object
	private Blackjack game;
	
	// hold the users' bet
	private double wager;
	
	// arrays keep track of card positions
	private int[] userCardPos;
	private int[] dealerCardPos;
	
	// shift the cards by this value
	private static final int SHIFT_X = 40;
	private static final int SHIFT_Y = 15;
	
	// define a few constants
	private final String WIN = "winner";
	private final String LOSE = "loser";
	private final String DRAW = "draw";
	
	/**
	 * create the panel
	 */
	public BlackjackPanel(Connection conn, String id, String balance, OutcomesPanel outcomesPanel) {
		super();
		this.setLayout(new BorderLayout());
		this.conn = conn;
		this.id = id;
		this.balance = balance;
		this.outcomesPanel = outcomesPanel;
		this.configurePanel(id, balance);
	}
	
	/**
	 * configure panel components
	 */
	private void configurePanel(String id, String balance) {
		// add JPanels to the center and bottom of the content pane
		int green = 100;
		this.centerPane = new JPanel();
		this.centerPane.setBorder(new LineBorder(new Color(150,150,150), 5));
		this.centerPane.setLayout(null);
		this.centerPane.setBackground(new Color(0, green, 0));
		this.add(this.centerPane, BorderLayout.CENTER);
		this.southPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.southPane.setBackground(new Color(0, green + 50, 0));
		this.add(this.southPane, BorderLayout.SOUTH);
		
		// configure some buttons
		this.hitBtn = new JButton("Hit");
		this.hitBtn.addActionListener(this);
		this.southPane.add(this.hitBtn);
		this.stayBtn = new JButton("Stay");
		this.stayBtn.addActionListener(this);
		this.southPane.add(this.stayBtn);
		this.dealBtn = new JButton("Deal");
		this.dealBtn.addActionListener(this);
		this.southPane.add(this.dealBtn);
		
		// configure a label to show users' cash balance
		int x = 15;
		int y = 15;
		int width = 200;
		int height = 25;
		green = 220;
		Color fgColor = new Color(0, green, 0);
		LineBorder labelBorder = new LineBorder(new Color(0, green, 0), 1);
		this.balanceLabel = new JLabel("", JLabel.CENTER);
		this.updateBalance(this.balance);
		this.balanceLabel.setBounds(x, y, width, height);
		this.balanceLabel.setBorder(labelBorder);
		this.balanceLabel.setForeground(fgColor);
		this.centerPane.add(this.balanceLabel);
		
		// configure a label to show the users' wager
		y = 45;
		this.wagerLabel = new JLabel("", JLabel.CENTER);
		this.updateWager();
		this.wagerLabel.setBounds(x, y, width, height);
		this.wagerLabel.setBorder(labelBorder);
		this.wagerLabel.setForeground(fgColor);
		this.centerPane.add(this.wagerLabel);
		
		// configure some labels to display hand values
		int sideLength = 35;
		y = 290;
		this.userValueLabel = new JLabel("", JLabel.CENTER);
		this.userValueLabel.setBounds(x, y, sideLength, sideLength);
		this.userValueLabel.setBorder(new LineBorder(new Color(0, green, 0), 1));
		this.userValueLabel.setForeground(new Color(0, green, 0));
		this.centerPane.add(this.userValueLabel);
		
		x = 725;
		y = 150;
		this.dealerValueLabel = new JLabel("", JLabel.CENTER);
		this.dealerValueLabel.setBounds(x, y, sideLength, sideLength);
		this.dealerValueLabel.setBorder(new LineBorder(new Color(0, green, 0), 1));
		this.dealerValueLabel.setForeground(new Color(0, green, 0));
		this.centerPane.add(this.dealerValueLabel);
		
		// initialize the game of blackjack
		this.game = new Blackjack();
		this.userCardPos = new int[2];
		this.dealerCardPos = new int[2];
	}
	
	/**
	 * start a game
	 */
	public void startGame() {
		if (this.game.checkDeckCount()) {
			System.out.println("New Deck"); // DEBUGGER
		}
		this.dealBtn.setEnabled(false);
		this.wager = this.placeBet();
		this.updateWager();
		this.game.shuffleDeck();
		this.game.deal();
		this.userCardPos[0] = 15; /* x */
		this.userCardPos[1] = 360; /* y */
		this.dealerCardPos[0] = 685; /* x */
		this.dealerCardPos[1] = 15; /* y */
		this.enableButtons();
		this.dealCards();
	}
	
	/**
	 * deal cards
	 */
	public void dealCards() {
		
		// deal the users cards
		ArrayList<Card> user = this.game.getUser();
		for (int i = 0; i < user.size(); i++) {
			JLabel cardLabel = user.get(i).getImageLabel();
			cardLabel.setBounds(this.userCardPos[0], this.userCardPos[1], 75, 100);
			this.centerPane.add(cardLabel);
			this.userCardPos[0] += SHIFT_X;
			this.userCardPos[1] -= SHIFT_Y;
		}
		
		// deal the dealers cards
		ArrayList<Card> dealer = this.game.getDealer();
		for (int i = 0; i < dealer.size(); i++) {
			JLabel cardLabel = dealer.get(i).getImageLabel();
			cardLabel.setBounds(this.dealerCardPos[0], this.dealerCardPos[1], 75, 100);
			this.centerPane.add(cardLabel);
			this.dealerCardPos[0] -= SHIFT_X;
			this.dealerCardPos[1] += SHIFT_Y;
		}
		
		// update the values on the screen
		this.updateValues();
	}
	
	/**
	 * remove cards from screen
	 */
	public void removeCards() {
		
		// remove users' card images from screen
		ArrayList<Card> user = this.game.getUser();
		for (int i = 0; i < user.size(); i++) {
			JLabel cardLabel = user.get(i).getImageLabel();
			this.centerPane.remove(cardLabel);
		}
		
		// remove dealers' card images from screen
		ArrayList<Card> dealer = this.game.getDealer();
		for (int i = 0; i < dealer.size(); i++) {
			JLabel cardLabel = dealer.get(i).getImageLabel();
			this.centerPane.remove(cardLabel);
		}
		this.update();
	}
	
	/**
	 * event handling method; implements ActionListener interface
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(this.hitBtn)) { /* user hits */
			int value = this.cardForUser();
			this.updateValues();
			if (value > 21) {
				this.disableButtons();
				JOptionPane.showMessageDialog(this, "YOU WENT OVER! YOU LOSE!");
				this.insertOutcome(this.LOSE); // insert result into outcomes table
				this.dealBtn.setEnabled(true);
			}
		} else if (source.equals(this.stayBtn)){ /* dealers turn */
			this.disableButtons();
			this.flipDealersCards();
			int value = this.game.getDealer().getValue();
			this.updateValues();
			while (value < 17) {
				value = this.cardForDealer();
				this.updateValues();
			}
			if (value > 21) {
				JOptionPane.showMessageDialog(this, "DEALER WENT OVER! YOU WIN!");
				this.insertOutcome(this.WIN); // insert result into outcomes table
				this.dealBtn.setEnabled(true);
			} else {
				int userValue = this.game.getUser().getValue();
				int dealerValue = this.game.getDealer().getValue();
				if (userValue > dealerValue) {
					JOptionPane.showMessageDialog(this, "YOU WIN!");
					this.insertOutcome(this.WIN); // insert result into outcomes table
				} else if (userValue < dealerValue) {
					JOptionPane.showMessageDialog(this, "YOU LOSE!");
					this.insertOutcome(this.LOSE);  // insert result into outcomes table
				} else {
					JOptionPane.showMessageDialog(this, "IT'S A DRAW!");
					this.insertOutcome(this.DRAW); // insert result into outcomes table
				}
				this.dealBtn.setEnabled(true);
			}
		} else { /* deal cards */
			this.removeCards();
			this.game.clearHands();
			this.userValueLabel.setText("");
			this.dealerValueLabel.setText("");
			this.startGame();
		}
		this.outcomesPanel.update(this.id); // update the outcomes panel
	}
	
	/**
	 * flip the dealers cards
	 */
	public void flipDealersCards() {
		
		// remove card images from screen
		ArrayList<Card> dealer = this.game.getDealer();
		for (int i = 0; i < dealer.size(); i++) {
			JLabel cardLabel = dealer.get(i).getImageLabel();
			this.centerPane.remove(cardLabel);
			Card c = dealer.get(i);
			if (!c.getVisibility()) {
				c.swapVisibility();
			}
		}
		this.update();
		
		// put the card images back
		this.dealerCardPos[0] += SHIFT_X * 2;
		this.dealerCardPos[1] -= SHIFT_Y * 2;
		for (int i = 0; i < dealer.size(); i++) {
			JLabel cardLabel = dealer.get(i).getImageLabel();
			cardLabel.setBounds(this.dealerCardPos[0], this.dealerCardPos[1], 75, 100);
			this.centerPane.add(cardLabel);
			this.dealerCardPos[0] -= SHIFT_X;
			this.dealerCardPos[1] += SHIFT_Y;
		}
		this.update();
	}
	
	/**
	 * give the user a card
	 * returns the users card value
	 */
	public int cardForUser() {
		this.game.hit("user");
		Player p = this.game.getUser();
		Card c = p.get(p.size() - 1);
		JLabel cardLabel = c.getImageLabel();
		cardLabel.setBounds(userCardPos[0], userCardPos[1], 75, 100);
		userCardPos[0] += SHIFT_X;
		userCardPos[1] -= SHIFT_Y;
		this.centerPane.add(cardLabel);
		this.update();
		int value = this.game.getUser().getValue();
		return value;
	}
	
	/**
	 * give the dealer a card
	 * returns the dealers card value
	 */
	public int cardForDealer() {
		this.game.hit("dealer");
		Player p = this.game.getDealer();
		Card c = p.get(p.size() - 1);
		JLabel cardLabel = c.getImageLabel();
		cardLabel.setBounds(dealerCardPos[0], dealerCardPos[1], 75, 100);
		this.dealerCardPos[0] -= SHIFT_X;
		this.dealerCardPos[1] += SHIFT_Y;
		this.centerPane.add(cardLabel);
		this.update();
		int value = this.game.getDealer().getValue();
		return value;
	}
	
	/**
	 * disable buttons on the screen
	 */
	public void disableButtons() {
		this.hitBtn.setEnabled(false);
		this.stayBtn.setEnabled(false);
	}
	
	/**
	 * enable buttons on the screen
	 */
	public void enableButtons() {
		this.hitBtn.setEnabled(true);
		this.stayBtn.setEnabled(true);
	}
	
	/**
	 * update GUI
	 */
	public void update() {
		this.centerPane.revalidate();
		this.centerPane.repaint();
	}
	
	/**
	 * update cash balance on GUI
	 */
	public void updateBalance(String balance) {	
		this.balanceLabel.setText("Balance: $" + balance);
	}
	
	/**
	 * update wager on GUI
	 */
	public void updateWager() {
		this.wagerLabel.setText("Wager: $" + this.wager);
	}
	
	/**
	 * update values on GUI
	 */
	public void updateValues() {
		int userValue = this.game.getUser().getValue();
		this.userValueLabel.setText(userValue + "");
		int dealerValue = this.game.getDealer().getValue();
		this.dealerValueLabel.setText(dealerValue + "");
	}

	/**
	 * allow the user to place a bet
	 */
	public double placeBet() {
		String betStr = JOptionPane.showInputDialog(this, "How much would you like to bet?");
		double bet = 0.00;
		try {
			while (betStr.equals("") || Double.parseDouble(betStr) < 10) {
				betStr = JOptionPane.showInputDialog(this, "You must place a bet to play!\n$10 is the minimum bet!");
			}
			bet = Double.parseDouble(betStr);
		} catch (Exception e) { // if the user hits 'Cancel', log them out
			MainApp.loggedIn = false;
		}
		return bet;
	}
	
	/**
	 * insert outcomes into the database
	 */
	public void insertOutcome(String outcome) {		
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
			if (outcome.equals("winner")) {
				newBalance += this.wager;
			} else if (outcome.equals("loser")) {
				newBalance -= this.wager;
			}
			this.balance = newBalance + "";
			
			// update database
			String update = "UPDATE users SET balance = " + this.balance + "WHERE id = " + this.id;
			int updateResult = stmt.executeUpdate(update); // execute update statement
			String insert = "INSERT INTO outcomes (user_id, date, time, outcome, wager, old_bal, new_bal, time_stamp)"
					+ " VALUES (" + this.id + ", CURDATE(), CURTIME(), '" + outcome + "', " + this.wager + ", " + oldBalance + ", "
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
				try {
					stmt.close();
				} catch(SQLException e) {
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		this.updateBalance(this.balance);
	}
}
