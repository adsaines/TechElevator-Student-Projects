package com.TechElevator.Pojos;

public class AbbreviatedUser {
	// used for the invitations page where only the username and userid are needed
	// also used to keep the passwords from being passed around
	
	private String username;
	private Long userId;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
