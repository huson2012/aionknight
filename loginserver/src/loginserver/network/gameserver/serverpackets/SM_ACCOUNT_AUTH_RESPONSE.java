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

package loginserver.network.gameserver.serverpackets;

import java.nio.ByteBuffer;
import loginserver.model.AccountTime;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.GsServerPacket;

/**
 * In this packet LoginServer is answering on GameServer request about valid authentication data and also sends account
 * name of user that is authenticating on GameServer.
 */
public class SM_ACCOUNT_AUTH_RESPONSE extends GsServerPacket
{
    /**
     * Account id
     */
    private final int accountId;

    /**
     * True if account is authenticated.
     */
    private final boolean ok;

    /**
     * account name
     */
    private final String accountName;

    /**
     * Access level
     */
    private final byte accessLevel;

    /**
     * Membership
     */
    private final byte membership;

    /**
     * Constructor.
     *
     * @param accountId
     * @param ok
     * @param accountName
     * @param accessLevel
     * @param membership
     */
    public SM_ACCOUNT_AUTH_RESPONSE(int accountId, boolean ok, String accountName, byte accessLevel, byte membership)
    {
        super(0x01);

        this.accountId = accountId;
        this.ok = ok;
        this.accountName = accountName;
        this.accessLevel = accessLevel;
        this.membership = membership;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(GsConnection con, ByteBuffer buf)
    {
        writeC(buf, getOpcode());
        writeD(buf, accountId);
        writeC(buf, ok ? 1 : 0);

        if (ok)
        {
            writeS(buf, accountName);

            AccountTime accountTime = con.getGameServerInfo().getAccountFromGameServer(accountId).getAccountTime();

            writeQ(buf, accountTime.getAccumulatedOnlineTime());
            writeQ(buf, accountTime.getAccumulatedRestTime());
            writeC(buf, accessLevel);
            writeC(buf, membership);
        }
    }
}
