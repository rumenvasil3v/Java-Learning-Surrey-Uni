package Main;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Main {
	public static void main(String[] args) {
		Map<String, Integer> people = new HashMap<String, Integer>();
		
		people.put("Rumen", 19);
		people.put("Dudu", 16);
		people.put("Lolo", 10);
		
		System.out.println(people);
		
		for (String name: people.keySet()) {
			System.out.println(name + " is " + people.get(name) + " years old.");
		}
		
		String sample = "the cat sat on the mat and the cat napped";
		String[] words = sample.split(" ");
		
		// Build a frequency map: words mapped to its frequency
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		for (String word: words) {
			Integer frequency = map.get(word);
			
			if (frequency == null) {
				map.put(word, 1);
			} else {
				map.put(word, frequency + 1);
			}
		}
		
		System.out.println();
		System.out.println(map.size() + " distinct words.");
		System.out.println(map);
		
		System.out.println(map.containsKey("cat"));
		System.out.println(map.get("cat"));
		System.out.println(map.remove("cat"));
		System.out.println(map);
		
		Book book = new Book();
		book.addItem(5);
	}
}
