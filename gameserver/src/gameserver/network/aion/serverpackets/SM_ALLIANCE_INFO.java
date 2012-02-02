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

import gameserver.model.alliance.PlayerAlliance;
import gameserver.model.group.LootDistribution;
import gameserver.model.group.LootGroupRules;
import gameserver.model.group.LootRuleType;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_ALLIANCE_INFO extends AionServerPacket
{
	private PlayerAlliance alliance;
	
	public SM_ALLIANCE_INFO(PlayerAlliance alliance)
	{
		this.alliance = alliance;
	}
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, 4);
		writeD(buf, alliance.getObjectId());
		writeD(buf, alliance.getCaptainObjectId());
		
		int i = 0;
		for (int group : alliance.getViceCaptainObjectIds())
		{
			writeD(buf,group);
			i++;
		}

		for (;i<4 ; i++)
		{
			writeD(buf,0);
		}

		LootGroupRules lootRules = this.alliance.getLootAllianceRules();
		LootRuleType lootruletype = lootRules.getLootRule();
		LootDistribution autodistribution = lootRules.getAutodistribution();

		writeD(buf, lootruletype.getId());
		writeD(buf, autodistribution.getId());
		writeD(buf, lootRules.getCommon_item_above());
		writeD(buf, lootRules.getSuperior_item_above());
		writeD(buf, lootRules.getHeroic_item_above());
		writeD(buf, lootRules.getFabled_item_above());
		writeD(buf, lootRules.getEthernal_item_above());
		writeD(buf, lootRules.getOver_ethernal());
		writeD(buf, lootRules.getOver_over_ethernal());
		writeC(buf, 0);
        writeD(buf, 63);
		writeD(buf, 0);

		for (i = 0; i < 4; i++)
		{
			writeD(buf, i);
			writeD(buf, 1000+i);
		}

		writeD(buf, 0); // System message ID
		writeS(buf, ""); // System message text
	}
}
