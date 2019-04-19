package com.TechElevator.JDBC;

import java.util.ArrayList;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.TechElevator.DAO.LeagueDao;
import com.TechElevator.Pojos.Invitation;
import com.TechElevator.Pojos.League;
import com.TechElevator.Pojos.LeagueScore;
import com.TechElevator.Pojos.User;
import com.TechElevator.UserSecurity.AuthProvider;
import com.TechElevator.UserSecurity.RequestAuthProvider;

@Component
public class JdbcLeagueDao implements LeagueDao {

	private JdbcTemplate jdbcTemplate;
	
	private JdbcUserDao jdbcUserDao;
	
	@Autowired
	private AuthProvider authprovider;
	
	@Autowired
	public JdbcLeagueDao(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
		
	}

	@Override
	public void addLeague(String leagueName, String courseName) {
		
		User leagueCreator = authprovider.getCurrentUser();
		
		String getCourseId = "SELECT courseid FROM courses WHERE coursename = ?;";
		Long courseid = jdbcTemplate.queryForObject(getCourseId, Long.class, courseName);
		String createLeagueQuery = "INSERT INTO league(leaguename, courseid, coursename, leagueorganizer) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(createLeagueQuery, leagueName, courseid, courseName, leagueCreator.getUserId());
		String LeagueIdQuery = "SELECT leagueid FROM league WHERE leaguename = ? AND organizerid = ?";
		Long getNewLeagueId = jdbcTemplate.queryForObject(getCourseId, Long.class, courseName);
		
		String insertOrganizerToLeagueUserTable = "INSERT INTO league_members_and_scores (leagueid, userid) VALUES (?, ?)";
		jdbcTemplate.update(insertOrganizerToLeagueUserTable, getNewLeagueId, leagueCreator.getUserId());
		
		// only switches you to organizer if you are currently a user
		if (leagueCreator.getRole().contentEquals(RequestAuthProvider.USER)){
			String makeLeagueOrganizer = "UPDATE users set userrole = ? WHERE userid = ?";
			jdbcTemplate.update(makeLeagueOrganizer, "League Organizer", leagueCreator.getUserId());
		}
	}

	@Override
	public void invitePlayerToLeague(Invitation invite) {
		String addInviteQuery = "INSERT INTO league_invitation(organizerid, invitee, leagueid) VALUES (?,?,?);";
		jdbcTemplate.update(addInviteQuery,invite.getOrganizerId(), invite.getInvitee(), invite.getLeagueId());
	}

	@Override
	public List<Invitation> getAllInvitationsForUser(User user) {
		List<Invitation> allInvitations = new ArrayList<>();
		String invitationQuery = "SELECT league_invitation.invitationid, league_invitation.organizerid, league_invitation.invitee, league_invitation.leagueid, league.leaguename, users.username " +
								"FROM league_invitation " +
								"JOIN users ON league_invitation.invitee = users.userid " +
								"JOIN league ON league_invitation.leagueid = league.leagueid  " +
								"WHERE league_invitation.invitee = ?;";
		
		SqlRowSet returnedInvitations = jdbcTemplate.queryForRowSet(invitationQuery, user.getUserId());
		while (returnedInvitations.next()) {
			allInvitations.add(mapInvitationToObject(returnedInvitations));
		}
		return allInvitations;
	}

	@Override
	public boolean acceptLeagueInvitation(Invitation invite) {
		boolean addedToLeague = false;
		String checkForLeagueMembership = "SELECT count(userid) FROM league_members_and_scores WHERE leagueid=? AND userid=?";
		Long numOfEntries = jdbcTemplate.queryForObject(checkForLeagueMembership, Long.class, invite.getLeagueId(), invite.getInvitee());
		
		if (numOfEntries ==0) {
			addedToLeague = true;
			String joinLeagueQuery = "INSERT INTO league_members_and_scores(leagueid,userid,bestscore) VALUES (?,?,?)";
			jdbcTemplate.update(joinLeagueQuery, invite.getLeagueId(), invite.getInvitee(), 0L);
		} 
		return addedToLeague;
	}

	@Override
	public List<User> getAllUsersInLeague(League league) {
		List<User> leagueMembers = new ArrayList<User>();
		String leagueMemberQuery = "SELECT userid FROM league_members_and_scores WHERE leagueid = ?";
		SqlRowSet returnedUsers = jdbcTemplate.queryForRowSet(leagueMemberQuery, league.getLeagueId());
		while (returnedUsers.next()) {
			leagueMembers.add(jdbcUserDao.mapResultToUser(returnedUsers));
		}
		return leagueMembers;
	}

