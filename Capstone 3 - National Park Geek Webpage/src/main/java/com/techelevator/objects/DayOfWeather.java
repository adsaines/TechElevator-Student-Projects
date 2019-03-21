package com.techelevator.objects;

import java.util.ArrayList;
import java.util.List;

public class DayOfWeather {
	
	private Integer forecastDay;
	private Integer tempLow; // in Fahrenheit by default
	private Integer tempHigh;
	private String forecast;
	private String tempScale;
	public static final String FAHREINHEIT = "f";
	public static final String CELSIUS = "c";
	
	public List<String> getWeatherSuggestions(){
		List<String> suggestions = new ArrayList<String>();
		
		if(forecast == "snow") {
			suggestions.add("Be sure to pack snowshoes.");

		}
		if(forecast == "rain") {
			suggestions.add("Be sure to pack rain gear and waterproof shoes.");

		}
		if(forecast == "thunderstorms") {
			suggestions.add("Be sure to find shelter and avoid hiking on any exposed ridges.");

		}
		if(forecast == "sunny") {
			suggestions.add("Be sure to pack sunblock.");

		}
		if(tempHigh > 75) {
			suggestions.add("Be sure to bring an extra gallon of water.");

		}
		if(tempHigh - tempLow >= 20) {
			suggestions.add("Be sure to wear breathable layers.");

		}
		if(tempLow < 20) {
			suggestions.add("cold tempatures can be harmful be sure to dress warmly.");
		}
		
		return suggestions;
	}
	
	public String getTempScale() {
		return tempScale;
	}
	
	public void setTempScale(String newScale) {
		this.tempScale = newScale;
	}
	
	public Integer getForecastDay() {
		return forecastDay;
	}
	public void setForecastDay(Integer forecastDay) {
		this.forecastDay = forecastDay;
	}
	
	public Integer getTempLow() {
		if (tempScale.equals("c")) {
			return convertToCelsius(tempLow);
		} 
		
		return tempLow; // default is fahrenheit
	}
	
	public void setTempLow(Integer tempLow) {
		this.tempLow = tempLow;
	}
	
	public Integer getTempHigh() {
		if (tempScale.equals("c")) {
			return convertToCelsius(tempHigh);
		}
		
		return tempHigh; // default is fahrenheit
	}
	
	public void setTempHigh(Integer tempHigh) {
		this.tempHigh = tempHigh;
	}
	public String getForecast() {
		return forecast;
	}
	public void setForecast(String forecast) {
		this.forecast = forecast;
	}
	
	private Integer convertToCelsius(Integer temp) {
		return (((temp-32) * 5)/9);
	}
}
