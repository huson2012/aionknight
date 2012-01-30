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

package loginserver.network.aion;

import java.nio.ByteBuffer;
import loginserver.network.aion.AionConnection.State;
import loginserver.network.aion.clientpackets.CM_AUTH_GG;
import loginserver.network.aion.clientpackets.CM_LOGIN;
import loginserver.network.aion.clientpackets.CM_PLAY;
import loginserver.network.aion.clientpackets.CM_SERVER_LIST;
import loginserver.network.aion.clientpackets.CM_UPDATE_SESSION;
import org.apache.log4j.Logger;

class AionPacketHandler
{
    /**
     * logger for this class
     */
    private static final Logger log = Logger.getLogger(AionPacketHandler.class);

    /**
     * Reads one packet from given ByteBuffer
     *
     * @param data
     * @param client
     * @return AionClientPacket object from binary data
     */
    public static AionClientPacket handle(ByteBuffer data, AionConnection client)
    {
        AionClientPacket msg = null;
        State state = client.getState();
        int id = data.get() & 0xff;

        switch (state)
        {
            case CONNECTED:
            {
                switch (id)
                {
                    case 0x07:
                        msg = new CM_AUTH_GG(data, client);
                        break;
                    case 0x08:
                        msg = new CM_UPDATE_SESSION(data, client);
                        break;
                    default:
                        unknownPacket(state, id);
                }
                break;
            }
            case AUTHED_GG:
            {
                switch (id)
                {
                    case 0x0B:
                        msg = new CM_LOGIN(data, client);
                        break;
                    default:
                        unknownPacket(state, id);
                }
                break;
            }
            case AUTHED_LOGIN:
            {
                switch (id)
                {
                    case 0x05:
                        msg = new CM_SERVER_LIST(data, client);
                        break;
                    case 0x02:
                        msg = new CM_PLAY(data, client);
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
        log.warn(String.format("Unknown packet recived from Aion client: 0x%02X state=%s", id, state.toString()));
    }
}
