package com.christiandevs.entities;

import java.util.*;

import com.flume2d.*;
import com.flume2d.graphics.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.tiled.*;
import com.christiandevs.ai.*;

public class World extends Entity
{
	
	private Player player;
	private GraphicList list;
	private PathFinder pathFinder;
	private Tilemap pathMap;
	private TiledMap tmx;
	
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
		tmx = TiledLoader.createMap(Gdx.files.internal(filename));
		if (tmx == null) return;
		
		width = tmx.width * tmx.tileWidth;
		height = tmx.height * tmx.tileHeight;
		
		pathMap = new Tilemap(null, tmx.tileWidth, tmx.tileHeight, tmx.width, tmx.height);
		
		loadMap(tmx);
		loadObjects(tmx);
		
		pathFinder = new PathFinder(pathMap);
	}
	
	private void loadMap(TiledMap tmx)
	{
		if (list.size() > 0)
			list.clear();
		
		// get the first tileset
		TileSet set = findTileSet(tmx, 1);
		
		Iterator<TiledLayer> it = tmx.layers.iterator();
		while (it.hasNext())
		{
			TiledLayer layer = it.next();
			Tilemap map = new Tilemap("gfx/tileset.png",
					tmx.tileWidth, tmx.tileHeight,
					tmx.width, tmx.height,
					set.spacing, set.margin);
			
			int tile = 0;
			for (int y = 0; y < layer.getHeight(); y++)
			{
				for (int x = 0; x < layer.getWidth(); x++)
				{
					tile = layer.tiles[y][x] - set.firstgid;
					map.setTile(x, y, tile);
					if (tile > -1)
						pathMap.setTile(x, y, tile);
				}
			}
			list.add(map);
		}
		setGraphic(list);
	}
	
	private TileSet findTileSet(TiledMap tmx, int gid)
	{
		TileSet set;
		Iterator<TileSet> it = tmx.tileSets.iterator();
		while (it.hasNext())
		{
			set = it.next();
			if (set.firstgid <= gid)
				return set;
		}
		return null;
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
				int y = obj.y;
				
				if (obj.type.equals("player"))
				{
					player.x = x;
					player.y = y;
				}
				else if (obj.type.equals("town") || obj.type.equals("dungeon"))
				{
					String mapFile = "maps/" + obj.type + "s/" + obj.name + ".tmx";
					scene.add(new Exit(x, y, obj.width, obj.height, mapFile, this));
				}
			}
		}
	}

	public List<PathNode> getPath(int x, int y, int gx, int gy)
	{
		return pathFinder.findPath(x / tmx.tileWidth, y / tmx.tileHeight, gx / tmx.tileWidth, gy / tmx.tileHeight);
	}
	
}
