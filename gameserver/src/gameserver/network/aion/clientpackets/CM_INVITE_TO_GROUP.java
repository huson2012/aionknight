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
import gameserver.services.AllianceService;
import gameserver.services.GroupService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.world.World;

public class CM_INVITE_TO_GROUP extends AionClientPacket
{
	private String			name;
	private int				inviteType;
	public CM_INVITE_TO_GROUP(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		inviteType = readC();
		name = readS();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		final String playerName = Util.convertName(name);

		final Player inviter = getConnection().getActivePlayer();
		final Player invited = World.getInstance().findPlayer(playerName);

		if(invited != null)
		{
			if(invited.getPlayerSettings().isInDeniedStatus(DeniedStatus.GROUP))
			{
				sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_INVITE_PARTY(invited.getName()));
				return;
			}

			switch (inviteType)
			{
				case 0:
					GroupService.getInstance().invitePlayerToGroup(inviter, invited);
					break;
				case 12:
					AllianceService.getInstance().invitePlayerToAlliance(inviter, invited);
					break;
				case 26:
					// TODO: League Service
					//LeagueService.getInstance().invitePlayerToLeague(inviter, invited);
					break;
				default:
					PacketSendUtility.sendMessage(inviter, "You used an unknown invite type: " + inviteType);
			}
		}
		else
			inviter.getClientConnection().sendPacket(SM_SYSTEM_MESSAGE.PLAYER_IS_OFFLINE(name));
	}
}