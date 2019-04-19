package com.TechElevator.Pojos;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class LeagueScore {

	@NotBlank(message="'userId' cannot, and should not, be blank.")
	private Long userId;
	@NotBlank(message="userName cannot be blank.")
	@Size(min=2,message="userName must contain more than two characters.")
	private String userName;
	@NotBlank(message="bestScore should not be blank. If no score was obtained then enter zero.")
	private Long score;
	
	private Long leagueId;
	
	// only used with match score submittals from the league details page
	@NotBlank(message="newScore cannot be empty. If not score was recorded, enter zero.")
	private Long newScore;
	
	
	public Long getLeagueId() {
		return leagueId;
	}
	public void setLeagueId(Long leagueId) {
		this.leagueId = leagueId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getScore() {
		return score;
	}
	public void setScore(Long score) {
		this.score = score;
	}
	public Long getNewScore() {
		return newScore;
	}
	public void setNewScore(Long newScore) {
		this.newScore = newScore;
	}
	
}
