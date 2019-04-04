package com.techelevator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.Campsite;
import com.techelevator.model.Park;
import com.techelevator.model.Reservation;

public class JDBCCombinedDAO implements CombinedDAO {

	private static final int SITE_LIST_LENGTH = 5;
	private JdbcTemplate jdbc;

	public JDBCCombinedDAO(DataSource datasource) {
		jdbc = new JdbcTemplate(datasource);
	}

	public List<Campsite> sitesAvailableForCampground(Campground camp, LocalDate fromDate, LocalDate toDate) {
		Reservation r = new Reservation();
		r.setFromDate(fromDate);
		r.setToDate(toDate);
		return getOpenSitesFromCampground(camp, r);
	}

	//TODO Bernard - refactor this to take dates not partial reservation object
	//TODO move this to park manager
	@Override
	public List<Campsite> getOpenSitesFromCampground(Campground campground, Reservation customer) {
		// create campsite list
		List<Campsite> availableSites = new ArrayList<Campsite>();

		// check reservation period
		boolean acceptablePeriod = customerDatesMatchCampgroundOpenPeriod(campground, customer);

		// if the date range is acceptable then query for available sites and fill the list
		// exclude all sites that have reservations that cross the customers desired date range

		if( acceptablePeriod ) {
			
			String query = "SELECT * FROM site WHERE campground_id = ? AND site_id NOT IN (" + 
			"SELECT site_id FROM site " + 
			"JOIN reservation USING (site_id) " +
			"WHERE (from_date BETWEEN ? AND ? OR to_date BETWEEN ? AND ?) " +
			"AND campground_id = ? " +
			") LIMIT " + SITE_LIST_LENGTH + ";";
			
			SqlRowSet siteRow = jdbc.queryForRowSet(query,
					campground.getCampgroundID(),
					customer.getFromDate(),
					customer.getToDate(),
					customer.getFromDate(),
					customer.getToDate(),
					campground.getCampgroundID()
					
					);
			while( siteRow.next() ) {
				availableSites.add(setupCampsiteFromDatabase(siteRow));
			}
			double fee = campground.getDailyfee() * (ChronoUnit.DAYS.between(customer.getFromDate(), customer.getToDate()) + 1);
			for( Campsite site: availableSites ) {
				site.setFee( fee );
			}
		}
		
		return availableSites;
	}

	// TODO move this to parkmanager
	@Override
	public List<Campsite> getOpenSitesFromCampgroundWithConditions(Campground campground, Reservation customer, Campsite site) {
		// create lists & base query
		List<Campsite> availableSites = new ArrayList<Campsite>();
		List<Long> campsiteNumbers = new ArrayList<Long>();

		String query = "SELECT site_number FROM site WHERE campground_id = ? AND max_occupancy >= ? AND max_rv_length >= ? ";
		
		// check reservation period
		boolean acceptablePeriod = customerDatesMatchCampgroundOpenPeriod(campground, customer);

		// if the date range is acceptable then query for available sites and fill the list
		// exclude all sites that have reservations that cross the customers desired date range
		if (acceptablePeriod == true) {

			// add special WHERE conditions to SQL query for advanced search
			if (site.isAccessible() == true) {
				query += "AND accessible IS TRUE ";
			}

			if (site.hasUtilities() == true ) {
				query += "AND utilities IS TRUE ";
			}
			
			
			// add final condition to query
			query += "AND site_id NOT IN (" + 
					"SELECT site_id FROM site " + 
					"JOIN reservation USING (site_id) " + 
					"WHERE (from_date BETWEEN ? AND ? OR to_date BETWEEN ? AND ?) " + 
					"AND campground_id = ? " + 
					") LIMIT " + SITE_LIST_LENGTH + ";";
			
			// get camp site numbers
			campsiteNumbers = jdbc.queryForList(query, Long.class, 
					campground.getCampgroundID(),
					site.getMaxOccupancy(),
					site.getMaxRvLength(),
					customer.getFromDate(), 
					customer.getToDate(), 
					customer.getFromDate(), 
					customer.getToDate(),
					campground.getCampgroundID()
					);
			
			// get camp site objects
			if (campsiteNumbers.size() !=0) {
				availableSites = getSiteObjectsFromCampground(campsiteNumbers);
			}
			
			for (Campsite s : availableSites) {
				s.setFee(campground.getDailyfee() * ChronoUnit.DAYS.between(customer.getFromDate(), customer.getToDate()));
			}

		}
		
		return availableSites;
	}

