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
import gameserver.network.aion.SystemMessageId;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.ExchangeService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

public class CM_EXCHANGE_REQUEST extends AionClientPacket
{
	public Integer			targetObjectId;

	public CM_EXCHANGE_REQUEST(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		targetObjectId = readD();
	}


	@Override
	protected void runImpl()
	{
		final Player activePlayer = getConnection().getActivePlayer();
		final Player targetPlayer = World.getInstance().findPlayer(targetObjectId);

		/**
		 * checkIf not trading with yourself.
		 */
		if(activePlayer != targetPlayer)
		{
			/**
			 * check if trade partner exists or is he/she a player.
			 */
			if(targetPlayer!=null)
			{
				if(targetPlayer.getPlayerSettings().isInDeniedStatus(DeniedStatus.TRADE))
				{
					sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_TRADE(targetPlayer.getName()));
					return;
				}
				sendPacket(SM_SYSTEM_MESSAGE.REQUEST_TRADE(targetPlayer.getName()));
				
				ExchangeService.getInstance().cancelExchange(activePlayer);
				ExchangeService.getInstance().cancelExchange(targetPlayer);

				RequestResponseHandler responseHandler = new RequestResponseHandler(activePlayer){
					@Override
					public void acceptRequest(Creature requester, Player responder)
					{
						ExchangeService.getInstance().registerExchange(activePlayer, targetPlayer);
					}

					@Override
					public void denyRequest(Creature requester, Player responder)
					{
						PacketSendUtility.sendPacket(activePlayer, new SM_SYSTEM_MESSAGE(SystemMessageId.EXCHANGE_HE_REJECTED_EXCHANGE, targetPlayer.getName()));
					}
				};

				boolean requested = targetPlayer.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_EXCHANGE_DO_YOU_ACCEPT_EXCHANGE,responseHandler);
				if(requested)
				{
					PacketSendUtility.sendPacket(targetPlayer, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_EXCHANGE_DO_YOU_ACCEPT_EXCHANGE, 0, activePlayer.getName()));
				}
			}
		}
		else
		{
			//TODO: send message, cannot trade with yourself.
		}
	}
}
