package com.techelevator;

public class Candy extends Snackable{
	
	public Candy(int stock, int itemPrice, String itemName, String itemLocationID) {
		super (stock, itemPrice, itemName, itemLocationID);
	}

	@Override
	public String getSound() {
		return "Munch munch, Yum!";
	}

}
