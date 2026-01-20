package Main;

public class StartPoint {

	public static void main(String[] args) {
		int firstNumber = 66528;
		int secondNumber = 52920;
		int remainder = firstNumber % secondNumber;
		int result = 0;

		while (remainder != 0) {
			firstNumber = secondNumber * ((int) (firstNumber / secondNumber)) + (remainder);

			firstNumber = secondNumber;
			secondNumber = remainder;
			result = remainder;

			remainder = firstNumber % secondNumber;
		}

		System.out.println(result);
	}

}
