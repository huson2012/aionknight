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
package gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;


import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;


/**

 * 
 */
public class SM_SUMMON_UPDATE extends AionServerPacket
{
	private Summon	summon;

	public SM_SUMMON_UPDATE(Summon summon)
	{
		this.summon = summon;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, summon.getLevel());
		writeH(buf, summon.getMode().getId());
		writeD(buf, 0);// unk
		writeD(buf, 0);// unk
		writeD(buf, summon.getLifeStats().getCurrentHp());
		writeD(buf, summon.getGameStats().getCurrentStat(StatEnum.MAXHP));
		writeD(buf, summon.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK));
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.PHYSICAL_DEFENSE));
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.MAGICAL_RESIST));
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.ACCURACY));
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.CRITICAL_RESIST));
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.BOOST_MAGICAL_SKILL));
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.MAGICAL_ACCURACY));		
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.MAGICAL_CRITICAL));
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.PARRY));
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.EVASION));
		writeD(buf, summon.getGameStats().getBaseStat(StatEnum.MAXHP));
		writeD(buf, summon.getGameStats().getBaseStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK));
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.PHYSICAL_DEFENSE));		
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.MAGICAL_RESIST));		
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.ACCURACY));
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.CRITICAL_RESIST));
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.BOOST_MAGICAL_SKILL));
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.MAGICAL_ACCURACY));		
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.MAGICAL_CRITICAL));
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.PARRY));
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.EVASION));		
	}

}
