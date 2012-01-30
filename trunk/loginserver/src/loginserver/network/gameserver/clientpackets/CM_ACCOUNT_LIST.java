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
import loginserver.GameServerTable;
import loginserver.controller.AccountController;
import loginserver.model.Account;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.serverpackets.SM_REQUEST_KICK_ACCOUNT;

/**
 * Reads the list of accoutn id's that are logged to game server
 */
public class CM_ACCOUNT_LIST extends GsClientPacket
{
    /**
     * Array with accounts that are logged in
     */
    private String[] accountNames;

    /**
     * Creates new packet instance.
     *
     * @param buf    packet data
     * @param client client
     */
    public CM_ACCOUNT_LIST(ByteBuffer buf, GsConnection client)
    {
        super(buf, client, 0x04);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl()
    {
        accountNames = new String[readD()];
        for (int i = 0; i < accountNames.length; i++)
        {
            accountNames[i] = readS();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl()
    {
        for (String s : accountNames)
        {
            Account a = AccountController.loadAccount(s);
            if (GameServerTable.isAccountOnAnyGameServer(a))
            {
                getConnection().sendPacket(new SM_REQUEST_KICK_ACCOUNT(a.getId()));
                continue;
            }
            getConnection().getGameServerInfo().addAccountToGameServer(a);
        }
    }
}
