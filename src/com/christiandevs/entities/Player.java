package com.christiandevs.entities;

import com.flume2d.graphics.Spritemap;
import com.flume2d.masks.AABB;
import com.flume2d.input.*;

public class Player extends Character
{

	public Player(int x, int y, int type)
	{
		super(x, y);
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
		if (Input.check(Key.SPACE))
		{
			// skip turn
			state = PlayState.Wait;
		}
		if (Input.touched)
		{
			Touch touch = Input.touches.get(0);
			touch.x += scene.camera.x;
			touch.y += scene.camera.y;
			if (canMoveTo(touch.x, touch.y))
			{
				getPathTo(touch.x, touch.y);
				state = PlayState.Moving;
			}
		}
	}
	
	@Override
	public void update()
	{
		switch (state)
		{
			case TakeTurn:
				processInput();
				break;
			case Moving:
				if (!followPath())
				{
					state = PlayState.Wait;
				}
				break;
		}
		super.update();
	}
	
}
