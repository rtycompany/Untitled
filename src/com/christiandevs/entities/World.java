package com.christiandevs.entities;

import java.util.Iterator;

import com.flume2d.*;
import com.flume2d.graphics.*;
import com.flume2d.utils.Input;
import com.flume2d.utils.Key;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.tiled.*;

public class World extends Entity
{
	
	private Player player;
	private GraphicList list;
	
	public int width, height;

	public World()
	{
		super();
		
		list = new GraphicList();
	}
	
	public void added()
	{
		player = new Player(0, this);
		scene.add(player);
		load("maps/world.tmx");
	}
	
	public void load(String filename)
	{
		TiledMap tmx = TiledLoader.createMap(Gdx.files.internal(filename));
		if (tmx == null) return;
		
		width = tmx.width;
		height = tmx.height;
		
		loadMap(tmx);
		loadObjects(tmx);
	}
	
	private void loadMap(TiledMap tmx)
	{
		if (list.size() > 0)
			list.clear();
		
		Iterator<TiledLayer> it = tmx.layers.iterator();
		while (it.hasNext())
		{
			TiledLayer layer = it.next();
			Tilemap map = new Tilemap("gfx/tileset.png",
					tmx.width * tmx.tileWidth, tmx.height * tmx.tileHeight,
					tmx.tileWidth, tmx.tileHeight);
			for (int y = 0; y < layer.getHeight(); y++)
			{
				for (int x = 0; x < layer.getWidth(); x++)
				{
					map.setTile(x, y, layer.tiles[y][x] - 1);
				}
			}
			list.add(map);
		}
		setGraphic(list);
	}
	
	private void loadObjects(TiledMap tmx)
	{
		Iterator<TiledObjectGroup> groups = tmx.objectGroups.iterator();
		while (groups.hasNext())
		{
			TiledObjectGroup group = groups.next();
			Iterator<TiledObject> it = group.objects.iterator();
			while (it.hasNext())
			{
				TiledObject obj = it.next();
				
				int x = obj.x;
				int y = (tmx.height * tmx.tileHeight) - obj.y;
				
				if (obj.type.equals("player"))
				{
					player.x = x + 8;
					player.y = y - 8;
				}
				else if (obj.type.equals("town") || obj.type.equals("dungeon"))
				{
					String mapFile = "maps/" + obj.type + "s/" + obj.name + ".tmx";
					scene.add(new Exit(x, y, obj.width, obj.height, mapFile, this));
				}
			}
		}
	}
	
}
