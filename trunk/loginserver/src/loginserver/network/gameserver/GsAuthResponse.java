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

/**
 * This class contains possible response that LoginServer may send to gameserver if authentication fail etc.
 */
public enum GsAuthResponse
{
    /**
     * Everything is OK
     */
    AUTHED(0),
    /**
     * Password/IP etc does not match.
     */
    NOT_AUTHED(1),
    /**
     * Requested id is not free
     */
    ALREADY_REGISTERED(2);

    /**
     * id of this enum that may be sent to client
     */
    private final byte responseId;

    /**
     * Constructor.
     *
     * @param responseId id of the message
     */
    private GsAuthResponse(int responseId)
    {
        this.responseId = (byte) responseId;
    }

    /**
     * Message Id that may be sent to client.
     *
     * @return message id
     */
    public byte getResponseId()
    {
        return responseId;
    }
}
