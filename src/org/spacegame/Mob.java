package org.spacegame;

import java.awt.Graphics;
import java.util.Random;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;

import org.spacegame.Laser;

public class Mob extends Entity {
	protected static Random random = new Random();
	public static final int SHOOT = 1;

	public double vmaxx, vmaxy;

	protected int lastShot;
	protected int shotDelay;
	protected int deathAnim, maxDeathAnim;

	public Mob(int x, int y, int w, int h, double vx, double vy, double vmaxx, double vmaxy) {
		super (x, y);

		this.vx = vx;
		this.vy = vy;
		this.w = w;
		this.h = h;

		m = 1.0;

		fx = 0.0;
		fy = 0.0;

		lastShot = 0;
		shotDelay = 2;
		deathAnim = 0;
		maxDeathAnim = 10;

		this.vmaxx = vmaxx;
		this.vmaxy = vmaxy;
	}

	public void tick(double millis) {
		vx = vx + (fx*millis/m);
		vy = vy + (fy*millis/m);

		x = x + (int) (vx*millis);
		y = y + (int) (vy*millis);

		if(shouldDie) {
			deathAnim++;
		}

		lastShot++;

		fx = 0;
		fy = 0;
	}

	@Override
	public void move(Direction d) {
		switch(d) {
			case UP:
				vy = -vmaxy;
				break;
			case DOWN:
				vy = vmaxy;
				break;
			case LEFT:
				vx = -vmaxx;
				break;
			case RIGHT:
				vx = vmaxx;
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
	public Entity[] action(int action) {
		if(canShoot()) {
			lastShot = 0;
			Entity[] r = new Entity[1];
			r[0] = new Laser(x+w/2, y);
			return r;
			//return {new Laser(x+w/2, y)};
		} else return null;
	}

	public boolean canShoot() {
		if (lastShot > shotDelay) return true;
		else return false;
	}

	@Override
	public void outOfBounds(Rectangle bounds) {
		int bx = bounds.x;
		int by = bounds.y;
		int bw = bounds.width;
		int bh = bounds.height;
	
		if (x < bx) x = bx;
		if (y < by) y = by;
		if (x+w > bx+bw) x = bx+bw-w;
		if (y+h > by+bh) y = by+bh-h;
	}

	@Override
	public boolean isDead() {
		if(shouldDie && deathAnim > maxDeathAnim) return true;
		else return false;
	}
}