	@Override
	public List<Campground> getAllCampgroundsInDatabase() {
		List<Campground> results = new ArrayList<Campground>();

		String query = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee FROM campground;";
		SqlRowSet allCampInfo = jdbc.queryForRowSet(query);

		while (allCampInfo.next()) {
			results.add(setupCampgroundFromDatabase(allCampInfo));
		}

		return results;
	}

	@Override
	public List<Campground> campgroundsInPark(final long park_id) {
		List<Campground> results = new ArrayList<Campground>();

		String query = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee FROM campground WHERE park_id = ?;";
		SqlRowSet allCampInfo = jdbc.queryForRowSet(query, park_id);

		while (allCampInfo.next()) {
			results.add(setupCampgroundFromDatabase(allCampInfo));
		}

		return results;
	}
	
	public String[] campgroundNamesInPark(final long park_id) {

		String query = "SELECT name FROM campground WHERE park_id = ?";

		List <String> campgroundNames = jdbc.queryForList(query, String.class, park_id);

		String[] results = new String[campgroundNames.size()];

		for (int i = 0; i<results.length; i+=1) {
			results[i] = campgroundNames.get(i);
		}

		return results;
	}

	@Override
	public List<Campsite> getSiteObjectsFromCampground(List<Long> campsiteNumbers) {
		List<Campsite> desiredInfo = new ArrayList<Campsite>();
		String campsiteQueryByNum = "SELECT site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " + 
									"FROM site WHERE site_id = ?";
		
		for(Long id : campsiteNumbers) {
			SqlRowSet siteInfo = jdbc.queryForRowSet(campsiteQueryByNum, id);

			//if the campsite does not exist then return null
			if (siteInfo.next()) {
				desiredInfo.add(setupCampsiteFromDatabase(siteInfo));
			} else {
				desiredInfo.add(null);
			}
		}

		return desiredInfo;
	}
	
	@Override
	public String[] allParkNames() {
		String query = "SELECT name FROM park;";
		List<String> result = jdbc.queryForList(query, String.class);
		
		String[] resultArr = new String[result.size()];
		resultArr = result.toArray(resultArr);

		return resultArr;
	}


	@Override
	public List<Reservation> getOneMonthOfReservationsForPark(Park park){
		// TODO Bernard: the test for this function isn't passing
		// I am able to extract specific date ranges using this query in visual studio code, but it is returning no objects for the test
		
		// list of reservations
		List<Reservation> reserved = new ArrayList<Reservation>();
		
		// define a date range, starting now, ending 1 month from now
		LocalDate rangeStart = LocalDate.now();
		LocalDate rangeEnd = LocalDate.now().plusMonths(1);
		
		// call all reservations starting or ending in that range for the passed park
		String query = "SELECT reservation_id, site_id, reservation.name, from_date, to_date, create_date, daily_fee FROM reservation " + 
						"JOIN site USING (site_id) " + 
						"JOIN campground USING (campground_id) " +
						"WHERE park_id = ? AND (from_date BETWEEN ? AND ? OR to_date BETWEEN ? AND ?); ";
		
		SqlRowSet returned = jdbc.queryForRowSet(query, park.getParkID(), rangeStart, rangeEnd, rangeStart, rangeEnd);
		
		while (returned.next()) {
			reserved.add(setupReservationFromDatabase(returned));
		}
		
		return reserved;
	}
	
