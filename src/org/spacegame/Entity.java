package org.spacegame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

public class Entity {
	public int x, y;
	public boolean shouldDie;

	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
		shouldDie = false;
	}

	public void tick(double millis) { }
	public void paint(Graphics g) { }
	public void move(Direction d) { }
	public void handleCollision(Entity e) { }
	
	public boolean isDead() {
		return false;
	}

	public Entity action(int aciton) { 	
		return null;
	}

	public Shape getShape() {
		return null;
	}



	public enum Direction {
		UP, DOWN, LEFT, RIGHT, STOPPED_VERTICAL, STOPPED_HORIZONTAL;
	}
}