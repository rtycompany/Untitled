package com.christiandevs.entities;

import java.util.*;
import com.christiandevs.rpg.Stat;
import com.flume2d.Engine;
import com.flume2d.Entity;
import com.flume2d.ai.PathNode;
import com.flume2d.graphics.Spritemap;

public abstract class Character extends Entity
{
	
	protected enum PlayState
	{
		Wait,
		StartTurn,
		Move,
		Attack
	}
	
	public String name;
	
	protected String classType;
	protected Stat health;
	protected int attack;
	protected int armor;
	protected int level;
	protected int moveSpaces;
	
	protected Spritemap sprite;
	protected Map map;
	
	private List<PathNode> pathNodes;
	private PathNode pathTarget;
	
	protected PlayState state;
	
	public Character()
	{
		this(0, 0);
	}
	
	public Character(int x, int y)
	{
		super(x, y);
		
		state = PlayState.Wait;
		
		health = new Stat(50);
		armor = 1;
		attack = 1;
		level = 1;
		moveSpaces = 3;
	}
	
	public void levelUp()
	{
		level += 1;
		health.modifyMax(level * 5);
		health.refill();
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
	
	public void startTurn()
	{
		state = PlayState.StartTurn;
	}
	
	public boolean isWaiting()
	{
		return (state == PlayState.Wait);
	}
	
	private float ease(float start, float target)
	{
		if (Math.abs(target - start) < 4)
			return target;
		
		if (target < start)
			return start - 2;
		
		return start + 2;
	}
	
	public void focusCamera(boolean smooth)
	{
		float targetX = x - Engine.width / 2 + sprite.frameWidth / 2;
		float targetY = y - Engine.height / 2 + sprite.frameHeight / 2;
		if (smooth)
		{
			// TODO: use a tween or something instead of this hacked method
			scene.camera.x = ease(scene.camera.x, targetX);
			scene.camera.y = ease(scene.camera.y, targetY);
		}
		else
		{
			scene.camera.x = targetX;
			scene.camera.y = targetY;
		}
	}
	
	protected Character attack(int dx, int dy)
	{
		return null;
	}
	
	public void setMap(Map map)
	{
		this.map = map;
	}
	
	protected boolean canMoveTo(int dx, int dy)
	{
		LinkedList<PathNode> nodes = new LinkedList<PathNode>();
		getWalkableArea(nodes, (int) x / map.tileWidth, (int) y / map.tileHeight, moveSpaces);
		return pathContains(nodes, dx / map.tileWidth, dy / map.tileHeight);
	}
	
	private boolean pathContains(List<PathNode> nodes, int x, int y)
	{
		Iterator<PathNode> it = nodes.iterator();
		while (it.hasNext())
		{
			PathNode node = it.next();
			if (node.x == x && node.y == y)
				return true;
		}
		return false;
	}
	
	protected void getWalkableArea(List<PathNode> nodes, int x, int y, int spaces)
	{
		if (spaces <= 0) return;
		
		int[] xvals = new int[]{x - 1, x + 1, x,     x    };
		int[] yvals = new int[]{y,     y,     y - 1, y + 1};
		
		for (int i = 0; i < xvals.length; i++)
		{
			x = xvals[i]; y = yvals[i];
			if (map.isWalkable(x, y) && !pathContains(nodes, x, y))
			{
				nodes.add(new PathNode(x, y));
				getWalkableArea(nodes, x, y, spaces - 1);
			}
		}
	}
	
	/**
	 * Get a path to the destination based on current position
	 * @param dx the x-axis destination value
	 * @param dy the y-axis destination value
	 */
	protected void getPathTo(int dx, int dy)
	{
		pathNodes = map.getPath((int) x, (int) y, dx, dy);
		pathTarget = getNextPathNode();
	}
	
	/**
	 * Gets the next path node
	 * @return the next path node
	 */
	private PathNode getNextPathNode()
	{
		if (pathNodes != null && pathNodes.size() > 0)
			return pathNodes.remove(0);
		return null;
	}
	
	/**
	 * Follows along the given path, if there is one
	 * @return still following the path?
	 */
	protected boolean followPath()
	{
		if (pathTarget == null)
			return false;
		
		int destX = pathTarget.x * map.tileWidth;
		int destY = pathTarget.y * map.tileHeight;
		int moveSpeed = 2;
		if (x == destX && y == destY)
		{
			// we arrived at the target destination, get the next target
			pathTarget = getNextPathNode();
		}
		else
		{
			if (x < destX)
			{
				x += moveSpeed;
				sprite.play("right");
			}
			else if (x > destX)
			{
				x -= moveSpeed;
				sprite.play("left");
			}
			
			if (y < destY)
			{
				y += moveSpeed;
				sprite.play("down");
			}
			else if (y > destY)
			{
				y -= moveSpeed;
				sprite.play("up");
			}
		}
		return true;
	}

}