	@Override
	public Park getParkByName(String name) {
		Park desiredPark = null;

		String query = "SELECT park_id, name, location, establish_date, area, visitors, description FROM park WHERE name = ?;";
		SqlRowSet rowset = jdbc.queryForRowSet(query, name);

		if ( rowset.next() ) {
			//if rowset contains information then use it to setup a park object, otherwise lave desiredPark null
			desiredPark = setupParkFromDataBase(rowset);
		}

		return desiredPark;
	}

	@Override
	public double getCostOfStay(Campground campground, Reservation customer) {
		double cost_of_stay;
		double daily_fee = campground.getDailyfee();

		if (daily_fee == 0 || customer.getFromDate() == null || customer.getToDate() == null) {
			// if from_date, to_date, or daily-cost are empty return -1
			cost_of_stay = -1;
		} else {
			// if all values are initialized find length of stay, 
			cost_of_stay = ( ChronoUnit.DAYS.between(customer.getFromDate(), customer.getToDate()) ) * daily_fee;
		}

		return cost_of_stay;
	}

	@Override
	public boolean addCampground(Campground newCampground) {
		boolean campCreated = true;

		// check to make sure that there are not other parks with this name
		// if there is flip campCreated to false
		String query = "SELECT name FROM campground WHERE park_id = ?";
		List <String> campgroundNames = jdbc.queryForList(query, String.class, newCampground.getParkID());

		for (String name : campgroundNames) {
			if (newCampground.getName().contentEquals(name)) {
				campCreated = false;
				break;
			}
		}

		//if no other campground with the same park_id exists then
		if (campCreated == true) {

			//find the next id value in the id sequence
			String queryForKey = "SELECT nextval('campground_campground_id_seq') as new_id;";
			SqlRowSet nextKey = jdbc.queryForRowSet(queryForKey);
			nextKey.next();
			newCampground.setCampgroundID((nextKey.getInt(1)));

			//add to db
			String createCamp = "INSERT INTO campground (campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES (?,?,?,?,?,?);";
			jdbc.update(createCamp,
					newCampground.getCampgroundID(),
					newCampground.getParkID(),
					newCampground.getName(),
					monthToString(newCampground.getOpenFromMonth()),
					monthToString(newCampground.getOpenToMonth()),
					new BigDecimal(newCampground.getDailyfee())
					);
		}

		return campCreated;
	}

	@Override
	public void removeCampground(Campground campground) {
		/*
		 * To remove a campground from this DB all references from it subsequent tables must be removed first.
		 * We remove all reservations, then sites, and then campgrounds. The parks table is not altered.
		 */

		// 1 - remove reservations from sites within the campground
		String query = "DELETE FROM reservation WHERE reservation_id IN ( " + 
				"SELECT reservation_id FROM reservation " + 
				"JOIN site USING (site_id) " + 
				"JOIN campground USING (campground_id) " +
				"WHERE campground_id = ? " +
				");";

		jdbc.update(query,campground.getCampgroundID());

		// 2 - remove sites from the campgrounds
		query = "DELETE FROM site WHERE site_id IN ( " + 
				"SELECT site_id FROM site " + 
				"JOIN campground USING (campground_id) " +
				"WHERE campground_id = ? " +
				");";

		jdbc.update(query,campground.getCampgroundID());

		// 3 - remove the campground itself
		query =  "DELETE FROM campground WHERE campground_id = ?";
		jdbc.update(query,campground.getCampgroundID());

	}

