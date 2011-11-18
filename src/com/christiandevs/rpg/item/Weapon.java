package com.christiandevs.rpg.item;

import com.christiandevs.rpg.SkillType;

public class Weapon extends Item
{

	public SkillType skill;
	public int power;
	public int accuracy;
	
	public Weapon()
	{
		super("Short Sword");
		skill = SkillType.WeaponBlade;
		power = 1;
		accuracy = 1;
	}

}
