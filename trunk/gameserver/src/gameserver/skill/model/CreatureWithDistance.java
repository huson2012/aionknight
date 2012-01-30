/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.skill.model;

import gameserver.model.gameobjects.Creature;

public class CreatureWithDistance implements Comparable<CreatureWithDistance>
{
	private Creature creature;
	private float distance = 0;
	
	public CreatureWithDistance(Creature creature, float distance)
	{
		this.creature = creature;
		this.distance = distance;
	}
	
	public Creature getCreature()
	{
		return this.creature;
	}
	
	public float getDistance()
	{
		return this.distance;
	}
	
	public void setCreature(Creature creature)
	{
		this.creature = creature;
	}
	
	public void setDistance(float distance)
	{
		this.distance = distance;
	}
	
	public boolean equals(Object o)
	{
		boolean	result = o!=null;
		result = (result)&&(o instanceof CreatureWithDistance);
		result = (result)&&(((CreatureWithDistance) o).distance ==this.distance);
		result = (result)&&(((CreatureWithDistance) o).creature.getObjectId()==this.creature.getObjectId());
		return result;
	}
	
	public int compareTo(CreatureWithDistance o)
	{
		int result = Math.round(this.distance - o.distance);
		
		if (result == 0)
			result = this.creature.getObjectId() - o.creature.getObjectId();
		
		return result;
	}
}