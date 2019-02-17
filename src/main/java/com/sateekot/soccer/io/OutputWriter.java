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
		printStream.println((char)27 + "[30m" +message);
	}
	
	public void printValidationMessage(String message) {
		printStream.println((char)27 + "[91m" + message);
	}
	
	public void printSuccessMessage(String message) {
		printStream.println((char)27 + "[36m" + message);
	}
	
	public void printFailMessage(String message) {
		printStream.println((char)27 + "[94m" + message);
	}
	
}
