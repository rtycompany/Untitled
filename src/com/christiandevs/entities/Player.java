package com.christiandevs.entities;

import com.flume2d.*;
import com.flume2d.graphics.Spritemap;
import com.flume2d.masks.AABB;
import com.flume2d.utils.*;

public class Player extends Entity
{
	
	protected Spritemap sprite;
	private World world;

	public Player(int type, World world)
	{
		super(0, 0);
		
		setMask(new AABB(16, 16));
		
		sprite = new Spritemap("gfx/character.png", 16, 16);
		type = type * 8;
		sprite.add("down",  new int[]{ type + 0, type + 1 });
		sprite.add("left",  new int[]{ type + 2, type + 3 });
		sprite.add("up",    new int[]{ type + 4, type + 5 });
		sprite.add("right", new int[]{ type + 6, type + 7 });
		sprite.play("down");
		setGraphic(sprite);
		
		this.layer = 5;
		this.type = "player";
		this.world = world;
		
		Input.add("up", new int[]{ Key.UP });
		Input.add("down", new int[]{ Key.DOWN });
		Input.add("left", new int[]{ Key.LEFT });
		Input.add("right", new int[]{ Key.RIGHT });
	}
	
	private void processInput()
	{
		if (Input.check("up"))
			y += 16;
		if (Input.check("down"))
			y -= 16;
		if (Input.check("left"))
			x -= 16;
		if (Input.check("right"))
			x += 16;
	}
	
	private void moveCamera()
	{
		scene.camera.x = x - Engine.width / 2;
		scene.camera.y = y - Engine.height / 2;
		
		if (scene.camera.x < 0)
			scene.camera.x = 0;
//		if (scene.camera.x > world.width - Engine.width)
//			scene.camera.x = world.width - Engine.width;
		
		if (scene.camera.y < 0)
			scene.camera.y = 0;
//		if (scene.camera.y > world.height - Engine.height)
//			scene.camera.y = world.height - Engine.height;
	}
	
	public void update()
	{
		processInput();
		moveCamera();
		super.update();
	}
	
}
