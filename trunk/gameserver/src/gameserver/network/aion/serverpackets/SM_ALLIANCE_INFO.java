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

		writeD(buf, 0);
		writeD(buf, 0);
	}
}