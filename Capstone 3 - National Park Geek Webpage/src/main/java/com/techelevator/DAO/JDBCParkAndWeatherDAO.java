package com.techelevator.DAO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.objects.DayOfWeather;
import com.techelevator.objects.Park;

@Component
public class JDBCParkAndWeatherDAO implements ParkAndWeatherDAO{
	
	private JdbcTemplate jdbc;
	
	@Autowired
	public JDBCParkAndWeatherDAO(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
	}

	@Override
	public List<DayOfWeather> getWeatherForPark(String parkCode) {
		List<DayOfWeather> parkForecast = new ArrayList<DayOfWeather>();
		
		// make query and extract info from DB
		String query = "SELECT fivedayforecastvalue, low, high, forecast FROM weather WHERE parkcode=?;";
		SqlRowSet results = jdbc.queryForRowSet(query,parkCode);
		
		// iterate through results and create weather objects
		while(results.next()) {
			parkForecast.add(makeDayFromDatabase(results));
		}
		
		return parkForecast;
	}
	
	private DayOfWeather makeDayFromDatabase(SqlRowSet values) {
		DayOfWeather day = new DayOfWeather();
		
		day.setForecastDay(values.getInt("fivedayforecastvalue"));
		day.setForecast(values.getString("forecast").replace(" cloudy", "Cloudy"));
		day.setTempHigh(values.getInt("high"));
		day.setTempLow(values.getInt("low"));
		day.setTempScale("f");
		
		return day;
	}

	@Override
	public Park getParkInformation(String parkCode) {
		Park park = null;
		String query = "SELECT * FROM park WHERE parkCode = ?;";
		SqlRowSet result = jdbc.queryForRowSet(query,parkCode);
		
		while (result.next()) {
			park = makeParkFromRowSet(result);
		}
		
		return park;
	}
	
	private Park makeParkFromRowSet(SqlRowSet values) {
		Park park = new Park();
		
		park.setAcreage(values.getLong("acreage"));
		park.setAnnualVisitorCount(values.getLong("annualvisitorcount"));
		park.setClimate(values.getString("climate"));
		park.setElevation(values.getLong("elevationinfeet"));
		park.setEntryFee(values.getLong("entryfee"));
		park.setInspirationalQuote(values.getString("inspirationalquote"));
		park.setInspirationalQuoteSource(values.getString("inspirationalquotesource"));
		park.setMilesOfTrail(values.getDouble("milesoftrail"));
		park.setNumberOfAnimalSpecies(values.getLong("numberofanimalspecies"));
		park.setNumOfCampsites(values.getLong("numberofcampsites"));
		park.setParkCode(values.getString("parkcode"));
		park.setParkDescription(values.getString("parkdescription"));
		park.setParkName(values.getString("parkname"));
		park.setState(values.getString("state"));
		park.setYearFounded(values.getInt("yearfounded"));
		
		return park;
	}

	@Override
	public List<Park> getAllParks() {
		List<Park> allParks = new ArrayList<Park>();
		
		// query, sort the parks alphabetically
		SqlRowSet results = jdbc.queryForRowSet("SELECT * FROM park ORDER BY parkname ASC");
		
		// populate list
		while (results.next()) {
			allParks.add(makeParkFromRowSet(results));
		}
		
		return allParks;
	}
	
	@Override
	public Map<String, String> getAllParkCodesWithNames(){
		Map<String, String> parkNames = new LinkedHashMap<String, String>();
		
		String query = "SELECT parkcode, parkname FROM park;";
		SqlRowSet returned = jdbc.queryForRowSet(query);
		
		while (returned.next()) {
			parkNames.put(returned.getString("parkcode"), returned.getString("parkname"));
		}
		
		return parkNames;
	}
	
}
