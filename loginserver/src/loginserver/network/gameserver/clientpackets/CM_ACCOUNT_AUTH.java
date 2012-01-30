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

package loginserver.network.gameserver.clientpackets;

import java.nio.ByteBuffer;
import loginserver.controller.AccountController;
import loginserver.network.aion.SessionKey;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;

/**
 * In this packet Gameserver is asking if given account sessionKey is valid at Loginserver side. [if user that is
 * authenticating on Gameserver is already authenticated on Loginserver]
 */
public class CM_ACCOUNT_AUTH extends GsClientPacket
{
    /**
     * SessionKey that GameServer needs to check if is valid at Loginserver side.
     */
    private SessionKey sessionKey;

    /**
     * Constructor.
     *
     * @param buf
     * @param client
     */
    public CM_ACCOUNT_AUTH(ByteBuffer buf, GsConnection client)
    {
        super(buf, client, 0x01);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl()
    {
        int accountId = readD();
        int loginOk = readD();
        int playOk1 = readD();
        int playOk2 = readD();

        sessionKey = new SessionKey(accountId, loginOk, playOk1, playOk2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl()
    {
        AccountController.checkAuth(sessionKey, getConnection());
    }
}
