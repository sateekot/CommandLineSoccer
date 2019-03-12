package com.sateekot.soccer.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.sateekot.soccer.exception.SoccerDBException;
import com.sateekot.soccer.exception.TeamAlreadyExistException;
import com.sateekot.soccer.io.InputReader;
import com.sateekot.soccer.io.OutputWriter;
import com.sateekot.soccer.model.GameStateModel;
import com.sateekot.soccer.model.GameStats;
import com.sateekot.soccer.model.Player;
import com.sateekot.soccer.model.PlayerStats;
import com.sateekot.soccer.model.SavedGame;
import com.sateekot.soccer.model.Team;
import com.sateekot.soccer.model.TeamDetails;
import com.sateekot.soccer.player.functionality.PlayerFunctionality;
import com.sateekot.soccer.repository.H2Repository;
import com.sateekot.soccer.repository.H2RepositoryImpl;
import com.sateekot.soccer.utils.GoalScoringPositions;
import com.sateekot.soccer.utils.PlayerType;
import com.sateekot.soccer.utils.SoccerUtils;
import com.sateekot.soccer.utils.SubMenu;
import com.sateekot.soccer.utils.TeamType;

/**
 * 
 * @author sateekot
 * Date 14-02-2019
 * This component provides different game state options like
 * 	1.Creating new game
 * 	2.Save game
 * 	3.Loading game
 */
public class GameControllerImpl implements GameController {
	
	private static final Logger LOGGER = Logger.getLogger(GameControllerImpl.class.getName());
	
	private H2Repository h2Repository;
	
	public GameControllerImpl() {
		
		//TODO It is hardcoded as we have only one implementation, 
		//we can use dependency injection if we have to use Spring.
		h2Repository = new H2RepositoryImpl();
	}

	private InputReader inputReader;
	private OutputWriter outputWriter;
	public GameControllerImpl(InputReader inputReader, OutputWriter outputWriter) {
		this.inputReader = inputReader;
		this.outputWriter = outputWriter;
		h2Repository = new H2RepositoryImpl();
	}
	
	/**
	 * Creates the new game
	 */
	@Override
	public void createNewGame() {
		int lengthOfTeamType = TeamType.values().length;
		for(int i=1; i<=lengthOfTeamType; i++) {
			outputWriter.printMessage(i +". "+TeamType.values()[i-1].getTeamType());
		}
		
		String userOptionString = inputReader.readInput();
		Integer inputOption = SoccerUtils.isValidOptionByUser(userOptionString, lengthOfTeamType);
		if(inputOption == Integer.MIN_VALUE) {
			outputWriter.printMessage("Please select the numeric value from the given options.");
			createNewGame();
		}
		if(inputOption == 0) {
			outputWriter.printMessage("Game cannot be saved here.");
			createNewGame();
		}

		TeamType teamType = TeamType.values()[inputOption-1];
		switch(teamType) {
			case NEW_TEAM:
				createNewTeamAndPlay();
				break;
			case SELECT_TEAM:
				outputWriter.printMessage("This functionality is not available in current release. Will be deployed in next release.");
				break;
			default:
				break;
		}
	}
	
