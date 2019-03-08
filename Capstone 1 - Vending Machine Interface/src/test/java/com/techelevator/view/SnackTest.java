package com.techelevator.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.techelevator.Candy;
import com.techelevator.Chips;
import com.techelevator.Drink;
import com.techelevator.Gum;
import com.techelevator.Snackable;


public class SnackTest {
	
	Snackable drinks;
	Snackable chips;
	Snackable candy;
	Snackable gum;
	
	@Before
	public void setUp() {
		
		drinks = new Drink(5, 3, "cola", "T3");
		chips = new Chips(5, 3, "Doritos", "G5");
		candy = new Candy(5, 3, "Snickers", "B1");
		gum = new Gum(5, 3, "Bubblemint", "N4");
	}
	
	@Test
	public void SnackClass_getfunctionTests() {
		
		Assert.assertEquals(5, drinks.getStock());
		Assert.assertEquals(5, chips.getStock());
		Assert.assertEquals(5, candy.getStock());
		Assert.assertEquals(5, gum.getStock());
		
		Assert.assertEquals(3, drinks.getPrice());
		Assert.assertEquals(3, chips.getPrice());
		Assert.assertEquals(3, candy.getPrice());
		Assert.assertEquals(3, gum.getPrice());
		
		Assert.assertEquals("T3", drinks.getLocationID());
		Assert.assertEquals("G5", chips.getLocationID());
		Assert.assertEquals("B1", candy.getLocationID());
		Assert.assertEquals("N4", gum.getLocationID());
		
		Assert.assertEquals("cola", drinks.getName());
		Assert.assertEquals("Doritos", chips.getName());
		Assert.assertEquals("Snickers", candy.getName());
		Assert.assertEquals("Bubblemint", gum.getName());
		
		Assert.assertEquals("Glug glug, Yum!", drinks.getSound());
		Assert.assertEquals("Crunch crunch, Yum!", chips.getSound());
		Assert.assertEquals("Munch munch, Yum!", candy.getSound());
		Assert.assertEquals("Chew chew, Yum!", gum.getSound());
		
	}
	
	
	

}
