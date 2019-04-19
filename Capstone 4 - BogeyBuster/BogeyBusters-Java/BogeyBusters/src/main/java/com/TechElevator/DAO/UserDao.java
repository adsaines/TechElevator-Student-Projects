package com.TechElevator.DAO;

import java.util.List;

import com.TechElevator.Pojos.AbbreviatedUser;
import com.TechElevator.Pojos.Course;
import com.TechElevator.Pojos.League;
import com.TechElevator.Pojos.User;

public interface UserDao {
	
	/**
	 * Saves new user to db upon register
	 * @param User object contains username, email, pass, role, name 
	 * @return a unique userId per user 
	 */
	public User saveUser(User user);
	
	/**
	 * Deletes old password and updates with new password, as well as salt & hashed
	 * @param user is searched to located previous password & salt
	 * @param newPassword is the string that updated password is set to
	 */
	public void changePassword(User user, String newPassword);
	
	/**
	 * searches for role for user
	 * @param id is used to identify user to find role
	 * @return user's role
	 */
	public String getRole (long id);
	
	/**
	 * gets user password by seraching userId
	 * @param id is the var used to obtain password
	 * @return user's password
	 */
	public String getPassword(long id);
	
	/**
	 * logs in user using username & password
	 * @param username is var used to determine user
	 * @param password is used to validate user is actual user
	 * @return user's info
	 */
	public boolean loginUser(String username, String password);
	
	/**
	 * gets user info based on username
	 * @param username is var used to find user
	 * @return user info
	 */
	public User getUserByUsername(String username);
	
	
	/**
	 * gets user if password is confirmed
	 * @param username is used to determine user
	 * @param password is checked to verify user
	 * @return user info
	 */
	public User getValidUserWithPassword(String username, String password);

	/**
	 * checks to see if username is already taken or not
	 * @param username is checked against db for uniqueness
	 * @return next page or try again
	 */
	boolean checkForUsername(String username);

	/**
	 * Returns a list of users who's names are similar to the search string
	 * @param search String containing the term to search for
	 * @return List<User> all users who have names containing the search term
	 */
	public List<AbbreviatedUser> getUsersByStringSearch(String search);
	
	/**
	 * Will add a score for the user to the selected golf course
	 * @param user
	 * @param course
	 * @param score
	 */
	public void addScoreForUser(User user, Course course, int score);
	
	/**
	 * Determines if the user is the organizer for the league in question
	 * @param user current user
	 * @param league the league in question
	 * @return boolean true if the user is the organizer of the league
	 */
	public boolean isLeagueOrganizer(User user, League league);
	
}
