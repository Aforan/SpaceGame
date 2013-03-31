package org.spacegame;

import org.spacegame.SpaceGame;
import org.spacegame.Entity;
import org.spacegame.Square;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.awt.Rectangle;

//Asteroid handler will handle moving AND ticking of all Asteroid Entities
public class AsteroidHandler implements Runnable{
	private static final Random random = new Random();

	private ArrayList<Entity> asteroidList;
	private Semaphore lock = new Semaphore(1);
	private Semaphore listLock = new Semaphore(1);

	public AsteroidHandler() {
		this.asteroidList = new ArrayList<Entity>();
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
		try {
			listLock.acquire();
			asteroidList.remove(e);
			listLock.release();
		} catch (Exception ex) {
			ex.printStackTrace();	
		} 	
	}

	public void tick() {
		synchronized(lock) {
			lock.notify();
		}
	}

	public boolean accepted(Entity asteroid) {
		Rectangle ashape = (Rectangle)asteroid.getShape();

		try {
			listLock.acquire();
			
			for (Entity e : asteroidList) {
				if (e.getShape().intersects(ashape)){
					listLock.release();
					return false;	
				} 
			}

			listLock.release();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
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
							//e.move(Entity.Direction.DOWN);
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