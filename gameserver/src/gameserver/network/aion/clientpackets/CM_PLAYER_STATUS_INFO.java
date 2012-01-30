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

import gameserver.model.alliance.PlayerAlliance;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.AllianceService;
import gameserver.services.GroupService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

/**
 * Called when entering the world and during group management
 */

public class CM_PLAYER_STATUS_INFO extends AionClientPacket
{
	/**
	 * Definitions
	 */
	private int				status;
	private int				playerObjId;
	private int				allianceGroupId;
	private int				secondObjectId;

	public CM_PLAYER_STATUS_INFO(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		status = readC();
		playerObjId = readD();
		allianceGroupId = readD();
		secondObjectId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player myActivePlayer = getConnection().getActivePlayer();

		switch(status)
		{
			// Note: This is currently used for PlayerGroup...
			// but it also is sent when leaving the alliance.
			case 9:
				getConnection().getActivePlayer().setLookingForGroup(playerObjId == 2);
			break;
            
			//Alliance Statuses
			case 14:
			case 15:
			case 16:
			case 19:
			case 23:
			case 24:
				AllianceService.getInstance().playerStatusInfo(myActivePlayer, status, playerObjId);
			break;
			case 25:
				PlayerAlliance alliance = myActivePlayer.getPlayerAlliance();

				if (alliance == null)
				{
					// Huh... packet spoofing?
					PacketSendUtility.sendPacket(myActivePlayer, new SM_SYSTEM_MESSAGE(1301015));
					return;
				}

				if (!alliance.hasAuthority(myActivePlayer.getObjectId()))
				{
					// You are not the leader!
					PacketSendUtility.sendPacket(myActivePlayer, new SM_SYSTEM_MESSAGE(1400749));
					return;
				}

				AllianceService.getInstance().handleGroupChange(alliance, playerObjId, allianceGroupId, secondObjectId);
			break;
			//PlayerGroup Statuses
			case 2:
			case 3:
			case 6:
				Player player = null;

				if(playerObjId == 0)
					player = getConnection().getActivePlayer();
				else
					player = World.getInstance().findPlayer(playerObjId);

				if(player == null || player.getPlayerGroup() == null)
					return;

				GroupService.getInstance().playerStatusInfo(status, player);
				break;
		}
	}
}
