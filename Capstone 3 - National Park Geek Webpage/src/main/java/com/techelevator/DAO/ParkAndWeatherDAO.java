package com.techelevator.DAO;

import java.util.List;
import java.util.Map;

import com.techelevator.objects.DayOfWeather;
import com.techelevator.objects.Park;

public interface ParkAndWeatherDAO {
	
	/**
	 * Returns the 5-day forecast for a specified park.
	 * 
	 * @param parkCode
	 * @return list<DayOfWeather> of daily weather objects to complete a five day forecast
	 */
	public List<DayOfWeather> getWeatherForPark(String parkCode);
	
	/**
	 * Returns the specified park information / object
	 * 
	 * @param parkCode
	 * @return Park of daily weather objects to complete a five day forecast
	 */
	public Park getParkInformation(String parkCode);
	
	/**
	 * Returns a list of all Parks in the DB
	 * 
	 * @param n/a
	 * @return List<Park>
	 */
	public List<Park> getAllParks();
	
	/**
	 * Returns a map of all the park names with their park codes in the DB
	 * 
	 * @param n/a
	 * @return Map<String, String>
	 */
	public Map<String, String> getAllParkCodesWithNames();

	
}
