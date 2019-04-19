package com.TechElevator.Controllers;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.TechElevator.DAO.TeeTimeDao;
import com.TechElevator.Pojos.Match;
import com.TechElevator.Pojos.TeeTime;
import com.TechElevator.Pojos.User;
import com.TechElevator.UserSecurity.AuthProvider;
import com.TechElevator.UserSecurity.RequestAuthProvider;
import com.TechElevator.UserSecurity.RequestResult;

@RestController
@CrossOrigin
@RequestMapping(path="/teeTime")
public class TeeTimeController {
	
	@Autowired
	private TeeTimeDao teeTimeDao;
	@Autowired
	private AuthProvider authprovider;
	@Autowired
	public TeeTimeController(TeeTimeDao teeTimeDao) {
		this.teeTimeDao = teeTimeDao;
	}
	
	@GetMapping(path="/userTeeTimes")
	public List<TeeTime> getUserTeeTimes() {
		return teeTimeDao.getUserTeeTimes(authprovider.getCurrentUser());
	}
	
	@PostMapping(path="/addTeeTime")
	public void addTeeTimeToDatabase(@RequestBody String teeTime, @RequestBody long courseId, @RequestBody String username) throws GeneralException {
			
		teeTimeDao.addTeeTime(teeTime, courseId, username);
		
//			RequestResult reqRes = new RequestResult();
			
//			if (results.hasErrors()) {
//				// load binding errors and return them
//				reqRes.setSuccess(false);
//				for (ObjectError error : results.getAllErrors()) {
//					reqRes.addError(error.getDefaultMessage());
//				}
//			} else {
//				// check if user is an league organizer
//				boolean roleQualified = authprovider.userHasRole(new String[] {RequestAuthProvider.LEAGUE_ORGANIZER, RequestAuthProvider.ADMIN});
//				if (roleQualified) {
//					reqRes.setSuccess(true);
//					teeTimeDao.addTeeTime(teeTime);
//				} else {
//					reqRes.setSuccess(false);
//					reqRes.addError("User role does not grant permission for that operation.");
//				}
//			}
//			return reqRes;
	}
		
	@PostMapping(path="/addPlayerToTeeTime")
	public void addPlayerToTeeTimeInDatabase(@RequestBody Long teeTimeId, @RequestBody Long userId) {
		teeTimeDao.addPlayerToTeeTime(userId, teeTimeId);
			
	}
	
	@GetMapping(path="/getMatchInfo")
	public List<Match> getMatchInfo() {
		return teeTimeDao.getMatchInfo(authprovider.getCurrentUser());
			
	}
}
