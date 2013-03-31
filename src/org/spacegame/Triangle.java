package org.spacegame;

import java.awt.Graphics;
import java.util.Random;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import org.spacegame.Laser;

public class Triangle extends Mob {
	private ArrayList<Upgrade> upgrades;
	private int numGuns;

	public Triangle(int x, int y, int w, int h, double vx, double vy) {
		super (x, y, w, h, vx, vy, 0.15, 0.15);
		upgrades = new ArrayList<Upgrade>();

		numGuns = 1;
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

	@Override
	public void tick(double millis) {
		super.tick(millis);
		ArrayList<Upgrade> del = new ArrayList<Upgrade>();

		for(Upgrade u : upgrades) {
			u.tick(millis);

			if(u.done) {
				del.add(u);
			}
		}

		for(Upgrade u : del) {
			upgrades.remove(u);
		}

		applyUpgrades();
	}

	public void applyUpgrades() {
		shotDelay = 2;
		numGuns = 1;

		for (Upgrade u : upgrades) {
			switch(u.powerup) {
				case LASER_SPEED_1:
					shotDelay = 1;
					break;
				case NUM_GUNS_2:
					numGuns = 2;
					break;

			}
		}
	}

	@Override
	public Entity[] action(int action) {
		Entity[] es = super.action(action);

		if(es == null) return null;

		if(numGuns == 2) {
			Laser l2 = new Laser(x, y, Laser.Y_VEL/2, Laser.Y_VEL);
			Laser l3 = new Laser(x, y, -Laser.Y_VEL/2, Laser.Y_VEL);

			Entity[] r = new Entity[es.length + 2];

			for (int i = 0; i < es.length; i++) {
				r[i] = es[i];
			}

			r[r.length-1] = l2;
			r[r.length-2] = l3;

			return r;
		}

		return es;
	}

	public void powerup(Circle.Powerup p) {
		System.out.println("POWAHHHHHHHHH!!!!!");
		upgrades.add(new Upgrade(p, 10000));
	}

	private class Upgrade {
		public Circle.Powerup powerup;
		private int timer;
		public boolean done;

		public Upgrade(Circle.Powerup p, int startTime) {
			powerup = p;
			timer = startTime;
			done = false;
		}

		public void tick(double millis) {
			timer -= millis;
			System.out.println(timer);

			if (timer <= 0) {
				done = true;
			}
		}
	}
}