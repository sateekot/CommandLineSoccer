package com.sateekot.soccer.repository;

import java.util.List;

import com.sateekot.soccer.exception.SoccerDBException;
import com.sateekot.soccer.exception.TeamAlreadyExistException;
import com.sateekot.soccer.model.GameStateModel;
import com.sateekot.soccer.model.GameStats;
import com.sateekot.soccer.model.PlayerStats;
import com.sateekot.soccer.model.SavedGame;
import com.sateekot.soccer.model.Team;

/**
 * 
 * @author sateekot
 * Date: 14-02-2019
 */
public interface H2Repository {

	/**
	 * 
	 * @param teamName
	 * @param playerList
	 * @return true/false
	 */
	boolean save(Team team) throws SoccerDBException,TeamAlreadyExistException;
	
	/**
	 * 
	 * @param teamName
	 * @return Players List
	 */
	Team loadTeam(String teamName) throws SoccerDBException;
	
	/**
	 * 
	 * @param teamName
	 * @return true/false
	 * @throws SoccerDBException
	 */
	boolean delete(String teamName) throws SoccerDBException;
	
	/**
	 * 
	 * @param gameStats
	 * @return true/false
	 * @throws SoccerDBException
	 * API to save game results for reports.
	 */
	boolean saveGameStats(GameStats gameStats) throws SoccerDBException;
	
	//boolean saveGameState(String userTeamName, List<PlayerStats> userTeamStats, List<Player> nonPlayedUsers, String computerTeamName, List<PlayerStats> compTeamStats, List<Player> nonPlayedComputerPlayers) throws SoccerDBException;
	
	/**
	 * 
	 * @param gameId
	 * @param userTeamName
	 * @param userTeamStats
	 * @param nonPlayedUsers
	 * @param computerTeamName
	 * @param compTeamStats
	 * @param nonPlayedComputerPlayers
	 * @return true/false
	 * @throws SoccerDBException
	 * API to save game state.
	 */
	boolean saveGameState(Integer gameId, String userTeamName, List<PlayerStats> userTeamStats, List<PlayerStats> nonPlayedUsers, String computerTeamName, List<PlayerStats> compTeamStats, List<PlayerStats> nonPlayedComputerPlayers) throws SoccerDBException;

	/**
	 * 
	 * @return List of saved games.
	 * @throws SoccerDBException
	 * API to lead saved games.
	 */
	List<SavedGame> loadSavedGames() throws SoccerDBException;
	
	/**
	 * 
	 * @param userTeamName
	 * @param comTeamName
	 * @param gameId
	 * @return Game saved state.
	 * @throws SoccerDBException
	 * API to load existing game with state.
	 */
	GameStateModel loadSaveGameState(String userTeamName, String comTeamName,Integer gameId) throws SoccerDBException;
	
	/**
	 * 
	 * @param gameId
	 * @throws SoccerDBException
	 * API to delete the saved games if they got played.
	 */
	void deleteSavedGame(Integer gameId) throws SoccerDBException;
}