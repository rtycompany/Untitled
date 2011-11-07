package com.christiandevs.entities;

import java.util.List;

import com.christiandevs.ai.PathNode;
import com.flume2d.Entity;

public class Character extends Entity
{
	protected World world;
	
	private List<PathNode> path;
	private PathNode target;
	
	public Character(World world)
	{
		this(0, 0, world);
	}
	
	public Character(int x, int y, World world)
	{
		super(x, y);
		this.world = world;
	}
	
	protected void getPathTo(int dx, int dy)
	{
		path = world.getPath((int) x, (int) y, dx, dy);
		target = getNextNode();
	}
	
	private PathNode getNextNode()
	{
		if (path != null && path.size() > 0)
			return path.remove(0);
		return null;
	}
	
	protected void followPath()
	{
		if (target != null)
		{
			// TODO: remove 16 in place of tile width/height values
			int destX = target.x * 16;
			int destY = target.y * 16;
			int moveSpeed = 16 / 4;
			if (x == destX && y == destY)
			{
				// we arrived at the target destination, get the next target
				target = getNextNode();
			}
			else
			{
				if (x < destX)
					x += moveSpeed;
				else if (x > destX)
					x -= moveSpeed;
				
				if (y < destY)
					y += moveSpeed;
				else if (y > destY)
					y -= moveSpeed;
			}
		}
	}

}
