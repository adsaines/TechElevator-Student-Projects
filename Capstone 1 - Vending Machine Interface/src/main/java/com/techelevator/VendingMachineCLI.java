package com.techelevator;

import java.util.Scanner;

import com.techelevator.view.Menu;

public class VendingMachineCLI {

	private static final String MAIN_MENU_EXIT = "Exit";
	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_EXIT};
	
	private static final String PURCHASE_MENU_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_FEED_MONEY, PURCHASE_MENU_SELECT_PRODUCT ,PURCHASE_MENU_FINISH_TRANSACTION};
	
	private static final String[] ACCEPTED_MONEY = {"$1","$2","$5","$10"};
	
	private Menu menu;
	
	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}
	
	public void run() {
		
		VendingMachine snackDispenser = new VendingMachine();
		boolean runProgram = true;
		
		while(runProgram) {
			String choice = (String)menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			Scanner scan = new Scanner(System.in);
			
			if(choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				
				// display vending machine items
				String[] inventoryArray = snackDispenser.getInventoryArray();
				for(String item: inventoryArray) {
					System.out.println(item);
				}
				
			} else if(choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				
				// must pass back the location ID
				while (!choice.equals(PURCHASE_MENU_FINISH_TRANSACTION)) { //while not "finish transaction"

					System.out.println("");
					System.out.print("Current Money Provided: " + MoneyHandling.getPriceString(snackDispenser.moneyInMachine));
					choice = (String)menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
					
					//call selection item from purchase menu
					//loop it until finish transaction occurs
					
					if (choice.equals(PURCHASE_MENU_FEED_MONEY)) {
						//ask customer to input money in 1,2,5,10 increments
						String moneyAdd = (String) menu.getChoiceFromOptions(ACCEPTED_MONEY);
						//add money to vending machine, remove dollar sign while doing so
						boolean moneyFed = snackDispenser.feedMoney(moneyAdd.replace("$", ""));
						
					} else if (choice.contentEquals(PURCHASE_MENU_SELECT_PRODUCT)) {
						//select product
						System.out.print("Enter slot number for desired snack: ");
						choice = scan.nextLine().toUpperCase();
				
						//purchaseable will return a string describing what the issue is if the item is not purchasable
						//purchaseable will be "yes" otherwise
						String successfulPurchase = snackDispenser.purchaseProduct(choice);
						
						//check if item was purchased
						if (successfulPurchase.contentEquals("yes") == false) {
							//output reason that the snack couldn't be bought
							System.out.println("");
							System.out.println(successfulPurchase);
							System.out.println("");
						} 
					}
				}
				
				//returns money in change, returns food noises
				System.out.println(snackDispenser.finishTransaction());
				
			}else if(choice.equals(MAIN_MENU_EXIT)) {
				//print sales report for this period
				System.out.println("");
				snackDispenser.report.printSalesReport();
				System.out.println("Exiting Program. Please come again.");
				//set program end condition
				runProgram = false;
				
			}
		}
	}
	
	public static void main(String[] args) {
		
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}
}
