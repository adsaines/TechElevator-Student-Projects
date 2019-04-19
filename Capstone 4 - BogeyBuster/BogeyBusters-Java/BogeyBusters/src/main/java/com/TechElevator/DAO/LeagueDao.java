package com.TechElevator.DAO;

import java.util.List;

import com.TechElevator.Pojos.Invitation;
import com.TechElevator.Pojos.League;
import com.TechElevator.Pojos.LeagueScore;
import com.TechElevator.Pojos.User;

public interface LeagueDao {
	
	// add a league
	/**
	 * Add a league to the DB for the referenced league organizer
	 * @param leagueOrganizer the user object of the league organizer
	 * @param league the information for the league
	 */
	public void addLeague(String leagueName, String courseName);
	
	// invite someone to a league
	/**
	 * Store an invite for a user in the system that references a league and it's organizer
	 * @param invite
	 */
	public void invitePlayerToLeague(Invitation invite);
	
	// get league invites for user
	/**
	 * Return all invitations that have been offered to the user
	 * @param user the user to whom the invitations are proferred
	 * @return List<Invitation> all the invitations that have been proferred to the user
	 */
	public List<Invitation> getAllInvitationsForUser(User user);
	
	// accept league invitation
	/**
	 * Adds the invitee to the referenced league in the DB and then removes the invitation from the DB
	 * @param invite contains the league reference
	 */
	public boolean acceptLeagueInvitation(Invitation invite);
	
	// get all users in a league
	/**
	 * Gets all users that are in a league, you must accept an invite before you can qualify as being in a league
	 * @param league the league in question
	 * @return List<User> a list of the users that have accepted invites to the league
	 */
	public List<User> getAllUsersInLeague(League league);
	
	// get all leagues created by an organizer
	/**
	 * Finds all of the leagues that are administered by a particular user
	 * @param leagueOrganizer the user in question
	 * @return List<League> a list of the leagues for the user
	 */
	public List<League> getAllLeaguesFromOrganizer(User leagueOrganizer);
	
	/**
	 * Finds and returns a list of users and their scores for a particular league
	 * @param leagueId identifying number for the league in the DB
	 * @return List<LeagueScore> a list of usernames and their bestscores for that particular league
	 */
	public List<LeagueScore> getScoresInLeague(Long leagueId);
	
	/**
	 * Inputs all passed scores to the league_members_and_scores table.
	 * ...Given that the score is not 0, and is better than the previous score.
	 * @param newScores a list of the newly entered scores for the league in question
	 * @param leagueId the league in question
	 */
	public void enterLeagueScores(List<LeagueScore> newScores, Long leagueId);

	/**
	 * Finds all of the leagues that a user has accepted an invitation to.
	 * @param leagueUser the user in question
	 * @return List<League> the leagues that the user is registered for
	 */
	List<League> getAllLeaguesForUser(User leagueUser);
	
	/**
	 * Remove the passed invitation from the DB
	 * @param invite the invitation to remove
	 */
	public void removeInvitation(Invitation invite);
	
}
