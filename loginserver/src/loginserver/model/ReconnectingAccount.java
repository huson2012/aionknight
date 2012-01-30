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

package loginserver.model;

/**
 * This object is storing Account and corresponding to it reconnectionKey for client that will be reconnecting to
 * LoginServer from GameServer using fast reconnect feature
 */
public class ReconnectingAccount
{
    /**
     * Account object of account that will be reconnecting.
     */
    private final Account account;
    /**
     * Reconnection Key that will be used for authenticating
     */
    private final int reconnectionKey;

    /**
     * Constructor.
     *
     * @param account
     * @param reconnectionKey
     */
    public ReconnectingAccount(Account account, int reconnectionKey)
    {
        this.account = account;
        this.reconnectionKey = reconnectionKey;
    }

    /**
     * Return Account.
     *
     * @return account
     */
    public Account getAccount()
    {
        return account;
    }

    /**
     * Return reconnection key for this account
     *
     * @return reconnectionKey
     */
    public int getReconnectionKey()
    {
        return reconnectionKey;
    }
}