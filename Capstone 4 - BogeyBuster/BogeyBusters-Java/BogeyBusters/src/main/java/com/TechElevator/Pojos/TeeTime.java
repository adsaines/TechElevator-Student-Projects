package com.TechElevator.Pojos;

import java.time.LocalDateTime;
import org.hibernate.validator.constraints.NotBlank;

public class TeeTime {
	
	@NotBlank(message="You must be logged in to set a tee time.")
	private Long teeTimeId; 
	
	@NotBlank(message="Please identify a course.")
	private Long courseId;
	
	@NotBlank(message="Please select a tee time.")
	private LocalDateTime teeTime;	
	
	private String courseName;
	
	
	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Long getTeeTimeId() {
		return teeTimeId;
	}

	public void setTeeTimeId(Long teeTimeId) {
		this.teeTimeId = teeTimeId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public LocalDateTime getTeeTime() {
		return teeTime;
	}

	public void setTeeTime(LocalDateTime teeTime) {
		this.teeTime = teeTime;
	}

}
	