package com.sateekot.soccer.player.functionality;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sateekot.soccer.utils.GoalScoringPositions;

/**
 * 
 * @author sateekot
 * Component provides the functionality for Computer Goal Keeper.
 */
public class ComputerGoalKeeperFunctionality implements PlayerFunctionality {

	

	@Override
	public boolean save(String userOption, String computerOption) {
		List<String> listOfString = Stream.of(GoalScoringPositions.values()).map(Enum::name).collect(Collectors.toList());
		Random randomGenerator = new Random();
		int randomNumber = randomGenerator.nextInt(listOfString.size());
		String randomOption = listOfString.get(randomNumber);
		if(userOption.equalsIgnoreCase(randomOption)) {
			return true;
		}
		return false;
	}

	@Override
	public void block() {
		// TODO Auto-generated method stub

	}

}
