package com.techelevator;

public class Drink extends Snackable{
	
	public Drink(int stock, int itemPrice, String itemName, String itemLocationID) {
		super (stock, itemPrice, itemName, itemLocationID);
	}

	@Override
	public String getSound() {
		return "Glug glug, Yum!";
	}

}
