package com.christiandevs.entities;

import com.flume2d.Engine;
import com.flume2d.graphics.Spritemap;
import com.flume2d.masks.AABB;

public class Monster extends Character
{
	
	private Spritemap sprite;
	private float moveTime;

	public Monster(int x, int y, World world)
	{
		super(x, y, world);
		
		sprite = new Spritemap("gfx/monster.png", 16, 16);
		int type = 0;
		sprite.add("down",  new int[]{ type + 0, type + 1 }, 0.3f);
		sprite.add("left",  new int[]{ type + 2, type + 3 }, 0.3f);
		sprite.add("up",    new int[]{ type + 4, type + 5 }, 0.3f);
		sprite.add("right", new int[]{ type + 6, type + 7 }, 0.3f);
		sprite.play("down");
		setGraphic(sprite);
		
		setMask(new AABB(sprite.frameWidth, sprite.frameHeight));
		
		this.type = "monster";
	}
	
	private void move()
	{
		x += (int) Math.floor(Math.random() * 2 - 1);
		y += (int) Math.floor(Math.random() * 2 - 1);
	}
	
	@Override
	public void update()
	{
		moveTime -= Engine.elapsed;
		if (moveTime < 0)
			move();
		super.update();
	}
	
}
