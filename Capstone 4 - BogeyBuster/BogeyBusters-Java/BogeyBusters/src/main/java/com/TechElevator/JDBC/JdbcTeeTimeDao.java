package com.TechElevator.JDBC;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import com.TechElevator.DAO.TeeTimeDao;
import com.TechElevator.Pojos.Match;
import com.TechElevator.Pojos.TeeTime;
import com.TechElevator.Pojos.User;

@Component
public class JdbcTeeTimeDao implements TeeTimeDao {
	
	@Autowired
	private JdbcUserDao userDao;

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public JdbcTeeTimeDao(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);	
	}
	


	@Override
	public void addPlayerToTeeTime(Long userId, Long teeTimeId) {
		String teeTimeInputQuery = "INSERT INTO user_teeTimes (userId, teeTimeId)" + "VALUES (?, ?);";
		
		jdbcTemplate.update(teeTimeInputQuery, userId, teeTimeId);
	}



	@Override
	public void addTeeTime(String teeTime, Long courseId, String username) {
		String inputQuery = "INSERT INTO tee_times (courseid, teetime) " + 
		"VALUES (?,?);";
		
		jdbcTemplate.update(inputQuery, courseId, teeTime);
		
		String teeTimeIdQuery = "SELECT teetimeid FROM tee_times WHERE courseid = ? AND teeTime = ?";
		
		Long teeTimeId = jdbcTemplate.queryForObject(teeTimeIdQuery, Long.class, courseId, teeTime);
		
		String getUserIdQuery = "SELECT userid FROM users WHERE username = ?";
		
		Long userId = jdbcTemplate.queryForObject(getUserIdQuery, Long.class, username);
		
		addPlayerToTeeTime(userId, teeTimeId);
	}
	
	
	private TeeTime getTeeTimeById(Long teeTimeId) {
		String getTeeTImeQuery = "SELECT * FROM tee_times WHERE teeTimeId = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(getTeeTImeQuery, teeTimeId);
		if (results.next()) {
			return createTeeTimeFromDB(results);
		} else {
			return null;
		}
	}



	@Override
	public List<TeeTime> getUserTeeTimes(User user) {
		// create query & list
		String getTeeTimequery = "SELECT userid, courseid, user_teetimes.teetimeid, teetime FROM user_teetimes " + 
								"JOIN tee_times ON user_teetimes.teetimeid = tee_times.teetimeid WHERE userid = ?";
		List<TeeTime> foundTeeTimes = new ArrayList<TeeTime>();
		
		// get rowset
		SqlRowSet teeTimeInfo = jdbcTemplate.queryForRowSet(getTeeTimequery, user.getUserId());
		
		// create list of tee times
		while (teeTimeInfo.next()) {
			foundTeeTimes.add(createTeeTimeFromDB(teeTimeInfo));
		}
		
		return foundTeeTimes;
	}
	
	private TeeTime createTeeTimeFromDB(SqlRowSet teeTimeInfo) {
		TeeTime newTeeTime = new TeeTime();
		
		newTeeTime.setTeeTimeId(teeTimeInfo.getLong("teetimeid"));
		newTeeTime.setCourseId(teeTimeInfo.getLong("courseid"));
		newTeeTime.setTeeTime(teeTimeInfo.getTimestamp("teetime").toLocalDateTime());
		
		return newTeeTime;
	}
	
	
	@Override
	public List<Match> getMatchInfo(User user) {
		List<TeeTime> userTeeTimes = getUserTeeTimes(user);
		List<Match> userMatches = new ArrayList<Match>();
		
		for(TeeTime teeTime : userTeeTimes) {
			String getPlayersQuery = "SELECT * FROM users JOIN user_teetimes ON user_teetimes.userid = users.userid WHERE teetimeid = ?;";
			String getCourseNameQuery = "SELECT coursename FROM courses WHERE courseid = ?"; 		 
			
			Match match = new Match();
			
			match.setTeeTime(getTeeTimeById(teeTime.getTeeTimeId()));
					
			match.setCourseName(jdbcTemplate.queryForObject(getCourseNameQuery, String.class, match.getTeeTime().getCourseId()));
			
			SqlRowSet playerInfo = jdbcTemplate.queryForRowSet(getPlayersQuery, teeTime.getTeeTimeId());
			
			while (playerInfo.next()) {
				match.addPlayer(userDao.mapResultToUser(playerInfo));
			}
			userMatches.add(match);
		}
		
		return userMatches;
		
	}
	
}
