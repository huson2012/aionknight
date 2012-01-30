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

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_GROUP_LOOT extends AionServerPacket
{	
	private int groupId;
	private int unk1;
	private int unk2;
	private int itemId;
	private int itemIndex;
	private int lootCorpseId;
	private int distributionId;
	private int playerId;
	private int luck;

	/**
	 * Start the roll options.
	 * 
	 * @param Player
	 *           Id must be 0 to start the Roll Options
	 */
	public SM_GROUP_LOOT(int groupId, int itemId, int itemIndex, int lootCorpseId, int distributionId)
	{
		this.groupId = groupId;
		this.unk1 = 1;
		this.unk2 = 1;
		this.itemId = itemId;
		this.itemIndex = itemIndex;
		this.lootCorpseId = lootCorpseId;
		this.distributionId = distributionId;
		this.playerId = 0;
		this.luck = 1;
	}

	/**
	 * Update the roll when someone rolls or passes.
	 */
	public SM_GROUP_LOOT(int groupId, int itemId, int itemIndex, int lootCorpseId, int distributionId, int playerId, int luck)
	{
		this.groupId = groupId;
		this.unk1 = 1;
		this.unk2 = 1;
		this.itemId = itemId;
		this.itemIndex = itemIndex;
		this.lootCorpseId = lootCorpseId;
		this.distributionId = distributionId;
		this.playerId = playerId;
		this.luck = luck;
	}

	/**
	 * Send a packet with the winner.
	 */
	public SM_GROUP_LOOT(int groupId, int itemId, int itemIndex, int lootCorpseId, int distributionId, int playerId)
	{
		this.groupId = groupId;
		this.unk1 = 1;
		this.unk2 = 1;
		this.itemId = itemId;
		this.itemIndex = itemIndex;
		this.lootCorpseId = lootCorpseId;
		this.distributionId = distributionId;
		this.playerId = playerId;
		this.luck = -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, groupId);
		writeD(buf, unk1);
		writeD(buf, unk2);
		writeD(buf, itemId);
		writeC(buf, itemIndex);
		writeD(buf, lootCorpseId);
		writeC(buf, distributionId);
		writeD(buf, playerId);
		writeD(buf, luck);
	}
}