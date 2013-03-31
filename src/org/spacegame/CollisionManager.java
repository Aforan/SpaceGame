package org.spacegame;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.awt.Shape;
import java.awt.Rectangle;

public class CollisionManager implements Runnable {
	private ArrayList<Entity> entities;
	private Semaphore listLock = new Semaphore(1);
	private Rectangle bounds;

	public CollisionManager(Rectangle bounds) {
		entities = new ArrayList<Entity>();
		this.bounds = bounds;
	}

	@Override
	public void run() {
		while (true) {
			try {
				listLock.acquire();
				checkCollisions();
				listLock.release();

				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void checkCollisions() {
		for (Entity a : entities) {
			Shape ashape = a.getShape();
			if (!bounds.contains((Rectangle)a.getShape())) a.outOfBounds(bounds);

			for (Entity b : entities) {
				Shape bshape = b.getShape();

				if (ashape instanceof Rectangle) {
					if (a != b && ashape.intersects((Rectangle)bshape)) {
						//System.out.println("Collision!" + a + " -> " + b);
						if(a instanceof Mob && b instanceof Mob && !a.shouldDie && !b.shouldDie) {
							squareCollision(a, b);
						} else {
							a.handleCollision(b);
							b.handleCollision(a);
						}
					}
				}
			}
		}
	}

	private void squareCollision(Entity a, Entity b) {
		Rectangle intersection = ((Rectangle)a.getShape()).intersection((Rectangle)b.getShape());
		double rvx = 0.0; 
		double rvy = 0.0;

		if ((b.x+(b.w/2)) < (a.x+(a.w/2))) {
			rvx = intersection.getWidth();
		} else if ((b.x+(b.w/2)) > (a.x+(a.w/2))) {
			rvx = -intersection.getWidth();
		}

		if ((b.y+(b.h/2)) < (a.y+(a.h/2))) {
			rvy = intersection.getHeight();
		} else if ((b.y+(b.h/2)) > (a.y+(a.h/2))) {
			rvy = -intersection.getHeight();
		}

		if (rvx != 0 && rvy != 0) {
			if(Math.abs(rvx) > Math.abs(rvy)) {
				rvx = 0;
			} else {
				rvy  =0;
			}
		}

		a.x += rvx/2;
		a.y += rvy/2;

		b.x += -rvx/2;
		b.y += -rvy/2;

		//System.out.println(fx);

		double vxa = a.vx;
		double vxb = b.vx;
		double vya = a.vy;
		double vyb = b.vy;

		double fxa = 0.9*(a.m*vxb-a.m*vxa);
		double fxb = 0.9*(b.m*vxa-b.m*vxb);

		double fya = 0.9*(a.m*vyb-a.m*vya);
		double fyb = 0.9*(b.m*vya-b.m*vyb);

		a.applyForce(fxa, fya, 1);
		b.applyForce(fxb, fyb, 1);			

		b.handleCollision(a);
		a.handleCollision(b);
	}

	public void updateList(ArrayList<Entity> newEntities) {
		try {
			listLock.acquire();
			entities = new ArrayList<Entity>(newEntities);
			listLock.release();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}