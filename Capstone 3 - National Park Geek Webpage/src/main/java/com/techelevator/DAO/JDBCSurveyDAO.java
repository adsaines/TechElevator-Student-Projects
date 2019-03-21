package com.techelevator.DAO;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.objects.Survey;

@Component
public class JDBCSurveyDAO implements SurveyDAO{
	
	private JdbcTemplate jdbc;

	@Autowired
	public JDBCSurveyDAO(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
	}

	@Override
	public Map<String, Integer> getNumberOfSurveys() {
		
		Map<String, Integer> results = new LinkedHashMap<String, Integer>();
		
		// define query
		String query = "SELECT parkname, count(surveyid) as numsurveys " + 
						"FROM survey_result JOIN park USING (parkcode) " + 
						"GROUP BY parkname " +
						"ORDER BY numSurveys DESC, parkname ASC";
		
		// extract information into linkedhashmap
		SqlRowSet queryValues = jdbc.queryForRowSet(query);
		
		while ( queryValues.next()) {
			results.put(queryValues.getString("parkname"),queryValues.getInt("numsurveys"));
		}
		
		return results;
	}

	@Override
	public void inputSurvey(Survey customer) {
		
		// define insert query
		String query = "INSERT INTO survey_result(parkcode, emailaddress, state, activitylevel)" +
					" VALUES (?,?,?,?);";
		
		// place into DB
		jdbc.update(query, customer.getParkCode(), customer.getEmail(), customer.getState(), customer.getActivityLevel());
	}

}
