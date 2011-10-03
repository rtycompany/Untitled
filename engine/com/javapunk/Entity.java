package com.javapunk;

import java.util.*;

import com.javapunk.graphics.Graphic;
import com.javapunk.masks.*;
import com.javapunk.math.*;

public class Entity implements IWorldEntity
{
	
	public int x;
	public int y;
	
	public Entity()
	{
		this(0, 0, null, null);
	}
	
	public Entity(int x, int y)
	{
		this(x, y, null, null);
	}
	
	public Entity(int x, int y, Graphic graphic)
	{
		this(x, y, graphic, null);
	}
	
	public Entity(int x, int y, Graphic graphic, Mask mask)
	{
		this.x = x;
		this.y = y;
		this.graphic = graphic;
		this.mask = mask;
		this.layer = 0;
	}
	
	public Entity collide(String type, int x, int y)
	{
		if (world == null) return null;
		Iterator<Entity> it = world.getTypes(type).iterator();
		while (it.hasNext())
		{
			Entity e = it.next();
			Vector2 result = null;
			result = mask.collide(e.mask);
			if (result != null)
			{
				x += result.x;
				y += result.y;
				return e;
			}
		}
		return null;
	}

	public void update()
	{
		if (graphic != null && graphic.isActive()) graphic.update();
	}
	
	public void render()
	{
		if (graphic != null) graphic.render(Engine.getInstance().getGraphicsContext(), x, y);
	}
	
	public double distanceFrom(Entity e)
	{
		return Math.sqrt((x - e.x) * (x - e.x) + (y - e.y) * (y - e.y));
	}
	
	public double distanceFromPoint(int px, int py)
	{
		return Math.sqrt((x - px) * (x - px) + (y - py) * (y - py));
	}
	

	@Override
	public void setWorld(World world)
	{
		this.world = world;
	}

	@Override
	public boolean hasWorld()
	{
		return (world != null);
	}

	@Override
	public void added()
	{
	}

	@Override
	public void removed()
	{
	}
	
	public String name;
	public String type;
	public int layer;

	public Graphic graphic;
	public Mask mask;
	
	private World world;
	
}
