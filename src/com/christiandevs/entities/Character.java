package com.christiandevs.entities;

import java.util.List;

import com.christiandevs.Stat;
import com.flume2d.Entity;
import com.flume2d.ai.PathNode;

public abstract class Character extends Entity
{
	
	protected Stat health;
	protected int attack;
	protected int armor;
	
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
		
		health = new Stat(10);
		armor = 1;
		attack = 1;
	}
	
	public void takeDamage(int value)
	{
		health.drain(value);
		if (health.depleted())
			kill();
	}
	
	public boolean isDead()
	{
		return health.depleted();
	}
	
	public void kill()
	{
		scene.remove(this);
	}
	
	/**
	 * Get a path to the destination based on current position
	 * @param dx the x-axis destination value
	 * @param dy the y-axis destination value
	 */
	protected void getPathTo(int dx, int dy)
	{
		path = world.getPath((int) x, (int) y, dx, dy);
		target = getNextPathNode();
	}
	
	/**
	 * Gets the next path node
	 * @return the next path node
	 */
	private PathNode getNextPathNode()
	{
		if (path != null && path.size() > 0)
			return path.remove(0);
		return null;
	}
	
	/**
	 * Follows along the given path, if there is one
	 */
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
				target = getNextPathNode();
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
