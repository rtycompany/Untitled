package com.christiandevs.entities;

import java.util.Iterator;

import com.flume2d.*;
import com.flume2d.graphics.*;
import com.flume2d.input.*;
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
		load("example.tmx");
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
		
		// get the first tileset
		TileSet set = findTileSet(tmx, 1);
		
		Iterator<TiledLayer> it = tmx.layers.iterator();
		while (it.hasNext())
		{
			TiledLayer layer = it.next();
			Tilemap map = new Tilemap("tmw_desert_spacing.png",
					tmx.tileWidth, tmx.tileHeight,
					tmx.width, tmx.height,
					set.spacing, set.margin);
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
