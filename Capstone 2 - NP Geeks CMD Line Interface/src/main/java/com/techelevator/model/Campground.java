package com.techelevator.model;

import java.time.LocalDate;
import java.util.List;

import com.techelevator.CombinedDAO;

public class Campground {
	/*
	 	has fields: campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee

		getters: all
		setters: all

		daily fee is stored as an integer amount where 100 = $1.00
	 */

	private long   campground_id;
	private long   park_id;
	private String name;
	private int    open_from_mm;
	private int    open_to_mm;
	private double daily_fee;

	// Model references
	private final CombinedDAO parkDAO;

	public Campground(CombinedDAO dao) {
		parkDAO = dao;
	}

	public String toString() {
		return name;
	}
	
	public List<Campsite> sitesAvailableFor(LocalDate fromDate, LocalDate toDate) {
		return parkDAO.sitesAvailableForCampground(this, fromDate, toDate);
	}
	
	public boolean isOpenDuring(LocalDate fromDate, LocalDate toDate) {
		return fromDate.getMonth().getValue() >= open_from_mm && toDate.getMonth().getValue()  <= open_to_mm;
	}

	// getters / setters
	public void setCampgroundID(long id) {
		campground_id = id;
	}

	public long getCampgroundID() {
		return campground_id;
	}

	public void setParkID(long id) {
		park_id = id;
	}

	public long getParkID() {
		return park_id;
	}

	public void setName(String newName) {
		name = newName;
	}

	public String getName() {
		return name;
	}

	public void setOpenFromMonth(String openFrom) {
		open_from_mm = Integer.parseInt(openFrom);
	}

	public int getOpenFromMonth() {
		return open_from_mm;
	}

	public void setOpenToMonth(String openTo) {
		open_to_mm = Integer.parseInt(openTo);
	}

	public int getOpenToMonth() {
		return open_to_mm;
	}

	public void setDailyFee(double daily_fee) {
		this.daily_fee = daily_fee;
	}

	public double getDailyfee() {
		return daily_fee;
	}
}
