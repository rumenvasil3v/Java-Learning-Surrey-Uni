package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadingWritingTextFiles { 
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// Read contents from a text file -> FileReader, BufferedReader, Scanner
		// Write contents to a text file -> FileWriter, BufferedWriter
		File file = new File("../dudu.txt");
		
		// FileReader
		try {			
			file.createNewFile();
		} catch (IOException e) {
			System.out.println("IOException occurred!");
			e.printStackTrace();
		}
		
		try {			
			FileReader myReader = new FileReader("../dudu.txt");
			int character = myReader.read();
			
			while (character != -1) {
				char current = (char)character;
				System.out.println(current);
				
				character = myReader.read();
			}
			
			myReader.close();
			System.out.println("Read the file successfully");
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		} finally {
			System.out.println("Always executes, it does not matter if it throws exception or not!");
		}
		
		// BufferedReader
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("../dudu.txt"));
			String line = bufferedReader.readLine();
			
			// When buffered reader reaches the end of the file and there are no more lines it returns null
			while (line != null) {
				System.out.println(line);
				
				line = bufferedReader.readLine();
			}
			
			bufferedReader.close();
			System.out.println("Read the file successfully!");
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		} finally {
			System.out.println("Read the file using Buffered Reader! This line always executes!");
		}
		
		// Scanner -> reading text files
		try {
			Scanner scanner = new Scanner(new File("../dudu.txt"));
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				System.out.println(line);
			}
			
			scanner.close();
			scanner.nextLine();
		} catch (FileNotFoundException e) {
			System.out.println("File was not found!");
		} catch (IllegalStateException e) {
			System.out.println("Scanner is already closed! You cannot perfom further operations!");
		} finally {
			System.out.println("Read the file using Scanner class! Yay :) !");
		}
		
		// Writing to text files
		
		// FileWriter
		
		try {
			FileWriter writer = new FileWriter("./recipe.txt", false);
			writer.write("This is my first written line. Hello!\n");
			writer.write("This is my second written line. Hello again!\n");
			writer.close();
			System.out.println("Created a file called 'recipe' and wrote two lines in it");
		} catch (IOException e) {
			System.out.println("Something interesting happened! MUAAAAHAHAHAHA");
			e.printStackTrace();
		}
		
		try {
			FileWriter writer = new FileWriter("./recipe.txt", true);
			
			writer.write("is it overwritten?");
			writer.close();
		} catch (IOException e) {
			
		}
		
		try {
			FileWriter writer = new FileWriter("./tasks.txt", false);
			writer.write("This is my first written line. Hello!\n");
			writer.write("This is my second written line. Hello again!\n");
			writer.close();
			System.out.println("Created a file called 'recipe' and wrote two lines in it");
		} catch (IOException e) {
			System.out.println("Something interesting happened! MUAAAAHAHAHAHA");
			e.printStackTrace();
		}
		
		try {
			FileWriter writer = new FileWriter("./tasks.txt", true);
			
			writer.write("is it overwritten?");
			writer.close();
		} catch (IOException e) {
			
		}
		
		// BufferedWriter
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./to-do.txt", false));
			
			bufferedWriter.write("I am writing to that file using BufferedWriter class.\n");
			bufferedWriter.write("That is some random line. Ignore it!");
			
			bufferedWriter.close();
			System.out.println("Wrote into that file successfully!");
		} catch (IOException e) {
			System.out.println("IOException occurred!");
			e.printStackTrace();
		}
		
		List<String> colours = new ArrayList<String>();
		colours.add("Blue");
		
		System.out.println(colours.get(1));
	}
}
