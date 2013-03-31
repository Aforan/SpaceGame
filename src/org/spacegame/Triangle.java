package org.spacegame;

import java.awt.Graphics;
import java.util.Random;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;

import org.spacegame.Laser;

public class Triangle extends Mob {
	public Triangle(int x, int y, int w, int h, double vx, double vy) {
		super (x, y, w, h, vx, vy, 0.15, 0.15);
	}
	
	public void paint(Graphics g) {
		if(!shouldDie) {
			g.setColor(Color.RED);
			int[] xp = {x+w/2, x, x+w};
			int[] yp = {y, y+h, y+h};
			g.fillPolygon(new Polygon(xp, yp, 3));
		} else {
			g.setColor(Color.RED);

			g.fillRect(x+w/2, y-deathAnim, w/3, h/3);
			g.fillRect(x+w/2-deathAnim, y+deathAnim, w/3, h/3);
			g.fillRect(x+w/2+deathAnim, y+deathAnim, w/3, h/3);			
		}
		
	}

	@Override
	public Shape getShape() {
		int[] xp = {x+w/2, x, x+w};
		int[] yp = {y, y+h, y+h};
		return new Polygon(xp, yp, 3).getBounds();
	}

		@Override
	public void handleCollision(Entity e) {
		if (e instanceof Square && !shouldDie) {
			System.out.println("Dying!!");
			shouldDie = true;
			deathAnim = 0;
		}
	}
}