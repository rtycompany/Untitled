package com.christiandevs.scenes;

import java.util.Iterator;

import com.christiandevs.entities.Map;
import com.christiandevs.entities.Character;
import com.flume2d.Engine;
import com.flume2d.Scene;

public class Battle extends Scene
{
	
	private Map map;
	private Character character;
	private Iterator<Character> it;

	public Battle()
	{
		map = new Map();
		map.load("maps/world.tmx");
		it = map.characters.iterator();
		character = it.next();
		add(map);
	}
	
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
	
	@Override
	public void update()
	{
		if (!character.isTakingTurn())
		{
			// if we ran out of characters to iterate, get a new one
			if (!it.hasNext())
				it = map.characters.iterator();
			character = it.next();
			character.canTakeTurn();
		}
		character.focusCamera();
		clampCameraToBounds();
		super.update();
	}
	
}