	@Override
	public void updateCampground(Campground campground) {
		// check to make sure that this park exists
		String query = "SELECT count(campground_id) AS count FROM campground WHERE name=? AND park_id=? ;";
		SqlRowSet returned = jdbc.queryForRowSet(query, campground.getName(), campground.getParkID());
		returned.next();
		int numCamp = returned.getInt("count");
		
		//if it does exist go ahead and update it
		if (numCamp == 1) {
			query = "UPDATE campground SET name=?, open_from_mm=?, open_to_mm=?, daily_fee=? WHERE campground_id = ?;";
			jdbc.update(query,
					campground.getName(),
					monthToString(campground.getOpenFromMonth()),
					monthToString(campground.getOpenToMonth()),
					new BigDecimal(campground.getDailyfee()),
					campground.getCampgroundID()
					);
		} else if (numCamp == 0){
			//if it doesn't exist then add it to the DB
			addCampground(campground);
		} else if (numCamp > 1) {
			System.out.println("Something weird happened and there is more than one campground with the same ID.");
		}

	}

	@Override
	public boolean addCampsite(Campsite site) {
		boolean siteCreated = true;

		// check to make sure that the campsite doesn't already exist
		String query = "SELECT count(site_id) FROM site WHERE campground_id=? AND site_number=?;";
		SqlRowSet returned = jdbc.queryForRowSet(query, site.getCampgroundID(), site.getSiteNumber());
		returned.next();
		int numOfSites = returned.getInt("count");
		
		if (numOfSites != 0) {
			// if it does, don't create it and set siteCreated to false
			siteCreated = false;
		} else {
			query = "SELECT nextval('site_site_id_seq') AS new_id FROM site ";
			returned = jdbc.queryForRowSet(query);
			returned.next();
			site.setSiteID(returned.getLong("new_id"));

			// if it doesn't, enter it into the DB
			query = "INSERT INTO site (site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) VALUES (?,?,?,?,?,?,?);";
			
			jdbc.update(query, 
					site.getSiteID(), 
					site.getCampgroundID(), 
					site.getSiteNumber(),
					site.getMaxOccupancy(),
					site.isAccessible(),
					site.getMaxRvLength(),
					site.hasUtilities()
					);
		}

		return siteCreated;
	}

	@Override
	public void removeCampsite(Campsite site) {
		// All reservations for the site must be removed before the site itself
		String query = "DELETE FROM reservation WHERE site_id IN ( SELECT site_id FROM site WHERE campground_id=? AND site_number=? );";
		jdbc.update(query,site.getCampgroundID(), site.getSiteNumber());

		// Remove the site
		query = "DELETE FROM site WHERE campground_id=? AND site_number=?;";
		jdbc.update(query, site.getCampgroundID(), site.getSiteNumber());
		
	}

	@Override
	public void updateCampsite(Campsite site) {
		// check for the compsite's existance
		String query = "SELECT count(site_id) AS count FROM site WHERE campground_id=? AND site_number=?";
		SqlRowSet returned = jdbc.queryForRowSet(query,site.getCampgroundID(), site.getSiteNumber());
		returned.next();
		int numOfSites = returned.getInt("count");

		if (numOfSites == 1) {
			// if it exists then update the fields
			query = "UPDATE site SET max_occupancy=?, accessible=?, max_rv_length=?, utilities=? WHERE campground_id=? AND site_number=?;";
			jdbc.update(query,site.getMaxOccupancy(), 
					site.isAccessible(), 
					site.getMaxRvLength(), 
					site.hasUtilities(), 
					site.getCampgroundID(), 
					site.getSiteNumber());

		} else if (numOfSites ==0) {
			// if it doesn't exist then throw it over to add campsite
			addCampsite(site);
		} else {
			System.out.println("Why must things be weird on occasion? There are more than one of these sites in the DB!");
		}

	}

	@Override
	public void removeReservation(Reservation customer) {
		String query = "DELETE FROM reservation WHERE reservation_id=?;";
		jdbc.update(query,customer.getReservationID());
	}

