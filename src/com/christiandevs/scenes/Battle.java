package com.christiandevs.scenes;

import java.util.*;

import com.christiandevs.entities.Map;
import com.christiandevs.entities.Character;
import com.flume2d.Engine;
import com.flume2d.Scene;

public class Battle extends Scene
{
	
	private Map map;
	private Character character;
	private PriorityQueue<Character> turns;

	public Battle()
	{
		map = new Map();
		add(map);
		map.load("maps/world.tmx");
		
		// Initialize character turn order
		Iterator<Character> it = map.characters.iterator();
		turns = new PriorityQueue<Character>();
		while (it.hasNext())
		{
			turns.offer(it.next());
		}
		getNextCharacter();
		character.focusCamera(true);
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
		character = turns.remove();
		character.canTakeTurn();
	}
	
	@Override
	public void update()
	{
		// Check if the character is done with their turn
		if (character.isWaiting())
		{
			// add the character back into turn order
			turns.offer(character);
			getNextCharacter();
		}
		
		// adjust camera to the character playing
		character.focusCamera();
		clampCameraToBounds();
		
		super.update();
	}
	
}
