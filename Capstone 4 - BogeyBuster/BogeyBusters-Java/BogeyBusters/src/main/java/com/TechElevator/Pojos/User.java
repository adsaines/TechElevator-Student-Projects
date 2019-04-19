package com.TechElevator.Pojos;


import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.TechElevator.UserSecurity.RequestAuthProvider;

public class User {

	private long userId;

	@NotBlank(message="Username cannot be blank.")
	@Size(min=2,message="username must contain more than two characters.")
	private String username;
	
	@NotBlank(message="Password cannot be blank.")
	@Size(min=5,message="Password must contain more than five characters.")
	private String password;

	@NotBlank(message="Password cannot be blank.")
	private String confirmPassword;
	
	private String role;
	
	@Email(message="Email cannot be blank.")
	private String email;
	
	
	@NotBlank(message="First Name cannot be blank.")
	private String firstName;
	
	@NotBlank(message="Last Name cannot be blank.")
	private String lastName;
	
	
	
	@AssertTrue(message="Passwords must match.")
	public boolean getPasswordsMatch() {
		if (password != null && confirmPassword != null && password.contentEquals(confirmPassword)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	@AssertTrue(message="Role can only be 'Golfer' or 'League Organizer'.")
	public boolean isProperRole() {
		if (role.contentEquals(RequestAuthProvider.USER) || role.contentEquals(RequestAuthProvider.LEAGUE_ORGANIZER)) {
			return true;
		} else {
			return false;
		}
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
