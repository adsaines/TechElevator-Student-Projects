package com.techelevator.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import com.techelevator.CombinedDAO;
import com.techelevator.JDBCCombinedDAO;
import com.techelevator.model.Campground;
import com.techelevator.model.Campsite;
import com.techelevator.model.Park;
import com.techelevator.model.Reservation;
import com.techelevator.view.Report;
import com.techelevator.view.UserIO;

/**
 * Controller to manage requests for camp ground information. 
 */
public class ParkManager {
	private final CombinedDAO parkDAO; /**< Model */
	private final UserIO      userIO;  /**< View */

	// internal settings
	private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yyyy");
	
	// cached data
	private final String[] parkNames;


	public ParkManager(DataSource datasource, UserIO userIO) {
		this.parkDAO = new JDBCCombinedDAO(datasource);
		this.userIO  = userIO;
		
		this.parkNames =  parkDAO.allParkNames();
	}
	
	/**
	 * 
	 * @param park a park object in the DB
	 * @return boolean true when a site is found
	 */
	public boolean dialogSearchByCampgroundWithConditions(Park park) {
		final Campground     camp;
		final List<Campsite> sites;
		final LocalDate      fromDate;
		final LocalDate      toDate;
		final Campsite       site;
		
		Reservation customer = new Reservation();
		Campsite conditions = new Campsite();

		userIO.println(Report.listCampgrounds(park, park.campgrounds()));

		// get camp ground
		{
			final int campCount = park.campgrounds().size();

			final int menuIndex = userIO.getUserInputAsInteger(
					"Which campground (enter 0 to cancel)? ",
					n -> n >= 0 && n <= campCount,
					"Invalid campground number. Enter a number between 0 and " + campCount);

			if( menuIndex == 0 ) {
				userIO.println("");
				return false; 
			}
			camp = park.campgrounds().get(menuIndex-1);
		}

		// get date range
		fromDate = getUserDate("arrival");
		customer.setFromDate(fromDate);
		
		toDate   = getUserDate("departure");
		customer.setToDate(toDate);
		
		// ask for accessible
		conditions.setAccesible(getAccessible());
		
		// ask for rv length
		conditions.setMaxOccupancy(userIO.getUserInputAsInteger("How many people do you want to accommodate? Enter '0' if it doesn't matter. "));
		
		// ask for utility
		conditions.setUtilities(getUtilities());
		
		// ask for occupancy
		conditions.setMaxOccupancy(userIO.getUserInputAsInteger("How long is your RV? Enter '0' if you don't have one. "));
		
		// get camp sites available
		// TODO sites available for with conditions
		sites = parkDAO.getOpenSitesFromCampgroundWithConditions(camp, customer, conditions);
		
		if( sites.isEmpty() ) {
			userIO.println(String.format("\nNo campsites available in %s from %s to %s with those requirements.\n",
					camp, fromDate, toDate));
			return false;
		}
		
		// display camp sites available
		userIO.println(Report.listCampsitesInCampground(sites));

		// get user selected camp site
		{
			List<Integer> siteNumbers = Campsite.listOfSiteNo(sites);
			siteNumbers.add(0); // menu cancel
			
			final int selectedSiteNo = userIO.getUserInputAsInteger("Which site should be reserved (enter 0 to cancel)? ",
					n -> siteNumbers.contains(n),
					siteNumbers.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(", ", "Invalid site number. Select from {", "}")));

			if( selectedSiteNo == 0 ) return false; // user cancelled
			
			site = sites.stream()
					.filter(s -> s.getSiteNumber() == selectedSiteNo)
					.findFirst()
					.orElse(null);
		}

		// get user name for reservation
		final String name = userIO.getUserInputAsString(
				"What name should the reservation be made under? ",
				text -> !text.trim().isEmpty(),
				"Name cannot be blank.").trim();

		// make reservation and insert into db
		Reservation r = parkDAO.addReservation(site, name, fromDate, toDate);
		
