package com.techelevator.view;

import org.junit.Test;

import com.techelevator.Transactions;

public class TransactionsTest {
	
	@Test
	public void transactions_prints_information_to_file() {
		
		Transactions.addTransaction("feedMoney", 500, 1000);
		
	}

}
