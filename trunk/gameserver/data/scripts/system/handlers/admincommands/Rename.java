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

package admincommands;

import commons.database.dao.DAOManager;
import gameserver.configs.administration.AdminConfig;
import gameserver.dao.PlayerDAO;
import gameserver.model.gameobjects.player.Friend;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.network.aion.serverpackets.SM_LEGION_UPDATE_MEMBER;
import gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.PlayerService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

import java.util.Iterator;

public class Rename extends AdminCommand
{
	public Rename()
	{
		super("rename");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{

		if (admin.getAccessLevel() < AdminConfig.COMMAND_RENAME)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}

		if (params.length < 2)
		{
			PacketSendUtility.sendMessage(admin, "syntax //rename <player name> <new player name>");
			return;
		}

		final Player player = World.getInstance().findPlayer(Util.convertName(params[0]));

		if (player == null)
		{
			PacketSendUtility.sendPacket(admin, SM_SYSTEM_MESSAGE.PLAYER_IS_OFFLINE(params[0]));
			return;
		}

		if (!PlayerService.isValidName(params[1]))
		{
			PacketSendUtility.sendPacket(admin, new SM_SYSTEM_MESSAGE(1400151));
			return;
		}

		if (!PlayerService.isFreeName(params[1]))
		{
			PacketSendUtility.sendPacket(admin, new SM_SYSTEM_MESSAGE(1400155));
			return;
		}

		player.getCommonData().setName(params[1]);
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		Iterator<Friend> knownFriends = player.getFriendList().iterator();

		DAOManager.getDAO(PlayerDAO.class).storePlayer(player);

		player.getKnownList().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run(Player p)
			{
				PacketSendUtility.sendPacket(p, new SM_PLAYER_INFO(player, player.isEnemyPlayer(p)));
				return true;
			}
		}, true);
		
		while (knownFriends.hasNext())
		{
			Friend nextObject = knownFriends.next();
			if (nextObject.getPlayer() != null)
			{
				if (nextObject.getPlayer().isOnline())
					PacketSendUtility.sendPacket(nextObject.getPlayer(), new SM_PLAYER_INFO(player, false));
			}
		}

		if (player.isLegionMember())
		{
			PacketSendUtility.broadcastPacketToLegion(player.getLegion(), new SM_LEGION_UPDATE_MEMBER(player, 0, ""));
		}
		PacketSendUtility.sendMessage(player, "You have been renamed to [" + params[1] + "] by " + admin.getName());
		PacketSendUtility.sendMessage(admin, "Player " + params[0] + " has been renamed to " + params[1]);
	}
}