		userIO.println("\nThe reservation has been made and the confirmation id is " + r.getReservationID());
		userIO.getUserInputAsString("Press the <Enter> key to continue...\n", s -> true, "");
		return true;
	}
	
	/**
	 * 
	 * Displays reservations that are occurring at the park within the next calendar month.
	 * 
	 * @param park Park object on which to initiate the camp ground search
	 * @return false on failure. true on success
	 */
	public boolean getParkReservations(Park park) {
		//TODO Displays reservations that are occurring at the park within the next calendar month.
		// get reservations for selected park for the next month by calling 
		List<Reservation> reservations = parkDAO.getOneMonthOfReservationsForPark(park);
		
		// pass list of reservations to report for formatting
		// display the formatted reservations
		userIO.println(Report.listReservationsInPark(reservations));
		
		// return to menu and go back to menu selections for park
		return true;
	}
	
	/**
	 * Find up to five reservations per camp ground within the selected park.
	 * 
	 * @param park Park object on which to initiate the camp site search
	 * @return false on failure. true on success
	 */
	public boolean dialogSearchByPark(Park park) {
		final List<Campground> camps;
		final LocalDate        fromDate;
		final LocalDate        toDate;
		
		// get date range & place into customer details
		fromDate = getUserDate("arrival");
		toDate   = getUserDate("departure");
		
		// get camp grounds open during the date range
		camps = park.groundsOpenDuring(fromDate, toDate);
		if( camps.isEmpty() ) {
			userIO.println(String.format("No campgrounds open in %s from %s to %s.\n",
					park, fromDate, toDate));
			userIO.println("");
			return false;
		}
		
		// each camp ground has a list of camp sites from 0 to 5
		@SuppressWarnings("unchecked")
		ArrayList<Campsite>[] sitesForCamp = new ArrayList[camps.size()];
		Set<Integer> siteNumbers = new TreeSet<Integer>();

		// get camp sites for each camp ground
		for( int n = 0; n < camps.size(); n++ ) {
			final List<Campsite> sites = camps.get(n).sitesAvailableFor(fromDate, toDate);
			sitesForCamp[n] = (ArrayList<Campsite>)sites;
			siteNumbers.addAll(Campsite.listOfSiteNo(sites));
		}
		
		userIO.println(Report.listCampsitesInPark(camps, sitesForCamp));

		// get camp ground
		int campIndex;
		{
			final int campCount = camps.size();

			campIndex = userIO.getUserInputAsInteger(
					"Which campground (enter 0 to cancel)? ",
					n -> n >= 0 && n <= campCount,
					"Invalid campground number. Enter a number between 0 and " + campCount);

			if( campIndex == 0 ) {
				userIO.println("");
				return false; 
			}
			--campIndex;
		}

		// get user selected camp site
		final Campsite site;
		{
			siteNumbers.add(0); // menu cancel
			
			final int selectedSiteNo = userIO.getUserInputAsInteger(
					"Which site should be reserved (enter 0 to cancel)? ",
					n -> siteNumbers.contains(n),
					siteNumbers.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(", ", "Invalid site number. Select from {", "}")));

			// user cancelled
			if( selectedSiteNo == 0 ) {
				userIO.println("");
				return false; 
			}
			
			site = sitesForCamp[campIndex].stream()
					.filter(s -> s.getSiteNumber() == selectedSiteNo)
					.findFirst()
					.orElse(null);
			
			// selected siteNo was not found in selected campground
			if( site == null) {
				userIO.println("Campsite " + selectedSiteNo + " is not available in Campground " + camps.get(campIndex));
				userIO.getUserInputAsString("Press the <Enter> key to continue...\n", s -> true, "");
				return false; 
			}
		}

		// get user name for reservation
		final String name = userIO.getUserInputAsString(
				"What name should the reservation be made under? ",
				text -> !text.trim().isEmpty(),
				"Name cannot be blank.").trim();

		// make reservation and insert into db
		Reservation r = parkDAO.addReservation(site, name, fromDate, toDate);
		
		userIO.println("\nThe reservation has been made and the confirmation id is " + r.getReservationID());
		userIO.getUserInputAsString("Press the <Enter> key to continue...\n", s -> true, "");
		
		return true;
	}
	
	/**
	 * @param park Park object on which to initiate the campground search
	 * @return false on failure. true on success
	 */
	public boolean dialogSearchByCampground(Park park) {
		final Campground     camp;
		final List<Campsite> sites;
		final LocalDate      fromDate;
		final LocalDate      toDate;
		final Campsite       site;

		userIO.println(Report.listCampgrounds(park, park.campgrounds()));

		// get camp ground
		{
			final int campCount = park.campgrounds().size();

			final int menuIndex = userIO.getUserInputAsInteger(
					"Which campground (enter 0 to cancel)? ",
					n -> n >= 0 && n <= campCount,
					"Invalid campground number. Enter a number between 0 and " + campCount);

			if( menuIndex == 0 ) {
				userIO.println("");
				return false; 
			}
			camp = park.campgrounds().get(menuIndex-1);
		}

		// get date range
		fromDate = getUserDate("arrival");
		toDate   = getUserDate("departure");
		
		// get camp sites available
		sites = camp.sitesAvailableFor(fromDate, toDate);
		if( sites.isEmpty() ) {
			userIO.println(String.format("No campsites available in %s from %s to %s.\n",
					camp, fromDate, toDate));
			return false;
		}
		
		// display camp sites available
		userIO.println(Report.listCampsitesInCampground(sites));

		// get user selected camp site
		{
			Set<Integer> siteNumbers = new TreeSet<Integer>(Campsite.listOfSiteNo(sites));
			siteNumbers.add(0); // menu cancel
			
			final int selectedSiteNo = userIO.getUserInputAsInteger("Which site should be reserved (enter 0 to cancel)? ",
					n -> siteNumbers.contains(n),
					siteNumbers.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(", ", "Invalid site number. Select from {", "}")));

			// user cancelled
			if( selectedSiteNo == 0 ) {
				userIO.println("");
				return false;
			}
			
			site = sites.stream()
					.filter(s -> s.getSiteNumber() == selectedSiteNo)
					.findFirst()
					.orElse(null);
		}

		// get user name for reservation
		final String name = userIO.getUserInputAsString(
				"What name should the reservation be made under? ",
				text -> !text.trim().isEmpty(),
				"Name cannot be blank.").trim();

		// make reservation and insert into db
		Reservation r = parkDAO.addReservation(site, name, fromDate, toDate);
		
		userIO.println("\nThe reservation has been made and the confirmation id is " + r.getReservationID());
		userIO.getUserInputAsString("Press the <Enter> key to continue...\n", s -> true, "");
		return true;
	}

	private LocalDate getUserDate(final String descriptor) {
		return LocalDate.parse(userIO.getUserInputAsString(
				String.format("What is the %s date (mm/dd/yyyy)? ", descriptor),
				s -> s.matches("([01])?\\d/([0-3])?\\d/\\d{4}"),
				"Invalid date input."), dateFormat);
	}
	
	private boolean getAccessible() {
		// ask for if the customer requires an accessible site
		String answer = userIO.getUserInputAsString("Does the site need to be accessible? (y/n) ", s -> s.matches("[yYnN]"), "Only 'y' or 'n' are accepted inputs. Please try again.");
		answer.toLowerCase();
		return answer.contentEquals("y");
	}
	
	private boolean getUtilities() {
		// as for if the customer requires utilities at the site
		String answer = userIO.getUserInputAsString("Does the site need to have utilities? (y/n) ", s -> s.matches("[yYnN]"), "Only 'y' or 'n' are accepted inputs. Please try again.");
		answer.toLowerCase();
		return answer.contentEquals("y");
	}
	
	public String[] ParkNames() {
		return parkNames;
	}
	
	public Park getParkByName(String parkName) {
		return parkDAO.getParkByName(parkName);
	}
}
