/*
 * Card.java
 * 
 * created: 11-02-2016
 * modified: 12-10-2016
 * 
 * class represents a playing card
 */

package com.cjimgarten.game.blackjack;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Card {
	
	// add some attributes
	private final String rank;
	private final String suit;
	private int value;
	private boolean visible;
	private JLabel upImageLabel;
	private JLabel downImageLabel;
	
	/**
	 * create a card
	 */
	public Card(String rank, String suit, int value, boolean visible, String imagePath, int imageWidth, int imageHeight) {
		this.rank = rank;
		this.suit = suit;
		this.value = value;
		this.visible = visible;
		ImageIcon upImageIcon = new ImageIcon(getClass().getResource(imagePath));
		ImageIcon downImageIcon = new ImageIcon(getClass().getResource("../../images/png/card_back_black.png"));
		upImageIcon = this.resizeImageIcon(upImageIcon, imageWidth, imageHeight); // resize upImageIcon
		downImageIcon = this.resizeImageIcon(downImageIcon, imageWidth, imageHeight); // resize downImageIcon
		this.upImageLabel = new JLabel(upImageIcon);
		this.downImageLabel = new JLabel(downImageIcon);
	}
	
	/**
	 * resize and return an image icon
	 */
	private ImageIcon resizeImageIcon(ImageIcon imageIcon, int width, int height) {
		Image image = imageIcon.getImage();
		image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(image);
		return imageIcon;
	}
	
	/**
	 * add some get methods
	 */
	public String getRank() {
		if (this.visible) {
			return this.rank;
		}
		return "_";
	}
	
	public String getSuit() {
		if (this.visible) {
			return this.suit;
		}
		return "_";
	}
	
	public int getValue() {
		if (this.visible) {
			return this.value;
		}
		return 0;
	}
	
	public boolean getVisibility() {
		return this.visible;
	}
	
	public JLabel getImageLabel() {
		if (this.visible) {
			return this.upImageLabel;
		}
		return this.downImageLabel;
	}
	
	/**
	 * swap the value of an Ace from 11 to 1 or vice versa
	 */
	public void swapAceValue() {
		if (!(this.rank.equals("Ace"))) {
			return;
		}
		if (this.value == 11) {
			this.value = 1;
		} else {
			this.value = 11;
		}
	}
	
	/**
	 * flip the card
	 */
	public void swapVisibility() {
		if (this.visible) {
			this.visible = false;
		} else {
			this.visible = true;
		}
	}
	
	/**
	 * return data about this instance
	 */
	public String toString() {
		return this.rank + " of " + this.suit + "s: " + this.value;
	}

}
