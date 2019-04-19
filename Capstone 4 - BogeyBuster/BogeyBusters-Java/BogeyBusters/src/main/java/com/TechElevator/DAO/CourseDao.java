package com.TechElevator.DAO;

import java.util.List;

import com.TechElevator.Pojos.Course;

public interface CourseDao {

	/**
	 * Adds a golf course to the DB
	 * @param course a Course object that contains all of the course information
	 * @return Course a completed course object with the ID from the DB
	 */
	public void addCourse(Course course);
	
	/**
	 * Finds a list of courses that have been entered into the DB from which the user can peruse and start a leaderboard for
	 * @param searchString will be searched as a partial string against all course names in the DB
	 * @return List<Course> a list of courses from the DB that contained the searchstring
	 */
	public List<Course> findCoursesByName(String searchString);
	
	/**
	 * Search for golf courses by state
	 * @param state a string containing the state name
	 * @return List<Course> a list of courses from 
	 */
	public List<Course> findCoursesByState(String state);
	
	/**
	 * Search for golf courses by city
	 * @param city a string containing the city name
	 * @return List<Course> a list of courses from 
	 */
	public List<Course> findCoursesByCity(String city);
	
	/**
	 * Search for golf courses by zip code
	 * @param zipcode a long representing the zip code
	 * @return List<Course> a list of courses from 
	 */
	public List<Course> findCoursesByZip(long zipcode);
	
	/**
	 * Finds all courses that are currently in the DB
	 * @return List<Course> a list of all the courses...
	 */
	public List<Course> getAllCourses();
	
}
