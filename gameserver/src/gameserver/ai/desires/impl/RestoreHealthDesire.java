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

package gameserver.ai.desires.impl;

import gameserver.ai.AI;
import gameserver.ai.desires.AbstractDesire;
import gameserver.ai.events.Event;
import gameserver.model.gameobjects.Creature;
import gameserver.model.siege.FortressGeneral;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;

public class RestoreHealthDesire extends AbstractDesire
{	
	private Creature owner;
	private int restoreHpValue;
	private int restoreMpValue;

	public RestoreHealthDesire(Creature owner, int desirePower)
	{
		super(desirePower);
		this.owner = owner;
		restoreHpValue = owner.getLifeStats().getMaxHp() / 5;
		restoreMpValue = owner.getLifeStats().getMaxMp() / 5;
	}
	
	@Override
	public boolean handleDesire(AI<?> ai)
	{
		if(owner == null || owner.getLifeStats().isAlreadyDead() || owner instanceof FortressGeneral)
			return false;
		
		if(!owner.isInCombat())
		{
			owner.getLifeStats().increaseHp(TYPE.NATURAL_HP, restoreHpValue);
			owner.getLifeStats().increaseMp(TYPE.NATURAL_MP, restoreMpValue);
		}

		if(owner.getLifeStats().isFullyRestoredHpMp())
		{
			ai.handleEvent(Event.RESTORED_HEALTH);
			return false;
		}
		return true;
	}

	@Override
	public int getExecutionInterval()
	{
		return 1;
	}

	@Override
	public void onClear()
	{
		// TODO: Auto-generated method stub
	}
}