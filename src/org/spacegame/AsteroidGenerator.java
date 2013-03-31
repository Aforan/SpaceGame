package org.spacegame;

import java.util.Random;
import java.util.ArrayList;

public class AsteroidGenerator{
	private static final Random random = new Random();
	private ArrayList<Entity> asteroidList;

	private double intensity;

	public AsteroidGenerator(double intensity) {
		this.intensity = intensity;
		asteroidList = new ArrayList<Entity>();
	}

	public void tick() {
		double r = random.nextDouble();

		if (r < intensity) {
			Square asteroid = new Square(random.nextInt(300), 0, 25, 25, 0, 0);
			try {
				asteroidList.add(asteroid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<Entity> getAsteroids() {
		if (asteroidList == null) return null;
		else {
			ArrayList<Entity> r = new ArrayList<Entity>(asteroidList);
			asteroidList.clear();

			return r;
		}
	}
}