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
import loginserver.GameServerInfo;
import loginserver.GameServerTable;
import loginserver.network.aion.AionAuthResponse;
import loginserver.network.aion.AionClientPacket;
import loginserver.network.aion.AionConnection;
import loginserver.network.aion.SessionKey;
import loginserver.network.aion.serverpackets.SM_LOGIN_FAIL;
import loginserver.network.aion.serverpackets.SM_PLAY_FAIL;
import loginserver.network.aion.serverpackets.SM_PLAY_OK;

public class CM_PLAY extends AionClientPacket
{
    /**
     * accountId is part of session key - its used for security purposes
     */
    private int accountId;
    /**
     * loginOk is part of session key - its used for security purposes
     */
    private int loginOk;
    /**
     * Id of game server that this client is trying to play on.
     */
    private byte servId;

    /**
     * Constructs new instance of <tt>SM_PLAY_FAIL</tt> packet.
     *
     * @param buf    packet data
     * @param client client
     */
    public CM_PLAY(ByteBuffer buf, AionConnection client)
    {
        super(buf, client, 0x02);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl()
    {
        accountId = readD();
        loginOk = readD();
        servId = (byte) readC();
        readD();
        readD();
        readD();
        readH();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl()
    {
        AionConnection con = getConnection();
        SessionKey key = con.getSessionKey();

        if (key.checkLogin(accountId, loginOk))
        {
            GameServerInfo gsi = GameServerTable.getGameServerInfo(servId);
            if (gsi == null || !gsi.isOnline())
            {
                con.sendPacket(new SM_PLAY_FAIL(AionAuthResponse.SERVER_DOWN));
            }
            // else if(serv gm only)
            // con.sendPacket(new SM_PLAY_FAIL(AionAuthResponse.GM_ONLY));
            else if (gsi.isFull())
            {
                con.sendPacket(new SM_PLAY_FAIL(AionAuthResponse.SERVER_FULL));
            }
            else
            {
                con.setJoinedGs();
                sendPacket(new SM_PLAY_OK(key, servId));
            }
        }
        else
        {
            con.close(new SM_LOGIN_FAIL(AionAuthResponse.SYSTEM_ERROR), true);
        }
    }
}