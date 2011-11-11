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
		// priority queue orders the characters by their speed
		character = turnQueue.remove();
		character.startTurn();
	}
	
	@Override
	public void update()
	{
		// Check if the character is done with their turn
		if (character.isWaiting())
		{
			// add the character back onto the queue
			turnQueue.offer(character);
			getNextCharacter();
		}
		
		// smooth adjust camera to the character playing
		character.focusCamera(true);
		clampCameraToBounds();
		
		super.update();
	}
	
}
