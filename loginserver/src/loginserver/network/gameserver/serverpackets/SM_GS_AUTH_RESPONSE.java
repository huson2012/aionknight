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
import loginserver.network.gameserver.GsAuthResponse;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.GsServerPacket;

/**
 * This packet is response for CM_GS_AUTH its notify Gameserver if registration was ok or what was wrong.
 */
public class SM_GS_AUTH_RESPONSE extends GsServerPacket
{
    /**
     * Response for Gameserver authentication
     */
    private final GsAuthResponse response;

    /**
     * Constructor.
     *
     * @param response
     */
    public SM_GS_AUTH_RESPONSE(GsAuthResponse response)
    {
        super(0x00);

        this.response = response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(GsConnection con, ByteBuffer buf)
    {
        writeC(buf, getOpcode());
        writeC(buf, response.getResponseId());
    }
}
