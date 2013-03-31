package org.spacegame;

import java.awt.Graphics;
import java.util.Random;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;

import org.spacegame.Laser;

public class Square extends Mob {
	private static Random r = new Random();
	public static final int SHOOT = 1;

	public Square(int x, int y, int w, int h, double vx, double vy) {
		super (x, y, w, h, vx, vy, 0.05, 0.05);
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
	public Shape getShape() {
		return new Rectangle(x, y, w, h);
	}

	@Override
	public void handleCollision(Entity e) {
		if (e instanceof Laser && !shouldDie) {
			shouldDie = true;
			deathAnim = 0;
		}
	}
}