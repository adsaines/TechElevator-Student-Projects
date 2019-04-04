package com.techelevator.model;

import java.util.List;
import static java.util.stream.Collectors.toList;

public class Campsite {
	/*
	 	has fields: site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities

		getters: all
		setters: all
	 */
	
	private long    site_id;
	private long    campground_id;
	private int     site_number;
	private int     max_occupancy;
	private boolean accessible;
	private int     max_rv_length;
	private boolean utilities;
	private double  fee;
	
	public static List<Integer> listOfSiteNo(List<Campsite> sites) {
		return sites.stream().mapToInt(s -> s.getSiteNumber()).boxed().collect(toList());
	}
	
	// getters / setters
	public long getSiteID() {
		return site_id;
	}
	
	public void setSiteID(long id) {
		site_id = id;
	}
	
	public long getCampgroundID() {
		return campground_id;
	}
	
	public void setCampgroundID(long id) {
		campground_id = id;
	}
	
	public int getSiteNumber() {
		return site_number;
	}
	
	public void setSiteNumber(int num) {
		site_number = num;
	}
	
	public int getMaxOccupancy() {
		return max_occupancy;
	}
	
	public void setMaxOccupancy(int max_occupancy)  {
		this.max_occupancy = max_occupancy;
	}
	
	public boolean isAccessible() {
		return accessible;
	}
	
	public void setAccesible(boolean val) {
		accessible = val;
	}
	
	public int getMaxRvLength() {
		return max_rv_length;
	}
	
	public void setMaxRvLength(int length) {
		max_rv_length = length;
	}
	

	public boolean hasUtilities() {
		return utilities;
	}
	
	public void setUtilities(boolean val) {
		utilities = val;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}
}
