package com.christiandevs.scenes;

import java.util.*;
import java.util.concurrent.*;

import com.christiandevs.entities.Map;
import com.christiandevs.entities.Character;
import com.flume2d.Engine;
import com.flume2d.Scene;

public class Battle extends Scene
{
	
	private Map map;
	private Character character;
	private Queue<Character> turnQueue;

	public Battle()
	{
		map = new Map();
		add(map);
		map.load("data/maps/world.tmx");
		
		// Initialize character turn order
		Iterator<Character> it = map.characters.iterator();
		// TODO: look into making this a DelayedQueue for turn times
		turnQueue = new LinkedBlockingQueue<Character>();
		while (it.hasNext())
		{
			turnQueue.offer(it.next());
		}
		getNextCharacter();
		character.focusCamera(false);
	}
	
	/**
	 * Makes sure the camera is within the bounds of the map
	 */
	private void clampCameraToBounds()
	{
		if (camera.x < 0)
			camera.x = 0;
		if (camera.x > map.width - Engine.width)
			camera.x = map.width - Engine.width;
		
		if (camera.y < 0)
			camera.y = 0;
		if (camera.y > map.height - Engine.height)
			camera.y = map.height - Engine.height;
	}
	
	/**
	 * Gets the next character in turn order
	 */
	private void getNextCharacter()
	{
		// Remove characters from the queue until we find someone who isn't dead
		do {
			// TODO: do something if the queue is empty
			character = turnQueue.remove();
		} while (character.isDead());
		character.startTurn();
	}
	
	/**
	 * If all enemies are dead, win.
	 * If all player characters are dead, lose.
	 * This could also check for variations (defeat the leader, capture a point, etc...)
	 */
	private void checkCondition()
	{
		int playerCount = 0;
		int enemyCount = 0;
		
		// the turn queue has all characters that are still alive
		Iterator<Character> it = turnQueue.iterator();
		while (it.hasNext())
		{
			Character c = it.next();
			if (c.type == "player")
				playerCount += 1;
			else
				enemyCount += 1;
		}
		
		if (enemyCount == 0)
		{
			System.out.println("You won!");
		}
		else if (playerCount == 0)
		{
			System.out.println("You lost!");
		}
	}
	
	@Override
	public void update()
	{
		// Check if the character is done with their turn
		if (character.isDone())
		{
			// add the character back onto the queue
			turnQueue.offer(character);
			checkCondition();
			getNextCharacter();
		}
		
		// smooth adjust camera to the character playing
		character.focusCamera(true);
		clampCameraToBounds();
		
		super.update();
	}
	
}
