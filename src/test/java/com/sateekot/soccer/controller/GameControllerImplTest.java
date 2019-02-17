package com.sateekot.soccer.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.sateekot.soccer.io.InputReader;
import com.sateekot.soccer.io.OutputWriter;
import com.sateekot.soccer.model.GameStateModel;
import com.sateekot.soccer.model.Player;
import com.sateekot.soccer.model.PlayerStats;
import com.sateekot.soccer.model.Team;
import com.sateekot.soccer.utils.PlayerType;

public class GameControllerImplTest {
	
//	@InjectMocks
	private GameController gameController;
	
	@Mock
	private InputReader inputReader;
	
	@Mock
	private OutputWriter outputWriter;
	
	
	@Before
	public void init() {
		inputReader = mock(InputReader.class);
		//outputWriter = mock(OutputWriter.class);
		outputWriter = new OutputWriter();
		gameController = new GameControllerImpl(inputReader,outputWriter);
		when(inputReader.readInput()).thenReturn("1");
	}

	@Test
	public void testCreateNewGame() {
		//fail("Not yet implemented");
	}

	@Test
	public void testPlayGame() {
		
		Team userTeam = testUserData();
		Team comTeam = testComData();
		List<PlayerStats> userTeamNonPlayedPlayerList = new ArrayList<>();
		List<PlayerStats> comTeamNonPlayedPlayerList = new ArrayList<>();
		userTeam.getPlayersList().forEach(player -> {
			PlayerStats playerStats = new PlayerStats(player, false, false);
			userTeamNonPlayedPlayerList.add(playerStats);
		});
		
		comTeam.getPlayersList().forEach(player -> {
			PlayerStats playerStats = new PlayerStats(player, false, false);
			comTeamNonPlayedPlayerList.add(playerStats);
		});
		GameStateModel gameState = new GameStateModel();
		gameState.setUserTeamName(userTeam.getTeamName());
		gameState.setUserTeamNonPlayedPlayerList(userTeamNonPlayedPlayerList);
		gameState.setUserTeamPlayedPlayerList(Collections.emptyList());
		gameState.setComTeamName(comTeam.getTeamName());
		gameState.setComTeamNonPlayedPlayerList(comTeamNonPlayedPlayerList);
		gameState.setComTeamPlayedPlayerList(Collections.emptyList());
		gameController.playGame(gameState);
	}
	
	private Team testUserData() {
		String teamName ="TestUserTeam";
		Player player1 = new Player("User1", PlayerType.FORWARD, 10);
		Player player2 = new Player("User2", PlayerType.FORWARD, 10);
		Player player3 = new Player("User3", PlayerType.DEFENDER, 10);
		Player player4 = new Player("User4", PlayerType.DEFENDER, 10);
		Player player5 = new Player("User5", PlayerType.MIDFIELDER, 10);
		Player player6 = new Player("User6", PlayerType.GOALKEEPER, 10);
		List<Player> playerList = new ArrayList<>();
		playerList.add(player1);playerList.add(player2);playerList.add(player3);
		playerList.add(player4);playerList.add(player5);playerList.add(player6);
		Team userTeam = new Team(teamName,playerList);
		return userTeam;
	}
	
	private Team testComData() {
		String teamName ="TestComputerTeam";
		Player player1 = new Player("Computer1", PlayerType.FORWARD, 10);
		Player player2 = new Player("Computer2", PlayerType.FORWARD, 10);
		Player player3 = new Player("Computer3", PlayerType.DEFENDER, 10);
		Player player4 = new Player("Computer4", PlayerType.DEFENDER, 10);
		Player player5 = new Player("Computer5", PlayerType.MIDFIELDER, 10);
		Player player6 = new Player("Computer6", PlayerType.GOALKEEPER, 10);
		List<Player> playerList = new ArrayList<>();
		playerList.add(player1);playerList.add(player2);playerList.add(player3);
		playerList.add(player4);playerList.add(player5);playerList.add(player6);
		Team computerTeam = new Team(teamName,playerList);
		return computerTeam;
	}

}
