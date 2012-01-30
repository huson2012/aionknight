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

package loginserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import loginserver.GameServerInfo;
import loginserver.GameServerTable;
import loginserver.controller.AccountController;
import loginserver.network.aion.AionConnection;
import loginserver.network.aion.AionServerPacket;
import org.apache.log4j.Logger;

public class SM_SERVER_LIST extends AionServerPacket
{
    /**
     * Logger for this class.
     */
    protected static Logger log = Logger.getLogger(SM_SERVER_LIST.class);

    /**
     * Constructs new instance of <tt>SM_SERVER_LIST</tt> packet.
     */
    public SM_SERVER_LIST()
    {
        super(0x04);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf)
    {
        Collection<GameServerInfo> servers = GameServerTable.getGameServers();
        Map<Integer, Integer> charactersCountOnServer;

        int accountId = con.getAccount().getId();
        int maxId = 0;
        int accessLvl;

        charactersCountOnServer = AccountController.getCharacterCountsFor(accountId);

        writeC(buf, getOpcode());
        writeC(buf, servers.size());// servers
        writeC(buf, con.getAccount().getLastServer());// last server
        for (GameServerInfo gsi : servers)
        {
            accessLvl = (int) (con.getAccount().getAccessLevel());
            if (gsi.getId() > maxId)
            {
                maxId = gsi.getId();
            }
            writeC(buf, gsi.getId());// server id
            writeB(buf, gsi.getIPAddressForPlayer(con.getIP())); // server IP
            writeD(buf, gsi.getPort());// port
            writeC(buf, 0x00); // age limit
            writeC(buf, 0x01);// pvp=1
            writeH(buf, gsi.getCurrentPlayers());// currentPlayers
            writeH(buf, gsi.canAccess(accessLvl) ? gsi.getMaxPlayers() : 0);// maxPlayers
            writeC(buf, gsi.canAccess(accessLvl) ? (gsi.isOnline() ? 1 : 0) : 0);// ServerStatus, up=1
            writeD(buf, gsi.canAccess(accessLvl) ? 1 : 0);// bits);
            writeC(buf, 1);// server.brackets ? 0x01 : 0x00);
        }

        writeH(buf, maxId + 1);
        writeC(buf, 0x01); // 0x01 for autoconnect

        for (int i = 1; i <= maxId; i++)
        {
            if (charactersCountOnServer.containsKey(i))
            {
                writeC(buf, charactersCountOnServer.get(i));
            }
            else
            {
                writeC(buf, 0);
            }
        }
    }
}
