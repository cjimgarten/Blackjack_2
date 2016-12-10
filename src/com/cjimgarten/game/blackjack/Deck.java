/*
 * Deck.java
 * 
 * created: 11-02-2016
 * modified: 12-10-2016
 * 
 * class represents a deck of cards
 */

package com.cjimgarten.game.blackjack;

import java.util.ArrayList;

public class Deck extends ArrayList<Card> {
	
	// add some attributes
	private int cardWidth;
	private int cardHeight;
	
	/**
	 * create a deck
	 */
	public Deck(int cardWidth, int cardHeight) {
		super();
		this.cardWidth = cardWidth;
		this.cardHeight = cardHeight;
		this.configureDeck();
	}
	
	/**
	 * configure the deck of cards
	 */
	private void configureDeck() {
		String[] ranks = {"Ace", "King", "Queen", "Jack", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
		String[] suits = {"Spade", "Club", "Diamond", "Heart"};
		int r = ranks.length;
		int s = suits.length;
		int value = 0;
		for (int i = 0; i < r; i++) {
			if (ranks[i].equals("Ace")) {
				value = 11;
			} else if (ranks[i].equals("King") || ranks[i].equals("Queen") || 
					ranks[i].equals("Jack") || ranks[i].equals("10")) {
				value = 10;
			} else {
				value--;
			}
			for (int j = 0; j < s; j++) {
				String imagePath = "../../images/png/" + ranks[i].toLowerCase() + "_of_" + suits[j].toLowerCase() + "s.png";
				Card c = new Card(ranks[i], suits[j], value, false, imagePath, this.cardWidth, this.cardHeight);
				this.add(c);
			}
		}
	}
	
	/**
	 * add a method to remove the top card from the deck
	 */
	public Card getTopCard(boolean visible) {
		if (visible) {
			this.get(0).swapVisibility();
		}
		return this.remove(0);
	}
	
	/**
	 * add a method to shuffle the deck
	 */
	public void shuffle() {
		// split the deck into two halves
		ArrayList<Card> h1 = new ArrayList<Card>(); // ArrayList to hold the first half
		ArrayList<Card> h2 = new ArrayList<Card>(); // ArrayList to hold the second half

		// fill the two halves with cards from the deck
		int n = this.size() / 2;
		for (int i = 0; i < this.size(); i++) {
			if (i < n) { // fill the first half
				h1.add(this.get(i));
			} else { // fill the second half
				h2.add(this.get(i));
			}
		}

		// merge the halves together
		for (int i = 0; i < this.size(); i++) {
			if (i % 2 != 0) { // pick a random card from the first half and put it back in the deck
				int j = (int)(Math.random() * (h1.size()));
				this.set(i, h1.remove(j));
			} else { // pick a random card from the second half and put it back in the deck
				int k = (int)(Math.random() * (h2.size()));
				this.set(i, h2.remove(k));
			}
		}
	}
}
