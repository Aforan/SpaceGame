package org.spacegame;

import java.util.Random;
import java.util.ArrayList;

public class AsteroidGenerator {
	private static final Random random = new Random();
	private ArrayList<Entity> asteroidList;
	private AsteroidHandler ah;

	private double intensity;

	public AsteroidGenerator(double intensity, AsteroidHandler ah) {
		this.intensity = intensity;
		asteroidList = new ArrayList<Entity>();

		this.ah = ah;
	}

	public void tick() {
		double r = random.nextDouble();

		if (r < intensity) {
			Square asteroid = new Square(random.nextInt(300), 0, 25, 25, random.nextInt(30)/100.0, 0);

			if(ah.accepted(asteroid)) {
				asteroidList.add(asteroid);
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