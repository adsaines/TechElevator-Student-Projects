package com.TechElevator.Controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.TechElevator.DAO.UserDao;
import com.TechElevator.Pojos.Login;
import com.TechElevator.Pojos.User;
import com.TechElevator.UserSecurity.AuthProvider;
import com.TechElevator.UserSecurity.JwtTokenHandler;
import com.TechElevator.UserSecurity.RequestResult;

@RestController
@CrossOrigin
public class LoginController {
	
	@Autowired
	private AuthProvider authProv;
	@Autowired
	private JwtTokenHandler tokenizer;
	@Autowired
	private UserDao userdao;

	@PostMapping(path="/login")
	public String LoginUser(@RequestBody Login login) throws GeneralException {
		// ask security to sign you in
		boolean response = authProv.signIn(login.getUsername(), login.getPassword());
		
		// check for a failure to login
		if (response == false) {
			throw new GeneralException("User information does not match our records.");
		}
		
		// if response is true then complete the user object and create a token for them
		User loggedInUser = userdao.getUserByUsername(login.getUsername());
		return tokenizer.createToken(loggedInUser.getUsername(), loggedInUser.getRole());
	}
	
	@PostMapping(path="/register")
	public RequestResult registerNewUser(@Valid @RequestBody User user, BindingResult results) {
		
		RequestResult reqRes = new RequestResult();
		
		if (results.hasErrors()) {
			// using jwt-backend example
			reqRes.setSuccess(false);
			for (ObjectError error : results.getAllErrors()) {
				reqRes.addError(error.getDefaultMessage());
			}
		} else {
			// check to make sure that the specified username is not already claimed
			boolean usernameExists = userdao.checkForUsername(user.getUsername());
			
			if (usernameExists == true) {
				reqRes.setSuccess(false);
				reqRes.addError("Username already exists.");
			} else {
				reqRes.setSuccess(true);
				authProv.register(user);
			}
		}
		return reqRes;
	}
	
	@PostMapping(path="/logout")
	public void logUserOutOfSystem() {
		authProv.logOff();
	}
}
