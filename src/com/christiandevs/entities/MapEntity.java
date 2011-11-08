package com.christiandevs.entities;

import com.flume2d.Entity;

public class MapEntity extends Entity
{
	
	protected Map map;
	
	public MapEntity(int x, int y)
	{
		super(x, y);
	}

	public void setMap(Map map)
	{
		this.map = map;
	}
}
