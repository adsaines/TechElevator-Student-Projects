package com.techelevator;

public class Gum extends Snackable {
	
	public Gum(int stock, int itemPrice, String itemName, String itemLocationID) {
		super (stock, itemPrice, itemName, itemLocationID);
	}

	@Override
	public String getSound() {
		return "Chew chew, Yum!";
	}

}
