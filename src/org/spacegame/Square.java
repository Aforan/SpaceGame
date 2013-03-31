package org.spacegame;

import java.awt.Graphics;
import java.util.Random;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;

import org.spacegame.Laser;

public class Square extends Mob {
	public static final double CONST_GRAV = 0.000001;

	private int health;
	public boolean lost;
	private Laser lastLaser;
	public static final int SHOOT = 1;
	private static Random r = new Random();


	public Square(int x, int y, int w, int h, double vx, double vy) {
		super (x, y, w, h, vx, vy, 0.05, 0.05);
		lost = false;
		health = 1;
		move(Direction.DOWN);
	}

	public void paint(Graphics g) {
		if(!shouldDie) {
			if(health == 1) {
				g.setColor(Color.YELLOW);
			} else {
				g.setColor(Color.RED);
			}

			g.fillRect(x, y, w, h);
		} else {
			g.setColor(Color.RED);

			g.fillRect(x-deathAnim, y-deathAnim, w/4, h/4);
			g.fillRect(x+deathAnim, y-deathAnim, w/4, h/4);
			g.fillRect(x-deathAnim, y+deathAnim, w/4, h/4);
			g.fillRect(x+deathAnim, y+deathAnim, w/4, h/4);			
		}
	}

	public void die() {
		shouldDie = true;
		deathAnim = 0;
	}

	@Override
	public void tick(double millis) {
		fy = CONST_GRAV;
		super.tick(millis);
	}

	@Override
	public Shape getShape() {
		return new Rectangle(x, y, w, h);
	}

	@Override
	public void handleCollision(Entity e) {
		if (e instanceof Laser && !shouldDie && e != lastLaser) {
			hit();
			lastLaser = (Laser)e;
		} else if(e instanceof Square) {
		}
	}

	public void hit() {
		health--;
		applyForce(0.0, -m*.5*vy, 1.0);
		if(health < 0) {
			shouldDie = true;
			deathAnim = 0;
		}
	}

	@Override
	public void move(Direction d) { 
		switch(d) {
			case DOWN:
				vy = vmaxy;
				break;
			case STOPPED_VERTICAL:
				vy = 0.0;
				break;
			case STOPPED_HORIZONTAL:
				vx = 0.0;
				break;
		}
	}

	@Override
	public void outOfBounds(Rectangle bounds) {
		int bx = bounds.x;
		int by = bounds.y;
		int bw = bounds.width;
		int bh = bounds.height;
	
		if (x < bx) { x = bx; vx = -vx; }
		if (y < by) y = by;
		if (x+w > bx+bw){ x = bx+bw-w-1; vx = -vx; }
		if (y+h > by+bh) lost = true;
	}
}