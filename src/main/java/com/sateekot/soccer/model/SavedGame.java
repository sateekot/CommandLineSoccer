package com.sateekot.soccer.model;

/**
 * 
 * @author sateekot
 * Date: 16-02-2019
 * Model class to save/load the game.
 */
public class SavedGame {

	private Integer gameId;
	private Integer userTeamId;
	private Integer comTeamId;
	private String userTeamName;
	private String comTeamName;
	
	
	
	public SavedGame(Integer gameId, Integer userTeamId, Integer comTeamId, String userTeamName, String comTeamName) {
		super();
		this.gameId = gameId;
		this.userTeamId = userTeamId;
		this.comTeamId = comTeamId;
		this.userTeamName = userTeamName;
		this.comTeamName = comTeamName;
	}
	public Integer getGameId() {
		return gameId;
	}
	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
	public Integer getUserTeamId() {
		return userTeamId;
	}
	public void setUserTeamId(Integer userTeamId) {
		this.userTeamId = userTeamId;
	}
	public Integer getComTeamId() {
		return comTeamId;
	}
	public void setComTeamId(Integer comTeamId) {
		this.comTeamId = comTeamId;
	}
	public String getUserTeamName() {
		return userTeamName;
	}
	public void setUserTeamName(String userTeamName) {
		this.userTeamName = userTeamName;
	}
	public String getComTeamName() {
		return comTeamName;
	}
	public void setComTeamName(String comTeamName) {
		this.comTeamName = comTeamName;
	}
	
	
}
