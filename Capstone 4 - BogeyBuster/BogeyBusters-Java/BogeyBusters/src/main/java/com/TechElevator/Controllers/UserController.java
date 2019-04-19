package com.TechElevator.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TechElevator.DAO.UserDao;
import com.TechElevator.Pojos.AbbreviatedUser;
import com.TechElevator.Pojos.Course;
import com.TechElevator.Pojos.League;
import com.TechElevator.Pojos.User;
import com.TechElevator.UserSecurity.AuthProvider;
import com.TechElevator.UserSecurity.RequestResult;

@RestController
@RequestMapping(path="/user")
@CrossOrigin
public class UserController {
	
	@Autowired
	private AuthProvider authProv;
	@Autowired
	private UserDao userDao;
	
	@PutMapping(path="/changePassword")
	public RequestResult changeUserPassword(@RequestBody String newPassword, @RequestBody String oldPassword) throws GeneralException {
		RequestResult reqRes = new RequestResult();
		boolean response = authProv.changePassword(oldPassword, newPassword);
		
		if (response == false) {
			reqRes.setSuccess(false);
			reqRes.addError("Could not change password.");
		} else {
			reqRes.setSuccess(true);
		}
		return reqRes;
	}
	
	@GetMapping(path="/getUsersForInvitations/{searchTerm}")
	public List<AbbreviatedUser> getUsersByNameAndEmail(@PathVariable String searchTerm) {
		return userDao.getUsersByStringSearch(searchTerm);
	}
	
	@GetMapping(path="/currentUserObject")
	public User getCurrentUser() {
		return authProv.getCurrentUser();
	}
	
	@PutMapping(path="/addScore")
	public RequestResult addUserScore(@RequestBody User user, @RequestBody Course course, @RequestBody int score) throws GeneralException {
		RequestResult reqRes = new RequestResult();
		userDao.addScoreForUser(user, course, score);
		reqRes.setSuccess(true);
		
		return reqRes;
	}
	
	@GetMapping(path="/getUserId")
	public long isLeagueOrganizer() throws GeneralException {
		return authProv.getCurrentUser().getUserId();
	}
	
	
	
}
