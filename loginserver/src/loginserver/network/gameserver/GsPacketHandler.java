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

package loginserver.network.gameserver;

import java.nio.ByteBuffer;
import loginserver.network.gameserver.GsConnection.State;
import loginserver.network.gameserver.clientpackets.CM_ACCOUNT_AUTH;
import loginserver.network.gameserver.clientpackets.CM_ACCOUNT_DISCONNECTED;
import loginserver.network.gameserver.clientpackets.CM_ACCOUNT_LIST;
import loginserver.network.gameserver.clientpackets.CM_ACCOUNT_RECONNECT_KEY;
import loginserver.network.gameserver.clientpackets.CM_BAN;
import loginserver.network.gameserver.clientpackets.CM_GS_AUTH;
import loginserver.network.gameserver.clientpackets.CM_GS_CHARACTER_COUNT;
import loginserver.network.gameserver.clientpackets.CM_LS_CONTROL;
import loginserver.network.gameserver.clientpackets.CM_MAC;
import loginserver.network.gameserver.clientpackets.CM_MACBAN_CONTROL;
import loginserver.network.gameserver.clientpackets.CM_REQUEST_MAC;

import org.apache.log4j.Logger;

class GsPacketHandler
{
    /**
     * logger for this class
     */
    private static final Logger log = Logger.getLogger(GsPacketHandler.class);

    /**
     * Reads one packet from given ByteBuffer
     *
     * @param data
     * @param client
     * @return GsClientPacket object from binary data
     */
    public static GsClientPacket handle(ByteBuffer data, GsConnection client)
    {
        GsClientPacket msg = null;
        State state = client.getState();
        int id = data.get() & 0xff;

        switch (state)
        {
            case CONNECTED:
            {
                switch (id)
                {
                    case 0x00:
                        msg = new CM_GS_AUTH(data, client);
                        break;
                    default:
                        unknownPacket(state, id);
                }
                break;
            }
            case AUTHED:
            {
                switch (id)
                {
                    case 0x01:
                        msg = new CM_ACCOUNT_AUTH(data, client);
                        break;
                    case 0x02:
                        msg = new CM_ACCOUNT_RECONNECT_KEY(data, client);
                        break;
                    case 0x03:
                        msg = new CM_ACCOUNT_DISCONNECTED(data, client);
                        break;
                    case 0x04:
                        msg = new CM_ACCOUNT_LIST(data, client);
                        break;
                    case 0x05:
                        msg = new CM_LS_CONTROL(data, client);
                        break;
                    case 0x06:
                        msg = new CM_BAN(data, client);
                        break;
                    case 0x07:
                        msg = new CM_GS_CHARACTER_COUNT(data, client);
                        break;
                        
                    case 0x10:
                    	msg = new CM_MAC(data, client);
                       	break;
                    case 0x11:
                      	msg = new CM_MACBAN_CONTROL(data, client);
                       	break;
                    case 0x12:
                       	msg = new CM_REQUEST_MAC(data, client);
                    break;
                    default:
                        unknownPacket(state, id);
                }
                break;
            }
        }
        return msg;
    }

    /**
     * Logs unknown packet.
     *
     * @param state
     * @param id
     */
    private static void unknownPacket(State state, int id)
    {
        log.warn(String.format("Unknown packet recived from Game Server: 0x%02X state=%s", id, state.toString()));
    }
}
