package org.spacegame;

import javax.swing.*;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;

import org.spacegame.Entity;
import org.spacegame.Triangle;
import org.spacegame.InputHandler;

public class SpaceGame extends JPanel implements Runnable{
	private static final int TICK_TIME = 60;
	private static double version = 0.01;
	private ArrayList<Entity> gameEntities;
	private InputHandler ih;
	private Entity player;

	public SpaceGame(int w, int h) {
		super(true);
		setMaximumSize(new Dimension(w, h));
		init();
	}

	public void init() {
		System.out.println("initializing");
		gameEntities = new ArrayList<Entity>();

		player = new Triangle(100, 100, 25, 25, 0, 0);
		gameEntities.add(player);

		ih = new InputHandler();
		setFocusable(true);
		addKeyListener(ih);
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

		ArrayList<Entity> del = new ArrayList<Entity>();

		for (Entity e : gameEntities) {
			e.tick(TICK_TIME);

			if (e.shouldDie) {
				del.add(e);
				continue;
			}
			e.paint(g);
		}


		for (Entity e : del) {
			gameEntities.remove(e);
		}

		paintComponent(g);		
	}

	private void clearScreen(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	public static void main(String[] args) {
		System.out.println("test");

		JFrame frame = new JFrame("Space Game v" + version);
		frame.setSize(new Dimension(450, 700));
		frame.setMaximumSize(new Dimension(350, 700));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		SpaceGame game = new SpaceGame(300, 600);
		frame.getContentPane().add(game);

		frame.setVisible(true);

		Thread thread = new Thread(game);
		thread.start();
	}
}