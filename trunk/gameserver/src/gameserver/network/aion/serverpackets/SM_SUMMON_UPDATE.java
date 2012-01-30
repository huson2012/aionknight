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

package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_SUMMON_UPDATE extends AionServerPacket
{
	private Summon summon;
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