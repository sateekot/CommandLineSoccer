package com.sateekot.soccer.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sateekot.soccer.exception.SoccerDBException;
import com.sateekot.soccer.exception.TeamAlreadyExistException;
import com.sateekot.soccer.model.GameStateModel;
import com.sateekot.soccer.model.GameStats;
import com.sateekot.soccer.model.Player;
import com.sateekot.soccer.model.PlayerStats;
import com.sateekot.soccer.model.SavedGame;
import com.sateekot.soccer.model.Team;
import com.sateekot.soccer.model.TeamDetails;
import com.sateekot.soccer.utils.PlayerType;

/**
 * 
 * @author sateekot
 * This component provides persistence layer apis to save, load, update and delete data from database.
 *
 */
public class H2RepositoryImpl implements H2Repository {
	
	private static final Logger LOGGER = Logger.getLogger(H2RepositoryImpl.class.getName());
	
	public static boolean createMetaData() {
		String dropQuery = "DROP ALL OBJECTS";
		String playerTypeSQL = "CREATE TABLE IF NOT EXISTS PLAYER_TYPE (ID INTEGER NOT NULL AUTO_INCREMENT, "
				+ "TYPE VARCHAR(20), "
				+ "PRIMARY KEY (ID))";
		String createTeamSQL = "CREATE TABLE IF NOT EXISTS TEAM_DETAILS (TEAM_ID INTEGER PRIMARY KEY AUTO_INCREMENT, "
				+ "TEAM_NAME VARCHAR(40), "
				+ "UNIQUE KEY UNIQUE_TEAM_NAME (TEAM_NAME) )";
		String createPlayerSQL = "CREATE TABLE IF NOT EXISTS PLAYER (PLAYER_ID INTEGER PRIMARY KEY AUTO_INCREMENT, "
				+ "PLAYER_NAME VARCHAR(255), "
				+ "PLAYER_TYPE INTEGER, "
				+ "PLAYER_POINTS INTEGER, "
				+ "TEAM_ID INTEGER, "
				+ "FOREIGN KEY (PLAYER_TYPE) REFERENCES PLAYER_TYPE(ID),"
				+ "FOREIGN KEY (TEAM_ID) REFERENCES TEAM_DETAILS(TEAM_ID))";
		
		String createGameStatsSQL = "CREATE TABLE IF NOT EXISTS GAME_STATS (GAME_ID INTEGER PRIMARY KEY AUTO_INCREMENT, "
				+ "USER_TEAM VARCHAR(40), "
				+ "COMPUTER_TEAM VARCHAR(40), "
				+ "USER_TEAM_GOALS INTEGER, "
				+ "COMPUTER_TEAM_GOALS INTEGER, "
				+ "MATCH_DATE TIMESTAMP AS CURRENT_TIMESTAMP, "
				+ "WINNER VARCHAR(40), "
				+ "PRIMARY KEY(GAME_ID))";
		
		String createSavedGamesSQL = "CREATE TABLE IF NOT EXISTS SAVED_GAMES ( GAME_ID INTEGER PRIMARY KEY AUTO_INCREMENT, "
				+ "USER_TEAM_ID INTEGER, "
				+ "COMPUTER_TEAM_ID INTEGER, "
				+ "FOREIGN KEY (USER_TEAM_ID) REFERENCES TEAM_DETAILS(TEAM_ID), "
				+ "FOREIGN KEY (COMPUTER_TEAM_ID) REFERENCES TEAM_DETAILS(TEAM_ID))";
		
		String createSavedGamesPlayerStates = "CREATE TABLE IF NOT EXISTS SAVED_GAME_PLAYER_STATES (ID INTEGER PRIMARY KEY AUTO_INCREMENT, "
				+ "PLAYER_ID INTEGER, "
				+ "IS_PLAYER_PLAYED INTEGER, "
				+ "IS_PLAYER_SCORED INTEGER, "
				+ "IS_COMPUTER_PLAYER INTEGER, "
				+ "GAME_ID INTEGER, "
				+ "FOREIGN KEY (PLAYER_ID) REFERENCES PLAYER(PLAYER_ID),"
				+ "FOREIGN KEY (GAME_ID) REFERENCES SAVED_GAMES(GAME_ID))";
		
		String insertPlayerTypeSQL = "INSERT INTO PLAYER_TYPE (TYPE) VALUES ('Forward');"
				+ "INSERT INTO PLAYER_TYPE (TYPE) VALUES ('GoalKeeper');"
				+ "INSERT INTO PLAYER_TYPE (TYPE) VALUES ('Midfielder');"
				+ "INSERT INTO PLAYER_TYPE (TYPE) VALUES ('Defender');";
		

		Connection connection = null;
		try {
			connection = H2ConnectionFactory.getConnection();
			Statement statement = connection.createStatement();
			statement.execute(dropQuery);
			statement.execute(playerTypeSQL);
			statement.execute(createTeamSQL);
			statement.execute(createPlayerSQL);
			statement.execute(insertPlayerTypeSQL);
			statement.execute(createGameStatsSQL);
			statement.execute(createSavedGamesSQL);
			statement.execute(createSavedGamesPlayerStates);
			
		} catch (SQLException sqlEx) {
			LOGGER.log(Level.SEVERE, "Exception occured while creating tables. Exception Details = "+sqlEx);
			return false;
		}
		finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException ex) {
					LOGGER.log(Level.SEVERE, "Exception occured while closing database connetion. Exception Details :: "+ex.getMessage());
				}
			}
		}
		if(!createComputerTeams()) {
			return false;
		}
		LOGGER.log(Level.INFO, "MetaData got created successfully.");
		return true;
	}
	
	public static boolean checkDBConnection() {
		String selectQuery = "SELECT * FROM PLAYER_TYPE";
		Connection connection = null;
		try {
			connection = H2ConnectionFactory.getConnection();
			Statement statement = connection.createStatement();
			return statement.execute(selectQuery);
		} catch (SQLException ex) {
			//TODO handle it properly.
			return false;
		}
	}

	@Override
	public boolean save(Team team) throws SoccerDBException, TeamAlreadyExistException{
		Connection connection = null;
		try {
			connection = H2ConnectionFactory.getConnection();
			//LOGGER.log(Level.INFO, "Inserting team name and players to database.");
			Statement statement = connection.createStatement();
			String insertTeamSQLStmt = "INSERT INTO TEAM_DETAILS (TEAM_NAME) VALUES ('"+team.getTeamName()+"')";
			statement.executeUpdate(insertTeamSQLStmt);
			
			String selectQueryForTeamId = "SELECT TEAM_ID FROM TEAM_DETAILS WHERE TEAM_NAME = '"+team.getTeamName()+"'";
			ResultSet result = statement.executeQuery(selectQueryForTeamId);
			Integer teamId = null;
			while(result.next()) {
				teamId = result.getInt(1);
			}
	        for(Player player : team.getPlayersList()) {
	        	Integer playerType = player.getPlayerType().ordinal()+1;
	        	String insertQuery = "INSERT INTO PLAYER (PLAYER_NAME,PLAYER_TYPE,PLAYER_POINTS,TEAM_ID) VALUES('"+player.getName()+"',"+playerType+","+player.getSkillPoints()+","+teamId+")";
	        	statement.executeUpdate(insertQuery);
	        } 
		} catch(SQLException ex) {
			if(ex.getMessage().contains("UNIQUE_TEAM_NAME")) {
				throw new TeamAlreadyExistException("Team already exists in database with the same name. Please select another name.");
			}
			throw new SoccerDBException("Exception occured while saving team details to databse.", ex);
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException ex) {
					throw new SoccerDBException("Exception occured while closing the database connection.", ex);
				}
			}
		}
		return true;
		
	}

	@Override
	public Team loadTeam(String teamName) throws SoccerDBException {
		Connection connection = null;
		List<Player> listOfPlayers = null;
		try {
			connection = H2ConnectionFactory.getConnection();
			
	        Statement stmt = connection.createStatement(); 
	        String sql =  "SELECT P.PLAYER_NAME,P.PLAYER_TYPE,PLAYER_POINTS FROM Player P INNER JOIN TEAM_DETAILS TD ON P.TEAM_ID = TD.TEAM_ID WHERE TD.TEAM_NAME = '"+teamName+"'";  
	        ResultSet result = stmt.executeQuery(sql);
	        listOfPlayers = new ArrayList<>();
	        while(result.next()) {
	        	Player player = new Player();
	        	player.setName(result.getString(1));
	        	player.setPlayerType(PlayerType.values()[result.getInt(2)-1]);
	        	player.setSkillPoints(result.getInt(3));
	        	listOfPlayers.add(player);
	        }
		} catch(SQLException ex) {
			throw new SoccerDBException("Exception occured while loading team details from databse.", ex);
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException ex) {
					throw new SoccerDBException("Exception occured while closing the database connection.", ex);
				}
			}
		}
		Team team = new Team(teamName, listOfPlayers);
		return team;
	}
	
	private static boolean createComputerTeams() {
		
		try {
		
			H2Repository h2Repository = new H2RepositoryImpl();
			String psgTeamName = "PSG";
			List<Player> psgPlayersList = new ArrayList<>();
			Player neymar = new Player("Neymar",PlayerType.FORWARD,60);
			Player cavani = new Player("Cavani",PlayerType.FORWARD,40);
			Player mbappe = new Player("Mbappe",PlayerType.MIDFIELDER,50);
			Player diMaria = new Player("Di Maria",PlayerType.MIDFIELDER,40);
			Player silva = new Player("Silva",PlayerType.DEFENDER,40);
			Player buffon = new Player("Buffon",PlayerType.GOALKEEPER,50);
			
			psgPlayersList.add(neymar);psgPlayersList.add(cavani);psgPlayersList.add(mbappe);
			psgPlayersList.add(diMaria);psgPlayersList.add(silva);psgPlayersList.add(buffon);
			
			Team psgTeam = new Team(psgTeamName,psgPlayersList);
			h2Repository.save(psgTeam);
			
			String bayernteamName = "Bayern Munich";
			List<Player> munichPlayersList = new ArrayList<>();
			Player muller = new Player("Muller",PlayerType.FORWARD,70);
			Player robert = new Player("Robert",PlayerType.FORWARD,50);
			Player robben = new Player("Robben",PlayerType.MIDFIELDER,60);
			Player kummich = new Player("Kimmich",PlayerType.MIDFIELDER,50);
			Player nikals = new Player("Nikals",PlayerType.DEFENDER,50);
			Player neuer = new Player("Neuer",PlayerType.GOALKEEPER,60);
			
			munichPlayersList.add(muller);munichPlayersList.add(robert);munichPlayersList.add(robben);
			munichPlayersList.add(kummich);munichPlayersList.add(nikals);munichPlayersList.add(neuer);
			
			Team bayernMunichTeam = new Team(bayernteamName,munichPlayersList);
			h2Repository.save(bayernMunichTeam);
			
			String barcaTeamName = "Barcelona";
			List<Player> barcaPlayersList = new ArrayList<>();
			Player messi = new Player("Messi",PlayerType.FORWARD,70);
			Player coutinho = new Player("Coutinho",PlayerType.FORWARD,50);
			Player rakitic = new Player("Rakitic",PlayerType.MIDFIELDER,60);
			Player alba = new Player("Alba",PlayerType.MIDFIELDER,50);
			Player pique = new Player("Pique",PlayerType.DEFENDER,50);
			Player ter = new Player("Ter Stegen",PlayerType.GOALKEEPER,60);
			
			barcaPlayersList.add(messi);barcaPlayersList.add(coutinho);barcaPlayersList.add(rakitic);
			barcaPlayersList.add(alba);barcaPlayersList.add(pique);barcaPlayersList.add(ter);
			
			Team barcaTeam = new Team(barcaTeamName,barcaPlayersList);
			h2Repository.save(barcaTeam);
		} catch(TeamAlreadyExistException taex) {
			LOGGER.log(Level.SEVERE, "Exception occured while creating game meta data. Exception Details :: "+taex);
			return false;
		}
		catch(SoccerDBException ex) {
			LOGGER.log(Level.SEVERE, "Exception occured while creating game meta data. Exception Details :: "+ex);
			return false;
		}
		
		return true;
	}

	@Override
	public boolean delete(String teamName) throws SoccerDBException {
		Connection connection = null;
		try {
			connection = H2ConnectionFactory.getConnection();
			Statement statement = connection.createStatement();
			String selectQueryForTeamId = "SELECT TEAM_ID FROM TEAM_DETAILS WHERE TEAM_NAME = '"+teamName+"'";
			ResultSet result = statement.executeQuery(selectQueryForTeamId);
			Integer teamId = null;
			while(result.next()) {
				teamId = result.getInt(1);
			}
			
			String deleteStmtForPlayers = "DELETE FROM PLAYER WHERE TEAM_ID = "+teamId;
			String deleteStmtForTeam = "DELETE FROM TEAM_DETAILS WHERE TEAM_ID = "+teamId;
			
			statement.executeUpdate(deleteStmtForPlayers);
			statement.executeUpdate(deleteStmtForTeam);
			
		} catch(SQLException ex) {
			throw new SoccerDBException("Exception occured while saving team details to databse.", ex);
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException ex) {
					throw new SoccerDBException("Exception occured while closing the database connection.", ex);
				}
			}
		}
		return true;
	}

	@Override
	public boolean saveGameStats(GameStats gameStats) throws SoccerDBException {
		
		Map<TeamDetails, List<PlayerStats>> result = gameStats.getGameStats();
		String userTeamName,compTeamName;
		userTeamName = compTeamName = null;
		Long userTeamGoals,compTeamGoals;
		userTeamGoals = compTeamGoals = null;
		for(Map.Entry<TeamDetails, List<PlayerStats>> entry : result.entrySet()) {
			TeamDetails teamDetails= entry.getKey();
			List<PlayerStats> teamStats = entry.getValue();
			if(teamDetails.isUserTeam()) {
				userTeamName = teamDetails.getTeamName();
				userTeamGoals = teamStats.stream().filter(PlayerStats::isScored).count();
				
			} else {
				compTeamName = teamDetails.getTeamName();
				compTeamGoals = teamStats.stream().filter(PlayerStats::isScored).count();
			}
		}
		String winner = (userTeamGoals > compTeamGoals) ? userTeamName : (userTeamGoals < compTeamGoals) ? compTeamName : "Draw";
		
		String insertQuery = "INSERT INTO GAME_STATS (USER_TEAM, COMPUTER_TEAM, "
				+ "USER_TEAM_GOALS, COMPUTER_TEAM_GOALS, "
				+ "WINNER) VALUES ('"+userTeamName+"','"+compTeamName+"',"+userTeamGoals+","+compTeamGoals+",'"+winner+"')";
		
		Connection connection = null;
		try {
			connection = H2ConnectionFactory.getConnection();
			Statement statement = connection.createStatement();
			statement.executeUpdate(insertQuery);
			
		} catch(SQLException ex) {
			throw new SoccerDBException("Exception occured while saving team details to databse.", ex);
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException ex) {
					throw new SoccerDBException("Exception occured while closing the database connection.", ex);
				}
			}
		}

		return true;
	}

	@Override
	public boolean saveGameState(Integer gameId, String userTeamName, List<PlayerStats> userTeamStats, List<PlayerStats> nonPlayedUsers, String computerTeamName, List<PlayerStats> compTeamStats, List<PlayerStats> nonPlayedComputerPlayers) throws SoccerDBException {
				
		
		Connection connection = null;
		try {
			connection = H2ConnectionFactory.getConnection();
			Statement statement = connection.createStatement();
			
			String deleteQueryForSavedGamePlayerStates = "DELETE FROM SAVED_GAME_PLAYER_STATES WHERE GAME_ID = "+gameId;
			String deleteQueryForSavedGames = "DELETE FROM SAVED_GAMES WHERE GAME_ID = "+gameId;
			statement.executeUpdate(deleteQueryForSavedGamePlayerStates);
			statement.executeUpdate(deleteQueryForSavedGames);
			
			
			String selectQueryForTeamIDs = "SELECT TEAM_ID,TEAM_NAME FROM TEAM_DETAILS WHERE TEAM_NAME IN ('"+userTeamName+"','"+computerTeamName+"')";
			ResultSet result = statement.executeQuery(selectQueryForTeamIDs);
			Integer userTeamId, compTeamId;
			userTeamId = compTeamId = null;
			while(result.next()) {
				if(userTeamName.equalsIgnoreCase(result.getString(2))) {
					userTeamId = result.getInt(1);
				} else {
					compTeamId = result.getInt(1);
				}
				
			}
			if(userTeamId != null && compTeamId != null) {
				String sqlInsertForSavedGames = "INSERT INTO SAVED_GAMES (USER_TEAM_ID, COMPUTER_TEAM_ID) VALUES ("+userTeamId+","+compTeamId+")";
				statement.executeUpdate(sqlInsertForSavedGames);
			}
			
			String selectQueryForSavedGameId = "SELECT GAME_ID FROM SAVED_GAMES WHERE USER_TEAM_ID = "+userTeamId+" AND COMPUTER_TEAM_ID="+compTeamId;
			result = statement.executeQuery(selectQueryForSavedGameId);
			Integer savedGameId = null;
			while(result.next()) {
				savedGameId = result.getInt(1);
			}
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO SAVED_GAME_PLAYER_STATES (PLAYER_ID,IS_PLAYER_PLAYED,IS_PLAYER_SCORED,IS_COMPUTER_PLAYER,GAME_ID) "
					+ "VALUES (SELECT PLAYER_ID FROM PLAYER WHERE PLAYER_NAME = ? AND TEAM_ID = ?,?,?,?,?)");
			
			for(PlayerStats playerStats : userTeamStats) {
				Integer isPlayerPlayed = (playerStats.isPlayed() == true) ? 1 : 0;
				Integer isPlayerScored = (playerStats.isScored() == true) ? 1 : 0;
				Integer isComputerPlayer = 0;
				preparedStatement.setString(1, playerStats.getPlayer().getName());
				preparedStatement.setInt(2, userTeamId);
				preparedStatement.setInt(3, isPlayerPlayed);
				preparedStatement.setInt(4, isPlayerScored);
				preparedStatement.setInt(5, isComputerPlayer);
				preparedStatement.setInt(6, savedGameId);
				preparedStatement.addBatch();
			}
			for(PlayerStats playerStats : nonPlayedUsers) {
				Integer isPlayerPlayed = 0;
				Integer isPlayerScored = 0;
				Integer isComputerPlayer = 0;
				preparedStatement.setString(1, playerStats.getPlayer().getName());
				preparedStatement.setInt(2, userTeamId);
				preparedStatement.setInt(3, isPlayerPlayed);
				preparedStatement.setInt(4, isPlayerScored);
				preparedStatement.setInt(5, isComputerPlayer);
				preparedStatement.setInt(6, savedGameId);
				preparedStatement.addBatch();
			}
			for(PlayerStats playerStats : compTeamStats) {
				Integer isPlayerPlayed = (playerStats.isPlayed() == true) ? 1 : 0;
				Integer isPlayerScored = (playerStats.isScored() == true) ? 1 : 0;
				Integer isComputerPlayer = 1;
				preparedStatement.setString(1, playerStats.getPlayer().getName());
				preparedStatement.setInt(2, compTeamId);
				preparedStatement.setInt(3, isPlayerPlayed);
				preparedStatement.setInt(4, isPlayerScored);
				preparedStatement.setInt(5, isComputerPlayer);
				preparedStatement.setInt(6, savedGameId);
				preparedStatement.addBatch();
			}
			for(PlayerStats playerStats : nonPlayedComputerPlayers) {
				Integer isPlayerPlayed = 0;
				Integer isPlayerScored = 0;
				Integer isComputerPlayer = 1;
				preparedStatement.setString(1, playerStats.getPlayer().getName());
				preparedStatement.setInt(2, compTeamId);
				preparedStatement.setInt(3, isPlayerPlayed);
				preparedStatement.setInt(4, isPlayerScored);
				preparedStatement.setInt(5, isComputerPlayer);
				preparedStatement.setInt(6, savedGameId);
				preparedStatement.addBatch();
			}
		
			preparedStatement.executeBatch();
			return true;
		} catch(SQLException ex) {
			ex.printStackTrace();
			throw new SoccerDBException("Exception occured while saving team details to databse.", ex);
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException ex) {
					throw new SoccerDBException("Exception occured while closing the database connection.", ex);
				}
			}
		}
	}

	
	@Override
	public List<SavedGame> loadSavedGames() throws SoccerDBException {
		
		List<SavedGame> listOfSavedGames = new ArrayList<>();
		Connection connection = null;
		try {
			connection = H2ConnectionFactory.getConnection();
			String seletQueryForSavedGames = "SELECT SG.GAME_ID, SG.USER_TEAM_ID, SG.COMPUTER_TEAM_ID,TD1.TEAM_NAME AS USER_TEAM,TD2.TEAM_NAME AS COM_TEAM FROM SAVED_GAMES SG INNER JOIN TEAM_DETAILS TD1 ON SG.USER_TEAM_ID = TD1.TEAM_ID \n" + 
					"INNER JOIN TEAM_DETAILS TD2 ON SG.COMPUTER_TEAM_ID = TD2.TEAM_ID";
			Statement statement = connection.createStatement();
			ResultSet savedGamesResult = statement.executeQuery(seletQueryForSavedGames);
			while(savedGamesResult.next()) {
				SavedGame savedGame = new SavedGame(savedGamesResult.getInt(1), savedGamesResult.getInt(2), savedGamesResult.getInt(3), savedGamesResult.getString(4), savedGamesResult.getString(5));
				listOfSavedGames.add(savedGame);
			}
		} catch(SQLException ex) {
			throw new SoccerDBException("Failed to load saved games.");
		} finally {
			H2ConnectionFactory.closeConnection(connection);
		}
		return listOfSavedGames;
	}

	@Override
	public GameStateModel loadSaveGameState(String userTeamName, String comTeamName, Integer gameId) throws SoccerDBException {
		
		GameStateModel gameState = new GameStateModel();
		Connection connection = null;
		try {
			connection = H2ConnectionFactory.getConnection();
			String seletQueryForSavedGames = "SELECT SGPS.ID, SGPS.PLAYER_ID, P.PLAYER_NAME, P.PLAYER_TYPE, P.PLAYER_POINTS, SGPS.IS_PLAYER_PLAYED, SGPS.IS_PLAYER_SCORED,SGPS.IS_COMPUTER_PLAYER FROM SAVED_GAME_PLAYER_STATES SGPS INNER JOIN PLAYER P ON SGPS.PLAYER_ID = P.PLAYER_ID WHERE GAME_ID = "+gameId;
			Statement statement = connection.createStatement();
			ResultSet savedGamesResult = statement.executeQuery(seletQueryForSavedGames);
			List<PlayerStats> userTeamPlayedPlayerList = new ArrayList<>();
			List<PlayerStats> userTeamNonPlayedPlayerList = new ArrayList<>();
			List<PlayerStats> comTeamPlayedPlayerList = new ArrayList<>();
			List<PlayerStats> comTeamNonPlayedPlayerList = new ArrayList<>();
			while(savedGamesResult.next()) {
				Integer isComputerPlayer = savedGamesResult.getInt("IS_COMPUTER_PLAYER");
				Integer isPlayerPlayed = savedGamesResult.getInt("IS_PLAYER_PLAYED");
				//Integer playerId = savedGamesResult.getInt("PLAYER_ID");
				String playerName = savedGamesResult.getString("PLAYER_NAME");
				Integer playerType = savedGamesResult.getInt("PLAYER_TYPE");
				Integer isPlayerScored = savedGamesResult.getInt("IS_PLAYER_SCORED");
				Integer playerPoints = savedGamesResult.getInt("PLAYER_POINTS");
				boolean result = (isPlayerScored == 1) ? true : false;
				PlayerStats playerStats = null;
				Player player = new Player(playerName, PlayerType.values()[playerType-1], playerPoints);
				if(isComputerPlayer == 1) {
					if(isPlayerPlayed == 1) {
						playerStats = new PlayerStats(player, result, true);
						comTeamPlayedPlayerList.add(playerStats);
					} else {
						playerStats = new PlayerStats(player, result, false);
						comTeamNonPlayedPlayerList.add(playerStats);
					}
					
				} else {
					if(isPlayerPlayed == 1) {
						playerStats = new PlayerStats(player, result, true);
						userTeamPlayedPlayerList.add(playerStats);
					} else {
						playerStats = new PlayerStats(player, result, false);
						userTeamNonPlayedPlayerList.add(playerStats);
					}
				}
			}
			
			gameState.setUserTeamName(userTeamName);
			gameState.setUserTeamPlayedPlayerList(userTeamPlayedPlayerList);
			gameState.setUserTeamNonPlayedPlayerList(userTeamNonPlayedPlayerList);
			gameState.setComTeamName(comTeamName);
			gameState.setComTeamPlayedPlayerList(comTeamPlayedPlayerList);
			gameState.setComTeamNonPlayedPlayerList(comTeamNonPlayedPlayerList);
			return gameState;
		} catch(SQLException ex) {
			throw new SoccerDBException("Failed to load saved game player state.");
		} finally {
			H2ConnectionFactory.closeConnection(connection);
		}
	}

	@Override
	public void deleteSavedGame(Integer gameId) throws SoccerDBException {
		Connection connection = null;
		try {
			connection = H2ConnectionFactory.getConnection();
			String deleteQueryForSavedGamePlayerStates = "DELETE FROM SAVED_GAME_PLAYER_STATES WHERE GAME_ID = "+gameId;
			String deleteQueryForSavedGames = "DELETE FROM SAVED_GAMES WHERE GAME_ID = "+gameId;
			Statement statement = connection.createStatement();
			statement.executeUpdate(deleteQueryForSavedGamePlayerStates);
			statement.executeUpdate(deleteQueryForSavedGames);
			

		} catch(SQLException ex) {
			throw new SoccerDBException("Failed to delete the saved game.");
		} finally {
			H2ConnectionFactory.closeConnection(connection);
		}
		
	}
	
}
