package org.spacegame;

public class GameManager{
	public int score, health, lives, gas;

	public GameManager() {
		this.score = 100;
		this.health = 0;
		this.lives = 0;
		this.gas = 0;		
	}

	public void tick(int enemiesKilled, int enemiesLost) {
		score += enemiesKilled*100;
		score -= enemiesLost*1000;
	}
}