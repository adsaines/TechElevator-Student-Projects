package com.TechElevator.Pojos;

import org.hibernate.validator.constraints.NotBlank;

public class League {

	@NotBlank(message="League Name cannot be blank.")
	private String name;
	@NotBlank(message="A course must be selected for your league.")
	private Long courseId;
	@NotBlank(message="You must be logged in to create a league.")
	private Long organizerId; 
	private Long leagueId;
	private String courseName;
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public Long getLeagueId() {
		return leagueId;
	}
	public void setLeagueId(Long leagueId) {
		this.leagueId = leagueId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	public Long getOrganizerId() {
		return organizerId;
	}
	public void setOrganizerId(Long organizerId) {
		this.organizerId = organizerId;
	}
}
