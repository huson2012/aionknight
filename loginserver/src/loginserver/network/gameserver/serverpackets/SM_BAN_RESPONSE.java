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

package loginserver.network.gameserver.serverpackets;

import java.nio.ByteBuffer;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.GsServerPacket;

/**
 * In this packet LoginServer is answering on GameServer ban request
 */
public class SM_BAN_RESPONSE extends GsServerPacket
{
    private final byte type;
    private final int accountId;
    private final String ip;
    private final int time;
    private final int adminObjId;
    private final boolean result;

    public SM_BAN_RESPONSE(byte type, int accountId, String ip, int time, int adminObjId, boolean result)
    {
        super(0x05);

        this.type = type;
        this.accountId = accountId;
        this.ip = ip;
        this.time = time;
        this.adminObjId = adminObjId;
        this.result = result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(GsConnection con, ByteBuffer buf)
    {
        writeC(buf, getOpcode());

        writeC(buf, type);
        writeD(buf, accountId);
        writeS(buf, ip);
        writeD(buf, time);
        writeD(buf, adminObjId);
        writeC(buf, result ? 1 : 0);
    }
}
