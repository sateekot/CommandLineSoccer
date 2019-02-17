package com.sateekot.soccer.utils;

/**
 * 
 * @author sateekot
 *	Enum for providing player type i.e. forward/striker, goal keeper, mid fielder and defender.
 */
public enum PlayerType {

	FORWARD("Forward"),
	GOALKEEPER("GoalKeeper"),
	MIDFIELDER("Midfielder"),
	DEFENDER("Defender");
	
	private String playerType;
	
	PlayerType(String playerType) {
		this.playerType= playerType;
	}
	
	public String getPlayerType() {
		return this.playerType;
	}
	
}
