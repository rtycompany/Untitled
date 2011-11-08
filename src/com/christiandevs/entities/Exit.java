package com.christiandevs.entities;

import com.flume2d.masks.AABB;

public class Exit extends MapEntity
{
	
	protected String mapFile;
	
	public Exit(int x, int y, int w, int h, String map)
	{
		super(x, y);
		setMask(new AABB(w, h));
		type = "exit";
		mapFile = map;
	}
	
	public void update()
	{
//		if (overlaps("player") != null)
//			world.load(mapFile);
		super.update();
	}
	
}
