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

package gameserver.model.gameobjects.stats;

public class Stat
{
	private StatEnum type;
	private int origin;
	private int base;
	private int bonus;
	private int old;
	
	public Stat(StatEnum type, int origin)
	{
		this.type = type;
		this.origin = origin;
		this.base = origin;
		this.bonus = 0;
		this.old = 0;
	}
	
	public Stat(StatEnum type)
	{
		this(type,0);
	}
	
	public StatEnum getType()
	{
		return type;
	}
	
	public void increase(int amount, boolean bonus)
	{
		if (bonus)
		{
			this.bonus += amount;
		}
		else
		{
			this.base = amount;
		}
	}
	
	public void set(int value, boolean bonus)
	{
		if (bonus)
		{
			this.bonus = value;
		}
		else
		{
			this.base = value;
		}
	}
	
	public int getOrigin()
	{
		return origin;
	}
	
	public int getBase()
	{
		return base;
	}
	
	public int getBonus()
	{
		return bonus;
	}
	
	public int getCurrent()
	{
		return base+bonus;
	}

	public int getOld()
	{
		return old;
	}
	
	public void reset()
	{
		old = base + bonus;
		base = origin;
		bonus = 0;
	}
	
	@Override
	public String toString()
	{
		final String s = type+":"+base+ '+' +bonus;
		return s;
	}
}
