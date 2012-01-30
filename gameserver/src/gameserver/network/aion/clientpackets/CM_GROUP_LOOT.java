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

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.services.DropService;

public class CM_GROUP_LOOT extends AionClientPacket
{	
	private int groupId;
	@SuppressWarnings("unused")
	private int unk1;
	@SuppressWarnings("unused")
	private int unk2;
	private int itemId;
	private int itemIndex;
	private int npcId;
	private int distributionId;
	private int roll;	
	private long bid;
	
	public CM_GROUP_LOOT(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		groupId = readD();
		unk1 = readD();
		unk2 = readD();
		itemId = readD();
		itemIndex = readC();
		npcId = readD();
		distributionId = readC();// 2: Roll 3: Bid
		roll = readD();// 0: Never Rolled 1: Rolled
		bid = readQ();// 0: No Bid else bid amount
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		if(player == null)
			return;
		
		switch(distributionId)
		{
			case 2:
				DropService.getInstance().handleRoll(player, groupId, roll, itemId, itemIndex, npcId, distributionId);
				break;
			case 3:
				DropService.getInstance().handleBid(player, groupId, bid, itemId, itemIndex, npcId);
				break;				
		}
	}
}