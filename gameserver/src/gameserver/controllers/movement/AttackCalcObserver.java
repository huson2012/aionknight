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

package gameserver.controllers.movement;

import gameserver.controllers.attack.AttackResult;
import gameserver.controllers.attack.AttackStatus;
import gameserver.model.gameobjects.Creature;
import java.util.List;

public class AttackCalcObserver
{
	/**
	 * @param status
	 * @return false
	 */
	public boolean checkStatus(AttackStatus status)
	{
		return false;
	}
	
	/**
	 * @param value
	 * @return value
	 */
	public void checkShield(List<AttackResult> attackList, Creature attacker)
	{
		
	}

	/**
	 * @param status
	 * @return
	 */
	public boolean checkAttackerStatus(AttackStatus status)
	{
		return false;
	}
	
	public float getBasePhysicalDamageMultiplier ()
	{
		return 1;
	}
	
	public float getBaseMagicalDamageMultiplier ()
	{
		return 1;
	}
}