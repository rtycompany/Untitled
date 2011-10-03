package com.javapunk.masks;

import com.javapunk.math.Vector2;

public interface Mask
{
	
	public abstract Vector2 collide(Mask mask);
	
}
