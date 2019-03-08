package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transactions {
	
	private static FileWriter writer;
	
	public static void addTransaction(String operation, int operationAmount, int moneyInMachine) {
		
		File transactions = new File("Log.txt");
		
		//Print line to transaction file.
		try {
			if(transactions.exists() == false) {
				transactions.createNewFile();
			}
			
			writer = new FileWriter(transactions, true);
			writer.write(getDate() + " " + operation + " " + MoneyHandling.getPriceString(moneyInMachine) + "  " + MoneyHandling.getPriceString(moneyInMachine + operationAmount) + "\n");
			writer.flush();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static String getDate() {
		SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MM-yyyy a hh:mm:ss");
		
		Date today = new Date();
		return simpleDate.format(today);
	}
	

}
