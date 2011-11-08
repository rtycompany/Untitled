package com.christiandevs.rpg;

public class Stat
{

	int maxValue;
	int value;
	
	/**
	 * Creates a stat at the default value
	 * @param defaultValue the value to set as max and to full
	 */
	public Stat(int defaultValue)
	{
		maxValue = value = defaultValue;
	}
	
	/**
	 * Decreases the stat value
	 * @param drainValue a positive value to decrease by
	 */
	public void drain(int drainValue)
	{
		value -= Math.abs(drainValue);
		if (value < 0)
			value = 0;
	}
	
	/**
	 * Increases the stat value
	 * @param buffValue a positive value to increase by
	 */
	public void buff(int buffValue)
	{
		value += Math.abs(buffValue);
		if (value > maxValue)
			value = maxValue;
	}
	
	/**
	 * Check if the stat value is zero
	 * @return true if the stat is zero
	 */
	public boolean depleted()
	{
		return (value == 0);
	}
	
	/**
	 * Check if the stat value is at max
	 * @return true if the stat is maxed
	 */
	public boolean full()
	{
		return (value == maxValue);
	}
	
	/**
	 * Gets the current value
	 * @return the stat value
	 */
	public int getValue()
	{
		return value;
	}
	
	public void refill()
	{
		value = maxValue;
	}
	
	/**
	 * Increases/decreases max value based on modifier
	 * @param maxModifier the amount to modify the stat max (up/down)
	 */
	public void modifyMax(int maxModifier)
	{
		maxValue += maxModifier;
	}
	
}