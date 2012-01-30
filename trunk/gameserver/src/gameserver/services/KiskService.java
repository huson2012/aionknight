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

package gameserver.services;

import gameserver.model.gameobjects.Kisk;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIE;
import gameserver.network.aion.serverpackets.SM_LEVEL_UPDATE;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import gameserver.world.container.KiskContainer;

public class KiskService
{
	private static final KiskContainer		kiskContainer = new KiskContainer();
	
	/**
	 * @param player
	 * @return kisk
	 */
	public Kisk getKisk(Player player)
	{
		return kiskContainer.get(player);
	}
	
	/**
	 * Remove kisk references and containers.
	 * @param kisk
	 */
	public static void removeKisk(Kisk kisk)
	{
		for (Player member : kisk.getCurrentMemberList())
		{
			kiskContainer.remove(member);
			
			if (member.getKisk() == kisk)
				member.setKisk(null);
			
			if (member.getKisk() == null)
				TeleportService.sendSetBindPoint(member);

			if (member.getLifeStats().isAlreadyDead())
				PacketSendUtility.sendPacket(member, new SM_DIE(false, false, 0));
		}
	}
	
	/**
	 * @param kisk
	 * @param player
	 */
	public static void onBind(Kisk kisk, Player player)
	{
		if (player.getKisk() != null)
		{
			kiskContainer.remove(player);
			player.getKisk().removePlayer(player);
		}
		
		kiskContainer.add(kisk, player);
		kisk.addPlayer(player);
		
		// Send Bind Point Data
		TeleportService.sendSetBindPoint(player);
		
		// Send System Message
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_BINDSTONE_REGISTER);
		
		// Send Animated Bind Flash
		PacketSendUtility.broadcastPacket(player, new SM_LEVEL_UPDATE(player.getObjectId(),
			2, player.getCommonData().getLevel()), true);
	}
	
	/**
	 * @param player
	 */
	public static void onLogin(Player player)
	{
		Kisk kisk = kiskContainer.get(player);
		if (kisk != null)
			kisk.reAddPlayer(player);
	}
}
