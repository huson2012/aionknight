/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.model.gameobjects.stats;

/**
 * @author blakawk
 *
 */
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
		final String s = type+":"+base+"+"+bonus;
		return s;
	}
}
