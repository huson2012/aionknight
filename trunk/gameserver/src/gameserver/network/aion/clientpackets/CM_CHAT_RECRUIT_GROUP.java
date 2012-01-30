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

import gameserver.model.gameobjects.player.DeniedStatus;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.GroupService;
import gameserver.utils.Util;
import gameserver.world.World;

public class CM_CHAT_RECRUIT_GROUP extends AionClientPacket
{
	private String name;
	
	public CM_CHAT_RECRUIT_GROUP(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		name = readS();
	}

	/**
	 * {@inheritDoc}n
	 */
	@Override
	protected void runImpl()
	{
		final String captainName = Util.convertName(name);

		final Player player = getConnection().getActivePlayer();
		final Player captain = World.getInstance().findPlayer(captainName);

		if(captain != null)
		{
			if(captain.getPlayerSettings().isInDeniedStatus(DeniedStatus.GROUP))
			{
				sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_INVITE_PARTY(captain.getName()));
				return;
			}

			GroupService.getInstance().requestToGroup(player, captain);
		}
		else
			player.getClientConnection().sendPacket(SM_SYSTEM_MESSAGE.PLAYER_IS_OFFLINE(name));
	}
}
