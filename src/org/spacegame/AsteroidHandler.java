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
	private Semaphore listLock = new Semaphore(1);

	public AsteroidHandler(ArrayList<Entity> asteroidList) {
		this.asteroidList = asteroidList;
	}

	public void addAsteroid(Entity e) {
		try {
			listLock.acquire();
			asteroidList.add(e);
			listLock.release();
		} catch (Exception ex) {
			ex.printStackTrace();	
		} 
		
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

				listLock.acquire();
				for (Entity e : asteroidList) {
					e.tick(SpaceGame.TICK_TIME);

					if (e instanceof Square) {
						if(e.shouldDie) {
							e.move(Entity.Direction.STOPPED_VERTICAL);
							e.move(Entity.Direction.STOPPED_HORIZONTAL);

						} else {

							e.move(Entity.Direction.DOWN);
						}
					}
				}

				listLock.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}	