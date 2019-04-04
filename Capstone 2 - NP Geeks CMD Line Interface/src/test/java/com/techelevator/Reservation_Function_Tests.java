package com.techelevator;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Reservation;

public class Reservation_Function_Tests {
	
	// location and data stream declarations
	private static SingleConnectionDataSource dataSource;
	private JDBCCombinedDAO dao;
	private JdbcTemplate jdbc;
	private Reservation dummyRes;
	private Test_Utilities newObjectCreator = new Test_Utilities(dataSource);

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
		
		dummyRes = newObjectCreator.newReservation();
		
	}

	// After each test, we rollback any changes that were made to the database so that everything is clean for the next test.
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void removeReservation_removes_the_specified_reservation() {
		// count num of reservations with dummyRes name
		String query = "SELECT count(name) AS count FROM reservation WHERE name = ?;";
		SqlRowSet info = jdbc.queryForRowSet(query,dummyRes.getName());
		info.next();
		int countBefore = info.getInt("count");
		
		// remove dummy
		dao.removeReservation(dummyRes);
		
		// count again
		query = "SELECT count(name) AS count FROM reservation WHERE name = ?;";
		info = jdbc.queryForRowSet(query,dummyRes.getName());
		info.next();
		int countAfter = info.getInt("count");
		
		Assert.assertEquals(countBefore-1, countAfter);
	}
	
	@Test
	public void saveReservation_saves_a_new_reservation_to_the_DB() {
		// count num of reservations with dummyRes name
		String query = "SELECT count(name) AS count FROM reservation WHERE name = ?;";
		SqlRowSet info = jdbc.queryForRowSet(query,dummyRes.getName());
		info.next();
		int countBefore = info.getInt("count");
		
		// add a second dummy
		dummyRes.setSiteID(2);
		dao.addReservation(dummyRes);
		
		// count again
		query = "SELECT count(name) AS count FROM reservation WHERE name = ?;";
		info = jdbc.queryForRowSet(query,dummyRes.getName());
		info.next();
		int countAfter = info.getInt("count");
		
		Assert.assertEquals(countBefore+1, countAfter);
	}
	
	@Test
	public void updateReservation_saves_a_reservation_that_isnt_in_the_db() {
		// count num of reservations with dummyRes name
		String query = "SELECT count(name) AS count FROM reservation WHERE name = ?;";
		SqlRowSet info = jdbc.queryForRowSet(query,dummyRes.getName());
		info.next();
		int countBefore = info.getInt("count");
		
		// add another reservation to the db
		dummyRes.setReservationID(0);
		dummyRes.setSiteID(1);
		dao.updateReservation(dummyRes);
		
		// count again
		query = "SELECT count(name) AS count FROM reservation WHERE name = ?;";
		info = jdbc.queryForRowSet(query,dummyRes.getName());
		info.next();
		int countAfter = info.getInt("count");
		
		Assert.assertEquals(countBefore+1, countAfter);
	}
}
