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

package gameserver.network.factories;

import gameserver.network.loginserver.LoginServerConnection.State;
import gameserver.network.loginserver.LsClientPacket;
import gameserver.network.loginserver.LsPacketHandler;
import gameserver.network.loginserver.clientpackets.*;

public class LsPacketHandlerFactory
{
	private LsPacketHandler	handler	= new LsPacketHandler();

	public static final LsPacketHandlerFactory getInstance()
	{
		return SingletonHolder.instance;
	}

	/**
	 * @param loginServer
	 */
	private LsPacketHandlerFactory()
	{

		addPacket(new CM_ACCOUNT_RECONNECT_KEY(0x03), State.AUTHED);
		addPacket(new CM_ACOUNT_AUTH_RESPONSE(0x01), State.AUTHED);
		addPacket(new CM_GS_AUTH_RESPONSE(0x00), State.CONNECTED);
		addPacket(new CM_REQUEST_KICK_ACCOUNT(0x02), State.AUTHED);
		addPacket(new CM_LS_CONTROL_RESPONSE(0x04), State.AUTHED);
		addPacket(new CM_BAN_RESPONSE(0x05), State.AUTHED);
		addPacket(new CM_MACBAN_LIST(0x09), State.AUTHED);
		addPacket(new CM_MACBAN_REQUEST_MAC(0x10), State.AUTHED);
		addPacket(new CM_LS_REQUEST_CHARACTER_COUNT(0x06), State.AUTHED);

	}

	private void addPacket(LsClientPacket prototype, State... states)
	{
		handler.addPacketPrototype(prototype, states);
	}

	public LsPacketHandler getPacketHandler()
	{
		return handler;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final LsPacketHandlerFactory instance = new LsPacketHandlerFactory();
	}
}