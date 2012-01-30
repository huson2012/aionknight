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

public class ItemSetStatEffectId extends StatEffectId
{
	// Effect for the number of item set parts equipped
	private int setpart;
	
	private ItemSetStatEffectId(int id, int setpart)
	{
		super(id,StatEffectType.ITEM_SET_EFFECT);
		this.setpart = setpart;
	}
	
	public static ItemSetStatEffectId getInstance (int id, int setpart)
	{
		return new ItemSetStatEffectId(id, setpart);
	}

	@Override
	public boolean equals(Object o)
	{
		boolean result = super.equals(o);
		result = (result)&&(o != null);
		result = (result)&&(o instanceof ItemSetStatEffectId);
		result = (result)&&(((ItemSetStatEffectId) o).setpart == setpart);
		return result;
	}

	@Override
	public int compareTo(StatEffectId o)
	{
		int result = super.compareTo(o);
		if (result == 0)
		{
			if (o instanceof ItemSetStatEffectId)
			{
				result = setpart - ((ItemSetStatEffectId) o).setpart;
			}
		}
		return result;
	}

	@Override
	public String toString()
	{
		final String str = super.toString() + ", parts:" + setpart;
		return str;
	}
}
