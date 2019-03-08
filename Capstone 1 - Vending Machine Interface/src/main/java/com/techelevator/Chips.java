package com.techelevator;

public class Chips extends Snackable{
	
	public Chips(int stock, int itemPrice, String itemName, String itemLocationID) {
		super (stock, itemPrice, itemName, itemLocationID);
	}

	@Override
	public String getSound() {
		return "Crunch crunch, Yum!";
	}
}
