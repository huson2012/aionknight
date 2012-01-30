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

package gameserver.model.gameobjects.stats.id;

import gameserver.model.gameobjects.stats.StatEffectType;

public class StatEffectId implements Comparable<StatEffectId>
{
	protected int id;
	protected StatEffectType type;
	
	protected StatEffectId(int id, StatEffectType type)
	{
		this.id = id;
		this.type = type;
	}
	
	public static StatEffectId getInstance(int id, StatEffectType type)
	{
		return new StatEffectId(id,type);
	}
	
	@Override
	public int hashCode()
	{
		return id;
	}
	
	@Override
	public boolean equals(Object o)
	{
		boolean result = true;
		result = (result)&&(o!=null);
		result = (result)&&(o instanceof StatEffectId);
		result = (result)&&(((StatEffectId)o).id==id);
		result = (result)&&(((StatEffectId)o).type.getValue()==type.getValue());
		return result;
	}
	
	@Override
	public int compareTo(StatEffectId o)
	{
		int result = 0;
		if (o==null)
		{
			result = id;
		}
		else
		{
			result = type.getValue() - o.type.getValue();
			if (result==0)
			{
				result = id - o.id;
			}
		}
		return result;
	}
	
	@Override
	public String toString()
	{
		final String str = "id: "+id+", type: "+type;
		return str;
	}
	
	public StatEffectType getType()
	{
		return type;
	}
}
