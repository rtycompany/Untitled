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
	private int tileID;
	
	public PathNode(int x, int y)
	{
		this(x, y, -1);
	}
	
	public PathNode(int x, int y, int tileID)
	{
		this.x = x;
		this.y = y;
		this.tileID = tileID;
	}
	
	public boolean isWalkable()
	{
		return (tileID > 0);
	}
	
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
	
}
