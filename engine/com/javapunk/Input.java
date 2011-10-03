package com.javapunk;

import java.awt.event.*;
import java.util.*;

public class Input implements KeyListener, FocusListener, MouseListener, MouseMotionListener
{
	private static boolean[] keys = new boolean[256];
	private static HashMap<String, int[]> keymap = new HashMap<String, int[]>();
	
	public static void add(String key, int[] values)
	{
		keymap.put(key, values);
	}
	
	public static boolean pressed(String key)
	{
		int[] values = keymap.get(key);
		if (values != null)
		{
			for (int i = 0; i < values.length; i++)
			{
				if (keys[values[i]])
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean pressed(int key)
	{
		return keys[key];
	}
	
	public static boolean released(String key)
	{
		int[] values = keymap.get(key);
		if (values != null)
		{
			for (int i = 0; i < values.length; i++)
			{
				if (keys[values[i]])
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public static boolean released(int key)
	{
		return !keys[key];
	}

	public void mouseDragged(MouseEvent e) { }
	public void mouseMoved(MouseEvent e) { }
	public void mouseClicked(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }

	public void focusGained(FocusEvent e)
	{
	}

	public void focusLost(FocusEvent e)
	{
		for (int i = 0; i < keys.length; i++)
		{
			keys[i] = false;
		}
	}

	public void keyPressed(KeyEvent e)
	{
		int code = e.getKeyCode(); 
		if (code > 0 && code < keys.length)
		{
			keys[code] = true;
		}
	}

	public void keyReleased(KeyEvent e)
	{
		int code = e.getKeyCode(); 
		if (code > 0 && code < keys.length)
		{
			keys[code] = false;
		}
	}

	public void keyTyped(KeyEvent e)
	{
	}
}