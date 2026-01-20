package main;

import javax.swing.JFrame;

public class Main {
	
	public static void main(String[] args) {
		// Existence of time
		
		JFrame window = new JFrame();
		
		// When the user clicks the 'x' button the window closes
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("2D Adventure");
		
		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel);
		
		window.pack();
		
		// The window will be displayed at the center of the screen
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gamePanel.startGameThread();
	}
}
