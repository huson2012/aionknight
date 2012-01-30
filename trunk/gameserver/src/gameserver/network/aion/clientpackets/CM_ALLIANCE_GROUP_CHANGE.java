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
import gameserver.services.AllianceService;
import gameserver.utils.PacketSendUtility;

public class CM_ALLIANCE_GROUP_CHANGE extends AionClientPacket
{
	private int allianceGroupId;
	private int playerObjectId;
	private int secondObjectId;
	
	public CM_ALLIANCE_GROUP_CHANGE(int opcode)
	{
		super(opcode);
	}
	
	@Override
	protected void readImpl()
	{
		playerObjectId = readD();
		allianceGroupId = readD();
		secondObjectId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		PlayerAlliance alliance = player.getPlayerAlliance();
		
		if (alliance == null)
		{
			// Huh... packet spoofing?
			PacketSendUtility.sendMessage(player, "You are not in an alliance.");
			return;
		}
		
		if (!alliance.hasAuthority(player.getObjectId()))
		{
			// You are not the leader!
			PacketSendUtility.sendMessage(player, "You do not have the authority for that.");
			return;
		}
		
		AllianceService.getInstance().handleGroupChange(alliance, playerObjectId, allianceGroupId, secondObjectId);
	}
}
