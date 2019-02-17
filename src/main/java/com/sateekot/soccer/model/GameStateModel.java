package com.sateekot.soccer.model;

import java.util.List;

/**
 * 
 * @author sateekot
 * Date: 15-02-2019
 * 
 */
public class GameStateModel {

	private Integer gameId;
	private String userTeamName;
	private List<PlayerStats> userTeamPlayedPlayerList;
	private List<PlayerStats> userTeamNonPlayedPlayerList;
	private String comTeamName;
	private List<PlayerStats> comTeamPlayedPlayerList;
	private List<PlayerStats> comTeamNonPlayedPlayerList;
	
	
	public Integer getGameId() {
		return gameId;
	}
	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
	public String getUserTeamName() {
		return userTeamName;
	}
	public void setUserTeamName(String userTeamName) {
		this.userTeamName = userTeamName;
	}
	public List<PlayerStats> getUserTeamPlayedPlayerList() {
		return userTeamPlayedPlayerList;
	}
	public void setUserTeamPlayedPlayerList(List<PlayerStats> userTeamPlayedPlayerList) {
		this.userTeamPlayedPlayerList = userTeamPlayedPlayerList;
	}
	public List<PlayerStats> getUserTeamNonPlayedPlayerList() {
		return userTeamNonPlayedPlayerList;
	}
	public void setUserTeamNonPlayedPlayerList(List<PlayerStats> userTeamNonPlayedPlayerList) {
		this.userTeamNonPlayedPlayerList = userTeamNonPlayedPlayerList;
	}
	public String getComTeamName() {
		return comTeamName;
	}
	public void setComTeamName(String comTeamName) {
		this.comTeamName = comTeamName;
	}
	public List<PlayerStats> getComTeamPlayedPlayerList() {
		return comTeamPlayedPlayerList;
	}
	public void setComTeamPlayedPlayerList(List<PlayerStats> comTeamPlayedPlayerList) {
		this.comTeamPlayedPlayerList = comTeamPlayedPlayerList;
	}
	public List<PlayerStats> getComTeamNonPlayedPlayerList() {
		return comTeamNonPlayedPlayerList;
	}
	public void setComTeamNonPlayedPlayerList(List<PlayerStats> comTeamNonPlayedPlayerList) {
		this.comTeamNonPlayedPlayerList = comTeamNonPlayedPlayerList;
	}
	
	
}
