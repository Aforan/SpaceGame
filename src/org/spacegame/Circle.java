package org.spacegame;

import org.spacegame.Entity;
import org.spacegame.Triangle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

public class Circle extends Mob {
	private double vx, vy;
	private Powerup powerup;

	public Circle(int x, int y, double vx, double vy) {
		super(x, y, 15, 15, vx, vy, 0.15, 0.15);

		double r = random.nextDouble();

		if(r < 0.5) {
			powerup = Powerup.LASER_SPEED_1;
		} else {
			powerup = Powerup.NUM_GUNS_2;
		}
	}

	@Override
	public Shape getShape() {
		return new Rectangle(x, y, 15, 15);

	}

	public void paint(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillOval(x, y, 15, 15);
	}

	@Override
	public void handleCollision(Entity e) {
		if (e instanceof Triangle && !shouldDie) {
			((Triangle)e).powerup(powerup);
			shouldDie = true;
		}
	}

	@Override
	public boolean isDead() {
		return shouldDie;
	}

	public enum Powerup {
		LASER_SPEED_1, NUM_GUNS_2;		
	}
}