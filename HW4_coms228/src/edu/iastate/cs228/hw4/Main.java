package edu.iastate.cs228.hw4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *	 
 * This class is responsible for reading a file, extracting the pattern and binary code,
 * and then using these to reconstruct and decode the original message.
 * 
 * The class uses a MsgTree to facilitate the decoding process.
 * 
 * @author Lucas Sorge
 */
public class Main {
    /**
     * 
     * It prompts the user for a filename, reads the file content, and then decodes the message
     * based on the pattern and binary code found in the file.
     * 
     * @param args The command-line arguments
     * @throws IOException If there is an issue with reading the file
     */
	public static void main(String [] args) throws IOException{
		System.out.println("Please enter filename to decode: ");
		Scanner scanner = new Scanner(System.in);
		String filename = scanner.nextLine();
		scanner.close();
		
		// Read the content of the file and separate the pattern and binary code
		String content = new String(Files.readAllBytes(Paths.get(filename))).trim();
		int pos = content.lastIndexOf('\n');
		String pattern = content.substring(0, pos);
		String binaryCode = content.substring(pos).trim();
		
		// Create a set of unique characters from the pattern
		Set<Character> character = new HashSet<>();
		for(char c : pattern.toCharArray()) {
			if (c !='ˆ') {
				character.add(c);
		}
	}
		// Convert the character set to a string
		String chardict = character.stream().map(String::valueOf).collect(Collectors.joining());
		
		// Construct the tree and decode the message
		MsgTree root = new MsgTree(pattern);
		MsgTree.printCodes(root, chardict);
		root.decode(root, binaryCode);
	}
}