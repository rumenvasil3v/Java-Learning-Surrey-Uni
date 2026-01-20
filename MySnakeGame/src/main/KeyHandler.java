package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	public boolean upPressed;
	public boolean downPressed;
	public boolean leftPressed;
	public boolean rightPressed;
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_W) {
			this.upPressed = true;
			this.downPressed = false;
			this.leftPressed = false;
			this.rightPressed = false;
		}
		
		if (code == KeyEvent.VK_S) {
			this.upPressed = false;
			this.downPressed = true;
			this.leftPressed = false;
			this.rightPressed = false;
		}
		
		if (code == KeyEvent.VK_A) {
			this.upPressed = false;
			this.downPressed = false;
			this.leftPressed = true;
			this.rightPressed = false;
		}
		
		if (code == KeyEvent.VK_D) {
			this.upPressed = false;
			this.downPressed = false;
			this.leftPressed = false;
			this.rightPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyValue = e.getKeyCode();
		
//		if (keyValue == KeyEvent.VK_W) {
//			this.upPressed = false;
//		}
//		
//		if (keyValue == KeyEvent.VK_S) {
//			this.downPressed = false;
//		}
//		
//		if (keyValue == KeyEvent.VK_A) {
//			this.leftPressed = false;
//		}
//		
//		if (keyValue == KeyEvent.VK_D) {
//			this.rightPressed = false;
//		}
	}

}
