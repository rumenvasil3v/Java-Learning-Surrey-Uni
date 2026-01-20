package Main;

import java.util.ArrayList;
import java.util.List;

public class Book {
	private List<Integer> testList;
	
	public Book() {
		this.testList = new ArrayList<Integer>();
	}
	
	public void addItem(int value) {
		Integer item = Integer.valueOf(value);
		
		testList.add(item);
	}
}
