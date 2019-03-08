package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SalesReport {
	
	File salesReport;

	private int totalSales;
	Map<Snackable, Integer> salesTracker = new HashMap<Snackable, Integer>();
	
	public SalesReport(){
		//Read previous SalesReport file.
		int reportNum = 0;
		String reportName = "SalesReport" + reportNum;
		salesReport = new File(reportName);
		
		while (salesReport.exists() == true) {
			// add one ot the number until an unused number is located
			reportNum +=1;
			reportName = "SalesReport" + reportNum;
			salesReport = new File(reportName);
		} 
		
		// now that the file name is verified, create a new report file
		try {
			salesReport.createNewFile();
	
		} catch (IOException e) {
			System.out.print("Creating Sales Report file.");
		}
		
	}
	
	public void itemInitialize(Snackable snack) {
		salesTracker.put(snack, 0);
	}
	
	public void itemPurchased(Snackable snack) {
		int numBought;
		
		//Update SalesReport with past items.
		numBought = salesTracker.getOrDefault(snack, 0);
		salesTracker.put(snack, numBought + 1);
		totalSales += snack.getPrice();
		
	}
	
	public void printSalesReport(){
		//Read previous SalesReport file.
		
	try {
		System.out.println("Creating Sales Report file.");
		PrintWriter writer = new PrintWriter(salesReport);
		Set<Snackable> keys = salesTracker.keySet();
		for (Snackable key: keys) {
			writer.println(key.getName() + "|" + salesTracker.get(key));
		}
		
		writer.println("");
		writer.println("**TOTAL SALES** " + MoneyHandling.getPriceString(totalSales));
		writer.flush();
		
	} catch (FileNotFoundException e) {
		System.out.println("There is no Sales Report file, oops?");
		e.printStackTrace();
	}
		
	}
	
		
	
	
	
}
