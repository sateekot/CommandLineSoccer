package com.sateekot.soccer.controller;

import com.sateekot.soccer.player.functionality.ComputerForwardFunctionality;
import com.sateekot.soccer.player.functionality.ComputerGoalKeeperFunctionality;
import com.sateekot.soccer.player.functionality.PlayerFunctionality;
import com.sateekot.soccer.player.functionality.UserForwardFunctionality;
import com.sateekot.soccer.player.functionality.UserGoalKeeperFunctionality;
import com.sateekot.soccer.utils.PlayerType;

/**
 * 
 * @author sateekot
 * Factory for player functionality.
 */
public class PlayerFactory {

	public static PlayerFunctionality getPlayer(PlayerType playerType, boolean isComputer) {
		PlayerFunctionality playerFunctionality = null;
		switch (playerType) {
		case GOALKEEPER:
			playerFunctionality = (isComputer == true) ? new ComputerGoalKeeperFunctionality() : new UserGoalKeeperFunctionality();
			break;
		case FORWARD:
			playerFunctionality = (isComputer == true) ? new ComputerForwardFunctionality() : new UserForwardFunctionality();
			break;
		//TODO DEFENDER, MIDFIELDER
		default:
			break;
		}
		return playerFunctionality;
	}
}
