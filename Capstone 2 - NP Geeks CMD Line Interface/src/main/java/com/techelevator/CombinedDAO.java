package com.techelevator;

import java.time.LocalDate;
import java.util.List;

import com.techelevator.model.Campground;
import com.techelevator.model.Campsite;
import com.techelevator.model.Park;
import com.techelevator.model.Reservation;

public interface CombinedDAO {
	
	/**
	 * Returns a list of open sites from the specified campground in the reservation date range.
	 * Uses the passed campground object to set the daily_cost for the customers reservation.
	 * If the customer is requesting a camp period outside of the campgrounds open season then a blank list is returned.
	 * 
	 * @param campground_id, customer an integer specifying the campground and a reservation object showing the customers desire
	 * @return List<Campsite> a list of the first five campsites that meet the requirements of the reservation
	 */
	//TODO - Bernard refactor this to take from to dates
	public List<Campsite> getOpenSitesFromCampground(Campground campground, Reservation customer);
	public List<Campsite> sitesAvailableForCampground(Campground camp, LocalDate fromDate, LocalDate toDate);
	
	/**
	 * Returns a list of open sites from the specified campground in the reservation date range.
	 * This search query also takes into account desired characteristics for the campsite.
	 * The conditions are passed via a Campsite object, if the field is a numerical value then 0 will indicate that the customer isn't
	 * concerned with it.
	 * If the field is a boolean value then false indicates that the customer isn't concerned with it.
	 * 
	 * @param campground_id, customer an integer specifying the campground and a reservation object showing the customers desire
	 * @return List<Campsite> a list of the first five campsites that meet the requirements of the reservation
	 */
	public List<Campsite> getOpenSitesFromCampgroundWithConditions(Campground campground, Reservation customer, Campsite site);

	/**
	 * Finds and returns the names of all of the campgrounds in the database.
	 * 
	 * @param n/a
	 * @return List<Campground> List of campground objects for camgrounds in database.
	 */
	public List<Campground> getAllCampgroundsInDatabase();

	/**
	 * park_id The id of the park that is being considered.
	 * 
	 * @param park_id The id of the park that is being considered.
	 * @return a list of campground objects from the specified park.
	 */
	public List<Campground> campgroundsInPark(final long park_id);

	/**
	 * Finds the desired campground and returns it as an object.
	 * The campground must exist within the database.
	 * 
	 * @param name, park_id A name and id that specifies the name of the campground and the park that it's in.
	 * @return a campground object of the specified campground.  Returns "null" if no matching campground was found.
	 */
	//public Campground getCampground(String name, int park_id);

	/**
	 * Returns the information for the sites sepcified in the list
	 * 
	 * @param List<Integer> an integer list specifying the campsites that we need information for
	 * @return List<Campsite> the request campsites, null if the campsite did not exist
	 */
	public List<Campsite> getSiteObjectsFromCampground(List<Long> siteNumbers);

	/**
	 * Returns an array of all the park names in the db.
	 * 
	 * @return an array of all the park names in the db.
	 */
	public String[] allParkNames();

	/**
	 * Finds and returns the specified park from the DB.
	 * 
	 * @param String that specifies the name of the park
	 * @return a park object of the specified park.  Returns "null" if no matching park was found.
	 */
	public Park getParkByName(String name);

	/**
	 * Calculates the cost of the proposed visit using the to date, from date, and daily_cost in the reservation object.
	 * 
	 * @return (Cost of Stay) If any of the three required fields are not filled out then the returned value will be set to -1.
	 */
	public double getCostOfStay(Campground campground, Reservation customer);
	
	/**
	 * Enters the passed campground into the DB.
	 * 
	 * @param newCampground A campground object that does not exist within that park.
	 * @return boolean True if the campground name does not already exist for that park.  False if the name is already used.
	 */
	public boolean addCampground(Campground newCampground);
	
	/**
	 * Removes the specified Campground from the DB.
	 * 
	 * @param campground A campground object that exists within the DB.
	 * @return void
	 */
	public void removeCampground(Campground campground);
	
	/**
	 * Updates the specified campground in the DB and creates a new one if that campground does not exist.
	 * 
	 * @param campground A campground object that exists within the DB.
	 * @return void
	 */
	public void updateCampground(Campground campground);
	
	/**
	 * Adds the site to the specified campground
	 * 
	 * @param Campsite Campsite object to be placed into the DB
	 * @return boolean True is site is added, false if it is not.
	 */
	public boolean addCampsite(Campsite site);

	/**
	 * Removes the site from the specified campground
	 * 
	 * @param Campsite Campsite object to be removed from the DB
	 * @return n/a
	 */
	public void removeCampsite(Campsite site);

	/**
	 * Updates the site in the DB. If the site does not exist then it is created.
	 * 
	 * @param Campsite Campsite object to be updated in the DB
	 * @return n/a
	 */
	public void updateCampsite(Campsite site);
	
	/**
	 * Adds the reservation to the DB
	 * 
	 * @param Reservation Reservation object to be placed into the DB
	 * @return boolean True is site is added, false if it is not.
	 */
	boolean addReservation(Reservation customer);
	Reservation addReservation(Campsite site, String name, LocalDate from, LocalDate to);
	
	/**
	 * Removes the reservation from the specified campground
	 * 
	 * @param Reservation Reservation object to be removed from the DB
	 * @return n/a
	 */
	void removeReservation(Reservation customer);

	/**
	 * Updates the reservation in the DB. If the site does not exist then it is created.
	 * 
	 * @param Reservation Reservation object to be updated in the DB
	 * @return n/a
	 */
	void updateReservation(Reservation customer);
	
	/**
	 * Enters the specified Park into the DB
	 * 
	 * @param newPark A park object that does not exist within the DB.
	 * @return boolean True if the park name does not already exist within the DB.  False if the name is already used.
	 */
	public boolean addPark(Park newPark);
	
	/**
	 * Removes the specified Park from the DB
	 * 
	 * @param Park A park object that exists within the DB.
	 * @return void
	 */
	public void removePark(Park park);
	
	/**
	 * Updates the specified Park in the DB and creates a new one if that park does not exist.
	 * 
	 * @param Park A park object that exists within the DB.
	 * @return void
	 */
	public void updatePark(Park park);
	
	
	/**
	 * Returns the saved / confirmed reservations from a park that have stays that overlap the time period.
	 * The time period is pre-determined to be one month starting from today (i.e. the day the query is asked).
	 * 
	 * @param Park, Reservation A park object that exists within the DB.
	 * @return List<Reservation> A list of campsite objects that are available for rental during the next month.
	 */
	public List<Reservation> getOneMonthOfReservationsForPark(Park park);
}