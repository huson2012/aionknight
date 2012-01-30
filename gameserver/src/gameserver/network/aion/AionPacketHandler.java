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

package gameserver.network.aion;

import gameserver.configs.network.NetworkConfig;
import gameserver.network.aion.AionConnection.State;
import gameserver.utils.Util;
import org.apache.log4j.Logger;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class AionPacketHandler
{
	/**
	 * logger for this class
	 */
	private static final Logger	log	= Logger.getLogger(AionPacketHandler.class);

	private Map<State, Map<Integer, AionClientPacket>>	packetsPrototypes	= new HashMap<State, Map<Integer, AionClientPacket>>();

	/**
	 * Reads one packet from given ByteBuffer
	 * 
	 * @param data
	 * @param client
	 * @return AionClientPacket object from binary data
	 */
	public AionClientPacket handle(ByteBuffer data, AionConnection client)
	{
		State state = client.getState();
		short id = data.getShort(); //2.7 Opcode is now 2 bytes.

		//TODO: Add client packet validation for opcode instead of skipping
		data.position(data.position() + 3);

		return getPacket(state, id, data, client);
	}

	public void addPacketPrototype(AionClientPacket packetPrototype, State... states)
	{
		for(State state : states)
		{
			Map<Integer, AionClientPacket> pm = packetsPrototypes.get(state);
			if(pm == null)
			{
				pm = new HashMap<Integer, AionClientPacket>();
				packetsPrototypes.put(state, pm);
			}
			pm.put(packetPrototype.getOpcode(), packetPrototype);
		}
	}

	private AionClientPacket getPacket(State state, int id, ByteBuffer buf, AionConnection con)
	{
		AionClientPacket prototype = null;

		Map<Integer, AionClientPacket> pm = packetsPrototypes.get(state);
		if(pm != null)
		{
			prototype = pm.get(id);
		}

		if(prototype == null)
		{
			unknownPacket(state, id, buf);
			return null;
		}

		AionClientPacket res = prototype.clonePacket();
		res.setBuffer(buf);
		res.setConnection(con);

		return res;
	}
	
	private void unknownPacket(State state, int id, ByteBuffer data)
	{
		if(NetworkConfig.DISPLAY_UNKNOWNPACKETS)
		{
			log.warn(String.format("[!] Unknown packet from: 0x%02X, state=%s %n%s", id, state
				.toString(), Util.toHex(data)));
		}
	}
}
