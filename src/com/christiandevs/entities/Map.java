package com.christiandevs.entities;

import java.util.*;

import com.flume2d.*;
import com.flume2d.ai.*;
import com.flume2d.graphics.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.tiled.*;

public class Map extends Entity implements IWalkable
{
	
	private Player player;
	private GraphicList list;
	private PathFinder pathFinder;
	private Tilemap pathMap;
	
	public int tileWidth, tileHeight;
	public int width, height;

	public Map()
	{
		list = new GraphicList();
		player = new Player(0);
	}
	
	public void added()
	{
		scene.add(player);
	}
	
	public void load(String filename)
	{
		TiledMap tmx = TiledLoader.createMap(Gdx.files.internal(filename));
		if (tmx == null) return;
		
		width = tmx.width * tmx.tileWidth;
		height = tmx.height * tmx.tileHeight;
		tileWidth = tmx.tileWidth;
		tileHeight = tmx.tileHeight;
		
		pathMap = new Tilemap(null, tmx.tileWidth, tmx.tileHeight, tmx.width, tmx.height);
		pathFinder = new PathFinder(tmx.width, tmx.height, this);
		
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
				
				MapEntity e = null;
				if (obj.type.equals("player"))
				{
					player.x = obj.x;
					player.y = obj.y;
					e = player;
				}
				else if (obj.type.equals("monster"))
				{
					e = new Monster(obj.x, obj.y);
					scene.add(e);
				}
				
				if (e != null)
					e.setMap(this);
			}
		}
	}

	public List<PathNode> getPath(int x, int y, int goalx, int goaly)
	{
		pathFinder.calculateNearestPoint = true; 
		return pathFinder.findPath(
				x / tileWidth, y / tileHeight,
				goalx / tileWidth, goaly / tileHeight
			);
	}

	@Override
	public boolean isWalkable(int x, int y)
	{
		int tile = pathMap.getTile(x, y);
		
		// check which tiles are "solid"
		if (tile < 1 || tile == 3 ||
			(tile > 7 && tile < 14) ||
			(tile > 15 && tile < 19) ||
			(tile > 23 && tile < 28))
			return false;
		return true;
	}
	
}