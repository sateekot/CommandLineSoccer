package com.sateekot.soccer.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * 
 * @author sateekot
 * Date 13-02-2019
 * InputReader reads the user options from console to application.
 */
public class InputReader {

	private final Scanner scanner;
	
	public InputReader(InputStream inputStream) {
		this.scanner = new Scanner(inputStream);
	}
	public InputReader(File file) throws FileNotFoundException {
		this.scanner = new Scanner(file);
	}
	public String readInput() {
		return scanner.nextLine();
	}
}
