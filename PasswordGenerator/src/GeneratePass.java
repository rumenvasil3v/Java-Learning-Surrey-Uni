import java.util.Random;

public class GeneratePass {
	private int numberOfCharacters;
	
	public GeneratePass(int numberOfCharacters) {
		this.numberOfCharacters = numberOfCharacters;
	}
	
	public int getNumberOfCharacters() {
		return this.numberOfCharacters;
	}
	
	public String generate() {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		
		for (int i = 0; i < this.numberOfCharacters; i++) {
			int randomNumber = random.nextInt(33, 128);
			char character = (char)randomNumber;
			
			sb.append(character);
		}
		
		return sb.toString().trim();
	}
}
