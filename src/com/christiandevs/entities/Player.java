package com.christiandevs.entities;

import com.flume2d.graphics.Spritemap;
import com.flume2d.masks.AABB;
import com.flume2d.input.*;

public class Player extends Character
{

	public Player(int x, int y, int type)
	{
		super(x, y);
		sprite = new Spritemap("data/gfx/character.png", 16, 16);
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
	
	@Override
	public void update()
	{
		switch (state)
		{
			case StartTurn:
				if (Input.check(Key.SPACE))
				{
					state = PlayState.Attack;
				}
				if (Input.touched)
				{
					Touch touch = Input.touches.get(0);
					touch.x += scene.camera.x;
					touch.y += scene.camera.y;
					//if (canMoveTo(touch.x, touch.y))
					{
						getPathTo(touch.x, touch.y);
						state = PlayState.Move;
					}
				}
				break;
			case Move:
				if (!followPath())
				{
					// we took our turn
					state = PlayState.Attack;
				}
				break;
			case Attack:
				if (Input.check(Key.SPACE))
				{
					state = PlayState.Wait;
				}
				if (Input.touched)
				{
					Touch touch = Input.touches.get(0);
					touch.x += scene.camera.x;
					touch.y += scene.camera.y;
					Character enemy = attack(touch.x, touch.y);
					if (enemy != null)
					{
						enemy.takeDamage(attack);
						state = PlayState.Wait;
					}
				}
				break;
		}
		super.update();
	}
	
}
