package com.christiandevs.entities;

import com.flume2d.*;
import com.flume2d.graphics.Spritemap;
import com.flume2d.masks.AABB;
import com.flume2d.input.*;

public class Player extends Character
{
	
	protected Spritemap sprite;

	public Player(int type, World world)
	{
		super(world);
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
	}
	
	private void processInput()
	{
		if (Input.touched)
		{
			Touch touch = Input.touches.get(0);
			touch.x += scene.camera.x;
			touch.y += scene.camera.y;
			getPathTo(touch.x, touch.y);
			/*
			if (path == null)
				System.out.println("No path");
			else
			{
				for (PathNode node : path)
				{
					System.out.println(node);
				}
			}
			*/
		}
	}
	
	private void moveCamera()
	{
		scene.camera.x = x - Engine.width / 2 + sprite.frameWidth / 2;
		scene.camera.y = y - Engine.height / 2 + sprite.frameHeight / 2;
		
		if (scene.camera.x < 0)
			scene.camera.x = 0;
		if (scene.camera.x > world.width - Engine.width)
			scene.camera.x = world.width - Engine.width;
		
		if (scene.camera.y < 0)
			scene.camera.y = 0;
		if (scene.camera.y > world.height - Engine.height)
			scene.camera.y = world.height - Engine.height;
	}
	
	public void update()
	{
		processInput();
		followPath();
		moveCamera();
		super.update();
	}
	
}
