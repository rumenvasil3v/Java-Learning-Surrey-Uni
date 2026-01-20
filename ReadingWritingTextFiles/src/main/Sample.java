package main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Sample {
	public static void main(String[] args) throws IOException {
		// absolute path
//		File myFile1 = new File("C:\\file.txt");
//		myFile1.createNewFile();
		
		// relative path depending on the current directory of my program
//		File myFile2 = new File("./myFile2.txt");
//		boolean isItCreated = myFile2.createNewFile();
//		
//		System.out.println("Is the file deleted -> " + myFile2.delete());
//		
//		File myThirdFile = new File("../myThirdFile.txt");
//		System.out.println("Is myThirdFile created -> " + myThirdFile.createNewFile());
//		
//		try {			
//			File file4 = new File("../../file4.txt");
//			System.out.println("Is file4 created -> " + file4.createNewFile());
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		} finally {			
//			System.out.println("This line will always execute");
//		}
//		
//		System.out.println("The program hasn't stopped. That is just the stack trace of the IOException.");
//		
//		System.out.println();
//		System.out.println();
//		
//		System.out.println("Metadata about the third created file:");
//		
//		System.out.println();
//		
//		System.out.println("The name of the third file -> " + myThirdFile.getName());
//		System.out.println("The absolute path of the file -> " + myThirdFile.getAbsolutePath());
//		System.out.println("Is the file readable -> " + myThirdFile.canRead());
//		System.out.println("Is the file writeable -> " + myThirdFile.canWrite());
//		System.out.println("Is the file executable -> " + myThirdFile.canExecute());
//		
//		File anotherMyThirdFile = new File("../myThirdFile.txt");
//		boolean isItPossibleToCreateAnotherMyThirdFile = anotherMyThirdFile.createNewFile();
//		System.out.println(isItPossibleToCreateAnotherMyThirdFile);
//		
		try {
			File myFifthFile = new File("./myFifthFile.txt");
			boolean isFifhtFileCreated = myFifthFile.createNewFile();
			
			if (isFifhtFileCreated) {
				System.out.println(myFifthFile.getName() + " created!");
			} else {
				System.out.println(myFifthFile.getName() + " already exists!");
			}
			
			boolean isFifthFileDeleted = myFifthFile.delete();
			boolean isItDeleted = myFifthFile.delete();
			
			if (isItDeleted) {
				System.out.println(myFifthFile.getAbsolutePath() + " that file was deleted successfully!");
			} else {
				System.out.println(myFifthFile.getAbsolutePath() + " that file was not able to be deleted!");
			}
		} catch (IOException e) {
			System.out.println("An IOException occurrs!");
			e.printStackTrace();
		}
		
		System.out.println();
		
		File mySixthFile = new File("./mySixthFile.txt");
		
		try {
			boolean isSixthFileCreated = mySixthFile.createNewFile();
			
			if (isSixthFileCreated) {
				System.out.println("File at path " + mySixthFile.getAbsolutePath() + " is created!");
			} else {
				System.out.println("File at path " + mySixthFile.getAbsolutePath() + " already exists!");
			}
		} catch (IOException e) {
			System.out.println("IOException occurrs!");
		}
		
		boolean isSixthFileDeletedFirstAttempt = mySixthFile.delete();
		
		if (isSixthFileDeletedFirstAttempt) {
			System.out.println("First attempt deletion of file at path " + mySixthFile.getAbsolutePath() + " is deleted!");
		} else {
			System.out.println("First attempt deletion of file at path " + mySixthFile.getAbsolutePath() + " already deleted!");
		}
		
		boolean isSixthFileDeletedSecondAttempt = mySixthFile.delete();
		
		if (isSixthFileDeletedSecondAttempt) {
			System.out.println("Second attempt deletion of file at path " + mySixthFile.getAbsolutePath() + " already deleted!");
		} else {
			System.out.println("Second attempt deletion of file at path " + mySixthFile.getAbsolutePath() + " already deleted!");
		}
		
		
	}
}