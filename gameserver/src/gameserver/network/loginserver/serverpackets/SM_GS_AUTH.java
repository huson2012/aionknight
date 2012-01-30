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

package gameserver.network.loginserver.serverpackets;

import commons.network.IPRange;
import gameserver.configs.network.IPConfig;
import gameserver.configs.network.NetworkConfig;
import gameserver.network.loginserver.LoginServerConnection;
import gameserver.network.loginserver.LsServerPacket;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * This is authentication packet that gs will send to login server for registration.
 */
public class SM_GS_AUTH extends LsServerPacket
{
	/**
	 * Constructs new instance of <tt>SM_GS_AUTH </tt> packet.
	 * 
	 */
	public SM_GS_AUTH()
	{
		super(0x00);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(LoginServerConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeC(buf, NetworkConfig.GAMESERVER_ID);
		writeC(buf, IPConfig.getDefaultAddress().length);
		writeB(buf, IPConfig.getDefaultAddress());

		List<IPRange> ranges = IPConfig.getRanges();
		int size = ranges.size();
		writeD(buf, size);
        for (IPRange ipRange : ranges) {
            byte[] min = ipRange.getMinAsByteArray();
            byte[] max = ipRange.getMaxAsByteArray();
            writeC(buf, min.length);
            writeB(buf, min);
            writeC(buf, max.length);
            writeB(buf, max);
            writeC(buf, ipRange.getAddress().length);
            writeB(buf, ipRange.getAddress());
        }

		writeH(buf, NetworkConfig.GAME_PORT);
		writeD(buf, NetworkConfig.MAX_ONLINE_PLAYERS);
		writeD(buf, NetworkConfig.REQUIRED_ACCESS);
		writeS(buf, NetworkConfig.LOGIN_PASSWORD);
	}
}
