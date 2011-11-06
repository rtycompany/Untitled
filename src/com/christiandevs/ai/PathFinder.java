package com.christiandevs.ai;

/* 
 * A* algorithm implementation.
 * Copyright (C) 2007, 2009 Giuseppe Scrivano <gscrivano@gnu.org>

 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
			
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/>.
 */

import java.util.*;

import com.flume2d.graphics.Tilemap;

/*
 * Example.
 */
public class PathFinder extends AStar<Node>
{
	private Tilemap map;
	private Node goal;
	
	public PathFinder(Tilemap map)
	{
		goal = new Node(0, 0);
		this.map = map;
	}
	
	public void setGoal(int x, int y)
	{
		goal.x = x;
		goal.y = y;
	}

	protected boolean isGoal(Node node)
	{
		return (node.x == goal.x) && (node.y == goal.y);
	}

	protected Double g(Node from, Node to)
	{
		if(from.x == to.x && from.y == to.y)
			return 0.0;

		if(testTile(to.x, to.y))
			return 1.0;

		return Double.MAX_VALUE;
	}

	protected Double h(Node from, Node to)
	{
		/* Use the Manhattan distance heuristic.  */
		return new Double(Math.abs(goal.x - to.x) + Math.abs(goal.y - to.y));
	}
	
	protected boolean testTile(int x, int y)
	{
		if (map.getTile(x, y) > 0)
			return true;
		return false;
	}

	protected List<Node> generateSuccessors(Node node)
	{
		List<Node> ret = new LinkedList<Node>();
		
		int x = node.x;
		int y = node.y;
		
		if(x < goal.x && testTile(x + 1, y))
			ret.add(new Node(x + 1, y));
		
		if(x > goal.x && testTile(x - 1, y))
			ret.add(new Node(x - 1, y));
		
		if(y < goal.y && testTile(x, y + 1))
			ret.add(new Node(x, y + 1));
		
		if(y > goal.y && testTile(x, y - 1))
			ret.add(new Node(x, y - 1));

		return ret;
	}

}
