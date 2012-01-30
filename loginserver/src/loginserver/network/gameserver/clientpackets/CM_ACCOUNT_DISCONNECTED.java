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
import loginserver.controller.AccountTimeController;
import loginserver.model.Account;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;

/**
 * In this packet GameServer is informing LoginServer that some account is no longer on GameServer [ie was disconencted]
 */
public class CM_ACCOUNT_DISCONNECTED extends GsClientPacket
{
    /**
     * AccountId of account that was disconnected form GameServer.
     */
    private int accountId;

    /**
     * Constructor.
     *
     * @param buf
     * @param client
     */
    public CM_ACCOUNT_DISCONNECTED(ByteBuffer buf, GsConnection client)
    {
        super(buf, client, 0x03);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl()
    {
        accountId = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl()
    {
        Account account = getConnection().getGameServerInfo().removeAccountFromGameServer(accountId);

        /**
         * account can be null if a player logged out from gs
         * {@link CM_ACCOUNT_RECONNECT_KEY
         */
        if (account != null)
        {
            AccountTimeController.updateOnLogout(account);
        }
    }
}
 
