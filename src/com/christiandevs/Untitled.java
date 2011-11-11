package com.christiandevs;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.christiandevs.scenes.*;
import com.flume2d.Engine;

public class Untitled extends Engine
{
	private static final String frameTitle = "Game";
	
	@Override
	public void create()
	{
		super.create();
		Engine.setScene(new Battle());
	}
	
	public static void main(String[] args)
	{
		new LwjglApplication(new Untitled(), frameTitle, 64, 64, true);
	}
}