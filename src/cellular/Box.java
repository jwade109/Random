package cellular;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.*;
import javax.swing.JFrame;
import javax.imageio.*;

import java.util.Timer;
import java.util.TimerTask;

public class Box
{
	private String name;
	private JFrame view;
	private Grid place;
	private GameWindow game;
	private long waitTime;
	private int width;
	private int height;
	private int cellDim;
	/**
	 *  Creates a new Box.
	 */
	public Box(String name, int width, int height, String type, long waitTime)
	{
		this.name = name;
		this.width = width;
		this.height = height;
		this.waitTime = waitTime;
		int maxState = 0;
		switch (type)
		{
		case "life":
			maxState = 2;
			cellDim = 4;
			break;
		case "ant":
			maxState = 10;
			cellDim = 8;
			break;
		case "test":
			maxState = 12;
			cellDim = 8;
			break;
		case "wire":
			maxState = 4;
			cellDim = 4;
		}
		place = new Grid(width, height, type);
		view = new JFrame(name);
		// The following closes all the windows together...
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Adding the game panel
		game = new GameWindow(place, maxState, cellDim, type);
		view.add(game);
		// Painting and revealing.
		view.pack();
		view.setVisible(true);
		// The 20 is for the header of the window.
		view.setSize(cellDim * width, 20 + cellDim * height);
	}
	/**
	 *  Returns the Grid under this Box.
	 */
	public Grid getGrid()
	{
		return place;
	}
	/**
	 *  Ticks the automata and updates the display.
	 */
	public void tick()
	{
		place.tick();
		view.repaint(waitTime, 0, 0, 
			width * cellDim, 20 + height * cellDim);
		try
		{
			Thread.currentThread().sleep(waitTime);
		}
		catch (InterruptedException ex)
		{
			ex.printStackTrace();
		}
	}
	/**
	 *  Runs the Box.
	 */
	public void run(int gens)
	{
		for (int i = 0; i < gens; i++)
		{
			this.tick();
		}
	}
	/**
	 *  Returns the GameWindow JPanel under this Box.
	 */
	public GameWindow getGameWindow()
	{
		return game;
	}
	/**
	 *  Sets the name of the box, and updates the window title.
	 */
	public void setName(String name)
	{
		this.name = name;
		JTextField newName = new JTextField(name);
		view.setTitle(newName.getText());
	}
}