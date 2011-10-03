package com.javapunk;

public interface IWorldEntity
{
	public void setWorld(World world);
	public boolean hasWorld();
	
	public void added();
	public void removed();
}
