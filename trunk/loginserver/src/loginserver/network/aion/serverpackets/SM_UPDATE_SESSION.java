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

package loginserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import loginserver.network.aion.AionConnection;
import loginserver.network.aion.AionServerPacket;
import loginserver.network.aion.SessionKey;

/**
 * This packet is send to client to update sessionKey [for fast reconnection feature]
 */
public class SM_UPDATE_SESSION extends AionServerPacket
{
    /**
     * accountId is part of session key - its used for security purposes
     */
    private final int accountId;
    /**
     * loginOk is part of session key - its used for security purposes
     */
    private final int loginOk;

    /**
     * Constructs new instance of <tt>SM_UPDATE_SESSION </tt> packet.
     *
     * @param key session key
     */
    public SM_UPDATE_SESSION(SessionKey key)
    {
        super(0x0c);

        this.accountId = key.accountId;
        this.loginOk = key.loginOk;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf)
    {
        writeC(buf, getOpcode());
        writeD(buf, accountId);
        writeD(buf, loginOk);
        writeC(buf, 0x00);// sysmsg if smth is wrong
    }
}
