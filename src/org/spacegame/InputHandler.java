package org.spacegame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {
	public boolean up, down, left, right, shoot;
	
	public InputHandler() {
		up = false;
		down = false;
		right = false;
		left = false;
		shoot = false;
	}

	private void toggle(int keyCode, boolean b) {
		if(keyCode == KeyEvent.VK_UP) up = b;
		if(keyCode == KeyEvent.VK_DOWN) down = b;
		if(keyCode == KeyEvent.VK_RIGHT) right = b;
		if(keyCode == KeyEvent.VK_LEFT) left = b;
		if(keyCode == KeyEvent.VK_SPACE) shoot = b;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		toggle(e.getKeyCode(), true);
	}

	
	@Override
	public void keyReleased(KeyEvent e) {
		toggle(e.getKeyCode(), false);
	}

	@Override
	public void keyTyped(KeyEvent e) {}

}
