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
public class PathFinder extends AStar<PathFinder.Node>
{
	private Tilemap map;
	private Node goal;

	public static class Node
	{
			public int x;
			public int y;
			
			Node(int x, int y)
			{
				this.x = x; 
				this.y = y;
			}
			
			public String toString()
			{
				return "(" + x + ", " + y + ") ";
			} 
	}
	
	public PathFinder(Tilemap map)
	{
		this.map = map;
		goal = new Node(0, 0);
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

		if(map.getTile(to.x, to.y) == 1)
			return 1.0;

		return Double.MAX_VALUE;
	}

	protected Double h(Node from, Node to)
	{
		/* Use the Manhattan distance heuristic.  */
		return new Double(Math.abs(goal.x - to.x) + Math.abs(goal.y - to.y));
	}

	protected List<Node> generateSuccessors(Node node)
	{
		List<Node> ret = new LinkedList<Node>();
		int x = node.x;
		int y = node.y;
		if(y < goal.y && map.getTile(x, y + 1) == 1)
				ret.add(new Node(x, y + 1));

		if(x < goal.x && map.getTile(x + 1, y) == 1)
				ret.add(new Node(x+1, y));

		return ret;
	}

	public static void main(String [] args)
	{
		Tilemap map = new Tilemap(null, 16, 16, 2, 3);
		PathFinder pf = new PathFinder(map);
		pf.setGoal(2, 3);

		System.out.println("Find a path from the top left corner to the right bottom one.");

		long begin = System.currentTimeMillis();
		List<Node> nodes = pf.compute(new PathFinder.Node(0, 0));
		long end = System.currentTimeMillis();


		System.out.println("Time = " + (end - begin) + " ms" );
		System.out.println("Expanded = " + pf.getExpandedCounter());
		System.out.println("Cost = " + pf.getCost());
		
		if(nodes == null)
		{
			System.out.println("No path");
		}
		else
		{
			System.out.print("Path = ");
			for(Node n : nodes)
				System.out.print(n);
			System.out.println();
		}
	}

}