	private void createNewTeamAndPlay() {
		outputWriter.printMessage("Enter Unique Team Name : ");
		String teamName = inputReader.readInput();
		if(teamName.equalsIgnoreCase("0")) {
			outputWriter.printMessage("0 (Zero) can not be used as Team name. It has special meaning(Save/Exit) in this game. Please create team and save the game.");
			createNewTeamAndPlay();
		}
		outputWriter.printMessage("Create 6 players");
		List<Player> playersList = new ArrayList<Player>();
		for(int i=1; i<=6;i++) {
			outputWriter.printMessage("Enter player"+i+" name:");
			String playerName = inputReader.readInput();
			if(playerName.equalsIgnoreCase("0")) {
				outputWriter.printMessage("0 (Zero) can not be used as Player name. It has special meaning(Save/Exit) in this game. Please create team and save the game.");
				i--;
				continue;
			}
			outputWriter.printMessage("Select Player Type(FORWARD/GOALKEEPER/MIDFIELDER/DEFENDER)");
			int playerTypeValuesSize = PlayerType.values().length;
			for(int j=0; j< playerTypeValuesSize; j++) {
				outputWriter.printMessage(j+1 +". "+PlayerType.values()[j]);
			}
			String inputSelected = inputReader.readInput();
			Integer inputOption = SoccerUtils.isValidOptionByUser(inputSelected, playerTypeValuesSize);
			if(inputOption == Integer.MIN_VALUE) {
				outputWriter.printMessage("Please select the numeric value from the given options.");
				i--;
				continue;
			}
			if(inputOption == 0) {
				outputWriter.printMessage("Please create team and then save the game.");
				i--;
				continue;
			}

			PlayerType playerType = PlayerType.values()[inputOption-1];
			Player player = new Player(playerName,playerType,0);
			if(playersList.contains(player)) {
				outputWriter.printMessage("Player with same name already created in your team. Please use differnt name");
				i--;
				continue;
			}
			playersList.add(player);
		}
		
		Team userTeam = new Team(teamName,playersList);
		try {
			if(!h2Repository.save(userTeam)) {
				LOGGER.log(Level.SEVERE, "You have got some problems with database connections.");
			}
			//TODO ask user to select computer team
			outputWriter.printMessage("Selecting computer team...");
			String comRandomTeam = SoccerUtils.comRandomTeam();
			Team comTeam = h2Repository.loadTeam(comRandomTeam);
			outputWriter.printMessage("Computer team selected: "+comTeam.getTeamName());
			outputWriter.printMessage("Computer Players: ");
			printPlayerInformation(comTeam.getPlayersList());

			List<PlayerStats> userTeamNonPlayedPlayerList = new ArrayList<>();
			List<PlayerStats> comTeamNonPlayedPlayerList = new ArrayList<>();
			playersList.forEach(player -> {
				PlayerStats playerStats = new PlayerStats(player, false, false);
				userTeamNonPlayedPlayerList.add(playerStats);
			});
			
			comTeam.getPlayersList().forEach(player -> {
				PlayerStats playerStats = new PlayerStats(player, false, false);
				comTeamNonPlayedPlayerList.add(playerStats);
			});
			
			boolean readyToPlay = true;
			// TODO change option 1 to Enter to start the game.
			while(true) {
				outputWriter.printMessage("Press 1 to start the game!");
				String userOptionString = inputReader.readInput();
				if(userOptionString.equals("1")) {
					readyToPlay = true;
					break;
				}
				if(userOptionString.equals("0")) {
					outputWriter.printMessage("...Saving Game...");
					subMenuDisplayV1(0, userTeam.getTeamName(), Collections.emptyList(), userTeamNonPlayedPlayerList, comTeam.getTeamName(), Collections.emptyList(), comTeamNonPlayedPlayerList);
					readyToPlay=false;
					break;
				}
			}

			if(readyToPlay) {
				GameStateModel gameState = new GameStateModel();
				gameState.setUserTeamName(userTeam.getTeamName());
				gameState.setUserTeamNonPlayedPlayerList(userTeamNonPlayedPlayerList);
				gameState.setUserTeamPlayedPlayerList(Collections.emptyList());
				gameState.setComTeamName(comTeam.getTeamName());
				gameState.setComTeamNonPlayedPlayerList(comTeamNonPlayedPlayerList);
				gameState.setComTeamPlayedPlayerList(Collections.emptyList());	
				
				playGame(gameState);
			}
			
		} catch(TeamAlreadyExistException taex) {
			outputWriter.printMessage(taex.getMessage());
		}
		catch (SoccerDBException ex) {
			ex.printStackTrace();
			LOGGER.log(Level.SEVERE, ex.getMessage());
		}
		
	}

	
	@Override
	public void playGame(GameStateModel gameState) {
		StringBuilder court = new StringBuilder();
		GameStats gameStats = new GameStats();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("Court");
		try {
			Scanner sc = new Scanner(inputStream);
			while (sc.hasNextLine()) {
				court.append((sc.nextLine())+" \n");
			}
			sc.close();
			outputWriter.printMessage(court.toString());
			
			List<PlayerStats> userTeamPlayerStats = new ArrayList<>();
			List<PlayerStats> comTeamPlayerStats = new ArrayList<>();
			Map<TeamDetails, List<PlayerStats>> gameResultMap = new HashMap<>();
			//TODO random selection between user and com
			List<PlayerStats> userPlayers = gameState.getUserTeamNonPlayedPlayerList();
			List<PlayerStats> listOfComPlayers = gameState.getComTeamNonPlayedPlayerList();
			int nonPlayedUsersCount = userPlayers.size();
			for(int j = 1; j<=nonPlayedUsersCount; j++) {
				outputWriter.printMessage("Choose player "+j+":  ");
				Integer size = userPlayers.size();
				printPlayerInformationV1(userPlayers);
				//TODO validate the input
				String userOptionStringForPlayer = inputReader.readInput();
				Integer userOptionNumberForPlayer = SoccerUtils.isValidOptionByUser(userOptionStringForPlayer, size);
				if(userOptionNumberForPlayer == Integer.MIN_VALUE) {
					outputWriter.printMessage("Please select the numeric value from the given options.");
					j--;
					continue;
				}
				if(userOptionNumberForPlayer == 0) {
					subMenuDisplayV1(gameState.getGameId(), gameState.getUserTeamName(),userTeamPlayerStats, userPlayers, gameState.getComTeamName(), comTeamPlayerStats, listOfComPlayers);
					break;
				}

				PlayerStats playerStats = userPlayers.get(userOptionNumberForPlayer-1);
				Player player = playerStats.getPlayer();
				outputWriter.printMessage("Select position of the goal post..");
				int sizeOfGoalScoringPositions = GoalScoringPositions.values().length;
				for(int k =1; k<= sizeOfGoalScoringPositions; k++) {
					outputWriter.printMessage(k+". "+GoalScoringPositions.values()[k-1]);
				}
				String userOptionStringForGoalPosition = inputReader.readInput();
				Integer userOptionNumberForGoalPosition = SoccerUtils.isValidOptionByUser(userOptionStringForGoalPosition, sizeOfGoalScoringPositions);
				while(userOptionNumberForGoalPosition == Integer.MIN_VALUE) {
					outputWriter.printMessage("Please select the numeric value from the given options.");
					userOptionStringForGoalPosition = inputReader.readInput();
					userOptionNumberForGoalPosition = SoccerUtils.isValidOptionByUser(userOptionStringForGoalPosition, sizeOfGoalScoringPositions);
				}
				if(userOptionNumberForGoalPosition == 0) {
					subMenuDisplayV1(gameState.getGameId(), gameState.getUserTeamName(),userTeamPlayerStats, userPlayers, gameState.getComTeamName(), comTeamPlayerStats, listOfComPlayers);
					break;
				}

				String userOptionForGoal = GoalScoringPositions.values()[userOptionNumberForGoalPosition-1].getPosition();
				PlayerFunctionality computerGoalKeeper = PlayerFactory.getPlayer(PlayerType.GOALKEEPER, true);
				PlayerStats userPlayerStats = new PlayerStats();
				userPlayerStats.setPlayer(player);
				if(computerGoalKeeper.save(userOptionForGoal, StringUtils.EMPTY)) {
					userPlayerStats.setScored(true);
					userPlayerStats.setPlayed(true);
					outputWriter.printMessage(player.getName()+ " scored the goal!");
				} else {
					userPlayerStats.setScored(false);
					userPlayerStats.setPlayed(true);
					outputWriter.printMessage(player.getName()+" missed it!");
				}
				userTeamPlayerStats.add(userPlayerStats);
				userPlayers.remove(playerStats);
				
				
				Player computerPlayer = selectComPlayerV1(listOfComPlayers);
				
				PlayerFunctionality computerForward = PlayerFactory.getPlayer(PlayerType.FORWARD, true);
				String compOptionForGoal = computerForward.shoot();
				outputWriter.printMessage("It is computer turn now. Save the goal.");
				outputWriter.printMessage("Select the jump position of GoalKeeper to save the goal..");
				for(int k =1; k<= GoalScoringPositions.values().length; k++) {
					outputWriter.printMessage(k+". "+GoalScoringPositions.values()[k-1]);
				}
				String userOptionStringToSaveGoal = inputReader.readInput();
				Integer userOptionNumberToSaveGoal = SoccerUtils.isValidOptionByUser(userOptionStringToSaveGoal, sizeOfGoalScoringPositions);
				
				while(userOptionNumberToSaveGoal == 0) {

					outputWriter.printMessage("Please finish the move and save the game!");
					userOptionStringToSaveGoal = inputReader.readInput();
					userOptionNumberToSaveGoal = SoccerUtils.isValidOptionByUser(userOptionStringToSaveGoal, sizeOfGoalScoringPositions);
				}
				userOptionNumberToSaveGoal = SoccerUtils.isValidOptionByUser(userOptionStringToSaveGoal, sizeOfGoalScoringPositions);
				while(userOptionNumberToSaveGoal == Integer.MIN_VALUE) {
					outputWriter.printMessage("Please select the numeric value from the given options.");
					userOptionStringToSaveGoal = inputReader.readInput();
					userOptionNumberToSaveGoal = SoccerUtils.isValidOptionByUser(userOptionStringToSaveGoal, sizeOfGoalScoringPositions);
				}
				
				String userOptionToSaveGoal = GoalScoringPositions.values()[userOptionNumberToSaveGoal-1].getPosition();
				PlayerFunctionality userGoalKeeper = PlayerFactory.getPlayer(PlayerType.GOALKEEPER, false);
				PlayerStats compPlayerStats = new PlayerStats();
				compPlayerStats.setPlayer(computerPlayer);
				if(userGoalKeeper.save(userOptionToSaveGoal, compOptionForGoal)) {
					compPlayerStats.setScored(false);
					compPlayerStats.setPlayed(true);
					outputWriter.printMessage("You saved the goal.");
				} else {
					compPlayerStats.setScored(true);
					compPlayerStats.setPlayed(true);
					outputWriter.printMessage("You let opponent to score the goal.");
				}
				comTeamPlayerStats.add(compPlayerStats);
				listOfComPlayers.remove(compPlayerStats);
			}			
			if(userPlayers.size() == 0) {
				userTeamPlayerStats.addAll(gameState.getUserTeamPlayedPlayerList());
				comTeamPlayerStats.addAll(gameState.getComTeamPlayedPlayerList());
				gameResultMap.put(new TeamDetails(gameState.getUserTeamName(), true), userTeamPlayerStats);
				gameResultMap.put(new TeamDetails(gameState.getComTeamName(), false), comTeamPlayerStats);
				gameStats.setGameStats(gameResultMap);
				long userTeamGoals = userTeamPlayerStats.stream().filter(PlayerStats::isScored).count();
				
				long comTeamGoals = comTeamPlayerStats.stream().filter(PlayerStats::isScored).count();
				if(userTeamGoals>comTeamGoals) {
					outputWriter.printMessage("You won the game.");
				} else if(userTeamGoals < comTeamGoals) {
					outputWriter.printMessage("You lost the game.");
				} else {
					outputWriter.printMessage("Draw.");
				}
				outputWriter.printMessage(gameState.getUserTeamName()+"        "+gameState.getComTeamName());;
				outputWriter.printMessage(userTeamGoals+"                 "+comTeamGoals);
				h2Repository.saveGameStats(gameStats);
				
				//TODO delete saved games if there are any.
				if(gameState.getGameId() != null) {
					h2Repository.deleteSavedGame(gameState.getGameId());
				}
			}
			
		} catch (Exception ex) {
			
			//TODO save to log file
			outputWriter.printMessage("Problems occured while playing the game.");
		}
		
	}
	
