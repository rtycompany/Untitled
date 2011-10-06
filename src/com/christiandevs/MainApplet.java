package com.christiandevs;


import java.applet.Applet;
import java.awt.BorderLayout;

import com.flume2d.Engine;

public class MainApplet extends Applet
{
	private static final long serialVersionUID = 1L;

	public void init()
	{
		setLayout(new BorderLayout());
		MainApp.init();
		add(Engine.getInstance(), BorderLayout.CENTER);
	}
	
	public void start()
	{
		MainApp.start();
	}

	public void stop()
	{
		MainApp.stop();
	}
}