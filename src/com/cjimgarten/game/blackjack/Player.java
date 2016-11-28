/*
 * Player.java
 * 
 * created: 11-03-2016
 * modified: 11-27-2016
 * 
 * class represents a card player
 */

package com.cjimgarten.game.blackjack;

import java.util.ArrayList;

public class Player extends ArrayList<Card> {
	
	// add some attributes
	private int value;
	
	/**
	 * create a player
	 */
	public Player() {
		super();
		this.value = 0;
	}
	
	/**
	 * add a method to get the value of the players hand
	 */
	public int getValue() {
		this.update();
		return this.value;
	}
	
	/**
	 * add a method to set the value of the players hand
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * add a method to add a card to the players hand
	 */
	public void addCard(Card c) {
		this.add(c);
		this.update();
	}
	
	/**
	 * add a method to update the value of the players hand
	 */
	private void update() {
		this.value = 0; // keep track of the hands value
		ArrayList<Integer> aces = new ArrayList<Integer>(); // hold the indexes of the aces

		// iterate over the players hand to calculate the total value
		for (int i = 0; i < this.size(); i++) {
			// keep a count of how many aces are in the players current hand
			if (this.get(i).getRank().compareTo("Ace") == 0) {
				aces.add(i);
			}
			this.value += this.get(i).getValue();
		}

		// if there are no Aces, return the hands value
		if (aces.isEmpty() || this.value <= 21) {
			return;
		}

		// if there are Aces or the hands value is greater than 21
		// check to see if an Aces value needs to be changed to 1
		for (int i = aces.size() - 1; i >= 0; i--) {
			if (this.get(aces.get(i)).getValue() == 11) {
				this.value -= 10;
				this.get(aces.get(i)).swapAceValue();
				return;
			}
		}
	}
}
