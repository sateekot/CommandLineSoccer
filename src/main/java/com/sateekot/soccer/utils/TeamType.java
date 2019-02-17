package com.sateekot.soccer.utils;

/**
 * 
 * @author sateekot
 * Date 14-02-2019
 * Enum for team selection i.e. 1. Creating new team 2. Selecting existing team.
 */
public enum TeamType {

	NEW_TEAM("Create team"),
	SELECT_TEAM("Select team");
	
	private String teamType;
	
	TeamType(String teamType) {
		this.teamType = teamType;
	}
	public String getTeamType() {
		return this.teamType;
	}
}
