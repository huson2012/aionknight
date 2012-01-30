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

public class SM_PLAY_OK extends AionServerPacket
{
    /**
     * playOk1 is part of session key - its used for security purposes [checked at game server side]
     */
    private final int playOk1;
    /**
     * playOk2 is part of session key - its used for security purposes [checked at game server side]
     */
    private final int playOk2;

    private final int serverId;

    /**
     * Constructs new instance of <tt>SM_PLAY_OK </tt> packet.
     *
     * @param key session key
     */
    public SM_PLAY_OK(SessionKey key, byte serverId)
    {
        super(0x07);

        this.playOk1 = key.playOk1;
        this.playOk2 = key.playOk2;
        this.serverId = serverId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf)
    {
        writeC(buf, getOpcode());
        writeD(buf, playOk1);
        writeD(buf, playOk2);
        writeC(buf, serverId);
    }
}
