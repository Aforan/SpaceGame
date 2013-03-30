package org.spacegame;

import java.awt.Graphics;
import java.util.Random;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;

import org.spacegame.Laser;

public class Square extends Entity {
	private static Random r = new Random();
	public static final int SHOOT = 1;

	public int w, h;
	private double vx, vy, m;
	private double fx, fy;

	private int lastShot;
	private int shotDelay;
	private int deathAnim, maxDeathAnim;

	public Square(int x, int y, int w, int h, double vx, double vy) {
		super (x, y);

		this.vx = vx;
		this.vy = vy;
		this.w = w;
		this.h = h;

		m = 1.0;

		fx = 0.0;
		fy = 0.0;

		lastShot = 0;
		shotDelay = 3;
		maxDeathAnim = 10;
	}

	public void tick(double millis) {
		vx = vx + (fx*millis/m);
		vy = vy + (fy*millis/m);

		x = x + (int) (vx*millis);
		y = y + (int) (vy*millis);

		if (x <= 0 || x >= 400) {
			x = x <= 0 ? 1 : 349;
			vx = -vx;
		} 

		if (y <= 0 || y >= 700) {
			y = y <= 0 ? 1 :699;
			vy = -vy;
		}

		if(shouldDie) {
			deathAnim++;
		}

		lastShot++;
	}

	public void paint(Graphics g) {
		if(!shouldDie) {
			g.setColor(Color.YELLOW);
			g.fillRect(x, y, w, h);
		} else {
			g.setColor(Color.YELLOW);

			g.fillRect(x-deathAnim, y-deathAnim, w/4, h/4);
			g.fillRect(x+deathAnim, y-deathAnim, w/4, h/4);
			g.fillRect(x-deathAnim, y+deathAnim, w/4, h/4);
			g.fillRect(x+deathAnim, y+deathAnim, w/4, h/4);			
		}
		
	}

	@Override
	public void move(Direction d) {
		switch(d) {
			case UP:
				vy = -0.15;
				break;
			case DOWN:
				vy = 0.15;
				break;
			case LEFT:
				vx = -0.15;
				break;
			case RIGHT:
				vx = 0.15;
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
	public Entity action(int action) {
		if(canShoot()) {
			lastShot = 0;
			return new Laser(x+w/2, y);
		} else return null;
	}

	@Override
	public Shape getShape() {
		return new Rectangle(x, y, w, h);
	}

	public boolean canShoot() {
		if (lastShot > shotDelay) return true;
		else return false;
	}

	@Override
	public void handleCollision(Entity e) {
		if (e instanceof Laser) {
			shouldDie = true;
			deathAnim = 0;
		}
	}

	@Override
	public boolean isDead() {
		if(shouldDie && deathAnim > maxDeathAnim) return true;
		else return false;
	}
}