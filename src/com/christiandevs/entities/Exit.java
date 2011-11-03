package com.christiandevs.entities;

import com.flume2d.Entity;
import com.flume2d.masks.AABB;

public class Exit extends Entity
{
	
	protected String mapFile;
	protected World world;
	
	public Exit(int x, int y, int w, int h, String map, World world)
	{
		super(x, y);
		setMask(new AABB(w, h));
		type = "exit";
		mapFile = map;
		this.world = world;
	}
	
	public void update()
	{
//		if (overlaps("player") != null)
//			world.load(mapFile);
		super.update();
	}
	
}
