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
import loginserver.controller.AccountController;
import loginserver.network.aion.AionClientPacket;
import loginserver.network.aion.AionConnection;

/**
 * This packet is send when client was connected to game server and now is reconnection to login server.
 */
public class CM_UPDATE_SESSION extends AionClientPacket
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
     * reconectKey is key that server sends to client for fast reconnection to login server - we will check if this key
     * is valid.
     */
    private int reconnectKey;

    /**
     * Constructs new instance of <tt>CM_UPDATE_SESSION </tt> packet.
     *
     * @param buf    packet data
     * @param client client
     */
    public CM_UPDATE_SESSION(ByteBuffer buf, AionConnection client)
    {
        super(buf, client, 0x08);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl()
    {
        accountId = readD();
        loginOk = readD();
        reconnectKey = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl()
    {
        AccountController.authReconnectingAccount(accountId, reconnectKey, getConnection());
    }
}
