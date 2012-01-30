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

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.network.aion.serverpackets.SM_SUMMON_UPDATE;
import gameserver.services.LifeStatsRestoreService;
import gameserver.utils.PacketSendUtility;

public class SummonLifeStats extends CreatureLifeStats<Summon>
{

	public SummonLifeStats(Summon owner)
	{
		super(owner, owner.getGameStats().getCurrentStat(StatEnum.MAXHP), owner.getGameStats().getCurrentStat(
			StatEnum.MAXMP));
	}

	@Override
	protected void onIncreaseHp(TYPE type, int value, int skillId, int logId)
	{
		Creature master = getOwner().getMaster();
		sendAttackStatusPacketUpdate(type, value, skillId, logId);
		
		if(master instanceof Player)
		{
			PacketSendUtility.sendPacket((Player) master, new SM_SUMMON_UPDATE(getOwner()));
		}
	}

	@Override
	protected void onIncreaseMp(TYPE type, int value, int skillId, int logId)
	{
		// TODO Auto-generated method stub	
	}

	@Override
	protected void onReduceHp()
	{
		// TODO Auto-generated method stub	
	}

	@Override
	protected void onReduceMp()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public Summon getOwner()
	{
		return (Summon) super.getOwner();
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
