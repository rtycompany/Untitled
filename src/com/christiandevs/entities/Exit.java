package com.christiandevs.entities;

import com.flume2d.Entity;
import com.flume2d.masks.AABB;

public class Exit extends Entity
{
	
	protected String mapFile;
	protected String id;
	
	public Exit(int x, int y, int w, int h, String mapFile, String id)
	{
		super(x, y);
		
		this.id = id;
		this.mapFile = mapFile;
		
		setMask(new AABB(w, h));
		type = "exit";
	}
	
	public void update()
	{
//		if (overlaps("player") != null)
//			world.load(mapFile);
		super.update();
	}
	
}
