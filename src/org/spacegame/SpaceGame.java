package org.spacegame;

import javax.swing.*;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Rectangle;

import java.util.Random;
import java.util.ArrayList;

import org.spacegame.Entity;
import org.spacegame.Triangle;
import org.spacegame.Square;
import org.spacegame.InputHandler;
import org.spacegame.CollisionManager;

public class SpaceGame extends JPanel implements Runnable{
	
	private static double version = 0.02;
	public static final int TICK_TIME = 60;
	private static final Random random = new Random();

	private Entity player;
	private GameManager gm;
	private InputHandler ih;
	private boolean running;
	private Rectangle bounds;
	private AsteroidHandler ah;
	private CollisionManager cm;
	private AsteroidGenerator ag;
	private int numKilled, numLost;
	private ArrayList<Entity> gameEntities;

	public SpaceGame(int w, int h) {
		super(true);
		setMaximumSize(new Dimension(w, h));
		setSize(new Dimension(w, h));

		bounds = new Rectangle(0, 0, w, h-getInsets().top);
		System.out.println(bounds.height);
		
		init();

		int nw = getWidth()-getInsets().right-getInsets().left;
		int nh = getHeight()-getInsets().top-getInsets().bottom;
		
		System.out.println(nw + ", " + nh);
	}

	public void init() {
		System.out.println("initializing");
		gameEntities = new ArrayList<Entity>();

		ah = new AsteroidHandler();
		Thread thread = new Thread(ah);
		thread.start();

		cm = new CollisionManager(bounds);
		thread = new Thread(cm);
		thread.start();

		gm = new GameManager();
		ag = new AsteroidGenerator(0.25, ah);

		player = new Triangle(100, 500, 25, 25, 0, 0);
		gameEntities.add(player);

		ih = new InputHandler();
		setFocusable(true);
		addKeyListener(ih);

		running = true;
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString("Score: " + gm.score, 25, 25);

		super.paintComponents(g);
	}

	@Override
	public void run() {
		while(running) {
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
			Entity[] as = player.action(Triangle.SHOOT);
			 if (as != null) {
			 	for (Entity a : as) 
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
		cm.updateList(gameEntities);

		getNewEntities();

		numLost = 0;
		numKilled = 0;
		
		ArrayList<Entity> del = new ArrayList<Entity>();
		ArrayList<Entity> addList = new ArrayList<Entity>();

		for (Entity e : gameEntities) {

			if (!(e instanceof Square)) {
				e.tick(TICK_TIME);
			}

			if(e instanceof Square) {
				Square te = (Square)e;
				if(te.lost) {
					numLost++;
					del.add(e);
				} else if(te.isDead()) {
					numKilled++;
				}

				if(te.shouldDie) {
					Double r = random.nextDouble();

					if (r < 0.005) {
						Circle c = new Circle(te.x, te.y, te.vx, te.vy);
						addList.add(c);
					}
				}
			}

			if (e.isDead()) {
				if(e instanceof Triangle) {
					lose();
				}

				del.add(e);
				continue;
			}
			e.paint(g);
		}
		
		for (Entity e : del) {
			gameEntities.remove(e);

			if(e instanceof Square) {
				ah.removeAsteroid(e);
			}
		}

		for (Entity e : addList) {
			gameEntities.add(e);
		}

		if (gm.score < 0) {
			lose();
		}

		gm.tick(numKilled, numLost);
		paintComponent(g);		
	}

	private void getNewEntities() {
		ArrayList<Entity> add = ag.getAsteroids();

		for (Entity e : add) {
			gameEntities.add(e);
			ah.addAsteroid(e);
		}
	}

	private void clearScreen(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	public void lose() {
		running = false;
		System.out.println("HOW DID YOU LOSE THIS EASY ASS GAME??!!!??");
	}

	public static void main(String[] args) {
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