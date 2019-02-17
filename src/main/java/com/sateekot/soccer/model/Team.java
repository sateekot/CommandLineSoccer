package com.sateekot.soccer.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author sateekot
 * Date: 13-02-2019
 * Model class for Team.
 */

public class Team implements Serializable{

	private String teamName;
	List<Player> playersList;
	
	public Team() {}
	
	public Team(String teamName, List<Player> listOfPlayers) {
		this.teamName = teamName;
		this.playersList = listOfPlayers;
	}
	
	@Override
	public String toString() {
		return "Team [teamName=" + teamName + ", playersList=" + playersList + "]";
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public List<Player> getPlayersList() {
		return playersList;
	}
	public void setPlayersList(List<Player> playersList) {
		this.playersList = playersList;
	}
	
	
	
}
