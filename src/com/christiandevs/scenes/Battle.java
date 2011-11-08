package com.christiandevs.scenes;

import com.christiandevs.entities.Map;
import com.flume2d.Scene;

public class Battle extends Scene
{

	public Battle()
	{
		Map map = new Map();
		map.load("maps/world.tmx");
		add(map);
	}
	
}
