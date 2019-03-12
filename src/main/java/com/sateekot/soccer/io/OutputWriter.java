package com.sateekot.soccer.io;

import java.io.PrintStream;

/**
 * 
 * @author sateekot
 * Date: 13-02-2019
 * OutputWriter provides application message to console.
 */
public class OutputWriter {


	private PrintStream printStream = new PrintStream(System.out);
	
	public void printMessage(String message) {
		printStream.println( message);
	}
	
//	public void printValidationMessage(String message) {
//		printStream.println(ANSI_BRIGHT_RED + message);
//	}
//	
//	public void printSuccessMessage(String message) {
//		printStream.println(ANSI_CYAN + message);
//	}
//	
//	public void printFailMessage(String message) {
//		printStream.println(ANSI_BRIGHT_BLUE + message);
//	}
	
}
