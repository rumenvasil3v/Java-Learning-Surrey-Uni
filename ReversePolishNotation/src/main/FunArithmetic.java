package main;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class FunArithmetic {

	public FunArithmetic() {

	}

	public int runCalculation(String expression) {
		String replaces = expression.replace('c', '-').replace('e', '+').replace('i', '*');
		String[] splitReplaces = replaces.split("[-+*]");

		Queue<String> numbers = new LinkedList<String>();
		for (int i = 0; i < splitReplaces.length; i++) {
			numbers.add(splitReplaces[i]);
		}

		Queue<Character> operators = new LinkedList<Character>();
		char[] characters = replaces.toCharArray();

		for (int i = 0; i < characters.length; i++) {
			if (characters[i] == '+' || characters[i] == '-' || characters[i] == '*') {
				operators.add(characters[i]);
			}
		}

		int result = this.calculate(numbers, operators);

		return result;
	}

	public int calculate(Queue<String> numbers, Queue<Character> operators) {
		int result = 0;
		int numberListSize = numbers.size();

		int firstNumber = Integer.parseInt(numbers.remove());
		int secondNumber = Integer.parseInt(numbers.remove());
		int thirdNumber = 1;
		char operation = operators.remove();
		char secondOperation = '\u0000';

		if (operators.element() == '*' && operation == '+') {
			secondOperation = operation;
			operation = operators.remove();
			thirdNumber = Integer.parseInt(numbers.remove());
		}

		switch (operation) {
		case '+':
			result = firstNumber + secondNumber;
			break;
		case '*':
			if (secondOperation != '\u0000') {
				result = secondNumber * thirdNumber;
				result += firstNumber;
			} else {
				result = firstNumber * secondNumber;
			}
			break;
		case '-':
			result = firstNumber - secondNumber;
			break;
		default:
			break;
		}

		while (numbers.size() > 0) {
			secondOperation = '\u0000';
			thirdNumber = 1;

			int currentNumber = Integer.parseInt(numbers.remove());
			char currentOperation = operators.remove();

			if (operators.size() == 0) {
				switch (currentOperation) {
				case '+':
					result += currentNumber;
					break;
				case '*':
					if (secondOperation != '\u0000') {
						result = result * thirdNumber;
						result += currentNumber;
					} else {
						result = result * currentNumber;
					}
					break;
				case '-':
					result -= currentNumber;
				}
			} else {
				if (operators.element() == '*' && currentOperation == '+') {
					secondOperation = currentOperation;
					currentNumber = '*';
					thirdNumber = Integer.parseInt(numbers.remove());
				}

				switch (currentOperation) {
				case '+':
					result += currentNumber;
					break;
				case '*':
					if (secondOperation != '\u0000') {
						result = result * thirdNumber;
						result += currentNumber;
					} else {
						result = result * currentNumber;
					}
					break;
				case '-':
					result -= currentNumber;
				}
			}
		}

		return result;
	}
}
