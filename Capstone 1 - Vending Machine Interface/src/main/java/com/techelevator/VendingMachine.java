package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class VendingMachine {
	

	public static final int INDEX_LOCATION_ID = 0;
	public static final int INDEX_SNACK_NAME = 1;
	public static final int INDEX_PRICE = 2;
	public static final int INDEX_SNACK_TYPE = 3;
	public static final int DEFAULT_STOCK = 5;
	
	public Map<String, Snackable> inventory = new LinkedHashMap<String, Snackable>();
	public List<Snackable> purchaseList = new ArrayList<Snackable>();
	public int moneyInMachine;
	
	public SalesReport report = new SalesReport();
	
	
	public VendingMachine() {
		
		moneyInMachine = 0;

		//Load inventory from provided file.
		File input = new File("vendingmachine.csv");
		
		try {
			Scanner scan = new Scanner(input);
			
			while (scan.hasNextLine()) {
				
				//extract object information
				String[] values = scan.nextLine().split("\\|");
				
				//check for item type
				//create instance of the object
				//load instance into inventory
				if (values[INDEX_SNACK_TYPE].equals("Chip")) {
					inventory.put(values[INDEX_LOCATION_ID], new Chips(DEFAULT_STOCK, MoneyHandling.toInteger(values[INDEX_PRICE]), values[INDEX_SNACK_NAME], values[INDEX_LOCATION_ID]));
				}
				
				if (values[INDEX_SNACK_TYPE].equals("Candy")) {
					inventory.put(values[INDEX_LOCATION_ID], new Candy(DEFAULT_STOCK, MoneyHandling.toInteger(values[INDEX_PRICE]), values[INDEX_SNACK_NAME], values[INDEX_LOCATION_ID]));
				}
				
				if (values[INDEX_SNACK_TYPE].equals("Drink")) {
					inventory.put(values[INDEX_LOCATION_ID], new Drink(DEFAULT_STOCK, MoneyHandling.toInteger(values[INDEX_PRICE]), values[INDEX_SNACK_NAME], values[INDEX_LOCATION_ID]));
				}
				
				if (values[INDEX_SNACK_TYPE].equals("Gum")) {
					inventory.put(values[INDEX_LOCATION_ID], new Gum(DEFAULT_STOCK, MoneyHandling.toInteger(values[INDEX_PRICE]), values[INDEX_SNACK_NAME], values[INDEX_LOCATION_ID]));
				}
				
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Inventory input not found!");
			e.printStackTrace();
		}
		
		Set <String> keys = inventory.keySet();
		
		for (String key : keys) {
			report.itemInitialize(inventory.get(key));
		}
		
	}
	
	
	public String[] getInventoryArray() {
		String[] itemsInInventory = new String[inventory.keySet().size() + 2]; //+2 for blank and header lines
		Set<String> keys = inventory.keySet();
		int itemCount = 2; //start after the header and blank line
		
		itemsInInventory[0] = "";
		itemsInInventory[1] = String.format("%s %20s %10s %10s", "Slot Number", "Item Name", "Price", "Stock");

		//extract item from hashmap
		for (String key : keys) {
			
			//get item from map
			Snackable item = inventory.get(key);
			String formattedObject;
			boolean soldOut = false;
			
			//creat string describing item
			if(item.getStock() == 0) {
				formattedObject = String.format("%s %29s %10s %10s", item.getLocationID(), item.getName(),MoneyHandling.getPriceString(item), "SOLD OUT");
			} else {
				formattedObject = String.format("%s %29s %10s %10s", item.getLocationID(), item.getName(),MoneyHandling.getPriceString(item), item.getStock());
			}
			
			//add formatted info to itemsInInventory
			itemsInInventory[itemCount] = formattedObject;
			
			//iterate to next index of items
			itemCount+=1;
		}
		
		return itemsInInventory;
	}
	
	public boolean feedMoney(String money) {
		//turn money into the local format
		int moneyAmount = MoneyHandling.toInteger(money);
		//check to make sure that the money is an acceptable amount
		boolean billAccepted = acceptableAmount(moneyAmount);
		
		if (acceptableAmount(moneyAmount) == true) {
			//send added money to transactions
			Transactions.addTransaction("FEED MONEY", moneyAmount, moneyInMachine);
			
			//edit moneyFeed
			moneyInMachine += moneyAmount;
		}
		return billAccepted;
	}
	
	private boolean acceptableAmount(int moneyAmount) {
		//only accept 1,2,5,10 dollars
		return moneyAmount == 100 || moneyAmount == 200 || moneyAmount == 500 || moneyAmount == 1000;
	}
	

	public String purchaseProduct(String order) {
		String purchaseResult = purchaseable(order);
		
		if (purchaseResult.contentEquals("yes")) {
			try {
				Snackable item = inventory.get(order);
				
				//take one from the stack of snacks
				item.iterateStock();
				//add to transaction log
				Transactions.addTransaction(item.getName() + " " + item.getLocationID(), item.getPrice()*(-1), moneyInMachine);
				//add to report
				report.itemPurchased(item);
				//add to purchaselist
				purchaseList.add(item);
				//modify money amount
				moneyInMachine -= item.getPrice();
			
			}catch (NullPointerException e) {
				System.out.println("Something weird happened, the product that was in is now out.");
			}
		}
		
		return purchaseResult;
	}
	
	public String finishTransaction() {
		//make change for customer, add to transaction log
		Transactions.addTransaction("GIVE CHANGE", moneyInMachine*(-1), moneyInMachine);
		String change = MoneyHandling.makeChange(moneyInMachine);
		
		moneyInMachine =0;
		
		//print sounds of eating the purchased snacks
		for (Snackable snack : purchaseList) {
			System.out.println(snack.getSound());
		}
		
		//clear the list of purchased items
		purchaseList.clear();
		
		return change;
	}
	
	private String purchaseable(String order) {
		boolean inStock = true;
		boolean haveMoney = true;
		String decision = "yes";
		
		Snackable item = inventory.get(order);
		
		//first check if the product exists
		if (item != null) {
			//check to see if the item is in stock
			//check to see if there is enough money to buy it
			//or both
			if (item.getStock() < 1 && item.getPrice() > moneyInMachine){
				inStock = false;
				decision = "The item is sold out, and there isn't enough money to buy it if it wasn't. Please add money and select another snack.";
			} else if (item.getStock() < 1){
				inStock = false;
				decision = "This item is sold out. Please select another snack.";
			} else if (item.getPrice() > moneyInMachine) {
				haveMoney = false;
				decision = "There is not enough money to purchase the item. Please add more money to the machine and try again.";
			}
		} else {
			//product doesn't exist
			decision ="That product is not in stock. Please chose another one.";
		}
			
		
		return decision;
	}
	
}
