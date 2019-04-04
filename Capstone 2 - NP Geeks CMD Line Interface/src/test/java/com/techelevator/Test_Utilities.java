package com.techelevator;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.Campsite;
import com.techelevator.model.Park;
import com.techelevator.model.Reservation;

public class Test_Utilities {
	// Campsite object creation things
	private static final int SITE_ID = 1000000;
	private static final int CAMPGROUND_ID = 1;
	private static final int SITE_NUMBER = 1000000;
	private static final int MAX_OCCUPENCY = 8;
	private static final boolean ACCESIBLE = true;
	private static final int MAX_RV_LENGTH = 53;
	private static final boolean UTILITIES = true;

	// Reservation object creation things
	private static final int RES_SITE_ID = 1;
	private static final String NAME = "DUMMY";
	private static final LocalDate FROM_DATE = LocalDate.of(1900, 1, 1);
	private static final LocalDate TO_DATE = LocalDate.of(1900, 1,5);
	private static final LocalDate CREATE_DATE = LocalDate.of(1899, 12, 1);

	// Park Object Creation things
	public  static final String TEST_PARK = "DUMMY";
	private static final String PARK_DESCRIPTION = "Best Description Ever!";
	private static final int VISITORS = 53;
	private static final int AREA = 100;
	private static final String LOCATION = "Mars";

	// Campground stuff
	private static final int PARK_ID = 1;
	private static final String OPEN_FROM = "01";
	private static final String OPEN_TO = "10";
	private static final double DAILY_FEE = 20.00;

	// data source stuff
	private JDBCCombinedDAO dao;
	private JdbcTemplate jdbc;

	public Test_Utilities(DataSource datasource) {
		jdbc = new JdbcTemplate(datasource);
		dao = new JDBCCombinedDAO(datasource);
	}

	public Campground newCampground() {

		// insert campground into DB
		String insertDummy = "INSERT INTO campground (park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES(?,?,?,?,?);";

		jdbc.update(insertDummy, PARK_ID, NAME, OPEN_FROM, OPEN_TO, new BigDecimal(DAILY_FEE));

		// get campground  id
		SqlRowSet returned = jdbc.queryForRowSet("SELECT campground_id FROM campground WHERE name = ?;", NAME);
		returned.next();

		Campground dummy = new Campground(dao);
		dummy.setCampgroundID(returned.getLong("campground_id"));
		dummy.setDailyFee(DAILY_FEE);
		dummy.setName(NAME);
		dummy.setOpenFromMonth(OPEN_FROM);
		dummy.setOpenToMonth(OPEN_TO);
		dummy.setParkID(PARK_ID);

		return dummy;
	}

	public Campground setupCampgroundFromDatabase(SqlRowSet campgroundInfo) {

		Campground newCamp = new Campground(dao);
		campgroundInfo.next();

		newCamp.setCampgroundID(campgroundInfo.getLong("campground_id"));
		newCamp.setDailyFee(campgroundInfo.getDouble("daily_fee"));
		newCamp.setName(campgroundInfo.getString("name"));
		newCamp.setOpenFromMonth(campgroundInfo.getString("open_from_mm"));
		newCamp.setOpenToMonth(campgroundInfo.getString("open_to_mm"));
		newCamp.setParkID(campgroundInfo.getLong("park_id"));

		return newCamp;
	}

	public Reservation newReservation() {
		String insertDummy = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date) " + 
				"VALUES(?,?,?,?,?)";

		jdbc.update(insertDummy, RES_SITE_ID, NAME, FROM_DATE, TO_DATE, CREATE_DATE);

		// setup park object to match injected park

		Reservation dummyRes = new Reservation();
		dummyRes.setCreateDate(CREATE_DATE);
		dummyRes.setFromDate(FROM_DATE);
		dummyRes.setName(NAME);
		dummyRes.setSiteID(SITE_ID);
		dummyRes.setToDate(TO_DATE);


		SqlRowSet returned = jdbc.queryForRowSet("SELECT reservation_id FROM reservation WHERE name = ?;", NAME);
		returned.next();
		dummyRes.setReservationID(returned.getLong("reservation_id"));

		return dummyRes;
	}

	public Reservation giveMeReservation() {

		Reservation dummyRes = new Reservation();
		dummyRes.setCreateDate(CREATE_DATE);
		dummyRes.setFromDate(FROM_DATE);
		dummyRes.setName(NAME);
		dummyRes.setSiteID(SITE_ID);
		dummyRes.setToDate(TO_DATE);

		return dummyRes;
	}

	public Campsite newCampsite() {
		// setup dummy park for testing
		String insertDummy = "INSERT INTO site (site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) " + 
				"VALUES(?,?,?,?,?,?,?)";

		jdbc.update(insertDummy, SITE_ID, CAMPGROUND_ID, SITE_NUMBER, MAX_OCCUPENCY, ACCESIBLE, MAX_RV_LENGTH, UTILITIES);

		// setup object to match injected dummy
		Campsite dummy = new Campsite();
		dummy.setAccesible(ACCESIBLE);
		dummy.setCampgroundID(CAMPGROUND_ID);
		dummy.setMaxOccupancy(MAX_OCCUPENCY);
		dummy.setMaxRvLength(MAX_RV_LENGTH);
		dummy.setSiteID(SITE_ID);
		dummy.setSiteNumber(SITE_NUMBER);
		dummy.setUtilities(UTILITIES);

		return dummy;
	}

	public Campsite giveMeCampsite() {

		Campsite dummy = new Campsite();
		dummy.setAccesible(ACCESIBLE);
		dummy.setCampgroundID(CAMPGROUND_ID);
		dummy.setMaxOccupancy(MAX_OCCUPENCY);
		dummy.setMaxRvLength(MAX_RV_LENGTH);
		dummy.setSiteID(SITE_ID);
		dummy.setSiteNumber(SITE_NUMBER);
		dummy.setUtilities(UTILITIES);

		return dummy;
	}

	public Campsite setupCampsiteFromDatabase(SqlRowSet siteInfo) {
		Campsite newSite = new Campsite();

		newSite.setAccesible(siteInfo.getBoolean("accessible"));
		newSite.setCampgroundID(siteInfo.getLong("campground_id"));
		newSite.setMaxOccupancy(siteInfo.getInt("max_occupancy"));
		newSite.setMaxRvLength(siteInfo.getInt("max_rv_length"));
		newSite.setSiteID(siteInfo.getLong("site_id"));
		newSite.setSiteNumber(siteInfo.getInt("site_number"));
		newSite.setUtilities(siteInfo.getBoolean("utilities"));

		return newSite;
	}

	public Park newPark() {
		// setup dummy park for testing
		String insertDummy = "INSERT INTO park (name, location, establish_date, area, visitors, description) " + 
				"VALUES(?,?,?,?,?,?)";

		final LocalDate date = LocalDate.now();
		jdbc.update(insertDummy, TEST_PARK, LOCATION, date, AREA, VISITORS, PARK_DESCRIPTION);

		SqlRowSet parkInfo = jdbc.queryForRowSet("SELECT * FROM park WHERE name = ? AND establish_date = ?", 
				TEST_PARK, date);

		// setup park object to match injected park
		if( parkInfo.next() ) {
			return new Park(dao,
					parkInfo.getLong("park_id"),
					parkInfo.getString("name"),
					parkInfo.getString("location"),
					parkInfo.getDate("establish_date").toLocalDate(),
					parkInfo.getInt("area"),
					parkInfo.getInt("visitors"),
					parkInfo.getString("description")
					);
		}
		return null;
	}
}
