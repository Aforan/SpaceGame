package org.spacegame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

public class Entity {
	public int x, y, w, h;
	public double vx, vy, fx, fy, m;
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

	public Entity[] action(int action) { 	
		return null;
	}

	public Shape getShape() {
		return null;
	}

	public void outOfBounds(Rectangle bounds) { }

	public enum Direction {
		UP, DOWN, LEFT, RIGHT, STOPPED_VERTICAL, STOPPED_HORIZONTAL;
	}

	public void applyForce(double fxx, double fyy, double deltat) {
		fx += fxx;
		fy += fyy;

		vx = vx + (fx*deltat/m);
		vy = vy + (fy*deltat/m);

		x = x + (int) (vx*deltat);
		y = y + (int) (vy*deltat);

		fx = 0;
		fy = 0;
	}
}