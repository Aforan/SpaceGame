package org.spacegame;

import javax.swing.*;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;
import java.util.Random;
import java.awt.Rectangle;
import java.awt.Shape;

import org.spacegame.Entity;
import org.spacegame.Triangle;
import org.spacegame.Square;
import org.spacegame.InputHandler;

public class SpaceGame extends JPanel implements Runnable{
	
	public static final int TICK_TIME = 60;

	private static final Random random = new Random();
	private static double version = 0.01;
	private Rectangle bounds;
	private ArrayList<Entity> gameEntities;
	private InputHandler ih;
	private Entity player;
	private AsteroidHandler ah;
	private AsteroidGenerator ag;

	public SpaceGame(int w, int h) {
		super(true);
		setMaximumSize(new Dimension(w, h));
		setSize(new Dimension(w, h));

		init();

		int nw = getWidth()-getInsets().right-getInsets().left;
		int nh = getHeight()-getInsets().top-getInsets().bottom;
		
		System.out.println(nw + ", " + nh);

		bounds = new Rectangle(0, 0, w, h-getInsets().top);
		System.out.println(bounds.height);
	}

	public void init() {
		System.out.println("initializing");
		gameEntities = new ArrayList<Entity>();

		player = new Triangle(100, 500, 25, 25, 0, 0);
		gameEntities.add(player);

		ArrayList<Entity> asteroidList = new ArrayList<Entity>();

		ah = new AsteroidHandler(asteroidList);
		Thread thread = new Thread(ah);
		thread.start();

		ih = new InputHandler();
		setFocusable(true);
		addKeyListener(ih);

		ag = new AsteroidGenerator(0.15);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
	}

	@Override
	public void run() {
		while(true) {
			tick();
		}
	}

	private void tick() {
		try {
			Thread.sleep(TICK_TIME);
		} catch (Exception e) {
			e.printStackTrace();
		}

		update();
	}

	private void handleInput() {
		if (ih.up) {
			player.move(Entity.Direction.UP);
		}
		if (ih.down) {
			player.move(Entity.Direction.DOWN);
		}
		if (ih.left) {
			player.move(Entity.Direction.LEFT);
		}
		if (ih.right) {
			player.move(Entity.Direction.RIGHT);
		}

		if(!(ih.up | ih.down)) {
			player.move(Entity.Direction.STOPPED_VERTICAL);
		}	
	
		if(!(ih.right | ih.left)) {
			player.move(Entity.Direction.STOPPED_HORIZONTAL);
		}

		if(ih.shoot) {
			Entity a = player.action(Triangle.SHOOT);
			 if (a != null) {
			 	gameEntities.add(a);
			 }
		}

	}

	private void update() {
		handleInput();

		Graphics g = getGraphics();
		clearScreen(g);

		ah.tick();
		ag.tick();

		getNewEntities();

		ArrayList<Entity> del = new ArrayList<Entity>();

		handleCollisions();
		//System.out.println("x: " + player.x + " y: " + player.y + " " + bounds.width + " " + bounds.height + " " + bounds.x + " " + bounds.y);

		for (Entity e : gameEntities) {

			if (!(e instanceof Square)) {
				e.tick(TICK_TIME);
			}

			if (e.isDead()) {
				del.add(e);
				continue;
			}
			e.paint(g);
		}

		for (Entity e : del) {
			if(e instanceof Triangle) {
				System.out.println("Player died");
			}
			gameEntities.remove(e);
		}

		paintComponent(g);		
	}

	private void getNewEntities() {
		ArrayList<Entity> add = ag.getAsteroids();

		for (Entity e : add) {
			gameEntities.add(e);
			ah.addAsteroid(e);
		}
	}

	private void handleCollisions() {
		for (Entity a : gameEntities) {
			Shape ashape = a.getShape();

			if (!bounds.contains((Rectangle)a.getShape())) a.outOfBounds(bounds);

			for (Entity b : gameEntities) {
				Shape bshape = b.getShape();

				if (ashape instanceof Rectangle) {
					if (a != b && ashape.intersects((Rectangle)bshape)) {
						//System.out.println("Collision!" + a + " -> " + b);
						a.handleCollision(b);
						b.handleCollision(a);
					}
				}
			}
		}
	}

	private void clearScreen(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	public static void main(String[] args) {
		System.out.println("test");

		JFrame frame = new JFrame("Space Game v" + version);
		frame.setSize(new Dimension(450, 700));
		//frame.setMaximumSize(new Dimension(450, 700));
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		SpaceGame game = new SpaceGame(450, 700);
		frame.getContentPane().add(game);

		frame.setVisible(true);

		Thread thread = new Thread(game);
		thread.start();
	}
}