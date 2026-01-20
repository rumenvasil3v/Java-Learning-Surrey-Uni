package Constructors;

public class Test {

	public static void main(String args[]) {
		BankAccount bankAccount1 = new BankAccount();
		BankAccount bankAccount2 = new BankAccount("Rumen Vasilev's");
		
		System.out.println(bankAccount1);
		System.out.println(bankAccount2);
	}
}