	private void printPlayerInformation(List<Player> listOfPlayers) {
		for(int i =1; i<=listOfPlayers.size();i++) {
			Player player = listOfPlayers.get(i-1);
			outputWriter.printMessage(i+". Name: "+player.getName()+" Type: "+player.getPlayerType().getPlayerType()+" Skill Points: "+player.getSkillPoints());;
		}
	}
	
	private void printPlayerInformationV1(List<PlayerStats> listOfPlayers) {
		for(int i =1; i<=listOfPlayers.size();i++) {
			Player player = listOfPlayers.get(i-1).getPlayer();
			outputWriter.printMessage(i+". Name: "+player.getName()+" Type: "+player.getPlayerType().getPlayerType()+" Skill Points: "+player.getSkillPoints());;
		}
	}
	
	
	private void subMenuDisplayV1(Integer gameId, String userTeamName, List<PlayerStats> playedUserStats, List<PlayerStats> notPlayedUserStats, String comTeamName, List<PlayerStats> comUserStats, List<PlayerStats> notPlayedComStats) {
		int lengthOfSubMenu = SubMenu.values().length;
		outputWriter.printMessage("Please S to save the game or E to exit the game.");
		for(int i=1; i<=lengthOfSubMenu; i++) {
			if(SubMenu.values()[i-1].name().equalsIgnoreCase("Save")) {
				outputWriter.printMessage(i +". (S) "+SubMenu.values()[i-1]);
			} else {
				outputWriter.printMessage(i +". (E) "+SubMenu.values()[i-1]);
			}
		}
		String userOptionForSubmenu = inputReader.readInput();		
		if(userOptionForSubmenu.equals("S")) {
			outputWriter.printMessage("...Saving Game...");
			try {
				h2Repository.saveGameState(gameId, userTeamName, playedUserStats,notPlayedUserStats, comTeamName, comUserStats,notPlayedComStats);
				outputWriter.printMessage("Game Saved successfully.");
			} catch (SoccerDBException ex) {
				outputWriter.printMessage("Failed to save the game.");
			}
		}else if(userOptionForSubmenu.equals("E")) {
			outputWriter.printMessage("...Exiting Game...");
		} else {
			subMenuDisplayV1(gameId, userTeamName, playedUserStats,notPlayedUserStats, comTeamName, comUserStats,notPlayedComStats);
		}
	}
	
	
	private Player selectComPlayerV1(List<PlayerStats> listOfComPlayers) {
		int size = listOfComPlayers.size();
		Random random = new Random();
		int randomPlayer = random.nextInt(size);
		return listOfComPlayers.get(randomPlayer).getPlayer();
	}

