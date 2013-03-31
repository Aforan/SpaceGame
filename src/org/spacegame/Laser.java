package org.spacegame;

import org.spacegame.Entity;
import org.spacegame.Triangle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

public class Laser extends Entity {
	public static final double Y_VEL = -0.25;

	private double vx, vy;

	public Laser(int x, int y) {
		super(x, y);
		this.vx = 0.0;
		this.vy = Y_VEL;
	}

	public Laser(int x, int y, double vx, double vy) {
		super(x, y);
		this.vx = vx;
		this.vy = vy;
	}

	public void tick(double millis) {
		x = x + (int) (vx*millis);
		y = y + (int) (vy*millis);
	}

	@Override
	public Shape getShape() {
		return new Rectangle(x, y, 3, 15);
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(x, y, 3, 15);
	}

	@Override
	public void handleCollision(Entity e) {
		if (!(e instanceof Laser) && !(e instanceof Triangle)) {
			if (!e.shouldDie) shouldDie = true;
		}
	}

	@Override
	public boolean isDead() {
		return shouldDie;
	}

	@Override
	public void outOfBounds(Rectangle bounds) {
		shouldDie = true;
	}
}