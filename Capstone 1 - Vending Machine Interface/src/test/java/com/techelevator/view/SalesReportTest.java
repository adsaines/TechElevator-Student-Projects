package com.techelevator.view;

import org.junit.Test;

import com.techelevator.VendingMachine;

public class SalesReportTest {
	
	@Test
	public void salesReport_makes_a_file() {
		
		VendingMachine vm = new VendingMachine();
		vm.report.printSalesReport();
		
	}
	
}
