package Main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Person {
	private String firstName;
	private String lastName;

	public Person(String firstName, String lastName) {
		firstName = this.firstName;
		this.lastName = lastName;
	}

	public int calculateNumberOfLines(String fileName) {
		int count = 0;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));

			while ((reader.readLine()) != null) {
				count++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}				
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		return count;
	}
}
