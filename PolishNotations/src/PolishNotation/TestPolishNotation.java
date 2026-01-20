package PolishNotation;

import java.util.Stack;

public class TestPolishNotation {

	public static void main(String[] args) {
		String expression = "(1+2)*3+100"; // 109
		double result = evaluate(expression);
		System.out.println(result);
	}

	static double evaluate(String expression) {
		char[] characters = expression.toCharArray();
		String allowedOperators = "+/*-";
		Stack<Double> numbers = new Stack<Double>();
		Stack<Character> operators = new Stack<Character>();
		
		for (int i = 0; i < characters.length; i++) {
			CharSequence current = new String(String.valueOf(characters[i]));
			
			if (characters[i] == '(') {

				operators.push(characters[i]);

			} else if (characters[i] == ')') {
				
				char operator = operators.pop();
				while (operator != '(') {
					double firstNumber = numbers.pop();
					double secondNumber = numbers.pop();
					double result = applyOperation(operator, firstNumber, secondNumber);
					numbers.push(result);
					
					operator = operators.pop();
				}
			} else if (allowedOperators.contains(current)) {
				
				while (operators.size() > 0 && priority(operators.peek()) > priority(characters[i])) {
					char operator = operators.pop();
					double firstNumber = numbers.pop();
					double secondNumber = numbers.pop();
					double result = applyOperation(operator, secondNumber, firstNumber);
					numbers.push(result);
				}
				
				operators.push(characters[i]);

			} else if (Character.isDigit(characters[i]) || characters[i] == '.') {
				StringBuilder sb = new StringBuilder();

				while (Character.isDigit(characters[i]) || characters[i] == '.') {
					sb.append(characters[i]);
					i++;
					if (i == characters.length) {
						break;
					}
				}

				numbers.push(Double.parseDouble(sb.toString()));
			}
		}

		return numbers.pop();
	}

	public static double applyOperation(char operation, double oparand1, double operand2) {
		double result = 0;

		switch (operation) {
		case '+':
			result = oparand1 + operand2;
			break;
		case '/':
			result = oparand1 / operand2;
			break;
		case '*':
			result = oparand1 * operand2;
			break;
		case '-':
			result = oparand1 - operand2;
			break;
		default:
			break;
		}

		return result;
	}
	
	public static int priority(char operation) {
		switch (operation) {
		case '+':
			return 1;
		case '/':
			return 2;
		case '*':
			return 2;
		case '-':
			return 1;
		default:
			return 0;
		}
	}
}
