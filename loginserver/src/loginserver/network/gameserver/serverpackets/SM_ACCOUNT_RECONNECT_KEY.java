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
 * In this packet LoginServer is sending response for CM_ACCOUNT_RECONNECT_KEY with account name and reconnectionKey.
 */
public class SM_ACCOUNT_RECONNECT_KEY extends GsServerPacket
{
    /**
     * accountId of account that will be reconnecting.
     */
    private final int accountId;
    /**
     * ReconnectKey that will be used for authentication.
     */
    private final int reconnectKey;

    /**
     * Constructor.
     *
     * @param accountId
     * @param reconnectKey
     */
    public SM_ACCOUNT_RECONNECT_KEY(int accountId, int reconnectKey)
    {
        super(0x03);

        this.accountId = accountId;
        this.reconnectKey = reconnectKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(GsConnection con, ByteBuffer buf)
    {
        writeC(buf, getOpcode());
        writeD(buf, accountId);
        writeD(buf, reconnectKey);
    }
}
