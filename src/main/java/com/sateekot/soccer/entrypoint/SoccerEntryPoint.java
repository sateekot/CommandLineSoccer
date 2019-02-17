package com.sateekot.soccer.entrypoint;

import com.sateekot.soccer.controller.GameLauncher;

/**
 * 
 * @author sateekot
 * Date: 13-02-2019
 * This component is the entrypoint for the game. It will invoke game launcher for different options.
 */
public class SoccerEntryPoint {

	public static void main(String[] args){
		GameLauncher.startGame();
	}

}
