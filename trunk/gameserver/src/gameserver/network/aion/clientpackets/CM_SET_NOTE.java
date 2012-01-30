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

import gameserver.model.gameobjects.player.Friend;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_FRIEND_LIST;
import gameserver.utils.PacketSendUtility;

/**
 * Received when a player sets his note
 */
public class CM_SET_NOTE extends AionClientPacket
{
	private String note;
	
	public CM_SET_NOTE(int opcode)
	{
		super(opcode);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		note = readS();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player activePlayer = getConnection().getActivePlayer();
		
		if (!note.equals(activePlayer.getCommonData().getNote()))
		{
			activePlayer.getCommonData().setNote(note);
			for (Friend friend : activePlayer.getFriendList()) // For all my friends
			{
				if (friend.isOnline() && friend.getPlayer() != null) // If the player is online
				{
					PacketSendUtility.sendPacket(friend.getPlayer(), new SM_FRIEND_LIST(friend.getPlayer().getFriendList()));
				}
			}			
		}
	}
}