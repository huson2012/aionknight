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
import gameserver.network.aion.serverpackets.SM_BLOCK_RESPONSE;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.SocialService;
import gameserver.world.World;
import org.apache.log4j.Logger;

public class CM_BLOCK_ADD extends AionClientPacket
{
	private static Logger log = Logger.getLogger(CM_BLOCK_ADD.class);
	
	private String 			targetName;
	private String			reason;
	
	public CM_BLOCK_ADD(int opcode)
	{
		super(opcode);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		targetName = readS();
		reason = readS();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		
		Player activePlayer = getConnection().getActivePlayer();
		
		Player targetPlayer = World.getInstance().findPlayer(targetName);
		
		//Trying to block self
		if (activePlayer.getName().equalsIgnoreCase(targetName))
		{
			sendPacket(new SM_BLOCK_RESPONSE(SM_BLOCK_RESPONSE.CANT_BLOCK_SELF, targetName));
		}
		
		//List full
		else if (activePlayer.getBlockList().isFull())
		{
			sendPacket(new SM_BLOCK_RESPONSE(SM_BLOCK_RESPONSE.LIST_FULL, targetName));
		}
		
		//Player offline
		else if (targetPlayer == null)
		{
			sendPacket(new SM_BLOCK_RESPONSE(SM_BLOCK_RESPONSE.TARGET_NOT_FOUND, targetName));
		}
		
		//Player is your friend
		else if (activePlayer.getFriendList().getFriend(targetPlayer.getObjectId()) != null)
		{
			sendPacket(SM_SYSTEM_MESSAGE.BLOCKLIST_NO_BUDDY);
		}
		
		//Player already blocked
		else if (activePlayer.getBlockList().contains(targetPlayer.getObjectId()))
		{
			sendPacket(SM_SYSTEM_MESSAGE.BLOCKLIST_ALREADY_BLOCKED);
		}
		
		//Try and block player
		else if (!SocialService.addBlockedUser(activePlayer, targetPlayer, reason))
		{
			log.error("Failed to add " + targetPlayer.getName() + " to the block list for " + activePlayer.getName() + " - check database setup.");
		}	
	}
}