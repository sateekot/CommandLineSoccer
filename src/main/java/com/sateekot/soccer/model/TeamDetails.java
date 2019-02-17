package com.sateekot.soccer.model;

/**
 * 
 * @author sateekot
 * Date: 14-02-2019
 */
public class TeamDetails {

	private String teamName;
	private boolean isUserTeam;
	
	
	public TeamDetails(String teamName, boolean isUserTeam) {
		super();
		this.teamName = teamName;
		this.isUserTeam = isUserTeam;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public boolean isUserTeam() {
		return isUserTeam;
	}
	public void setUserTeam(boolean isUserTeam) {
		this.isUserTeam = isUserTeam;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isUserTeam ? 1231 : 1237);
		result = prime * result + ((teamName == null) ? 0 : teamName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TeamDetails other = (TeamDetails) obj;
		if (isUserTeam != other.isUserTeam)
			return false;
		if (teamName == null) {
			if (other.teamName != null)
				return false;
		} else if (!teamName.equals(other.teamName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "TeamDetails [teamName=" + teamName + ", isUserTeam=" + isUserTeam + "]";
	}
	
}
