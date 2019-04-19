package com.TechElevator.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.NOT_FOUND)
public class GeneralException extends Exception{

	// Default generated exception ID
	private static final long serialVersionUID = 1L;
	
	public GeneralException(String message) {
		super(message);
	}

}
