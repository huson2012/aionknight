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

package gameserver.network.aion.clientpackets;

import gameserver.model.alliance.PlayerAlliance;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.LootDistribution;
import gameserver.model.group.LootGroupRules;
import gameserver.model.group.LootRuleType;
import gameserver.model.group.PlayerGroup;
import gameserver.network.aion.AionClientPacket;

public class CM_DISTRIBUTION_SETTINGS extends AionClientPacket
{
		
	private LootRuleType lootrules; //0-free-for-all, 1-round-robin 2-leader
	private LootDistribution autodistribution;
	//rare item distribution
	//0-normal, 2-Roll-dice,3-bid
	private int common_item_above;
	private int superior_item_above;
	private int heroic_item_above;
	private int fabled_item_above;
	private int ethernal_item_above;
	private int over_ethernal;
	private int over_over_ethernal;
	
	public CM_DISTRIBUTION_SETTINGS(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		@SuppressWarnings("unused")
		int unk1 = readD();

		int rules = readD();
		switch(rules)
		{
			case 0:
				this.lootrules = LootRuleType.FREEFORALL;
				break;
			case 1:
				this.lootrules = LootRuleType.ROUNDROBIN;
				break;
			case 2:
				this.lootrules = LootRuleType.LEADER;
				break;
			default:
				this.lootrules = LootRuleType.FREEFORALL;
				break;
		}

		int autoDist = readD();
		switch(autoDist)
		{
			case 0:
				this.autodistribution = LootDistribution.NORMAL;
				break;
			case 2:
				this.autodistribution = LootDistribution.ROLL_DICE;
				break;
			case 3:
				this.autodistribution = LootDistribution.BID;
				break;
			default: // It happens!
				this.autodistribution = LootDistribution.NORMAL;
				break;
		}

		this.common_item_above = readD();
		this.superior_item_above = readD();
		this.heroic_item_above = readD();
		this.fabled_item_above = readD();
		this.ethernal_item_above = readD();
		this.over_ethernal = readD();
		this.over_over_ethernal = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player leader = getConnection().getActivePlayer();
		if(leader != null)
		{
			if(leader.isInAlliance())
			{
				PlayerAlliance pa = leader.getPlayerAlliance();

				if(pa != null)
				{
					pa.setLootAllianceRules(new LootGroupRules(this.lootrules,
					this.autodistribution, this.common_item_above,
					this.superior_item_above, this.heroic_item_above,
					this.fabled_item_above, this.ethernal_item_above,
					this.over_ethernal, this.over_over_ethernal));
				}
			}else if(leader.isInGroup())
			{
				PlayerGroup pg = leader.getPlayerGroup();
				
				if(pg != null)
				{
					pg.setLootGroupRules(new LootGroupRules(this.lootrules,
					this.autodistribution, this.common_item_above,
					this.superior_item_above, this.heroic_item_above,
					this.fabled_item_above, this.ethernal_item_above,
					this.over_ethernal, this.over_over_ethernal));
				}
			}
		}
	}
}
