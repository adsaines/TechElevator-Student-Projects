package com.techelevator.model;

import java.time.LocalDate;

public class Reservation {
	/*
		Includes: reservation_id, site_id, name, from_date, to_date, create_date
		
		Will need getters for: all
		Will need setters for: all
		
		No calculated fields?
	 */
	
	private long      reservation_id;
	private long      site_id;
	private String    name;
	private LocalDate from_date;
	private LocalDate to_date;
	private LocalDate create_date;
	
	// this value comes from the campground table
	private double daily_fee;
	
	public void setDailyFee(double cost) {
		daily_fee = cost;
	}
	
	public double getDailyFee() {
		return daily_fee;
	}
	
	public long getReservationID() {
		return reservation_id;
	}
	
	public void setReservationID(long id) {
		reservation_id = id;
	}
	
	public long getSiteID() {
		return site_id;
	}
	
	public void setSiteID(long id) {
		site_id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public LocalDate getFromDate() {
		return from_date;
	}
	
	public void setFromDate(LocalDate date) {
		from_date = date;
	}
	
	public LocalDate getToDate() {
		return to_date;
	}
	
	public void setToDate(LocalDate date) {
		to_date = date;
	}
	public LocalDate getCreateDate() {
		return create_date;
	}
	
	public void setCreateDate(LocalDate date) {
		create_date = date;
	}
}
