package com.sateekot.soccer.player.functionality;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.sateekot.soccer.controller.PlayerFactory;
import com.sateekot.soccer.utils.PlayerType;

public class UserGoalKeeperFunctionalityTest {

	PlayerFunctionality playerFunctionality;
	@Before
	public void init() {
		playerFunctionality = PlayerFactory.getPlayer(PlayerType.GOALKEEPER, false);
	}
	@Test
	public void test() {
		String userOption = "left";
		String comOption = "left";
		assertTrue(playerFunctionality.save(userOption, comOption));
	}

	@Test
	public void test_fail() {
		String userOption = "left";
		String comOption = "center";
		assertFalse(playerFunctionality.save(userOption, comOption));
	}
}
