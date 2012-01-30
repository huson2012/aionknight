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

public class SM_LS_CONTROL_RESPONSE extends GsServerPacket
{

    private final int type;
    private final boolean result;
    private final String playerName;
    private final int param;
    private final String adminName;
    private final int accountId;

    public SM_LS_CONTROL_RESPONSE(int type, boolean result, String playerName, int accountId, int param, String adminName)
    {
        super(0x04);

        this.type = type;
        this.result = result;
        this.playerName = playerName;
        this.param = param;
        this.adminName = adminName;
        this.accountId = accountId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(GsConnection con, ByteBuffer buf)
    {
        writeC(buf, getOpcode());

        writeC(buf, type);
        writeC(buf, result ? 1 : 0);
        writeS(buf, adminName);
        writeS(buf, playerName);
        writeC(buf, param);
        writeD(buf, accountId);
    }
}
