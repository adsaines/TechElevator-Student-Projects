package com.TechElevator.DAO;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.TechElevator.Pojos.Match;
import com.TechElevator.Pojos.TeeTime;
import com.TechElevator.Pojos.User;

public interface TeeTimeDao {
		
	// invite someone to a league
	public void addPlayerToTeeTime(Long userId, Long teeTimeId);

	// add a league
	public void addTeeTime(String teeTime, Long courseId, String username);
	
	// return list of your tee times
	public List<TeeTime> getUserTeeTimes(User user);
	
	//return list of tee times and username for matches
	public List<Match> getMatchInfo(User user);
}
