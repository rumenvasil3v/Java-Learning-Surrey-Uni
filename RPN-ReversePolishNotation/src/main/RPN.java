package main;

import java.util.Stack;

public final class RPN {
	private Stack<Character> operations;
	private Stack<Integer> numbers;
	
	public RPN() {
		
	}
	
	public int calculate(String[] characters) {
		
		for (int i = 0; i < characters.length; i++) {
			if (characters[i] == "+" ||
				characters[i] == "-" ||
				characters[i] == "*" ||
				characters[i] == "^" ||
				characters[i] == "/") {
				this.operations.pusH()
			}
		}
		
		return 0;
	}
}
