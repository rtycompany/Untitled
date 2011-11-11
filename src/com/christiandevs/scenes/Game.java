package com.christiandevs.scenes;

import com.flume2d.Scene;
import com.christiandevs.entities.*;

public class Game extends Scene
{
	private Map map;
	
	public Game()
	{
		map = new Map();
		add(map);
		map.load("maps/world.tmx");
	}
}
