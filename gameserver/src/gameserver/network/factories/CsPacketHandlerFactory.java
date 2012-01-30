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

import gameserver.network.chatserver.ChatServerConnection.State;
import gameserver.network.chatserver.CsClientPacket;
import gameserver.network.chatserver.CsPacketHandler;
import gameserver.network.chatserver.clientpackets.CM_CS_AUTH_RESPONSE;
import gameserver.network.chatserver.clientpackets.CM_CS_PLAYER_AUTH_RESPONSE;

public class CsPacketHandlerFactory
{
	private CsPacketHandler	handler	= new CsPacketHandler();

	/**
	 * @param injector
	 */
	public CsPacketHandlerFactory()
	{
		addPacket(new CM_CS_AUTH_RESPONSE(0x00), State.CONNECTED);
		addPacket(new CM_CS_PLAYER_AUTH_RESPONSE(0x01), State.AUTHED);
	}

	/**
	 * 
	 * @param prototype
	 * @param states
	 */
	private void addPacket(CsClientPacket prototype, State... states)
	{
		handler.addPacketPrototype(prototype, states);
	}

	/**
	 * @return handler
	 */
	public CsPacketHandler getPacketHandler()
	{
		return handler;
	}
}