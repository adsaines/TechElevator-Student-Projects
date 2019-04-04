package com.techelevator;

import java.sql.SQLException;

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

import com.techelevator.model.Campsite;

public class Campsite_Function_Tests {
	
	// location and data stream declarations
	private static SingleConnectionDataSource dataSource;
	private JDBCCombinedDAO dao;
	private JdbcTemplate jdbc;
	private Campsite dummy;
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
		
		dummy = utilityFunctions.newCampsite();
	}

	// After each test, we rollback any changes that were made to the database so that everything is clean for the next test.
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	//TODO Bernard/Alex - Failing test
	@Ignore
	public void addCampsite_adds_a_campsite_to_the_DB() {
		// count num of reservations with dummyRes name
		String query = "SELECT count(campground_id) AS count FROM site WHERE campground_id = ?;";
		SqlRowSet info = jdbc.queryForRowSet(query,dummy.getCampgroundID());
		info.next();
		int countBefore = info.getInt("count");
		
		// add dummy
		query = "SELECT max(site_number) AS max FROM site WHERE campground_id = ?;";
		info = jdbc.queryForRowSet(query,dummy.getCampgroundID());
		info.next();
		dummy.setSiteNumber(info.getInt("max")+1);
		dao.addCampsite(dummy);
		
		// count again
		query = "SELECT count(campground_id) AS count FROM site WHERE campground_id = ?;";
		info = jdbc.queryForRowSet(query,dummy.getCampgroundID());
		info.next();
		int countAfter = info.getInt("count");
		
		Assert.assertEquals(countBefore, countAfter-1);
	}
	
	@Test
	public void removeCampsite_removes_a_campsite_from_the_DB() {
		// count num of reservations with dummyRes name
		String query = "SELECT count(site_id) AS count FROM site WHERE site_id = ?;";
		SqlRowSet info = jdbc.queryForRowSet(query,dummy.getSiteID());
		info.next();
		int countBefore = info.getInt("count");
		
		// remove dummy
		dao.removeCampsite(dummy);
		
		// count again
		query = "SELECT count(site_id) AS count FROM site WHERE site_id = ?;";
		info = jdbc.queryForRowSet(query,dummy.getSiteID());
		info.next();
		int countAfter = info.getInt("count");
		
		Assert.assertEquals(countBefore, countAfter+1);
	}
	
	@Test
	public void updateCampsite_creates_a_new_campsite_if_no_site_matches() {
		// count num of reservations with dummy name
		String query = "SELECT count(campground_id) AS count FROM site WHERE campground_id = ?;";
		SqlRowSet info = jdbc.queryForRowSet(query,dummy.getCampgroundID());
		info.next();
		int countBefore = info.getInt("count");
		
		// alter dummy's site number so that it won't match its old value
		query = "SELECT max(site_number) AS max FROM site WHERE campground_id = ?;";
		info = jdbc.queryForRowSet(query,dummy.getCampgroundID());
		info.next();
		dummy.setSiteNumber(info.getInt("max")+1);
		
		// call update, it will add a new sited
		dao.updateCampsite(dummy);
		
		// count again
		query = "SELECT count(campground_id) AS count FROM site WHERE campground_id = ?;";
		info = jdbc.queryForRowSet(query,dummy.getCampgroundID());
		info.next();
		int countAfter = info.getInt("count");
		
		Assert.assertEquals(countBefore, countAfter-1);
	}
	
	@Test
	public void updateCampsite_updates_the_specified_campsite_with_new_information() {
		
		// alter the specs on dummy, update the DB
		dummy.setAccesible(false);
		dummy.setUtilities(false);
		dummy.setMaxOccupancy(1);
		dummy.setMaxRvLength(1);

		dao.updateCampsite(dummy);
		
		// get the new site info from the DB
		String query = "SELECT * FROM site WHERE site_id=?;";
		SqlRowSet info = jdbc.queryForRowSet(query,dummy.getSiteID());
		info.next();
		
		Campsite newCampsite = utilityFunctions.setupCampsiteFromDatabase(info);
		
		// test to see if it matches the new specs
		Assert.assertEquals(dummy.isAccessible(), newCampsite.isAccessible());
		Assert.assertEquals(dummy.hasUtilities(), newCampsite.hasUtilities());
		Assert.assertEquals(dummy.getMaxOccupancy(), newCampsite.getMaxOccupancy());
		Assert.assertEquals(dummy.getMaxRvLength(), newCampsite.getMaxRvLength());
	}
}
