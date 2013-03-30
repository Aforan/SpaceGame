package org.spacegame;
import java.awt.Graphics;
import java.util.Random;
import java.awt.Color;

public class Particle extends Entity {
	private static Random r = new Random();

	public int x, y;
	private double vx, vy, m;
	private double fx, fy;

	public Particle(int x, int y, double vx, double vy) {
		super (x, y);

		this.vx = vx;this.vy = vy;

		m = 1.0;

		fx = 0.0;
		fy = 0.0;
	}

	public void tick(double millis) {
		vx = vx + (fx*millis/m);
		vy = vy + (fy*millis/m);

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
		g.setColor(Color.RED);
		g.fillOval(x, y, 8, 8);
	}

	@Override
	public void move(Direction d) {
		switch(d) {
			case UP:
				System.out.println("Moving up");
				vy = -0.1;
				break;
			case DOWN:
				System.out.println("Moving down");
				vy = 0.1;
				break;
			case LEFT:
				System.out.println("Moving left");
				vx = -0.1;
				break;
			case RIGHT:
				System.out.println("Moving right");
				vx = 0.1;
				break;
			case STOPPED_VERTICAL:
				System.out.println("Stopped vertical");
				vy = 0.0;
				break;
			case STOPPED_HORIZONTAL:
				System.out.println("Stopped horizontal");
				vx = 0.0;
				break;
		}
	}


}