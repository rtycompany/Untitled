package com.christiandevs.scenes;

import com.flume2d.Scene;
import com.christiandevs.entities.*;

public class Game extends Scene
{
	private World world;
	
	public Game()
	{
		world = new World();
		add(world);
	}
}
