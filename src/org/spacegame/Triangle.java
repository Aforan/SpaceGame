package org.spacegame;

import java.awt.Graphics;
import java.util.Random;
import java.awt.Color;
import java.awt.Polygon;

import org.spacegame.Laser;

public class Triangle extends Entity {
	private static Random r = new Random();
	public static final int SHOOT = 1;

	public int x, y, w, h;
	private double vx, vy, m;
	private double fx, fy;

	private int lastShot;
	private int shotDelay;

	public Triangle(int x, int y, int w, int h, double vx, double vy) {
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

		lastShot++;
	}

	public void paint(Graphics g) {
		g.setColor(Color.RED);
		int[] xp = {x+w/2, x, x+w};
		int[] yp = {y, y+h, y+h};
		g.fillPolygon(new Polygon(xp, yp, 3));
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

	public boolean canShoot() {
		if (lastShot > shotDelay) return true;
		else return false;
	}
}