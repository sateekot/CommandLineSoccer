package com.sateekot.soccer.player.functionality;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sateekot.soccer.utils.GoalScoringPositions;

/**
 * 
 * @author sateekot
 * Computer Forward functionality.
 */
public class ComputerForwardFunctionality implements PlayerFunctionality {

	@Override
	public
	String shoot() {
		List<String> listOfString = Stream.of(GoalScoringPositions.values()).map(Enum::name).collect(Collectors.toList());
		Random randomGenerator = new Random();
		int randomNumber = randomGenerator.nextInt(listOfString.size());
		String randomOption = listOfString.get(randomNumber);
		return randomOption;
	}
}
