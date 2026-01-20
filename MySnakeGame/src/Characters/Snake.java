package Characters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import Utilities.Position;
import main.GamePanel;
import main.KeyHandler;

public class Snake extends Entity {
	public LinkedList<Position> body;
	private boolean shouldGrow;
	private String direction;

	public Snake(GamePanel gp, KeyHandler keyH) {
		this.keyH = keyH;
		this.gp = gp;
		this.setDefaultSnakeProperties();
	}

	private void setDefaultSnakeProperties() {
		this.x = 10;
		this.y = 10;
		this.direction = "";

		this.keyH.upPressed = false;
		this.keyH.downPressed = false;
		this.keyH.leftPressed = false;
		this.keyH.rightPressed = false;

		if (this.body != null) {
			this.body.clear();
		} else {
			this.body = new LinkedList<Position>();
		}

		this.body.addFirst(new Position(this.x, this.y));
		this.shouldGrow = false;
	}

	public void update() {
		if (keyH.upPressed == true) {
			this.y -= gp.playerSpeed;
			this.direction = "up"; 
		}

		if (keyH.downPressed == true) {
			this.y += gp.playerSpeed;
			this.direction = "down";
		}

		if (keyH.leftPressed == true) {
			this.x -= gp.playerSpeed;
			this.direction = "right";
		}

		if (keyH.rightPressed == true) {
			this.x += gp.playerSpeed;
			this.direction = "left";
		}

		Position position = new Position(this.x, this.y);
		body.addFirst(position);

		if (this.shouldGrow) {
			this.shouldGrow = false;
		} else {
			body.removeLast();
		}

//		boolean isThereACollistion = this.checkWallCollision();
//		Position head = this.body.peek();
//		
//		for (Position currentPosition : this.body) {
//			int currentX = currentPosition.getX();
//			int currentY = currentPosition.getY();
//			
//			switch (this.direction) {
//			case "up":
//				if (head.getY() + 13 == currentY) {
//					System.exit(0);
//				}
//				break;
//			case "down": 
//				if (head.getY() - 13 == currentY) {
//					System.exit(0);
//				}
//				break;
//			case "left":
//				if (head.getX() - 13 == currentX) {
//					System.exit(0);
//				}
//				break;
//			case "right":
//				if (head.getX() + 13 == currentX) {
//					System.exit(0);
//				}
//				break;
//				default:
//					break;
//			}
			
//			System.out.println("Current x: " + currentX + " Current y: " + currentY);
//		}

//		return isThereACollistion;
	}

	public void draw(Graphics2D g2) {
		g2.setColor(Color.white);

		for (Position position : this.body) {
			g2.drawString("@", position.getX(), position.getY());
		}
	}

	public void extendSnake() {
		this.shouldGrow = true;
	}

	public int getSnakeLength() {
		return this.body.size();
	}

	private boolean checkWallCollision() {
		Position positionHead = body.peekFirst();
		boolean doesItHit = false;

		if (positionHead.getX() <= 0 || positionHead.getX() > this.gp.TILE_WIDTH_SIZE || positionHead.getY() <= 0
				|| positionHead.getY() > this.gp.TILE_HEIGHT_SIZE) {
			doesItHit = true;
		}

		return doesItHit;
	}

	public void reset() {
		this.setDefaultSnakeProperties();
	}
}
