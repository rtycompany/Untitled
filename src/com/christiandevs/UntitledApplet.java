package com.christiandevs;

import com.badlogic.gdx.backends.lwjgl.LwjglApplet;
import com.christiandevs.scenes.Battle;
import com.flume2d.Engine;

public class UntitledApplet extends Engine
{
	
	@Override
	public void create()
	{
		super.create();
		Engine.setScene(new Battle());
	}
	
	public static void main(String[] args)
	{
		new LwjglApplet(new Untitled(), true);
	}
}
