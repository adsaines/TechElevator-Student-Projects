package com.techelevator;

import java.util.UnknownFormatConversionException;

public class MoneyHandling {
	
	private static final int DOLLAR_CONVERSION = 100;
	
	public static int toInteger(String moneyAmount) {
		//Convert moneyAmount into integer.
		moneyAmount = moneyAmount.replace("$", "");
		return (int) (Double.parseDouble(moneyAmount) * DOLLAR_CONVERSION);
	}
	

	public static String getPriceString(Snackable item) {
		String priceString="not formatted";
		
		try {
			priceString =  String.format("$%s.%s", item.getPrice()/DOLLAR_CONVERSION, decimalConversion(item.getPrice()%DOLLAR_CONVERSION));
		} catch (UnknownFormatConversionException e) {
			System.out.println("The price is not calculating right.");
		}
		return priceString;
	}

	public static String getPriceString(int money) {
		String priceString="not formatted";
		
		try {
			priceString =  String.format("$%s.%s", money/DOLLAR_CONVERSION, decimalConversion(money%DOLLAR_CONVERSION));
		} catch (UnknownFormatConversionException e) {
			System.out.println("The price is not calculating right.");
		}
		return priceString;
	}

	private static String decimalConversion(int value) {
		String decimalVal = "";
		
		if (value < 10) {
			//add a zero to the front of it
			decimalVal = "0" + value;
		} else {
			decimalVal += value;
		}
		
		return decimalVal;
	}
	
	public static String makeChange(int moneyAmount) {
		String change = "";
		int numChange = 0;
		int quarters = 25;
		int dimes = 10;
		int nickels = 5;
		
		//return least number of coins possible
		//return only in quarters, nickels and dimes
		
		numChange = moneyAmount / quarters;
		moneyAmount -= quarters * numChange;
		
		if (numChange != 0) {
			change += numChange + " Quarter(s), ";
		}

		numChange = moneyAmount / dimes;
		moneyAmount -= dimes * numChange;

		if (numChange != 0) {
			change += numChange + " Dime(s), ";
		}

		numChange = moneyAmount / nickels;
		moneyAmount -= nickels * numChange;
		
		if (numChange != 0) {
			change += numChange + " Nickel(s)";
		}
		
		if (change.contentEquals("")) {
			// no change was dispensed, say so
			change = "No change to dispense.";
		}
		
		return change;
	}
	
}
