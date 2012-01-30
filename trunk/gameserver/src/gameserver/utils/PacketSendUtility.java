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

package gameserver.utils;

import commons.objects.filter.ObjectFilter;
import gameserver.model.ChatType;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.model.legion.Legion;
import gameserver.network.aion.AionServerPacket;
import gameserver.network.aion.serverpackets.SM_MESSAGE;

/**
 * This class contains static methods, which are utility methods, all of them are interacting only with objects passed
 * as parameters.<br>
 * These methods could be placed directly into Player class, but we want to keep Player class as a pure data holder.<br>
 */
public class PacketSendUtility
{
	/**
	 * Sends message to player (used for system messages)
	 * 
	 * @param player
	 * @param msg
	 */
	public static void sendMessage(Player player, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.ANNOUNCEMENTS));
	}

	/**
	 * Sends message to player (used for system notices)
	 * 
	 * @param player
	 * @param msg
	 */
	public static void sendSysMessage(Player player, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.SYSTEM_NOTICE));
	}

	/**
	 * Send packet to this player.
	 * 
	 * @param player
	 * @param packet
	 */
	public static void sendPacket(Player player, AionServerPacket packet)
	{
		if(player != null && player.getClientConnection() != null)
			player.getClientConnection().sendPacket(packet);
	}

	/**
	 * Broadcast packet to all visible players.
	 * 
	 * @param player
	 * 
	 * @param packet
	 *           ServerPacket that will be broadcast
	 * @param toSelf
	 *           true if packet should also be sent to this player
	 */
	public static void broadcastPacket(Player player, AionServerPacket packet, boolean toSelf)
	{
		if(toSelf)
			sendPacket(player, packet);

		broadcastPacket(player, packet);
	}

	/**
	 * Broadcast packet to all visible players.
	 * 
	 * @param visibleObject
	 * @param packet
	 */
	public static void broadcastPacketAndReceive(VisibleObject visibleObject, AionServerPacket packet)
	{
		if(visibleObject instanceof Player)
			sendPacket((Player)visibleObject, packet);

		broadcastPacket(visibleObject, packet);
	}

	/**
	 * Broadcast packet to all Players from knownList of the given visible object.
	 * 
	 * @param visibleObject
	 * @param packet
	 */
	public static void broadcastPacket(VisibleObject visibleObject, final AionServerPacket packet)
	{
		visibleObject.getKnownList().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run(Player obj)
			{
				sendPacket(obj, packet);
				return true;
			}
		}, true);
	}
	
	/**
	 * Broadcasts packet to all Players from knownList of the given visible object within the specified distance in meters
	 * 
	 * @param visibleObject
	 * @param packet
	 * @param distance
	 */
	public static void broadcastPacket(final VisibleObject visibleObject, final AionServerPacket packet, final int distance)
	{
		visibleObject.getKnownList().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run(Player p)
			{
				if(MathUtil.getDistance(visibleObject, p) <= distance)
				{
					sendPacket(p, packet);
				}
				return true;
			}
		}, true);
	}

	/**
	 * Broadcasts packet to all visible players matching a filter
	 * 
	 * @param player
	 * 
	 * @param packet
	 *           ServerPacket to be broadcast
	 * @param toSelf
	 *           true if packet should also be sent to this player
	 * @param filter
	 *           filter determining who should be messaged
	 */
	public static void broadcastPacket(Player player, final AionServerPacket packet, boolean toSelf, final ObjectFilter<Player> filter)
	{
		if(toSelf)
		{
			sendPacket(player, packet);
		}

		player.getKnownList().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run (Player target)
			{
				if(filter.acceptObject(target))
					sendPacket(target, packet);
				return true;
			}
		}, true);
	}

	/**
	 * Broadcasts packet to all legion members of a legion
	 * 
	 * @param legion
	 *           Legion to broadcast packet to
	 * @param packet
	 *           ServerPacket to be broadcast
	 */
	public static void broadcastPacketToLegion(Legion legion, AionServerPacket packet)
	{
		for(Player onlineLegionMember : legion.getOnlineLegionMembers())
		{
			sendPacket(onlineLegionMember, packet);
		}
	}

	public static void broadcastPacketToLegion(Legion legion, AionServerPacket packet, int playerObjId)
	{
		for(Player onlineLegionMember : legion.getOnlineLegionMembers())
		{
			if(onlineLegionMember.getObjectId() != playerObjId)
				sendPacket(onlineLegionMember, packet);
		}
	}
}
