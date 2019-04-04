package com.techelevator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.Campsite;
import com.techelevator.model.Park;
import com.techelevator.model.Reservation;

public class Park_Function_Tests {
	
	// location and data stream declarations
	private static SingleConnectionDataSource dataSource;
	private JDBCCombinedDAO dao;
	private JdbcTemplate jdbc;
	private Park marsPark;
	private Test_Utilities utilityFunctions = new Test_Utilities(dataSource);

	@BeforeClass
	public static void setupDataSource() {
		// Before any tests are run, this method initializes the datasource for testing.
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		//dataSource.setPassword("password");
		dataSource.setAutoCommit(false); //disable autocommit for this connection
	}
	
	@AfterClass
	public static void closeDataSource() throws SQLException {
		// After all tests have finished running, this method will close the DataSource
		dataSource.destroy();
	}

	@Before
	public void setup() {
		
		// setup DB connection & ParkJDBC class
		jdbc = new JdbcTemplate(dataSource);
		dao = new JDBCCombinedDAO(dataSource);
		
		marsPark = utilityFunctions.newPark();
	}

	// After each test, we rollback any changes that were made to the database so that everything is clean for the next test.
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void getAllParkNames_returns_an_array_of_park_names() {
		String[] parknames = dao.allParkNames();
		
		// check each name that comes from park names for it's presence in the DB.
		for (String name : parknames) {
			
			 String query = "SELECT name FROM park WHERE name = ?;";
			 SqlRowSet queryValue = jdbc.queryForRowSet(query, name);
			 
			 if (queryValue.next()) {
				 Assert.assertEquals(name, queryValue.getString("name"));
			 } else {
				 Assert.assertTrue(false);
			 }
		}
	}
	
	@Test
	public void getParkByName_returns_the_specified_park() {
		Park dummyPark = dao.getParkByName(marsPark.getName());

		Assert.assertEquals( dummyPark.getLocation(), marsPark.getLocation() );
		Assert.assertEquals( dummyPark.getName(), marsPark.getName());
		Assert.assertEquals( dummyPark.getArea(), marsPark.getArea() );
		Assert.assertEquals( dummyPark.getVisitors(), marsPark.getVisitors() );
		Assert.assertEquals( dummyPark.getDescription(), marsPark.getDescription() );
	}
	
	@Test
	public void addPark_adds_a_new_park_that_doesnt_exist() {
		// create new park & change name
		Park newPark = utilityFunctions.newPark();
		// TODO we need to be able to redefine newPark's name so that it can be entered again
		
		boolean parkAdded = dao.addPark(newPark);
		
		if (parkAdded == true) {
			Park newlyAdded = dao.getParkByName(Test_Utilities.TEST_PARK);
					
			Assert.assertEquals( newlyAdded.getLocation(), marsPark.getLocation() );
			Assert.assertEquals( newlyAdded.getArea(), marsPark.getArea() );
			Assert.assertEquals( newlyAdded.getVisitors(), marsPark.getVisitors() );
			Assert.assertEquals( newlyAdded.getDescription(), marsPark.getDescription() );
		} else {
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void addPark_does_not_add_a_new_park_with_the_same_name() {
		
		boolean parkAdded = dao.addPark(marsPark);
		
		if (parkAdded == false) {
			
			SqlRowSet returned = jdbc.queryForRowSet("SELECT count(name) AS name_num FROM park WHERE name=?", marsPark.getName());
			returned.next();
			int numOfParks = returned.getInt("name_num");
			
			Assert.assertEquals(1, numOfParks);
			
		} else {
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void removePark_removes_the_specifed_park() {
		
		// count parks before deletion
		SqlRowSet returned = jdbc.queryForRowSet("SELECT count(name) AS name_num FROM park;");
		returned.next();
		int parksBeforeDeletion = returned.getInt("name_num");
		
		// remove the dummy park
		dao.removePark(marsPark);
		
		// count parks after deletion
		returned = jdbc.queryForRowSet("SELECT count(name) AS name_num FROM park;");
		returned.next();
		int parksAfterDeletion = returned.getInt("name_num");
		
		// the after deletion number should be one less than the before
		Assert.assertEquals(parksBeforeDeletion -1, parksAfterDeletion);
	}
	

	// public List<Reservation> getOneMonthOfReservationsForPark(Park park)
	@Test
	public void month_of_reservations_returns_entered_reservations() {
		
		// TODO Bernard: this test won't pass, not entirely sure why, apologies
		
		boolean checks = false;
		
		// use marsPark, creat campground and campsite
		Campground camp = utilityFunctions.newCampground();
		// camp.setParkId(marsPark.getParkID());
		
		Campsite site = utilityFunctions.giveMeCampsite();
		site.setCampgroundID(camp.getCampgroundID());
		
		dao.updateCampground(camp);
		checks = dao.addCampsite(site);
		
		// create two reservations starting, one ends tomorrow, one starts in 28 days
		Reservation res1 = utilityFunctions.giveMeReservation();
		Reservation res2 = utilityFunctions.giveMeReservation();
		
		res1.setFromDate(LocalDate.now().minusDays(1));
		res1.setToDate(LocalDate.now().plusDays(3));
		res1.setSiteID(site.getSiteID());

		res2.setFromDate(LocalDate.now().plusDays(20));
		res2.setToDate(LocalDate.now().plusDays(30));
		res2.setSiteID(site.getSiteID());
		
		checks = dao.addReservation(res1);
		checks = dao.addReservation(res2);
		
		// call the get reservations function, check for two reservations
		List<Reservation> returned = dao.getOneMonthOfReservationsForPark(marsPark);
		
		Assert.assertEquals(2, returned.size());
	}
}
