package com.christiandevs.entities;

import com.flume2d.Entity;
import com.flume2d.graphics.Spritemap;
import com.flume2d.masks.AABB;

public class Monster extends Character
{
	
	private Entity target;
	
	public Monster(int x, int y)
	{
		super(x, y);
		
		sprite = new Spritemap("data/gfx/character.png", 16, 16);
		int type = 8;
		sprite.add("down",  new int[]{ type + 0, type + 1 }, 0.3f);
		sprite.add("left",  new int[]{ type + 2, type + 3 }, 0.3f);
		sprite.add("up",    new int[]{ type + 4, type + 5 }, 0.3f);
		sprite.add("right", new int[]{ type + 6, type + 7 }, 0.3f);
		sprite.play("down");
		setGraphic(sprite);
		
		setMask(new AABB(sprite.frameWidth, sprite.frameHeight));
		
		this.type = "monster";
	}
	
	public void update()
	{
		if (state == PlayState.StartTurn)
		{
			// we need to find a target and move towards it
			if (target == null)
			{
				target = scene.findClosest("player", x, y);
				getPathTo((int) target.x, (int) target.y);
			}
			
			if (!followPath())
			{
				// we finished following the path
				state = PlayState.Wait;
				target = null;
			}
		}
		super.update();
	}
	
}
