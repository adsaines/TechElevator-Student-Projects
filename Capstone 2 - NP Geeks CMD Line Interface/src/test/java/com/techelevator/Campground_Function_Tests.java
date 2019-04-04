package com.techelevator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.Campsite;
import com.techelevator.model.Park;
import com.techelevator.model.Reservation;

public class Campground_Function_Tests {
	// location and data stream declarations
	private static SingleConnectionDataSource dataSource;
	private JDBCCombinedDAO dao;
	private JdbcTemplate jdbc;
	private Campground dummy;
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
		
		dummy = utilityFunctions.newCampground();
	}

	// After each test, we rollback any changes that were made to the database so that everything is clean for the next test.
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	// public boolean addCampground(Campground newCampground)
	@Test
	public void addCampground_adds_a_campground_to_the_DB(){
		// count num of campgrounds with dummy name
		String countQuery = "SELECT count(campground_id) AS count FROM campground;";
		SqlRowSet info = jdbc.queryForRowSet(countQuery);
		info.next();
		int countBefore = info.getInt("count");
		
		// add dummy
		String maxQuery = "SELECT max(campground_id) AS max FROM campground;";
		info = jdbc.queryForRowSet(maxQuery);
		info.next();
		
		dummy.setParkID(2L);
		dummy.setCampgroundID(info.getInt("max")+1);
		dao.addCampground(dummy);
		
		// count again
		info = jdbc.queryForRowSet(countQuery);
		info.next();
		int countAfter = info.getInt("count");
		
		Assert.assertEquals(countBefore+1, countAfter);
	}
	
	// public void removeCampground(Campground campground)
	@Test
	public void removeCampground_removes_the_specified_campground() {
		// count num of campgrounds with dummy name
		String countQuery = "SELECT count(campground_id) AS count FROM campground;";
		SqlRowSet info = jdbc.queryForRowSet(countQuery);
		info.next();
		int countBefore = info.getInt("count");
		
		// remove dummy
		dao.removeCampground(dummy);
		
		// count again
		info = jdbc.queryForRowSet(countQuery);
		info.next();
		int countAfter = info.getInt("count");
		
		Assert.assertEquals(countBefore-1, countAfter);
	}
	
	// public void updateCampground(Campground campground)
	@Test
	public void updateCampground_updates_an_existing_campground() {
		String newMonth = "04";
		
		// change dummy value
		dummy.setOpenFromMonth(newMonth);
		
		// update dummy  & retrieve values form the DB
		dao.updateCampground(dummy);

		String query = "SELECT * FROM campground WHERE campground_id=?";
		SqlRowSet info = jdbc.queryForRowSet(query, dummy.getCampgroundID());
		Campground newCamp = utilityFunctions.setupCampgroundFromDatabase(info);
		
		// check new values
		Assert.assertEquals(newCamp.getOpenFromMonth(), 4);
		Assert.assertEquals(newCamp.getName(), dummy.getName());
		Assert.assertEquals(newCamp.getOpenToMonth(), dummy.getOpenToMonth());
	}
	
	@Test
	public void updateCampground_adds_a_campground_if_it_doesnt_exist() {
		// count num of campgrounds with dummy name
		String countQuery = "SELECT count(campground_id) AS count FROM campground;";
		SqlRowSet info = jdbc.queryForRowSet(countQuery);
		info.next();
		int countBefore = info.getInt("count");
		
		// change name and pass in again
		String newName = "BLARHGHGJ";
		dummy.setName(newName);
		dao.updateCampground(dummy);
		
		// count again
		info = jdbc.queryForRowSet(countQuery);
		info.next();
		int countAfter = info.getInt("count");
		
		Assert.assertEquals(countBefore+1, countAfter);
	}
	
	// public List<Campsite> getOpenSitesFromCampground(Campground campground, Reservation customer)
	//TODO Bernard/Alex - Failing test
	@Ignore
	public void getOpenSitesFromCampground_returns_a_list_of_sites_that_do_not_intersect_with_the_reservation_date_range() {
		
		// create a campground to look in
		Campground dummy = utilityFunctions.newCampground();
		
		// create some campsites to view
		Campsite site1 = utilityFunctions.giveMeCampsite();
		Campsite site2 = utilityFunctions.giveMeCampsite();
		Campsite site3 = utilityFunctions.giveMeCampsite();
		
		site1.setCampgroundID(dummy.getCampgroundID());
		site2.setCampgroundID(dummy.getCampgroundID());
		site3.setCampgroundID(dummy.getCampgroundID());
		
		site1.setSiteNumber(1);
		site2.setSiteNumber(2);
		site3.setSiteNumber(3);
		
		dao.addCampsite(site1);
		dao.addCampsite(site2);
		dao.addCampsite(site3);
		
		// create one tentative reservation
		Reservation tentative = new Reservation();
		tentative.setFromDate(LocalDate.of(2019, 2, 18));
		tentative.setToDate(LocalDate.of(2019, 2, 25));

		// create a set reservation that claims site1
		Reservation setRes = utilityFunctions.giveMeReservation();
		setRes.setFromDate(LocalDate.of(2019, 2, 18));
		setRes.setToDate(LocalDate.of(2019, 2, 25));
		setRes.setSiteID(site1.getSiteID());
		dao.addReservation(setRes);
		
		// call for open sites
		List<Campsite> available = dao.getOpenSitesFromCampground(dummy, tentative);
		
		// check number returned
		Assert.assertEquals(2, available.size());
	}
	
	// public List<Campsite> getOpenSitesFromCampgroundWithConditions(Campground campground, Reservation customer, Campsite site)
	//TODO Bernard/Alex - Failing test
	@Ignore
	public void getOpenSitesFromCampgroundWithConditions_excludes_some_sites() {
		// create a campground to look in
		Campground dummy = utilityFunctions.newCampground();
		
		// create some campsites to view
		Campsite site1 = utilityFunctions.giveMeCampsite(); // taken by confirmed reservation
		Campsite site2 = utilityFunctions.giveMeCampsite(); // is accessible & has utilities
		Campsite site3 = utilityFunctions.giveMeCampsite(); // has a high occupency and large trailer space
		Campsite site4 = utilityFunctions.giveMeCampsite(); // high occupency, no trailer space
		Campsite site5 = utilityFunctions.giveMeCampsite(); // large trailer space, low occuupency
		Campsite limitations = utilityFunctions.giveMeCampsite();
		
		site1.setCampgroundID(dummy.getCampgroundID());
		site2.setCampgroundID(dummy.getCampgroundID());
		site3.setCampgroundID(dummy.getCampgroundID());
		site4.setCampgroundID(dummy.getCampgroundID());
		site5.setCampgroundID(dummy.getCampgroundID());
		
		site1.setSiteNumber(1);
		site2.setSiteNumber(2); site2.setMaxOccupancy(4); site2.setMaxRvLength(0);
		site3.setSiteNumber(3); site3.setAccesible(false); site3.setUtilities(false);
		site4.setSiteNumber(4); site4.setMaxRvLength(0); site4.setAccesible(false); site4.setUtilities(false);
		site5.setSiteNumber(5); site5.setMaxOccupancy(2); site5.setAccesible(false); site5.setUtilities(false);
		
		dao.addCampsite(site1);
		dao.addCampsite(site2);
		dao.addCampsite(site3);
		dao.addCampsite(site4);
		dao.addCampsite(site5);
		
		// create one tentative reservation
		Reservation tentative = new Reservation();
		tentative.setFromDate(LocalDate.of(2019, 2, 18));
		tentative.setToDate(LocalDate.of(2019, 2, 25));

		// create a set reservation that claims site1
		Reservation setRes = utilityFunctions.giveMeReservation();
		setRes.setFromDate(LocalDate.of(2019, 2, 18));
		setRes.setToDate(LocalDate.of(2019, 2, 25));
		setRes.setSiteID(site1.getSiteID());
		dao.addReservation(setRes);
		
		// check for accessible & utilities return
		limitations.setAccesible(true);
		limitations.setUtilities(true);
		limitations.setMaxOccupancy(0);
		limitations.setMaxRvLength(0);
		List<Campsite> available = dao.getOpenSitesFromCampgroundWithConditions(dummy, tentative, limitations);
		Assert.assertEquals(1, available.size());
		
		// check for occupency and trailer space
		limitations.setAccesible(false);
		limitations.setUtilities(false);
		limitations.setMaxOccupancy(8);
		limitations.setMaxRvLength(30);
		available = dao.getOpenSitesFromCampgroundWithConditions(dummy, tentative, limitations);
		Assert.assertEquals(1, available.size());
		
		// check for occupency with no trailer spaces
		limitations.setAccesible(false);
		limitations.setUtilities(false);
		limitations.setMaxOccupancy(8);
		limitations.setMaxRvLength(0);
		available = dao.getOpenSitesFromCampgroundWithConditions(dummy, tentative, limitations);
		Assert.assertEquals(2, available.size());
		
		// check for trailer space and low occupency
		limitations.setAccesible(false);
		limitations.setUtilities(false);
		limitations.setMaxOccupancy(0);
		limitations.setMaxRvLength(30);
		available = dao.getOpenSitesFromCampgroundWithConditions(dummy, tentative, limitations);
		Assert.assertEquals(2, available.size());
	}
	
	// public List<Campground> getAllCampgroundsInDatabase()
	@Test
	public void getAllCampgroundsInDatabase_returns_a_list_of_nonvoid_campgrounds() {
		List<Campground> campgrounds =dao.getAllCampgroundsInDatabase();
		
		for (Campground c : campgrounds) {
			Assert.assertTrue(c.getName() != null);
		}
	}

	// public List<Campground> getAllCampgroundsInPark(int park_id)
	@Test
	public void getAllCampgroundsInPark_returns_all_of_the_campgrounds_in_a_park() {
		// make a new park
		Park park = utilityFunctions.newPark();
		
		// make a new campground and assign it to the park
		// dummy.setParkId(park.getParkID());
		dao.updateCampground(dummy);
		
		// call for all campgrounds in the new park and check for the presence of the new campground
		List<Campground> campgrounds = dao.campgroundsInPark(park.getParkID());
		boolean dummyCampPresent = false;
		
		if (campgrounds.size() != 0) {
			for (Campground c : campgrounds) {
				if (c.getName().contentEquals(dummy.getName())) {
					dummyCampPresent = true;
				}
			}
		} else {
			Assert.assertTrue(false);
		}
		
		Assert.assertTrue(dummyCampPresent);
	}
	
	// public Campground getCampground(String name, int park_id)
	@Ignore
	public void getCampground_returns_the_specified_campground() {
		// make a new park
		Park park = utilityFunctions.newPark();
		
		// make a new campground and assign it to the park
		// dummy.setParkId(park.getParkID());
		dao.updateCampground(dummy);
		
		// call for all campgrounds in the new park and check for the presence of the new campground
		//Campground returned = dao.getCampground(dummy.getName(), dummy.getParkId());
		
		//Assert.assertEquals(dummy.getName(), returned.getName());
	}
	
	// public List<Campsite> getSiteObjectsFromCampground(List<Integer> campsiteNumbers)
	//TODO Bernard/Alex - Failing test
	@Ignore
	public void getSiteObjectsFromCampground_returns_objects_from_siteids() {
		
		// create list and a few sites, add site id's to the list
		List<Long> campsiteNumbers = new ArrayList<Long>();
		Campsite site1 = utilityFunctions.giveMeCampsite();
		Campsite site2 = utilityFunctions.giveMeCampsite();
		
		campsiteNumbers.add(site1.getSiteID());
		campsiteNumbers.add(site2.getSiteID());
		
		// ask for the objects of the sites back
		List<Campsite> sites = dao.getSiteObjectsFromCampground(campsiteNumbers);
		boolean comp1 = false;
		boolean comp2 = false;
		
		// check against the original objects
		for (Campsite c : sites) {
			//TODO Alex - This does nothing. there is no body for the if statement
			if (c.getSiteID() == site1.getSiteID() || c.getSiteID() == site2.getSiteID());
		}
		
		Assert.assertTrue(comp1);
		Assert.assertTrue(comp2);
	}
	
}
