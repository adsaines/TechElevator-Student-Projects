package com.TechElevator.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TechElevator.DAO.LeagueDao;
import com.TechElevator.DAO.UserDao;
import com.TechElevator.Pojos.Invitation;
import com.TechElevator.Pojos.League;
import com.TechElevator.Pojos.LeagueScore;
import com.TechElevator.UserSecurity.AuthProvider;
import com.TechElevator.UserSecurity.JwtTokenHandler;
import com.TechElevator.UserSecurity.RequestAuthProvider;
import com.TechElevator.UserSecurity.RequestResult;

@RestController
@CrossOrigin
@RequestMapping(path="/league")
public class LeagueController {
	@Autowired
	private AuthProvider authprovider;
	@Autowired
	private LeagueDao leagueDao;
	
	@PostMapping(path="/createLeague")
	public void createLeague(@RequestBody League league) {
		System.out.println(league.getName());
		System.out.println(league.getCourseName());
		leagueDao.addLeague(league.getName(), league.getCourseName());
	}
	
	@PostMapping(path="/inviteUser")
	public RequestResult inviteUserToLeague(@RequestBody Invitation invite, BindingResult results) {
		RequestResult reqRes = new RequestResult();
		
		if (results.hasErrors()) {
			// load binding errors and return them
			reqRes.setSuccess(false);
			for (ObjectError error : results.getAllErrors()) {
				reqRes.addError(error.getDefaultMessage());
			}
		} else {
			
			// check if user is a league organizer or Administrator
			boolean roleQualified = authprovider.userHasRole(new String[] {RequestAuthProvider.LEAGUE_ORGANIZER, RequestAuthProvider.ADMIN});
			if (roleQualified) {
				invite.setOrganizerId(authprovider.getCurrentUser().getUserId());
				reqRes.setSuccess(true);
				leagueDao.invitePlayerToLeague(invite);
			} else {
				reqRes.setSuccess(false);
				reqRes.addError("User role does not grant permission for that operation.");
			}
		}
		return reqRes;
	}
	
	@GetMapping(path="/getUserInvitations")
	public List<Invitation> getAllInvitationsForUser(){
		return leagueDao.getAllInvitationsForUser(authprovider.getCurrentUser());
	}
	
	@PostMapping(path="/acceptLeagueInvitation")
	public RequestResult acceptLeagueInvitation(@RequestBody Invitation invite) {
		RequestResult reqRes = new RequestResult();
		boolean addedToLeague = leagueDao.acceptLeagueInvitation(invite);
		
		if (addedToLeague == true) {
			// user has been added to the league
			reqRes.setSuccess(true);
		} else {
			// user is already in the league, pass back message stating so
			reqRes.setSuccess(false);
			reqRes.addError("User is already in league. Invitation has been removed.");
		}
		
		// in both conditions the invitation is no longer required
		leagueDao.removeInvitation(invite);
		
		return reqRes;
	}
	
	@PostMapping(path="/declineLeagueInvitation")
	public void declineLeagueInvitation(@RequestBody Invitation invite) {
		leagueDao.removeInvitation(invite);
	}
	
	@GetMapping(path="/myLeagues")
	public List<League> getLeaguesForUser() {
		return leagueDao.getAllLeaguesForUser(authprovider.getCurrentUser());
	}
	
	
	// get all users in a league
	
	@GetMapping(path="/getLeagueScores/{leagueId}")
	public List<LeagueScore> getScoresForMyLeague(@PathVariable Long leagueId){
		return leagueDao.getScoresInLeague(leagueId);
	}
	
	@PostMapping(path="/submitLeagueScores/{leagueId}")
	public RequestResult submitNewScoresForLeague(@PathVariable Long leagueId, @RequestBody List<LeagueScore> newScores, BindingResult results) {
		RequestResult reqRes = new RequestResult();
		
		if (results.hasErrors()) {
			// load binding errors and return them
			reqRes.setSuccess(false);
			for (ObjectError error : results.getAllErrors()) {
				reqRes.addError(error.getDefaultMessage());
			}
		} else {
			// check if user is a league organizer or Administrator
			boolean roleQualified = authprovider.userHasRole(new String[] {RequestAuthProvider.LEAGUE_ORGANIZER, RequestAuthProvider.ADMIN});
			if (roleQualified) {
				reqRes.setSuccess(true);
				leagueDao.enterLeagueScores(newScores, leagueId);
			} else {
				reqRes.setSuccess(false);
				reqRes.addError("User role does not grant permission for that operation.");
			}
		}
		return reqRes;
	}
}
