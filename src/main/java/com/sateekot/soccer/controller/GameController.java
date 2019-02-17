package com.sateekot.soccer.controller;

import com.sateekot.soccer.model.GameStateModel;

/**
 * 
 * @author sateekot
 * Date 16-02-2019
 * GameController provides apis for creating new game, loading existing game.
 */
public interface GameController {

	/**
	 * Creates new game.
	 */
	void createNewGame();
	
	/**
	 * Start playing game.
	 * @param gameState
	 */
	void playGame(GameStateModel gameState);
	
	/**
	 * Load existing game.
	 */
	void loadGame();
}