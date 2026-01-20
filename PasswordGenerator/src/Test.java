
public class Test {
	
	public static void main(String[] args) {
		GeneratePass pass = new GeneratePass(21);
		StorePasswords passwords = new StorePasswords();
		
		passwords.addPassword("Rumen", pass.generate());
		passwords.addPassword("Dudu", pass.generate());
		passwords.addPassword("Koko", pass.generate());
		System.out.println(passwords.toString());
	}
}
