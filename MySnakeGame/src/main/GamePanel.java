package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Characters.Snake;
import Characters.SnakeMeal;

public class GamePanel extends JPanel implements Runnable {
	private static final int ORIGINAL_TILE_SIZE = 4;
	private static final int SCALE_ORIGINAL_TILE_SIZE = 3;

	private static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE_ORIGINAL_TILE_SIZE;
	private static final int PANEL_HEIGHT = 20;
	private static final int PANEL_WIDTH = 20;

	public static final int TILE_HEIGHT_SIZE = TILE_SIZE * PANEL_HEIGHT;
	public static final int TILE_WIDTH_SIZE = TILE_SIZE * PANEL_WIDTH;

	private static final int FPS = 60;

	KeyHandler keyH = new KeyHandler();
	Thread gameThread;

	Snake snake = new Snake(this, keyH);
	SnakeMeal snakeMeal = new SnakeMeal(this, keyH, snake);

	private int x;
	private int y;
	public int width;
	public int height;
	public int playerSpeed;
	private int moveCounter;
	private boolean isGameOver = false;

	public GamePanel() {
		this.setPreferredSize(new Dimension(TILE_WIDTH_SIZE, TILE_HEIGHT_SIZE));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.setDefaultValues();
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}

	private void setDefaultValues() {
		this.x = 100;
		this.y = 100;
		this.width = 200;
		this.height = 300;
		this.playerSpeed = 13;
		this.moveCounter = 0;
	}

	public void startGameThread() {
		this.gameThread = new Thread(this);
		this.gameThread.start();
	}

	@Override
	public void run() {
		double drawInterval = 1000000000 / FPS;
		double nextDrawInterval = System.nanoTime() + drawInterval;

		while (this.gameThread != null) {
			
			if (this.moveCounter >= 10) {
				if (!this.isGameOver) {					
					update();		
				}
				
				this.moveCounter = 0;
			}
			
			repaint();
			
			try {
				double remainingTime = nextDrawInterval - System.nanoTime();
				remainingTime = remainingTime / 1000000;

				if (remainingTime < 0) {
					remainingTime = 0;
				}

				Thread.sleep((long) remainingTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			nextDrawInterval += drawInterval;
			this.moveCounter++;
		}
	}
	
	public void resetGame() {
		this.isGameOver = false;
		this.snake.reset();
	}

	public void update() {
//		boolean result = this.snake.update();
		this.snake.update();
//		if (result) {
//			this.isGameOver = true;
//			int response = JOptionPane.showConfirmDialog(this, "Do you want to try again?");
//			
//			if (response == JOptionPane.YES_OPTION) {
//				this.resetGame();
//			} else {
//				System.exit(0);
//			}
//		}
		
		this.snakeMeal.update();
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2D = (Graphics2D) graphics;

		this.snake.draw(graphics2D);
		this.snakeMeal.draw(graphics2D);

		graphics2D.dispose();
	}
}
