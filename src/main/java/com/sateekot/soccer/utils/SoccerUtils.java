package com.sateekot.soccer.utils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.math.NumberUtils;
import static com.sateekot.soccer.utils.SoccerConstants.computerTeamsList;

/**
 * 
 * @author sateekot
 * Date: 14-02-2019
 * Utility provider for the application.
 */
public class SoccerUtils {

	public static boolean isValidOption(Integer selectedOption, int size) {
		return (selectedOption >= 0 && selectedOption <= size) ? true : false;
	}
	
	public static boolean isNumber(String value) {
		if(NumberUtils.isCreatable(value)) {
			return true;
		}
		return false;
	}
	
	public static Integer isValidOptionByUser(String selectedOption, int size) {
		Integer userOption = null;
		try {
			userOption = Integer.parseInt(selectedOption);
		} catch (NumberFormatException nfe) {
			return userOption.MIN_VALUE;
		}
		
		if(!SoccerUtils.isValidOption(userOption, size)) {
			return userOption.MIN_VALUE;
		}
		return userOption;
	}
	
	public static String comRandomTeam() {
		Random randomGenerator = new Random();
		int randomNumber = randomGenerator.nextInt(computerTeamsList.size());
		String randomTeam = computerTeamsList.get(randomNumber);
		return randomTeam;
	}
}
