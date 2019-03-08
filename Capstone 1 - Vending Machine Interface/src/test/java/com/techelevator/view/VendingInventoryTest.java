package com.techelevator.view;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.techelevator.VendingMachine;

public class VendingInventoryTest {
	VendingMachine inventory;
	@Before
	public void setUp() {
		inventory = new VendingMachine();
		//inventory.feedMoney("$5");
	}
	
	@Test
	public void getInventoryArray_returns_string_array() {
		
		Assert.assertTrue(inventory.getInventoryArray() instanceof String[]);
	}
	
	@Test
	public void feedMoney_only_accepts_dollars() {
		//inventory.feedMoney("$5");
		Assert.assertTrue(inventory.feedMoney("$1"));
		Assert.assertTrue(inventory.feedMoney("$2"));
		Assert.assertTrue(inventory.feedMoney("$5"));
		Assert.assertTrue(inventory.feedMoney("$10"));
	}
	
	@Test
	public void feedMoney_does_not_accept_change() {
	
		Assert.assertFalse(inventory.feedMoney("4"));
		Assert.assertFalse(inventory.feedMoney("8"));
		Assert.assertFalse(inventory.feedMoney("2.18"));
	}
	
	@Test
	public void purchaseable_returns_false_if_value_is_not_found() {
		Assert.assertEquals("That product is not in stock. Please chose another one.",inventory.purchaseProduct("Z8"));
		Assert.assertEquals("That product is not in stock. Please chose another one.",inventory.purchaseProduct("8"));
		Assert.assertEquals("That product is not in stock. Please chose another one.",inventory.purchaseProduct("Z"));
		Assert.assertEquals("That product is not in stock. Please chose another one.",inventory.purchaseProduct("DD8"));
		Assert.assertEquals("That product is not in stock. Please chose another one.",inventory.purchaseProduct(""));
	}
	
	@Test
	public void purchaseable_returns_true_if_value_is_found() {
		
		inventory.feedMoney("$10");
		inventory.feedMoney("$10");
		
		Assert.assertEquals("yes",inventory.purchaseProduct("A1"));
		Assert.assertEquals("yes",inventory.purchaseProduct("B2"));
		Assert.assertEquals("yes",inventory.purchaseProduct("C3"));
		Assert.assertEquals("yes",inventory.purchaseProduct("D4"));
	}
	
	@Test
	public void selectProduct_modifies_moneyFeed_when_buying() {
		
		inventory.feedMoney("$10");
		inventory.purchaseProduct("A1");
		Assert.assertEquals(695, inventory.moneyInMachine);
	}
	
	@Test
	public void finishTransaction_sets_moneyFeed_to_zero() {
		inventory.feedMoney("$10");
		Assert.assertEquals(1000, inventory.moneyInMachine);
		inventory.finishTransaction();
		Assert.assertEquals(0, inventory.moneyInMachine);
	}
	
}