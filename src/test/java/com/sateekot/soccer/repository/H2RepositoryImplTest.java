package com.sateekot.soccer.repository;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.sateekot.soccer.exception.SoccerDBException;
import com.sateekot.soccer.exception.TeamAlreadyExistException;
import com.sateekot.soccer.model.GameStateModel;
import com.sateekot.soccer.model.Player;
import com.sateekot.soccer.model.PlayerStats;
import com.sateekot.soccer.model.Team;
import com.sateekot.soccer.utils.PlayerType;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class H2RepositoryImplTest {
	
	private H2RepositoryImpl h2RepositoryImpl;
	
	@Before
	public void init() {
		h2RepositoryImpl = new H2RepositoryImpl();
	}
	
	
	@Test
	public void test1_createMetaData() {
		boolean result = H2RepositoryImpl.createMetaData();
		assertTrue(result);
	}

	@Test
	public void test2_Save() throws SoccerDBException,TeamAlreadyExistException {
		String teamName = "UserTeam";

		List<Player> listOfPlayers = loadUserTeamData();
		Team team = new Team(teamName, listOfPlayers);
		
		boolean result = h2RepositoryImpl.save(team);
		assertTrue(result);
		
	}

	@Test
	public void test3_Load() throws SoccerDBException  {
		Team team = h2RepositoryImpl.loadTeam("UserTeam");
		//team.getPlayersList().stream().forEach(System.out::println);
		assertEquals(6, team.getPlayersList().size());
	}
	
	
	@Ignore
	@Test
	public void test4_Delete() throws SoccerDBException {
		boolean result = h2RepositoryImpl.delete("UserTeam");
		assertTrue(result);
	}
	

	@Test
	public void test5_saveGameState() throws SoccerDBException {
		String userTeamName = "UserTeam";
		String computerTeamName = "PSG";
		List<Player> listOfUserTeamPlayers = loadUserTeamData();
		List<Player> listOfComputerTeamPlayers = loadComputerTeamData(computerTeamName);
		List<PlayerStats> userTeamPlayedPlayerList = new ArrayList<>();
		List<PlayerStats> userTeamNonPlayedPlayerList = new ArrayList<>();
		List<PlayerStats> comTeamPlayedPlayerList = new ArrayList<>();
		List<PlayerStats> comTeamNonPlayedPlayerList = new ArrayList<>();
		listOfUserTeamPlayers.forEach(player -> {
			PlayerStats playerStats = new PlayerStats(player, false, false);
			userTeamNonPlayedPlayerList.add(playerStats);
		});
		
		listOfComputerTeamPlayers.forEach(player -> {
			PlayerStats playerStats = new PlayerStats(player, false, false);
			comTeamNonPlayedPlayerList.add(playerStats);
		});

		
		
		assertTrue(h2RepositoryImpl.saveGameState(0, userTeamName, userTeamNonPlayedPlayerList, userTeamPlayedPlayerList, computerTeamName, comTeamNonPlayedPlayerList, comTeamPlayedPlayerList));
		
	}
	
	@Test
	public void test6_loadSaveGameState() throws SoccerDBException {
		GameStateModel gameState = h2RepositoryImpl.loadSaveGameState("KSK", "PSG", 1);
		assertEquals(0, gameState.getUserTeamPlayedPlayerList().size());
		assertEquals(6, gameState.getUserTeamNonPlayedPlayerList().size());
		assertEquals(0, gameState.getComTeamPlayedPlayerList().size());
		assertEquals(6, gameState.getComTeamNonPlayedPlayerList().size());
		
	}
	
	private List<Player> loadUserTeamData() { 
		List<Player> listOfPlayers = new ArrayList<>();
		Player player1 = new Player("Sateesh", PlayerType.FORWARD, 0);
		Player player2 = new Player("Hareesh", PlayerType.FORWARD, 0);
		Player player3 = new Player("Manuu", PlayerType.MIDFIELDER, 0);
		Player player4 = new Player("Simha", PlayerType.MIDFIELDER, 0);
		Player player5 = new Player("Vasu", PlayerType.DEFENDER, 0);
		Player player6 = new Player("Manoj", PlayerType.GOALKEEPER, 0);
		listOfPlayers.add(player1);listOfPlayers.add(player2);
		listOfPlayers.add(player3);listOfPlayers.add(player4);
		listOfPlayers.add(player5);listOfPlayers.add(player6);
		return listOfPlayers;
	}
	
	private List<Player> loadComputerTeamData(String teamName) throws SoccerDBException { 
		Team team = h2RepositoryImpl.loadTeam(teamName);
		return team.getPlayersList();
	}

}
