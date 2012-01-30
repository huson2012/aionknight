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

package loginserver.network.aion;

import java.nio.ByteBuffer;
import commons.network.packet.BaseServerPacket;

public abstract class AionServerPacket extends BaseServerPacket
{
    /**
     * Constructs a new server packet with specified id.
     *
     * @param opcode packet opcode.
     */
    protected AionServerPacket(int opcode)
    {
        super(opcode);
    }

    /**
     * Write and encrypt this packet data for given connection, to given buffer.
     *
     * @param con
     * @param buf
     */
    public final void write(AionConnection con, ByteBuffer buf)
    {
        buf.putShort((short) 0);
        writeImpl(con, buf);
        buf.flip();
        buf.putShort((short) 0);
        ByteBuffer b = buf.slice();

        short size = (short) (con.encrypt(b) + 2);
        buf.putShort(0, size);
        buf.position(0).limit(size);
    }

    /**
     * Write data that this packet represents to given byte buffer.
     *
     * @param con
     * @param buf
     */
    protected abstract void writeImpl(AionConnection con, ByteBuffer buf);
}
