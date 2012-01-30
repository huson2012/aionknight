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
import loginserver.GameServerTable;
import loginserver.controller.AccountController;
import loginserver.network.aion.AionAuthResponse;
import loginserver.network.aion.AionClientPacket;
import loginserver.network.aion.AionConnection;
import loginserver.network.aion.serverpackets.SM_LOGIN_FAIL;

public class CM_SERVER_LIST extends AionClientPacket
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
     * Constructs new instance of <tt>CM_SERVER_LIST </tt> packet.
     *
     * @param buf
     * @param client
     */
    public CM_SERVER_LIST(ByteBuffer buf, AionConnection client)
    {
        super(buf, client, 0x05);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl()
    {
        accountId = readD();
        loginOk = readD();
        readD();// unk
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
        if (con.getSessionKey().checkLogin(accountId, loginOk))
        {
            if (GameServerTable.getGameServers().isEmpty())
            {
                con.close(new SM_LOGIN_FAIL(AionAuthResponse.NO_GS_REGISTERED), true);
            }
            else
            {
                AccountController.loadCharactersCount(accountId);
            }
        }
        else
        {
            /**
             * Session key is not ok - inform client that smth went wrong - dc client
             */
            con.close(new SM_LOGIN_FAIL(AionAuthResponse.SYSTEM_ERROR), true);
        }
    }
}