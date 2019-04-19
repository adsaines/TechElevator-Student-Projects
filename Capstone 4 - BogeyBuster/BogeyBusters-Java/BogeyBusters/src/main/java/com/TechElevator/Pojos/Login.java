package com.TechElevator.Pojos;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class Login {

	@NotBlank(message="username cannot be blank.")
	private String username;
	@NotBlank(message="password cannot be blank.")
	@Size(min=5,message="password must contain more than five characters.")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
