package com.techelevator.DAO;

import java.util.Map;

import com.techelevator.objects.Park;
import com.techelevator.objects.Survey;

public interface SurveyDAO {
	
	/**
	 * Returns the number of surveys for all parks
	 * 
	 * @param n/a
	 * @return Map<String, Integer>
	 */
	public Map<String, Integer> getNumberOfSurveys();
	
	/**
	 * Inputs the passed survey object
	 * 
	 * @param Survey
	 * @return n/a
	 */
	public void inputSurvey(Survey customer);
	
}
