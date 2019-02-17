package com.sateekot.soccer.controller;

import static com.sateekot.soccer.utils.MainMenu.EXIT_GAME;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sateekot.soccer.io.InputReader;
import com.sateekot.soccer.io.OutputWriter;
import com.sateekot.soccer.repository.H2RepositoryImpl;
import com.sateekot.soccer.utils.MainMenu;
import com.sateekot.soccer.utils.SoccerUtils;

/**
 * 
 * @author sateekot
 * Date - 14-02-2019
 * This component launches the game.
 */
public class GameLauncher {
	
	private static final Logger LOGGER = Logger.getLogger(GameLauncher.class.getName()); 

	public static void startGame() {
		OutputWriter outputWriter = new OutputWriter();
		InputReader inputReader = new InputReader(System.in);
		if(!H2RepositoryImpl.checkDBConnection()) {
			outputWriter.printValidationMessage("Creaing metadata for first time.");
			H2RepositoryImpl.createMetaData();
		}
		StringBuilder welcomeMessage = new StringBuilder();
		welcomeMessage.append("------------------------------------------------------------------------------\n");
		welcomeMessage.append("Welcome to Command Line Soccer!\n");
		File file = new File(ClassLoader.getSystemResource("WelcomeMessage").getFile());
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				welcomeMessage.append((sc.nextLine())+" \n");
			}
			sc.close();
			System.out.println(welcomeMessage.toString());
		} catch(FileNotFoundException fnfe) {
			LOGGER.log(Level.SEVERE, "Welcome file not found!");
		}
		
		
		Integer userOptionNumber = 0;
		do {
			int lengthOfMainMenuItems = MainMenu.values().length;
			for(int i=1; i<=lengthOfMainMenuItems; i++) {
				outputWriter.printMessage(i +". "+MainMenu.values()[i-1].getValue());
			}
			String userOptionString = inputReader.readInput();
			
			userOptionNumber = SoccerUtils.isValidOptionByUser(userOptionString, lengthOfMainMenuItems);
			if(userOptionNumber == Integer.MIN_VALUE) {
				outputWriter.printValidationMessage("Please select the numeric value from the given options.");
				continue;
			}
			if(userOptionNumber == 0) {
				outputWriter.printValidationMessage("Game cannot be saved here.");
				continue;
			}

			MainMenu mainMenu = MainMenu.values()[userOptionNumber-1];
			switch(mainMenu) {
			case NEW_GAME:
				GameControllerImpl gameController = new GameControllerImpl(inputReader, outputWriter);
				gameController.createNewGame();
				break;
			case LOAD_GAME:
				GameControllerImpl loadGame = new GameControllerImpl(inputReader, outputWriter);
				loadGame.loadGame();
				break;
			default:
				outputWriter.printMessage("Default!");
			}
		} while(EXIT_GAME.ordinal()+1 != userOptionNumber);
		outputWriter.printMessage("Thank you for playing!");
	
	}
}