	@Override
	public List<League> getAllLeaguesFromOrganizer(User leagueOrganizer) {
		List<League> allLeagues = new ArrayList<League>();
		String leagueQuery = "SELECT * FROM league WHERE leagueorganizer = ?";
		SqlRowSet returnedLeagues = jdbcTemplate.queryForRowSet(leagueQuery, authprovider.getCurrentUser().getUserId());
		while (returnedLeagues.next()) {
			allLeagues.add(mapLeagueToObject(returnedLeagues));
		}
		return allLeagues;
	}
	
	@Override
	public List<League> getAllLeaguesForUser(User leagueUser) {
		List<League> allLeagues = new ArrayList<League>();
		String leagueQuery = "SELECT * FROM league " + 
				"JOIN league_members_and_scores ON league.leagueid = league_members_and_scores.leagueid " +
				"WHERE league_members_and_scores.userid = ?";
		SqlRowSet returnedLeagues = jdbcTemplate.queryForRowSet(leagueQuery, authprovider.getCurrentUser().getUserId());
		while (returnedLeagues.next()) {
			allLeagues.add(mapLeagueToObject(returnedLeagues));
		}
		return allLeagues;
	}

	@Override
	public List<LeagueScore> getScoresInLeague(Long leagueId) {
		List<LeagueScore> leagueScores = new ArrayList<LeagueScore>();
		String scoreQuery = "SELECT league_members_and_scores.userid, users.username, bestscore, league.leagueid FROM league_members_and_scores " +
							"JOIN users ON league_members_and_scores.userid = users.userid " + 
							"JOIN league ON league_members_and_scores.leagueid = league.leagueid " + 
							"WHERE league_members_and_scores.leagueid = ?" +
							"ORDER BY bestscore ASC";
		
		SqlRowSet returnedValues = jdbcTemplate.queryForRowSet(scoreQuery, leagueId);
		
		while (returnedValues.next()) {
			leagueScores.add(mapLeagueScoreFromDbToObject(returnedValues));
		}
		
		return leagueScores;
	}
	
	private League mapLeagueToObject(SqlRowSet leagueInfo) {
		League newLeague = new League();
		newLeague.setLeagueId(leagueInfo.getLong("leagueid"));
		newLeague.setName(leagueInfo.getString("leaguename"));
		newLeague.setCourseId(leagueInfo.getLong("courseid"));
		newLeague.setOrganizerId(leagueInfo.getLong("leagueorganizer"));
		newLeague.setCourseName(leagueInfo.getString("coursename"));
		return newLeague;
	}
	
	private LeagueScore mapLeagueScoreFromDbToObject(SqlRowSet info) {
		LeagueScore newScore = new LeagueScore();
		newScore.setUserId(info.getLong("userid"));
		newScore.setScore(info.getLong("bestscore"));
		newScore.setUserName(info.getString("username"));
		newScore.setLeagueId(info.getLong("leagueid"));
		return newScore;
	}
	private Invitation mapInvitationToObject(SqlRowSet info) {
		Invitation newInvitation = new Invitation();
		newInvitation.setOrganizerId(info.getLong("organizerid"));
		newInvitation.setInvitee(info.getLong("invitee"));
		newInvitation.setLeagueId(info.getLong("leagueid"));
		newInvitation.setOrganizerName(info.getString("username"));
		newInvitation.setLeagueName(info.getString("leaguename"));
		newInvitation.setInvitationId(info.getLong("invitationid"));
		return newInvitation;
	}
	
	@Override
	public void enterLeagueScores(List<LeagueScore> newScores, Long leagueId) {
		String submitScoreQuery = "UPDATE league_members_and_scores SET bestscore = ? WHERE leagueid = ? AND userid = ?;";
		Long bestScore = 0L;
		
		for (LeagueScore userScore : newScores) {
			// exclude newScores of 0, they are un-entered values
			// if the currentScore is 0 then no scores have been entered for them yet, set the bestScore to the new entry
			if (userScore.getScore() == 0) {
				bestScore = userScore.getNewScore();
			} else if (userScore.getNewScore() != 0) {
				// compare the newScore and the bestScore to determine which is better
				bestScore = userScore.getNewScore() < userScore.getScore() ? userScore.getNewScore() : userScore.getScore();
			}
			// submit the score that is the better of the two
			jdbcTemplate.update(submitScoreQuery, bestScore, leagueId, userScore.getUserId());
		}
	}

	@Override
	public void removeInvitation(Invitation invite) {
		String removalQuery = "DELETE FROM league_invitation WHERE invitationid = ?;";
		jdbcTemplate.update(removalQuery, invite.getInvitationId());
	}
}
