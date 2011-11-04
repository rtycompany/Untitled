package com.christiandevs;

import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.christiandevs.scenes.*;
import com.flume2d.Engine;

public class MainApp extends Engine
{
	private static final String frameTitle = "Game";
	
	@Override
	public void create()
	{
		super.create();
		Engine.setScene(new Game());
	}
	
	public static void main(String[] args)
	{
		new JoglApplication(new MainApp(), frameTitle, 800, 600, true);
	}
}