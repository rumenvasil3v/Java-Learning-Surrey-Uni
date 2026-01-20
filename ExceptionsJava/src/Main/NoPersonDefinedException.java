package Main;

public class NoPersonDefinedException extends RuntimeException {
		
	public NoPersonDefinedException() {
		super("No person defined");
	}
	
	public NoPersonDefinedException(String message) {
		super(message);
	}
}
