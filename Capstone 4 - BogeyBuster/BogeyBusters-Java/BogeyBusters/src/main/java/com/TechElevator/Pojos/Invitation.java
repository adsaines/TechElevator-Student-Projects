package com.TechElevator.Pojos;

import org.hibernate.validator.constraints.NotBlank;

public class Invitation {

	private Long organizerId;
	@NotBlank(message="An invitee must be selected before you can send an invite.")
	private Long invitee;
	@NotBlank(message="You must select one of your leagues to invite a new player to.")
	private Long leagueId;
	private String organizerName;
	private String leagueName;
	private Long invitationId;
	
	public String getOrganizerName() {
		return organizerName;
	}
	public void setOrganizerName(String organizerName) {
		this.organizerName = organizerName;
	}
	public String getLeagueName() {
		return leagueName;
	}
	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}
	public Long getInvitationId() {
		return invitationId;
	}
	public void setInvitationId(Long invitationId) {
		this.invitationId = invitationId;
	}
	public Long getOrganizerId() {
		return organizerId;
	}
	public void setOrganizerId(Long organizerId) {
		this.organizerId = organizerId;
	}
	public Long getInvitee() {
		return invitee;
	}
	public void setInvitee(Long invitee) {
		this.invitee = invitee;
	}
	public Long getLeagueId() {
		return leagueId;
	}
	public void setLeagueId(Long leagueId) {
		this.leagueId = leagueId;
	}
	
}
