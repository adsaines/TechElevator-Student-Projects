package com.techelevator.objects;

import java.util.ArrayList;
import java.util.Arrays;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class Survey {
	
	public static final ArrayList<String> stateAbbreviations = new ArrayList<String>(Arrays.asList("AK","AL","AR","AZ","CA","CO","CT","DC","DE","FL","GA","GU",
			"HI","IA","ID", "IL","IN","KS","KY","LA","MA","MD","ME","MH",
			"MI","MN","MO","MS","MT","NC","ND","NE","NH","NJ","NM","NV",
			"NY", "OH","OK","OR","PA","PR","PW","RI","SC","SD","TN","TX",
			"UT","VA","VI","VT","WA","WI","WV","WY"));
	
	public static final ArrayList<String> activityLevelList = new ArrayList<String>(Arrays.asList("Sedentary",
			"Couch Potato", "Infrequent Gym Visitor",
			"Frequent Gym Visitor", "Marathon Runner"));
	
	@NotBlank(message="No park was selected. Please select a park.")
	@Size(min=3, max=5, message="The selected park does not exist.")
	private String parkCode;
	
	@NotBlank(message="That isn't an email.")
	@Email(message="That isn't an email.")
	private String email;
	
	@NotBlank(message="No state was selected. Please select a state.")
	@Size(min=2, max=2, message="The selected state does not exist.")
	private String state;

	@NotBlank(message="No Activity Level was selected. Please select a Activity Level.")
	private String activityLevel;
	
	@AssertTrue
	public boolean stateExists(String state) {
		if(stateAbbreviations.contains(state) == true) {
			return true;
		}
		else {
			return false;
		}
	}
	@AssertTrue
	public boolean activityLevelExists(String activityLevel) {
		if(activityLevelList.contains(activityLevel) == true) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getParkCode() {
		return parkCode;
	}
	public void setParkCode(String parkCode) {
		this.parkCode = parkCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getActivityLevel() {
		return activityLevel;
	}
	public void setActivityLevel(String activityLevel) {
		this.activityLevel = activityLevel;
	}
	
}
