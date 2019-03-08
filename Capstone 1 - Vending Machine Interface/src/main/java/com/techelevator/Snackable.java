package com.techelevator;

public abstract class Snackable {
	
	private int price;
	private int stock;
	private String name;
	private String locationID;
	
	public Snackable(int stock, int itemPrice, String itemName, String itemLocationID) {
		this.stock = stock;
		this.price = itemPrice;
		this.name = itemName;
		this.locationID = itemLocationID;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLocationID() {
		return locationID;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void iterateStock() {
		if (stock != 0) {
			stock -= 1;
		}
	}
	
	public int getStock() {
		return stock;
	}
	
	public abstract String getSound();
}
