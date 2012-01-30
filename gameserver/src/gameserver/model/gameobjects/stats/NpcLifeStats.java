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

import gameserver.model.gameobjects.Npc;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.services.LifeStatsRestoreService;

public class NpcLifeStats extends CreatureLifeStats<Npc>
{
	/**
	 * 
	 * @param owner
	 */
	public NpcLifeStats(Npc owner)
	{
		super(owner, owner.getGameStats().getCurrentStat(StatEnum.MAXHP), owner.getGameStats().getCurrentStat(
			StatEnum.MAXMP));
	}

	@Override
	protected void onIncreaseHp(TYPE type, int value, int skillId, int logId)
	{
		sendAttackStatusPacketUpdate(type, value, skillId, logId);
	}

	@Override
	protected void onIncreaseMp(TYPE type, int value, int skillId, int logId)
	{
		// nothing todo
	}

	@Override
	protected void onReduceHp()
	{
		// nothing todo		
	}

	@Override
	protected void onReduceMp()
	{
		// nothing todo	
	}
	
	@Override
	protected void triggerRestoreTask()
	{
		if(lifeRestoreTask == null && !alreadyDead)
		{
			this.lifeRestoreTask = LifeStatsRestoreService.getInstance().scheduleHpRestoreTask(this);
		}
	}
}
