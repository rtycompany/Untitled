package com.christiandevs.entities;

import java.util.List;

import com.christiandevs.ai.*;
import com.flume2d.*;
import com.flume2d.graphics.Spritemap;
import com.flume2d.masks.AABB;
import com.flume2d.input.*;

public class Player extends Entity
{
	
	protected Spritemap sprite;
	private World world;
	
	private List<PathNode> path;
	private int pathIndex;

	public Player(int type, World world)
	{
		super(0, 0);
		
		sprite = new Spritemap("gfx/character.png", 16, 16);
		type = type * 8;
		sprite.add("down",  new int[]{ type + 0, type + 1 }, 0.3f);
		sprite.add("left",  new int[]{ type + 2, type + 3 }, 0.3f);
		sprite.add("up",    new int[]{ type + 4, type + 5 }, 0.3f);
		sprite.add("right", new int[]{ type + 6, type + 7 }, 0.3f);
		sprite.play("down");
		setGraphic(sprite);
		
		setMask(new AABB(sprite.frameWidth, sprite.frameHeight));
		
		this.layer = 5;
		this.type = "player";
		this.world = world;
	}
	
	private void processInput()
	{
		if (Input.touched)
		{
			Touch touch = Input.touches.get(0);
			touch.x += scene.camera.x;
			touch.y += scene.camera.y;
			path = world.getPath((int) x, (int) y, touch.x, touch.y);
			pathIndex = 0;
			if (path == null)
				System.out.println("No path");
			else
			{
				for (PathNode node : path)
				{
					System.out.println(node);
				}
			}
		}
	}
	
	private void moveCamera()
	{
		scene.camera.x = x - Engine.width / 2;
		scene.camera.y = y - Engine.height / 2;
		
		if (scene.camera.x < 0)
			scene.camera.x = 0;
		if (scene.camera.x > world.width - Engine.width)
			scene.camera.x = world.width - Engine.width;
		
		if (scene.camera.y < 0)
			scene.camera.y = 0;
		if (scene.camera.y > world.height - Engine.height)
			scene.camera.y = world.height - Engine.height;
	}
	
	private void followPath()
	{
		if (path != null && pathIndex < path.size())
		{
			PathNode n = path.get(pathIndex);
			if (n != null)
			{
				x = n.x * 16 + 8;
				y = n.y * 16 + 8;
				pathIndex += 1;
			}
		}
	}
	
	public void update()
	{
		processInput();
		followPath();
		moveCamera();
		super.update();
	}
	
}
