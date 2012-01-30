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

package gameserver.model.gameobjects.stats.modifiers;

import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.gameobjects.stats.StatModifierPriority;

public class AddModifier extends SimpleModifier
{
	@Override
	public int apply(int stat, int currentStat)
	{
		/**
		 * default min limit is 0, default max limit is infinity
		 */
		boolean applyLimit = true;
		
		switch(getStat())
		{
			case SPEED:
			case FLY_SPEED:
			case ATTACK_SPEED:
			case FIRE_RESISTANCE:
			case EARTH_RESISTANCE:
			case WIND_RESISTANCE:
			case WATER_RESISTANCE:
			case BOOST_MAGICAL_SKILL:
				applyLimit = false;
				break;
		}
		int chkValue;
		
		if(isBonus())
		{
			chkValue = Math.round(value);
			if(applyLimit)
			{
				if(chkValue + currentStat < 0)
					return -currentStat;
				else
					return chkValue;
			}
			else
				return chkValue;
		}
		else
		{
			chkValue =  Math.round(stat + value);
			if(applyLimit)
			{
				if(chkValue < 0)
					return 0;
				else
					return chkValue;
			}
			else
				return chkValue;
		}
	}

	@Override
	public StatModifierPriority getPriority()
	{
		return StatModifierPriority.MEDIUM;
	}
	
	public static AddModifier newInstance (StatEnum stat, int value, boolean isBonus)
	{
		AddModifier m = new AddModifier();
		m.setStat(stat);
		m.setValue(value);
		m.setBonus(isBonus);
		m.nextId();
		return m;
	}
}
