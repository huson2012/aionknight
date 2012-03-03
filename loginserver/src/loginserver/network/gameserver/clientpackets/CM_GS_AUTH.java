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
import java.util.ArrayList;
import java.util.List;
import loginserver.GameServerTable;
import loginserver.dao.GameServersDAO;
import loginserver.network.aion.AionConnection;
import loginserver.network.gameserver.GsAuthResponse;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.GsConnection.State;
import loginserver.network.gameserver.serverpackets.SM_GS_AUTH_RESPONSE;
import loginserver.network.gameserver.serverpackets.SM_MACBAN_LIST;
import loginserver.utils.ThreadPoolManager;
import commons.database.dao.DAOManager;
import commons.network.IPRange;

import org.apache.log4j.Logger;

/**
 * This is authentication packet that gs will send to login server for registration.
 */
public class CM_GS_AUTH extends GsClientPacket
{
	private static final Logger log = Logger.getLogger(AionConnection.class);
    /**
     * Password for authentication
     */
    private String password;

    /**
     * Id of GameServer
     */
    private byte gameServerId;

    /**
     * Maximum number of players that this Gameserver can accept.
     */
    private int maxPlayers;

    /**
     * Required access level to login
     */
    private int requiredAccess;

    /**
     * Port of this Gameserver.
     */
    private int port;

    /**
     * Default address for server
     */
    private byte[] defaultAddress;

    /**
     * List of IPRanges for this gameServer
     */
    private List<IPRange> ipRanges;

    /**
     * Constructor.
     *
     * @param buf
     * @param client
     */
    public CM_GS_AUTH(ByteBuffer buf, GsConnection client)
    {
        super(buf, client, 0x00);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl()
    {
        gameServerId = (byte) readC();

        defaultAddress = readB(readC());
        int size = readD();
        ipRanges = new ArrayList<IPRange>(size);
        for (int i = 0; i < size; i++)
        {
            ipRanges.add(new IPRange(readB(readC()), readB(readC()), readB(readC())));
        }

        port = readH();
        maxPlayers = readD();
        requiredAccess = readD();
        password = readS();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runImpl()
    {
    	final GsConnection client = this.getConnection();

        GsAuthResponse resp = GameServerTable.registerGameServer(client, gameServerId, defaultAddress, ipRanges, port, maxPlayers, requiredAccess, password);

        switch (resp)
        {
            case AUTHED:
            	log.info("Gameserver #"+gameServerId+" is now online.");
                getConnection().setState(State.AUTHED);
                DAOManager.getDAO(GameServersDAO.class).writeGameServerStatus(GameServerTable.getGameServerInfo(gameServerId));
                sendPacket(new SM_GS_AUTH_RESPONSE(resp));
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					client.sendPacket(new SM_MACBAN_LIST());
			}}, 500);
                break;

            default:
                client.close(new SM_GS_AUTH_RESPONSE(resp), true);
        }
    }
}
