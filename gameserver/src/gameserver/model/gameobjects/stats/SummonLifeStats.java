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