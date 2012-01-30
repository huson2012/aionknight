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

package gameserver.network.loginserver;

import gameserver.network.loginserver.LoginServerConnection.State;
import org.apache.log4j.Logger;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class LsPacketHandler
{
	/**
	 * logger for this class
	 */
	private static final Logger	log	= Logger.getLogger(LsPacketHandler.class);

	private static Map<State, Map<Integer, LsClientPacket>>	packetPrototypes	= new HashMap<State, Map<Integer, LsClientPacket>>();

	/**
	 * Reads one packet from given ByteBuffer
	 * 
	 * @param data
	 * @param client
	 * @return GsClientPacket object from binary data
	 */
	public LsClientPacket handle(ByteBuffer data, LoginServerConnection client)
	{
		State state = client.getState();
		int id = data.get() & 0xff;

		return getPacket(state, id, data, client);
	}

	public void addPacketPrototype(LsClientPacket packetPrototype, State... states)
	{
		for(State state : states)
		{
			Map<Integer, LsClientPacket> pm = packetPrototypes.get(state);
			if(pm == null)
			{
				pm = new HashMap<Integer, LsClientPacket>();
				packetPrototypes.put(state, pm);
			}
			pm.put(packetPrototype.getOpcode(), packetPrototype);
		}
	}

	private LsClientPacket getPacket(State state, int id, ByteBuffer buf, LoginServerConnection con)
	{
		LsClientPacket prototype = null;

		Map<Integer, LsClientPacket> pm = packetPrototypes.get(state);
		if(pm != null)
		{
			prototype = pm.get(id);
		}

		if(prototype == null)
		{
			unknownPacket(state, id);
			return null;
		}

		LsClientPacket res = prototype.clonePacket();
		res.setBuffer(buf);
		res.setConnection(con);

		return res;
	}

	/**
	 * Logs unknown packet.
	 * 
	 * @param state
	 * @param id
	 */
	private void unknownPacket(State state, int id)
	{
		log.warn(String.format("[!] Recived unknown packet from LS: 0x%02X state=%s", id, state.toString()));
	}
}