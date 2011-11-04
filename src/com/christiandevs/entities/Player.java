package com.christiandevs.entities;

import com.flume2d.*;
import com.flume2d.graphics.Spritemap;
import com.flume2d.masks.AABB;
import com.flume2d.input.*;

public class Player extends Entity
{
	
	protected Spritemap sprite;
	private World world;

	public Player(int type, World world)
	{
		super(200, 200);
		
		sprite = new Spritemap("tmw_desert_spacing.png", 32, 32);
		type = type * 8;
		sprite.add("down",  new int[]{ type + 0, type + 1 });
		sprite.add("left",  new int[]{ type + 2, type + 3 });
		sprite.add("up",    new int[]{ type + 4, type + 5 });
		sprite.add("right", new int[]{ type + 6, type + 7 });
		sprite.play("down");
		setGraphic(sprite);
		
		setMask(new AABB(sprite.frameWidth, sprite.frameHeight));
		
		this.layer = 5;
		this.type = "player";
		this.world = world;
		
		Input.define("up", new int[]{ Key.UP });
		Input.define("down", new int[]{ Key.DOWN });
		Input.define("left", new int[]{ Key.LEFT });
		Input.define("right", new int[]{ Key.RIGHT });
	}
	
	private void processInput()
	{
		if (Input.check("up"))
			y += sprite.frameHeight;
		if (Input.check("down"))
			y -= sprite.frameHeight;
		if (Input.check("left"))
			x -= sprite.frameWidth;
		if (Input.check("right"))
			x += sprite.frameWidth;
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
