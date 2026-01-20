package Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class Test {

	public static void main(String[] args) {
		List<String> names = new ArrayList<String>();
		List<String> years = new ArrayList<String>();
		List<Student> students = new ArrayList<Student>();
		
		names.add("Rumen");
		names.add("Dudu");
		names.add("Dimitar");
		
		years.add("Koleda");
		years.add("Jojo");
		years.add("Lolo");
		years.add("Dimitrinka");
		years.add("Hoho");
		
		int indexOfDudu = Collections.binarySearch(names, "Dimitar");
		
		for (String name: names) {
			System.out.println(name);
		}
		
		System.out.println(indexOfDudu);
		
		Collections.copy(years, names);
		
		for (String year: years) {
			System.out.println(year);
		}
		
		List<Integer> numbers = new ArrayList<Integer>();
		
		numbers.add(2);
		numbers.add(12);
		numbers.add(7);
		numbers.add(19);
		
		for (Integer number: numbers) { // [2, 12, 7, 19]
			System.out.println(number);
		}
		
		int maxNumber = Collections.max(numbers);
		System.out.println(maxNumber);
		
		int minNumber = Collections.min(numbers);
		System.out.println(minNumber);
		
		Collections.rotate(numbers, 3); // [12, 7, 19, 2]
		
		for (Integer number: numbers) {
			System.out.println(number);
		}
		
		Collections.shuffle(years);
		
		for (String year: years) {
			System.out.println(year);
		}
		
		Collections.sort(numbers);
		System.out.println();
		
		Collections.swap(numbers, 1, 2);
		
		for (Integer number: numbers) {
			System.out.println(number);
		}
	}

}
