package com.christiandevs;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.christiandevs.worlds.*;
import com.flume2d.Engine;

public class MainApp
{
	private static final String frameTitle = "Untitled Game";
	private static final boolean fullscreen = false;
	
	private static final Engine engine = Engine.getInstance();
	
	public static void init()
	{
		engine.init(640, 480);
		engine.world = new Game();
	}
	
	public static void start()
	{
		engine.start();
	}
	
	public static void stop()
	{
		engine.stop();
	}
	
	public static void main(String[] args)
	{
		init();
		JFrame frame = new JFrame(frameTitle);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(engine, BorderLayout.CENTER);
		
		if (fullscreen)
		{
			frame.setUndecorated(true);
			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		}
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		frame.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				Component c = (Component)e.getSource();
				Dimension newSize = c.getSize();
				engine.setScreen(newSize.width, newSize.height);
			}
			public void componentHidden(ComponentEvent e) { }
			public void componentMoved(ComponentEvent e) { }
			public void componentShown(ComponentEvent e) { }
		});

		start();
	}
}