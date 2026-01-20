package Characters;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import main.GamePanel;
import main.KeyHandler;

public class SnakeMeal extends Entity {
	private String meal;
	private Random random;
	private boolean isItEaten;
	private Snake snake;

	public SnakeMeal(GamePanel gamePanel, KeyHandler kh, Snake snake) {
		this.gp = gamePanel;
		this.keyH = kh;
		this.random = new Random();
		this.isItEaten = false;
		this.meal = "🍎";
		this.snake = snake;
		this.x = generateRandomNumber(GamePanel.TILE_WIDTH_SIZE);
		this.y = generateRandomNumber(GamePanel.TILE_HEIGHT_SIZE);
	}

	public void setIsItEaten(boolean isItEaten) {
		this.isItEaten = isItEaten;
	}

	public void update() {
		
	}

	public void draw(Graphics2D g2) {
		if (Math.abs(this.snake.x - this.x) <= 10 && Math.abs(this.snake.y - this.y) <= 10) {
			this.snake.extendSnake();
			this.setIsItEaten(true);
			isItEaten = true;
		}

		g2.setColor(Color.red);
		if (isItEaten) {
			this.x = generateRandomNumber(GamePanel.TILE_WIDTH_SIZE);
			this.y = generateRandomNumber(GamePanel.TILE_HEIGHT_SIZE);
			this.isItEaten = false;
			g2.drawString(meal, this.x, this.y);
		} else {
			g2.drawString(meal, this.x, this.y);
		}
	}

	private int generateRandomNumber(int boundary) {
		int someRandomNumber = random.nextInt(0, boundary - 40);

		return someRandomNumber;
	}
}
