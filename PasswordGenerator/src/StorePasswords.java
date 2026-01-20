import java.util.HashMap;

public class StorePasswords {
	private HashMap<String, String> passwords;
	
	public StorePasswords() {
		this.passwords = new HashMap<String, String>();
	}
	
	public void addPassword(String username, String password) {
		passwords.put(username, password);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (String key: this.passwords.keySet()) {
			sb.append("Username: " + key + "; Password: " + this.passwords.get(key) + "\n");
		}
		
		return sb.toString().trim();
	}
}
