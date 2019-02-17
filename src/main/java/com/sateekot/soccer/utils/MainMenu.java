package com.sateekot.soccer.utils;

/**
 * 
 * @author sateekot
 * Date: 14-02-2019
 * Enum for main menu options.
 */
public enum MainMenu {

	NEW_GAME("Start new game"),
	LOAD_GAME("Load a saved game"),
	EXIT_GAME("Exit");
	
	private String value;
	
	private MainMenu(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
}
