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
import loginserver.dao.AccountDAO;
import loginserver.model.Account;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.serverpackets.SM_LS_CONTROL_RESPONSE;
import commons.database.dao.DAOManager;

public class CM_LS_CONTROL extends GsClientPacket
{
    private String accountName;
    private int param;
    private int type;
    private String playerName;
    private String adminName;

    public CM_LS_CONTROL(ByteBuffer buf, GsConnection client)
    {
        super(buf, client, 0x05);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl()
    {

        type = readC();
        adminName = readS();
        accountName = readS();
        playerName = readS();
        param = readC();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl()
    {

        Account account = DAOManager.getDAO(AccountDAO.class).getAccount(accountName);
        switch (type)
        {
            case 1:
                account.setAccessLevel((byte) param);
                break;
            case 2:
                account.setMembership((byte) param);
                break;
        }
        boolean result = DAOManager.getDAO(AccountDAO.class).updateAccount(account);
        sendPacket(new SM_LS_CONTROL_RESPONSE(type, result, playerName, account.getId(), param, adminName));
    }
}
