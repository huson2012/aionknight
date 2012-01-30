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

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.model.siege.AethericField;
import gameserver.model.siege.SiegeLocation;
import gameserver.network.aion.serverpackets.SM_SIEGE_LOCATION_INFO;
import gameserver.services.SiegeService;
import gameserver.utils.PacketSendUtility;

public class AethericFieldController extends NpcController
{	
	public void onDie(Creature lastAttacker)
	{
		super.onDie(lastAttacker);
		int id = getOwner().getFortressId();
		SiegeLocation loc = SiegeService.getInstance().getSiegeLocation(id);
		
		// Disable field
		loc.setShieldActive(false);
		
		//TODO: Find sys message sended on generator death
		getOwner().getKnownList().doOnAllPlayers(new Executor<Player>(){		
			
			@Override
			public boolean run(Player object)
			{
				// Needed to update the display of shield effect
				PacketSendUtility.sendPacket(object, new SM_SIEGE_LOCATION_INFO());
				return true;
			}
		}, true);
	}

	@Override
	public AethericField getOwner()
	{
		return (AethericField) super.getOwner();
	}	
}