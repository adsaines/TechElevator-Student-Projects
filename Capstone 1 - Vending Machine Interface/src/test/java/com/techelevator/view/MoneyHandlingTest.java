package com.techelevator.view;

import org.junit.Test;

import com.techelevator.Chips;
import com.techelevator.MoneyHandling;
import com.techelevator.Snackable;

import org.junit.Assert;

public class MoneyHandlingTest {

	@Test
	public void getPriceStringReturnString() {
		
		//returns a string
		Snackable chips = new Chips(5, 3, "Doritos", "G5");
		Assert.assertEquals("$0.03", MoneyHandling.getPriceString(chips) );
		
	}
	
	@Test
	public void getPriceStringUsingIntReturnString() {
		
		//returns a string
		Snackable chips = new Chips(5, 3, "Doritos", "G5");
		Assert.assertEquals("$0.03", MoneyHandling.getPriceString(chips.getPrice()) );
		
	}
	
	@Test
	public void priceToIntegerReturnsInteger() {
		
		//returns a string
		Assert.assertEquals(3, MoneyHandling.toInteger("0.03") );
		Assert.assertEquals(350, MoneyHandling.toInteger("3.50") );
		Assert.assertEquals(405, MoneyHandling.toInteger("4.05") );
	

	}
	
	@Test
	public void makeChangeReturnsChange() {
		
		//returns a string
		Assert.assertEquals("0 Quarter(s), 1 Dime(s), 1 Nickel(s)", MoneyHandling.makeChange(15) );
		Assert.assertEquals("7 Quarter(s), 0 Dime(s), 1 Nickel(s)", MoneyHandling.makeChange(180) );
		Assert.assertEquals("6 Quarter(s), 0 Dime(s), 0 Nickel(s)", MoneyHandling.makeChange(150) );
	
	}
}
