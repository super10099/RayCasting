package core;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class InputHandler implements KeyListener{
	
	
	public InputHandler(Engine game) {
		game.addKeyListener(this);
	}
	
	public class Key {
		private boolean keyToggled = false;
		
		public boolean isPressed() {
			return keyToggled;
		}
		
		public void setToggle(boolean isPressed) {
			keyToggled = isPressed;
		}
	}
	
	public Key left = new Key();
	public Key right = new Key();
	public Key up = new Key();
	public Key down = new Key();
	
	public Key turnRight = new Key();
	public Key turnLeft = new Key();	
	
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}
	
	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
	}
	
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void toggleKey(int keyCode, boolean isPressed) {
		
		//movements
		if (keyCode == KeyEvent.VK_A)
			left.setToggle(isPressed);
		if (keyCode == KeyEvent.VK_D)
			right.setToggle(isPressed);
		if (keyCode == KeyEvent.VK_W)
			up.setToggle(isPressed);
		if (keyCode == KeyEvent.VK_S)
			down.setToggle(isPressed);
		
		// turns
		if (keyCode == KeyEvent.VK_Q)
			turnLeft.setToggle(isPressed);
		if (keyCode == KeyEvent.VK_E)
			turnRight.setToggle(isPressed);
	}
	
}