	@Override
	public void updateReservation(Reservation customer) {
		String query = "SELECT count(reservation_id) AS count FROM reservation WHERE reservation_id=?;";
		SqlRowSet returned = jdbc.queryForRowSet(query,customer.getReservationID());
		returned.next();
		int numOfSites = returned.getInt("count");

		if (numOfSites == 1) {
			// if it exists then update the fields
			query = "UPDATE reservation SET from_date=?, to_date=?, create_date=? WHERE reservation_id=?;";
			jdbc.update(query,
					customer.getFromDate(),
					customer.getToDate(),
					customer.getCreateDate(),
					customer.getReservationID()
					);

		} else if (numOfSites ==0) {
			// if it doesn't exist then throw it over to add reservation
			addReservation(customer);
		} else {
			System.out.println("Oh the drama! Two id's match this query! The world is falling apart!");
		}
	}

	// TODO Bernard - refactor all key IDs are long
	public Reservation addReservation(Campsite site, String name, LocalDate from, LocalDate to) {
		Reservation result = new Reservation();
		result.setSiteID(site.getSiteID());
		result.setName(name);
		result.setFromDate(from);
		result.setToDate(to);
		if( !addReservation(result) ) {
			result = null;
		}
		return result;
	}
	
	@Override
	public boolean addReservation(Reservation customer) {
		String query = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date) " + 
				"VALUES (?,?,?,?,?);";

		customer.setCreateDate(LocalDate.now());

		int reservationAdded = jdbc.update(query,
				customer.getSiteID(),
				customer.getName(),
				customer.getFromDate(),
				customer.getToDate(),
				customer.getCreateDate()
				);

		if (reservationAdded != 0) {
			query = "SELECT reservation_id FROM reservation WHERE name=? AND create_date=?;";
			SqlRowSet id = jdbc.queryForRowSet(query, customer.getName(), customer.getCreateDate());
			id.next();
			customer.setReservationID(id.getLong("reservation_id"));
		}

