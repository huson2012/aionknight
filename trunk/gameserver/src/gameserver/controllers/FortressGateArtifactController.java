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

package gameserver.controllers;

import gameserver.model.DescriptionId;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.model.siege.FortressGateArtifact;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.utils.PacketSendUtility;

public class FortressGateArtifactController extends NpcController
{	
	@Override
	public void onDialogRequest(final Player player)
	{
		RequestResponseHandler artifactHandler = new RequestResponseHandler(player){
			
			@Override
			public void denyRequest(Creature requester, Player responder)
			{
				// Close window
			}
			
			@Override
			public void acceptRequest(Creature requester, Player responder)
			{
				RequestResponseHandler acceptItem = new RequestResponseHandler(player){
					
					@Override
					public void denyRequest(Creature requester, Player responder)
					{
						// Refuse item do nothing
					}
					
					@Override
					public void acceptRequest(Creature requester, Player responder)
					{
						onActivate(player);
					}
				};
				if(player.getResponseRequester().putRequest(160016, acceptItem))
				{
					PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(160016, player.getObjectId(), new DescriptionId(2*716568+1)));
				}
			}
		};
		if(player.getResponseRequester().putRequest(160027, artifactHandler))
		{
			PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(160027, player.getObjectId()));
		}
	}
	
	public void onActivate(Player player)
	{
		
	}
	
	@Override
	public void onRespawn()
	{
		super.onRespawn();
	}

	@Override
	public FortressGateArtifact getOwner()
	{
		return (FortressGateArtifact) super.getOwner();
	}
}