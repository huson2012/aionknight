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
import loginserver.model.Account;
import org.apache.log4j.Logger;
import commons.network.packet.BaseClientPacket;

/**
 * Base class for every Aion -> LS Client Packet
 */
public abstract class AionClientPacket extends BaseClientPacket<AionConnection>
{
    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(AionClientPacket.class);

    /**
     * Constructs new client packet.
     *
     * @param buf    packet data
     * @param client client
     * @param opcode packet id
     */
    protected AionClientPacket(ByteBuffer buf, AionConnection client, int opcode)
    {
        super(buf, opcode);
        setConnection(client);
    }

    /**
     * run runImpl catching and logging Throwable.
     */
    @Override
    public final void run()
    {
        try
        {
            runImpl();
        }
        catch (Throwable e)
        {
            String name;
            Account account = getConnection().getAccount();
            if (account != null)
            {
                name = account.getName();
            }
            else
            {
                name = getConnection().getIP();
            }

            log.error("error handling client (" + name + ") message " + this, e);
        }
    }

    /**
     * Send new AionServerPacket to connection that is owner of this packet. This method is equvalent to:
     * getConnection().sendPacket(msg);
     *
     * @param msg
     */
    protected void sendPacket(AionServerPacket msg)
    {
        getConnection().sendPacket(msg);
    }
}