	@Override
	public void loadGame() {
		outputWriter.printMessage("Loading saved games..Please wait.");
		try {
			List<SavedGame> listOfSavedGames = h2Repository.loadSavedGames();
			outputWriter.printMessage("Select game to load.");
			Map<Integer, SavedGame> userOptionToGameIdMap = new HashMap<>();
			for(int i=1 ; i<=listOfSavedGames.size(); i++) {
				SavedGame savedGame = listOfSavedGames.get(i-1);
				userOptionToGameIdMap.put(i, savedGame);
				outputWriter.printMessage(i +". "+savedGame.getUserTeamName() +" vs "+savedGame.getComTeamName());
			}
			
			String userOptionToLoadSavedGame = inputReader.readInput();
			int lengthOfSavedGamesList =listOfSavedGames.size();
			Integer userOption = SoccerUtils.isValidOptionByUser(userOptionToLoadSavedGame, lengthOfSavedGamesList);
			while(userOption == 0) {

				outputWriter.printMessage("Game cannot be saved here.");
				userOptionToLoadSavedGame = inputReader.readInput();
				userOption = SoccerUtils.isValidOptionByUser(userOptionToLoadSavedGame, lengthOfSavedGamesList);
			}
			userOption = SoccerUtils.isValidOptionByUser(userOptionToLoadSavedGame, lengthOfSavedGamesList);
			while(userOption == Integer.MIN_VALUE) {
				outputWriter.printMessage("Please select the numeric value from the given options.");
				userOptionToLoadSavedGame = inputReader.readInput();
				userOption = SoccerUtils.isValidOptionByUser(userOptionToLoadSavedGame, lengthOfSavedGamesList);
			}
			SavedGame savedGame = userOptionToGameIdMap.get(userOption);
			GameStateModel gameState = h2Repository.loadSaveGameState(savedGame.getUserTeamName(), savedGame.getComTeamName(), savedGame.getGameId());
			gameState.setGameId(savedGame.getGameId());
			playGame(gameState);
			
		} catch(SoccerDBException sdbex) {
			outputWriter.printMessage(sdbex.getMessage());
		}
				
	}
}
