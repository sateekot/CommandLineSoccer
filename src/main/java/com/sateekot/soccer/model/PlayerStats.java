package com.sateekot.soccer.model;

/**
 * 
 * @author sateekot
 *
 */
public class PlayerStats {

	private Player player;
	private boolean isScored;
	
	private boolean isPlayed;
	
	public PlayerStats() {}
	
	public PlayerStats(Player player, boolean isScored, boolean isPlayed) {
		super();
		this.player = player;
		this.isScored = isScored;
		this.isPlayed = isPlayed;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public boolean isScored() {
		return isScored;
	}
	public void setScored(boolean isScored) {
		this.isScored = isScored;
	}
	
	public boolean isPlayed() {
		return isPlayed;
	}
	public void setPlayed(boolean isPlayed) {
		this.isPlayed = isPlayed;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isPlayed ? 1231 : 1237);
		result = prime * result + (isScored ? 1231 : 1237);
		result = prime * result + ((player == null) ? 0 : player.hashCode());
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
		PlayerStats other = (PlayerStats) obj;
		if (isPlayed != other.isPlayed)
			return false;
		if (isScored != other.isScored)
			return false;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "PlayerStats [player=" + player + ", isScored=" + isScored + ", isPlayed=" + isPlayed + "]";
	}
	
	
	
}
