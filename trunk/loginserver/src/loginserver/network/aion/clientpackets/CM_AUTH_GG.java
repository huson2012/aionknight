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

package loginserver.network.aion.clientpackets;

import java.nio.ByteBuffer;
import loginserver.network.aion.AionAuthResponse;
import loginserver.network.aion.AionClientPacket;
import loginserver.network.aion.AionConnection;
import loginserver.network.aion.AionConnection.State;
import loginserver.network.aion.serverpackets.SM_AUTH_GG;
import loginserver.network.aion.serverpackets.SM_LOGIN_FAIL;

public class CM_AUTH_GG extends AionClientPacket
{
    /**
     * session id - its should match sessionId that was send in Init packet.
     */
    private int sessionId;

    /**
     * private final int data1; private final int data2; private final int data3; private final int data4;
     */

    /**
     * Constructs new instance of <tt>CM_AUTH_GG</tt> packet.
     *
     * @param buf
     * @param client
     */
    public CM_AUTH_GG(ByteBuffer buf, AionConnection client)
    {
        super(buf, client, 0x07);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl()
    {
        sessionId = readD();
        readD();
        readD();
        readD();
        readD();
        readD();
        readD();
        readH();
        readC();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl()
    {
        AionConnection con = getConnection();
        if (con.getSessionId() == sessionId)
        {
            con.setState(State.AUTHED_GG);
            con.sendPacket(new SM_AUTH_GG(sessionId));
        }
        else
        {
            /**
             * Session id is not ok - inform client that smth went wrong - dc client
             */
            con.close(new SM_LOGIN_FAIL(AionAuthResponse.SYSTEM_ERROR), true);
        }
    }
}
