package com.sateekot.soccer.model;

import java.io.Serializable;

import com.sateekot.soccer.utils.PlayerType;

/**
 * 
 * @author sateekot
 * Date : 13-02-2019
 * Model class for Player
 */

public class Player implements Serializable {

	private String name;
	private PlayerType playerType;
	private Integer skillPoints;
	
	public Player() {
		
	}
	
	public Player(String name, PlayerType playerType, Integer skillPoints) {
		super();
		this.name = name;
		this.playerType = playerType;
		this.skillPoints = skillPoints;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PlayerType getPlayerType() {
		return playerType;
	}

	public void setPlayerType(PlayerType playerType) {
		this.playerType = playerType;
	}

	public Integer getSkillPoints() {
		return skillPoints;
	}

	public void setSkillPoints(Integer skillPoints) {
		this.skillPoints = skillPoints;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", playerType=" + playerType + ", skillPoints=" + skillPoints + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
	
}
