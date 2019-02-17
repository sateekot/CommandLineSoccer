package com.sateekot.soccer.player.functionality;

/**
 * 
 * @author sateekot
 * Component provides the functionality for User Goal Keeper.
 */
public class UserGoalKeeperFunctionality implements PlayerFunctionality {

	@Override
	public boolean save(String userOption, String computerOption) {
		 
		if(computerOption.equalsIgnoreCase(userOption)) {
			return true;
		}
		return false;
	}
}
