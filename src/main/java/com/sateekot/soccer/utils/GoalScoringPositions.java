package com.sateekot.soccer.utils;

/**
 * 
 * @author sateekot
 * Enum provides Goal scoring positions.
 */
public enum GoalScoringPositions {

	// Level 1
	LEFT("left"),
	CENTER("centre"),
	RIGHT("right");
	
	/*
	 *  Next Levels 
	 *  1. Top left, Bottom Left, Center, Top right, Bottom right
	 *  2. Top left, Top centre, Top right, Middle left, Middle center, Middle right, Bottom left, Bottom center, Bottom right
	 */
	
	private String position;
	
	GoalScoringPositions(String position) {
		this.position = position;
	}
	
	public String getPosition( ) {
		return this.position;
	}
}
