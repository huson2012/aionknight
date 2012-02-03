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
import gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;

public class CM_EXIT_LOCATION extends AionClientPacket
{

	public CM_EXIT_LOCATION(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{

	}

	@Override
	protected void runImpl()
	{	
		final Player activePlayer = getConnection().getActivePlayer();
		if(activePlayer.getInDarkPoeta() || activePlayer.getInDredgion())
		{
			switch(activePlayer.getCommonData().getRace().getRaceId())
			{
			case 0:
				TeleportService.teleportTo(activePlayer, 110010000, 1, 1444.9f, 1577.2f, 572.9f, 0);
				break;
			case 1:
				TeleportService.teleportTo(activePlayer, 120010000, 1, 1657.5f, 1398.7f, 194.7f, 0);
				break;
			}
		}
		PacketSendUtility.sendPacket(activePlayer, new SM_INSTANCE_SCORE(0, 14400000, 2097152, 0, 0, 0, 0));
	}
}
