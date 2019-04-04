package com.techelevator.model;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

import com.techelevator.CombinedDAO;

public class Park {
	/*
	 	has fields: park_id, name, location, establish_date, area, visitors, description

		getters: all
		setters: all
	 */
	
	private final long      parkID;
	private final String    name;
	private final String    location;
	private final LocalDate establishDate;
	private final int       area;
	private final int       visitors;
	private final String    description;
	
	// Model references
	private final CombinedDAO      parkDAO;
	private final List<Campground> myCampgrounds;

	public Park(
			CombinedDAO dao, 
			long      park_id,
			String    name,
			String    location,
			LocalDate establish_date,
			int       area,
			int       visitors,
			String    description
			) {
		this.parkID        = park_id;
		this.name          = name;
		this.location      = location;
		this.establishDate = establish_date;
		this.area          = area;
		this.visitors      = visitors;
		this.description   = description;

		this.parkDAO       = dao;
		
		// cached data
		myCampgrounds = parkDAO.campgroundsInPark(park_id);
	}
	
	public String toString() {
		return name;
	}
	
	public List<Campground> campgrounds() {
		return myCampgrounds;
	}
	
	/**
	 * Returns the list of open campgrounds in this park.
	 * 
	 * @param Park, Reservation A park object that exists within the DB and a reservation object that specifies the customers desired date range.
	 * @return List<Campground> A list of campground objects that are open during the requested period.
	 */
	public List<Campground> groundsOpenDuring(LocalDate fromDate, LocalDate toDate) {
		// camp grounds open during the specified date range
		return myCampgrounds.stream()
				.filter(camp -> camp.isOpenDuring(fromDate, toDate))
				.collect(toList());
	}

	public long getParkID() {
		return parkID;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLocation() {
		return location;
	}
	
	public LocalDate getEstablishDate() {
		return establishDate;
	}
	
	public int getArea() {
		return area;
	}
	
	public int getVisitors() {
		return visitors;
	}
	
	public String getDescription() {
		return description;
	}
}
