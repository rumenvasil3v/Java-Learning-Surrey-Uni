package main;

import java.awt.Color;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JTextField textField = new JTextField();
		
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("My Snake Game");
		
		Random random = new Random();
		System.out.println(random.nextInt(12));
		
		GamePanel panel = new GamePanel();
		frame.add(panel);
		frame.pack();
		
		frame.setLocationRelativeTo(null); // center of the screen 
		frame.setVisible(true);
		
		panel.startGameThread();
	}
}
// TODO: Adding Score and algorithm for extending the snake