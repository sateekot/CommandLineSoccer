package com.sateekot.soccer.model;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author sateekot
 *
 */
public class GameStats {
	private Map<TeamDetails, List<PlayerStats>> gameStats;

	public Map<TeamDetails, List<PlayerStats>> getGameStats() {
		return gameStats;
	}

	public void setGameStats(Map<TeamDetails, List<PlayerStats>> gameStats) {
		this.gameStats = gameStats;
	}

	@Override
	public String toString() {
		return "GameStats [gameStats=" + gameStats + "]";
	}
	
	
}
