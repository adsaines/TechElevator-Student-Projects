package com.TechElevator.Pojos;

import java.util.ArrayList;
import java.util.List;

public class Match {
	
	private TeeTime teeTime;
	
	private String courseName;
	
	private List<User> players = new ArrayList<User>();


	
	public TeeTime getTeeTime() {
		return teeTime;
	}

	public void setTeeTime(TeeTime teeTime) {
		this.teeTime = teeTime;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public List<User> getPlayers() {
		return players;
	}

	public void setPlayers(List<User> players) {
		this.players = players;
	}
	
	public void addPlayer(User player) {
		this.players.add(player);
	}

}
