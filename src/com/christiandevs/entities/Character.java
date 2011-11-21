package com.christiandevs.entities;

import java.util.*;
import com.christiandevs.rpg.*;
import com.christiandevs.rpg.item.*;
import com.flume2d.Engine;
import com.flume2d.Entity;
import com.flume2d.ai.PathNode;
import com.flume2d.graphics.Spritemap;

public abstract class Character extends Entity
{
	
	protected enum PlayState
	{
		Wait,
		StartTurn,
		Move,
		Attack
	}
	
	public String name;
	
	protected String classType;
	
	protected Stat health;
	protected Stat energy;
	protected Stat fatigue;
	
	protected int evade;
	protected int strength;
	protected int stamina;
	protected int accuracy;
	
	protected Weapon weapon;
	protected Armor armor;
	protected HashMap<SkillType, Integer> skills;
	
	protected int level;
	protected int moveSpaces;
	
	protected Spritemap sprite;
	protected Map map;
	
	private List<PathNode> pathNodes;
	private PathNode pathTarget;
	
	protected PlayState state;
	
	public Character()
	{
		this(0, 0);
	}
	
	public Character(int x, int y)
	{
		super(x, y);
		
		state = PlayState.Wait;
		initSkills();
		
		health = new Stat(50);
		energy = new Stat(20);
		fatigue = new Stat(20);
		
		strength = 1;
		stamina = 2;
		level = 1;
		moveSpaces = 3;
	}
	
	/**
	 * Creates a skill hashmap with all values initialized to 1
	 */
	private void initSkills()
	{
		skills = new HashMap<SkillType, Integer>();
		SkillType[] types = SkillType.values();
		for (int i = 0; i < types.length; i++)
		{
			skills.put(types[i], 1);
		}
	}
	
	/**
	 * Called when a character levels up
	 */
	public void levelUp()
	{
		level += 1;
		health.modifyMax(level * 5);
		health.refill();
	}
	
	/**
	 * Character takes a hit
	 * @param value the amount of damage taken
	 */
	public void takeDamage(int value)
	{
		health.drain(value);
		if (health.depleted())
			kill();
	}
	
	/**
	 * Check if the character is dead
	 * @return Am I Dead?
	 */
	public boolean isDead()
	{
		return health.depleted();
	}
	
	/**
	 * Kills off the character
	 */
	public void kill()
	{
		scene.remove(this);
	}
	
	/**
	 * Starts the character turn by setting the state
	 */
	public void startTurn()
	{
		state = PlayState.StartTurn;
	}
	
	/**
	 * Are we still waiting for the character to do something?
	 * @return character is still thinking
	 */
	public boolean isWaiting()
	{
		return (state == PlayState.Wait);
	}
	
	/**
	 * A dumb easing function that moves the camera
	 * @param start the start point
	 * @param target the end point
	 * @return the amount to ease
	 */
	private float ease(float start, float target)
	{
		if (Math.abs(target - start) < 4)
			return target;
		
		if (target < start)
			return start - 2;
		
		return start + 2;
	}
	
	/**
	 * Moves or snaps the camera to focus on the character
	 * @param smooth moves gradually over time
	 */
	public void focusCamera(boolean smooth)
	{
		float targetX = x - Engine.width / 2 + sprite.frameWidth / 2;
		float targetY = y - Engine.height / 2 + sprite.frameHeight / 2;
		if (smooth)
		{
			// TODO: use a tween or something instead of this hacked method
			scene.camera.x = ease(scene.camera.x, targetX);
			scene.camera.y = ease(scene.camera.y, targetY);
		}
		else
		{
			scene.camera.x = targetX;
			scene.camera.y = targetY;
		}
	}
	
	/**
	 * How aware is this character of another
	 * @param c The character to check against
	 * @return a value between 0 and 1 on how aware we are of the character
	 */
	protected float awareness(Character c)
	{
		// back facing should be something like 0.25
		return 0;
	}
	
	protected int getWeaponRating()
	{
		if (weapon == null)
			return 1;
		return weapon.power + skills.get(weapon.skill);
	}
	
	protected int getArmorRating()
	{
		if (armor == null)
			return 0;
		return armor.defense + skills.get(armor.skill);
	}
	
	protected SkillType getArmorSkill()
	{
		if (armor == null)
			return SkillType.ArmorNone;
		
		return armor.skill;
	}
	
	protected SkillType getWeaponSkill()
	{
		if (weapon == null)
			return SkillType.WeaponUnarmed;
		
		return weapon.skill;
	}
	
	protected void increaseSkill(SkillType skill, int value)
	{
		skills.put(skill, skills.get(skill) + value);
	}
	
