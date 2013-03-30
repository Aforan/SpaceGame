package org.spacegame;

import org.spacegame.SpaceGame;
import org.spacegame.Entity;
import org.spacegame.Square;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

//Asteroid handler will handle moving AND ticking of all Asteroid Entities
public class AsteroidHandler implements Runnable{
	private static final Random random = new Random();

	private ArrayList<Entity> asteroidList;
	private Semaphore lock = new Semaphore(1);

	public AsteroidHandler(ArrayList<Entity> asteroidList) {
		this.asteroidList = asteroidList;
	}

	public void addAsteroid(Entity e) {
		asteroidList.add(e);
	}

	public void removeAsteroid(Entity e) {
		asteroidList.remove(e);
	}

	public void tick() {
		synchronized(lock) {
			lock.notify();
		}
	}

	@Override
	public void run() {
		while(true) {
			try {
				synchronized(lock) {
					lock.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		

			for (Entity e : asteroidList) {
				e.tick(SpaceGame.TICK_TIME);

				if (e instanceof Square) {
					if(e.shouldDie) {
						e.move(Entity.Direction.STOPPED_VERTICAL);
						e.move(Entity.Direction.STOPPED_HORIZONTAL);

					} else {
					/*	double r = random.nextDouble();

						if(r < 0.16) e.move(Entity.Direction.UP);
						else if(r < 0.32) e.move(Entity.Direction.DOWN);
						else if(r < 0.48) e.move(Entity.Direction.LEFT);
						else if(r < 0.64) e.move(Entity.Direction.RIGHT);
						else if(r < 0.8) e.move(Entity.Direction.STOPPED_VERTICAL);
						else e.move(Entity.Direction.STOPPED_HORIZONTAL);
					*/

						e.move(Entity.Direction.DOWN);
					}
				}
			}
		}
	}
}	