		return reservationAdded != 0;
	}

	@Override
	public boolean addPark(Park newPark) {
		boolean parkCreated = true;

		// check to make sure that there are not other parks with this name
		// if there is flip parkCreated to false
		String query = "SELECT name FROM park;";
		List<String> parkNames = jdbc.queryForList(query, String.class);

		for (String name : parkNames) {
			String newParkName = newPark.getName();
			if (newParkName.contentEquals(name)) {
				parkCreated = false;
				break;
			}
		}

		//if no other parks share the name of this one then add newPark to the DB
		if (parkCreated == true) {

			//find the next id value in the id sequence
			query = "SELECT nextval('park_park_id_seq') as new_id;";
			SqlRowSet nextKey = jdbc.queryForRowSet(query);

			if (nextKey.next()) {
				//add to db
				query = "INSERT INTO park (park_id, name, location, establish_date, area, visitors, description) " + 
						"VALUES (?, ?, ?, ?, ?, ?, ?);";
				jdbc.update(query,
						nextKey.getLong("new_id"),
						newPark.getName(),
						newPark.getLocation(),
						newPark.getEstablishDate(),
						newPark.getArea(),
						newPark.getVisitors(),
						newPark.getDescription()
						);
			} else {
				parkCreated = false;
			}
		}

		return parkCreated;
	}

	@Override
	public void removePark(Park park) {
		/*
		 * To remove a park from this DB all references from it subsequent tables must be removed first.
		 * We remove all reservations, then sites, then campgrounds, then the park itself.
		 */

		// 1 - remove reservations from sites within the park
		String query = "DELETE FROM reservation WHERE reservation_id IN ( " + 
				"SELECT reservation_id FROM reservation " + 
				"JOIN site USING (site_id) " + 
				"JOIN campground USING (campground_id) " +
				"JOIN park USING (park_id) " +
				"WHERE park_id = ? " +
				");";

		jdbc.update(query,park.getParkID());

		// 2 - remove sites from the campgrounds within the park
		query = "DELETE FROM site WHERE site_id IN ( " + 
				"SELECT site_id FROM site " + 
				"JOIN campground USING (campground_id) " +
				"JOIN park USING (park_id) " +
				"WHERE park_id = ? " +
				");";

		jdbc.update(query,park.getParkID());

		// 3 - remove campgrounds within the park
		query = "DELETE FROM campground WHERE campground_id IN ( " + 
				"SELECT campground_id FROM campground " + 
				"JOIN park USING (park_id) " +
				"WHERE park_id = ? " +
				");";

		jdbc.update(query,park.getParkID());

		// 4 - remove the park itself
		query =  "DELETE FROM park WHERE park_id = ?";
		jdbc.update(query,park.getParkID());
	}

	@Override
	public void updatePark(Park park) {
		// check to make sure that this park exists
		String query = "SELECT count(park_id) AS park_count FROM park WHERE park_id = ?;";
		SqlRowSet numParksWithName= jdbc.queryForRowSet(query, park.getParkID());
		numParksWithName.next();
		int numParks = numParksWithName.getInt("park_count");

		//if it does exist go ahead and update it
		if (numParks == 1) {
			query = "UPDATE park SET name=?, location=?, establish_date=?, area=?, visitors=?, description=? WHERE park_id = ?;";
			jdbc.update(query,
					park.getName(),
					park.getLocation(),
					park.getEstablishDate(),
					park.getArea(),
					park.getVisitors(),
					park.getDescription(),
					park.getParkID()
					);
		} else if (numParks == 0){
			//if it doesn't exist then send it to addPark()
			addPark(park);
		} else if (numParks > 1) {
			System.out.println("Something weird happened and there is more than one park with the same ID.");
		}
	}

	private Campsite setupCampsiteFromDatabase(SqlRowSet siteInfo) {
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

	private Campground setupCampgroundFromDatabase(SqlRowSet campgroundInfo) {

		Campground newCamp = new Campground(this);
		
		newCamp.setCampgroundID(campgroundInfo.getLong("campground_id"));
		newCamp.setDailyFee(campgroundInfo.getDouble("daily_fee"));
		newCamp.setName(campgroundInfo.getString("name"));
		newCamp.setOpenFromMonth(campgroundInfo.getString("open_from_mm"));
		newCamp.setOpenToMonth(campgroundInfo.getString("open_to_mm"));
		newCamp.setParkID(campgroundInfo.getLong("park_id"));

		return newCamp;
	}

	// TODO move to Park Manager
	private boolean customerDatesMatchCampgroundOpenPeriod(Campground campground, Reservation customer) {

		// check the months of the customers reservation against the months that the campground is open
		// if the start date is before the open period or the end date is after the close period don't do anything

		int visitFromMonth = customer.getFromDate().getMonth().getValue();
		int visitToMonth = customer.getToDate().getMonth().getValue();

		return visitFromMonth >= campground.getOpenFromMonth() && visitToMonth <= campground.getOpenToMonth();
	}

	private Park setupParkFromDataBase(SqlRowSet parkInfo) {
		return new Park(this,
				parkInfo.getLong("park_id"),
				parkInfo.getString("name"),
				parkInfo.getString("location"),
				parkInfo.getDate("establish_date").toLocalDate(),
				parkInfo.getInt("area"),
				parkInfo.getInt("visitors"),
				parkInfo.getString("description")
				);
	}

	private String monthToString(int monthVal) {
		String month = Integer.toString(monthVal);

		if (month.length() == 1) {
			month = "0" + month;
		}

		return month;
	}
	
	private Reservation setupReservationFromDatabase(SqlRowSet info) {
		Reservation customer = new Reservation();
		
		customer.setCreateDate(LocalDate.now());
		customer.setDailyFee(info.getDouble("daily_fee"));
		customer.setFromDate(info.getDate("from_date").toLocalDate());
		customer.setToDate(info.getDate("to_date").toLocalDate());
		customer.setName(info.getString("name"));
		customer.setReservationID(info.getLong("reservation_id"));
		customer.setSiteID(info.getLong("site_id"));
		
		return customer;
	}

}
