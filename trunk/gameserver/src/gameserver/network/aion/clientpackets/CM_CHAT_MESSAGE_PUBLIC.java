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

//import org.apache.log4j.Logger;

import commons.objects.filter.ObjectFilter;
import gameserver.configs.main.GSConfig;
import gameserver.model.ChatType;
import gameserver.model.alliance.PlayerAlliance;
import gameserver.model.alliance.PlayerAllianceGroup;
import gameserver.model.alliance.PlayerAllianceMember;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_MESSAGE;
import gameserver.restrictions.RestrictionsManager;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.ChatHandler;
import gameserver.utils.chathandlers.ChatHandlerResponse;
import gameserver.utils.chathandlers.ChatHandlers;
import gameserver.world.World;
import javolution.util.FastList;
import org.apache.log4j.Logger;

/**
 * Packet that reads normal chat messages.<br>
 */
public class CM_CHAT_MESSAGE_PUBLIC extends AionClientPacket
{

	/**
	 * Logger
	 */
	private static final Logger	log	= Logger.getLogger(CM_CHAT_MESSAGE_PUBLIC.class);

	/**
	 * Chat type
	 */
	private ChatType			type;

	/**
	 * Chat message
	 */
	private String				message;

	/**
	 * Constructs new client packet instance.
	 * 
	 * @param opcode
	 */
	public CM_CHAT_MESSAGE_PUBLIC(int opcode)
	{
		super(opcode);
	}

	/**
	 * Reads chat message
	 */
	@Override
	protected void readImpl()
	{
		type = ChatType.getChatTypeByInt(readC());
		message = readS();
	}

	/**
	 * Prints debug info
	 */
	@Override
	protected void runImpl()
	{

		final Player player = getConnection().getActivePlayer();

		if (player == null)
			return;
		//log.info(String.format("Public Message [%s]: %s, Type: %s", player.getName(), message, type));
		FastList<ChatHandler> chatHandlers = ChatHandlers.getInstance().getHandlers();
		for (FastList.Node<ChatHandler> n = chatHandlers.head(), end = chatHandlers.tail(); (n = n.getNext()) != end;)
		{
			ChatHandlerResponse response = n.getValue().handleChatMessage(type, message, player);
			if(response.isBlocked())
				return;

			message = response.getMessage();
		}

		if(RestrictionsManager.canChat(player))
		{
			switch(this.type)
			{
				case GROUP:
					if (player.getPlayerGroup() == null && player.getPlayerAlliance() == null)
						return;
					
					if(GSConfig.LOG_CHAT)
						log.info(String.format("[MESSAGE] - GROUP <%d>: [%s]> %s", player.getPlayerGroup().getGroupId(), player.getName(), message));
					
					broadcastToGroupMembers(player);
					break;
				case ALLIANCE:
					if (player.getPlayerAlliance() == null)
						return;
					
					if(GSConfig.LOG_CHAT)
						log.info(String.format("[MESSAGE] - ALLIANCE <%d>: [%s]> %s", player.getPlayerAlliance().getObjectId(), player.getName(), message));
					
					broadcastToAllianceMembers(player);
					break;
					
				case GROUP_LEADER:
					if (!player.isInGroup() && !player.isInAlliance())
						return;
					
					if(GSConfig.LOG_CHAT)
						log.info(String.format("[MESSAGE] - LEADER_ALERT: [%s]> %s", player.getName(), message));
					
					// Alert must go to entire group or alliance.
					if (player.isInGroup())
						broadcastToGroupMembers(player);
					else
						broadcastToAllianceMembers(player);
					break;
				case LEGION:				
					if(GSConfig.LOG_CHAT)
						log.info(String.format("[MESSAGE] - LEGION <%s>: [%s]> %s", player.getLegion().getLegionName(), player.getName(), message));
					
					broadcastToLegionMembers(player);
					break;
				default:
					if(GSConfig.LOG_CHAT)
						log.info(String.format("[MESSAGE] - ALL: [%s]> %s", player.getName(), message));
					
					broadcastToNonBlockedPlayers(player);
				break;
			}
		}
	}

	/**
	 * Sends message to all players that are not in blocklist
	 * 
	 * @param player
	 */
	private void broadcastToNonBlockedPlayers(final Player player)
	{
		PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, message, type), true,
			new ObjectFilter<Player>(){

				@Override
				public boolean acceptObject(Player object)
				{
					return !object.getBlockList().contains(player.getObjectId());
				}
			});
	}

	/**
	 * Sends message to all group members (regular player group, or alliance sub-group
	 * Error 105, random value for players to report. Should never happen.
	 * 
	 * @param player
	 */
	private void broadcastToGroupMembers(final Player player)
	{
		if(player.isInGroup())
		{
			for(Player groupPlayer : player.getPlayerGroup().getMembers())
			{
				PacketSendUtility.sendPacket(groupPlayer, new SM_MESSAGE(player, message, type));
			}
			World.getInstance().doOnAllPlayers(new Executor<Player>(){
				
				@Override
				public boolean run(Player object)
				{
					if(object.getAccessLevel() > 2)
					{
						if(object.spyedGroups.contains(player.getPlayerGroup().getGroupId()))
						{
							PacketSendUtility.sendMessage(object, "GSPY [" + player.getPlayerGroup().getGroupId() + "] " + player.getName() + " : " + message);
						}
					}
					return true;
				}
			},true);
		}
		else if (player.isInAlliance())
		{
			PlayerAllianceGroup allianceGroup = player.getPlayerAlliance().getPlayerAllianceGroupForMember(player.getObjectId());
			if(allianceGroup != null)
			{
				for(PlayerAllianceMember allianceMember : allianceGroup.getMembers())
				{
					if (allianceMember.isOnline())
						PacketSendUtility.sendPacket(allianceMember.getPlayer(),
							new SM_MESSAGE(player, message, type));
				}
			}
		}
		else
		{
			PacketSendUtility.sendMessage(player, "You are not in an alliance or group. (Error 105)");
		}
	}

	/**
	 * Sends message to all alliance members
	 * 
	 * @param player
	 */
	private void broadcastToAllianceMembers(final Player player)
	{
		PlayerAlliance alliance = player.getPlayerAlliance();
		if(alliance != null)
		{
			for(PlayerAllianceMember allianceMember : alliance.getMembers())
			{
				if (!allianceMember.isOnline()) continue;
				PacketSendUtility.sendPacket(allianceMember.getPlayer(), new SM_MESSAGE(player, message, type));
			}
		}
	}
	
	/**
	 * Sends message to all legion members
	 * 
	 * @param player
	 */
	private void broadcastToLegionMembers(final Player player)
	{
		if(player.isLegionMember())
		{
			PacketSendUtility.broadcastPacketToLegion(player.getLegion(), new SM_MESSAGE(player, message, type));
			World.getInstance().doOnAllPlayers(new Executor<Player>(){
				
				@Override
				public boolean run(Player object)
				{
					if(object.getAccessLevel() > 2)
					{
						if(object.spyedLegions.contains(player.getLegion().getLegionId()))
						{
							PacketSendUtility.sendMessage(object, "LSPY [" + player.getLegion().getLegionName() + "] " + player.getName() + " : " + message);
						}
					}
					return true;
				}
			},true);
		}
	}
}
