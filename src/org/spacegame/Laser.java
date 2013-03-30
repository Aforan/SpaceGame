package org.spacegame;

import org.spacegame.Entity;
import java.awt.Color;
import java.awt.Graphics;

public class Laser extends Entity {
	private static final double Y_VEL = -0.25;

	private double vx, vy;

	public Laser(int x, int y) {
		super(x, y);
		this.vx = 0.0;
		this.vy = Y_VEL;
	}
	public void tick(double millis) {
		x = x + (int) (vx*millis);
		y = y + (int) (vy*millis);

		if (x <= 0 || x >= 400) {
			shouldDie = true;
		} 

		if (y <= 0 || y >= 700) {
			shouldDie = true;
		}
	}

	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, 5, 15);
	}
}