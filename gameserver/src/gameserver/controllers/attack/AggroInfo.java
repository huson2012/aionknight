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

package gameserver.controllers.attack;

import gameserver.model.gameobjects.AionObject;

public class AggroInfo
{
	private AionObject attacker;
	private int hate;
	private int damage;
	
	/**
	 * @param attacker
	 */
	AggroInfo(AionObject attacker)
	{
		this.attacker = attacker;
	}

	/**
	 * @return attacker
	 */
	public AionObject getAttacker()
	{
		return attacker;
	}

	/**
	 * @param damage
	 */
	public void addDamage(int damage)
	{
		this.damage += damage;
		if (this.damage < 0)
			this.damage = 0;
	}

	/**
	 * @param damage
	 */
	public void addHate(int damage)
	{
		this.hate += damage;
		if (this.hate < 1)
			this.hate = 1;
	}

	/**
	 * @return hate
	 */
	public int getHate()
	{
		return this.hate;
	}

	/**
	 * @param hate
	 */
	public void setHate(int hate)
	{
		this.hate = hate;
	}

	/**
	 * @return damage
	 */
	public int getDamage()
	{
		return this.damage;
	}

	/**
	 * @param damage
	 */
	public void setDamage(int damage)
	{
		this.damage = damage;
	}
}