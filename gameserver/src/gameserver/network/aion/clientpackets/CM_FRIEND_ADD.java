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

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.DeniedStatus;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_FRIEND_RESPONSE;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.SocialService;
import gameserver.world.World;

/**
 * Received when a user tries to add someone as his friend
 */
public class CM_FRIEND_ADD extends AionClientPacket
{
	private String targetName;
	
	public CM_FRIEND_ADD(int opcode) {
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		targetName = readS();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		
		final Player activePlayer = getConnection().getActivePlayer();
		final Player targetPlayer = World.getInstance().findPlayer(targetName);
		
	
		if (targetName.equalsIgnoreCase(activePlayer.getName()))
		{
				//Adding self to friend list not allowed - Its blocked by the client by default, so no need to send an error
		}
		//if offline
		else if (targetPlayer == null) 
		{	
			sendPacket(new SM_FRIEND_RESPONSE(targetName, SM_FRIEND_RESPONSE.TARGET_OFFLINE));
		}
		else if (activePlayer.getFriendList().getFriend(targetPlayer.getObjectId()) != null)
		{
			sendPacket(new SM_FRIEND_RESPONSE(targetPlayer.getName(), SM_FRIEND_RESPONSE.TARGET_ALREADY_FRIEND));
		}
		else if (activePlayer.getFriendList().isFull())
		{
			sendPacket(SM_SYSTEM_MESSAGE.BUDDYLIST_LIST_FULL);
		}
		else if (targetPlayer.getFriendList().isFull())
		{
			sendPacket(new SM_FRIEND_RESPONSE(targetPlayer.getName(),SM_FRIEND_RESPONSE.TARGET_LIST_FULL));
		}
		else if (activePlayer.getBlockList().contains(targetPlayer.getObjectId()))
		{
			sendPacket(new SM_FRIEND_RESPONSE(targetPlayer.getName(), SM_FRIEND_RESPONSE.TARGET_BLOCKED));
		}
		else if (targetPlayer.getBlockList().contains(activePlayer.getObjectId()))
		{
			sendPacket(SM_SYSTEM_MESSAGE.YOU_ARE_BLOCKED_BY(targetName));
		}
		else // Send request
		{
			RequestResponseHandler responseHandler = new RequestResponseHandler(activePlayer) {
				
				@Override
				public void acceptRequest(Creature requester, Player responder)
				{
					if (!targetPlayer.getCommonData().isOnline())
					{
						sendPacket(new SM_FRIEND_RESPONSE(targetName , SM_FRIEND_RESPONSE.TARGET_OFFLINE));
					}
					else if (activePlayer.getFriendList().isFull() || 
						responder.getFriendList().isFull())
					{
                    }
					else
					{
						SocialService.makeFriends((Player)requester, responder);
					}
					
				}

				@Override
				public void denyRequest(Creature requester, Player responder)
				{
					sendPacket(new SM_FRIEND_RESPONSE(targetName, SM_FRIEND_RESPONSE.TARGET_DENIED));
					
				}
			};
			
			boolean requested = targetPlayer.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_BUDDYLIST_ADD_BUDDY_REQUETS,responseHandler);
			//If the player is busy and could not be asked
			if (!requested)
			{
				sendPacket(SM_SYSTEM_MESSAGE.BUDDYLIST_BUSY);
			}
			else 
			{
				if(targetPlayer.getPlayerSettings().isInDeniedStatus(DeniedStatus.FRIEND))
				{
					sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_FRIEND(targetPlayer.getName()));
					return;
				}
				//Send question packet to buddy
				targetPlayer.getClientConnection()
					.sendPacket(new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_BUDDYLIST_ADD_BUDDY_REQUETS, activePlayer.getObjectId(), activePlayer.getName()));
			}
		}
	}
	
}
