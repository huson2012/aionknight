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
import java.sql.Timestamp;
import loginserver.GameServerInfo;
import loginserver.GameServerTable;
import loginserver.controller.AccountController;
import loginserver.controller.BannedIpController;
import loginserver.dao.AccountDAO;
import loginserver.dao.AccountTimeDAO;
import loginserver.model.Account;
import loginserver.model.AccountTime;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.serverpackets.SM_BAN_RESPONSE;
import commons.database.dao.DAOManager;

/**
 * The universal packet for account/IP bans
 */
public class CM_BAN extends GsClientPacket
{
    /**
     * Ban type
     * 1 = account
     * 2 = IP
     * 3 = Full ban (account and IP)
     */
    private byte type;

    /**
     * Account to ban
     */
    private int accountId;

    /**
     * IP or mask to ban
     */
    private String ip;

    /**
     * Time in minutes. 0 = infinity;
     * If time < 0 then it's unban command
     */
    private int time;

    /**
     * Object ID of Admin, who request the ban
     */
    private int adminObjId;

    /**
     * Constructor.
     *
     * @param buf
     * @param client
     */
    public CM_BAN(ByteBuffer buf, GsConnection client)
    {
        super(buf, client, 0x06);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl()
    {
        this.type = (byte) readC();
        this.accountId = readD();
        this.ip = readS();
        this.time = readD();
        this.adminObjId = readD();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl()
    {
        boolean result = false;

        // Ban account
        if ((type == 1 || type == 3) && accountId != 0)
        {
            Account account = null;

            // Find account on GameServers
            for (GameServerInfo gsi : GameServerTable.getGameServers())
            {
                if (gsi.isAccountOnGameServer(accountId))
                {
                    account = gsi.getAccountFromGameServer(accountId);
                    break;
                }
            }

            // 1000 is 'infinity' value
            Timestamp newTime = null;
            if (time >= 0)
            {
                newTime = new Timestamp(time == 0 ? 1000 : System.currentTimeMillis() + time * 60000);
            }

            if (account != null)
            {
                AccountTime accountTime = account.getAccountTime();
                accountTime.setPenaltyEnd(newTime);
                account.setAccountTime(accountTime);
                result = true;
            }
            else
            {
                AccountTime accountTime = DAOManager.getDAO(AccountTimeDAO.class).getAccountTime(accountId);
                accountTime.setPenaltyEnd(newTime);
                result = DAOManager.getDAO(AccountTimeDAO.class).updateAccountTime(accountId, accountTime);
            }
        }

        // Ban IP
        if (type == 2 || type == 3)
        {
            if (accountId != 0) // If we got account ID, then ban last IP
            {
                String newip = DAOManager.getDAO(AccountDAO.class).getLastIp(accountId);
                if (!newip.isEmpty())
                {
                    ip = newip;
                }
            }
            if (!ip.isEmpty())
            {
                // Unban first. For banning it needs to update time
                if (BannedIpController.isBanned(ip))
                {
                    // Result set for unban request
                    result = BannedIpController.unbanIp(ip);
                }
                if (time >= 0) // Ban
                {
                    Timestamp newTime = time != 0 ? new Timestamp(System.currentTimeMillis() + time * 60000) : null;
                    result = BannedIpController.banIp(ip, newTime);
                }
            }
        }

        // Now kick account
        if (accountId != 0)
        {
            AccountController.kickAccount(accountId);
        }

        // Respond to GS
        sendPacket(new SM_BAN_RESPONSE(type, accountId, ip, time, adminObjId, result));
    }
}
