/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.network.aion;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import gameserver.configs.network.NetworkConfig;
import gameserver.network.aion.AionConnection.State;
import gameserver.utils.Util;

public class AionPacketHandler
{
	/**
	 * logger for this class
	 */
	private static final Logger							log					= Logger.getLogger(AionPacketHandler.class);

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
		int id = data.get() & 0xff;

		/** Second opcodec. */
		data.position(data.position() + 2);

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
			log.warn(String.format("Unknown packet recived from Aion client: 0x%02X, state=%s %n%s", id, state
				.toString(), Util.toHex(data)));
		}
	}
}