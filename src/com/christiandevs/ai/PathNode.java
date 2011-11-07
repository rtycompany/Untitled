package com.christiandevs.ai;

public class PathNode
{
	public int x;
	public int y;
	
	public int f;
	public int g;
	public int h;
	public int cost;
	
	public PathNode parent;
	public boolean walkable;
	
	public PathNode(int x, int y)
	{
		this(x, y, true);
	}
	
	public PathNode(int x, int y, boolean walkable)
	{
		this.x = x;
		this.y = y;
		this.walkable = walkable;
	}
	
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
	
}