	/**
	 * Finds an enemy and attacks if one is found
	 * @param type the enemy type to search for
	 * @param dx the x-axis position to search at
	 * @param dy the y-axis position to search at
	 * @return if we successfully attacked an enemy
	 */
	protected boolean attack(String type, int dx, int dy)
	{
		Entity e = scene.findAt(type, dx, dy);
		if (e == null)
			return false;
		
		Character enemy = (Character) e;
		return attack(enemy);
	}
	
	/**
	 * Attacks an enemy character
	 * @param enemy the character to attack
	 * @return if we successfully attacked the character
	 */
	protected boolean attack(Character enemy)
	{
		fatigue.buff(stamina);
		if (fatigue.depleted())
			return false;
		
		if (accuracy - fatigue.getValue() < (enemy.evade - fatigue.getValue()) * enemy.awareness(this))
		{
			// Missed enemy, increase skill
			enemy.increaseSkill(enemy.getArmorSkill(), 1);
			fatigue.drain(1);
			return false;
		}
		else
		{
			int attack = (strength * getWeaponRating());
			int defense = (enemy.strength * enemy.getArmorRating());
			int damage = attack - defense;
			enemy.takeDamage(damage);
			
			increaseSkill(getWeaponSkill(), 1);
			fatigue.drain(2);
		}
		return true;
	}
	
	/**
	 * Sets the map value
	 * @param map the map object this character exists on
	 */
	public void setMap(Map map)
	{
		this.map = map;
	}
	
	/**
	 * Check if we can move to a destination
	 * @param dx the x-axis destination
	 * @param dy the y-axis destination
	 * @return if we can move to that point
	 */
	protected boolean canMoveTo(int dx, int dy)
	{
		LinkedList<PathNode> nodes = new LinkedList<PathNode>();
		getWalkableArea(nodes, (int) x / map.tileWidth, (int) y / map.tileHeight, moveSpaces);
		return pathContains(nodes, dx / map.tileWidth, dy / map.tileHeight);
	}
	
	/**
	 * Check if a path contains a point
	 * @param path the path to check
	 * @param x
	 * @param y
	 * @return if the path contains the point
	 */
	private boolean pathContains(List<PathNode> path, int x, int y)
	{
		Iterator<PathNode> it = path.iterator();
		while (it.hasNext())
		{
			PathNode node = it.next();
			if (node.x == x && node.y == y)
				return true;
		}
		return false;
	}
	
	/**
	 * Determine the walkable area of the character (recursive)
	 * @param nodes a list of nodes to populate with the walkable area
	 * @param x the starting x-axis value
	 * @param y the starting y-axis value
	 * @param spaces the number of spaces away from the starting point
	 */
	protected void getWalkableArea(List<PathNode> nodes, int x, int y, int spaces)
	{
		if (spaces <= 0) return;
		
		int[] xvals = new int[]{x - 1, x + 1, x,     x    };
		int[] yvals = new int[]{y,     y,     y - 1, y + 1};
		
		for (int i = 0; i < xvals.length; i++)
		{
			x = xvals[i]; y = yvals[i];
			if (map.isWalkable(x, y) && !pathContains(nodes, x, y))
			{
				nodes.add(new PathNode(x, y));
				getWalkableArea(nodes, x, y, spaces - 1);
			}
		}
	}
	
	/**
	 * Get a path to the destination based on current position
	 * @param dx the x-axis destination value
	 * @param dy the y-axis destination value
	 */
	protected void getPathTo(int dx, int dy)
	{
		pathNodes = map.getPath((int) x, (int) y, dx, dy);
		pathTarget = getNextPathNode();
	}
	
	/**
	 * Gets the next path node
	 * @return the next path node
	 */
	private PathNode getNextPathNode()
	{
		if (pathNodes != null && pathNodes.size() > 0)
			return pathNodes.remove(0);
		return null;
	}
	
	/**
	 * Follows along the given path, if there is one
	 * @return still following the path?
	 */
	protected boolean followPath()
	{
		if (pathTarget == null)
			return false;
		
		int destX = pathTarget.x * map.tileWidth;
		int destY = pathTarget.y * map.tileHeight;
		int moveSpeed = 2;
		if (x == destX && y == destY)
		{
			// we arrived at the target destination, get the next target
			pathTarget = getNextPathNode();
		}
		else
		{
			if (x < destX)
			{
				x += moveSpeed;
				sprite.play("right");
			}
			else if (x > destX)
			{
				x -= moveSpeed;
				sprite.play("left");
			}
			
			if (y < destY)
			{
				y += moveSpeed;
				sprite.play("down");
			}
			else if (y > destY)
			{
				y -= moveSpeed;
				sprite.play("up");
			}
		}
		return true;
	}

}
