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

import loginserver.model.Account;
import commons.utils.Rnd;

public class SessionKey
{
    /**
     * accountId - will be used for authentication on Game Server side.
     */
    public final int accountId;
    /**
     * login ok key
     */
    public final int loginOk;
    /**
     * play ok1 key
     */
    public final int playOk1;
    /**
     * play ok2 key
     */
    public final int playOk2;

    /**
     * Create new SesionKey for this Account
     *
     * @param acc
     */
    public SessionKey(Account acc)
    {
        this.accountId = acc.getId();
        this.loginOk = Rnd.nextInt();
        this.playOk1 = Rnd.nextInt();
        this.playOk2 = Rnd.nextInt();
    }

    /**
     * Create new SesionKey with given values.
     *
     * @param accountId
     * @param loginOk
     * @param playOk1
     * @param playOk2
     */
    public SessionKey(int accountId, int loginOk, int playOk1, int playOk2)
    {
        this.accountId = accountId;
        this.loginOk = loginOk;
        this.playOk1 = playOk1;
        this.playOk2 = playOk2;
    }

    /**
     * Check if given values are ok.
     *
     * @param accountId
     * @param loginOk
     * @return true if accountId and loginOk match this SessionKey
     */
    public boolean checkLogin(int accountId, int loginOk)
    {
        return this.accountId == accountId && this.loginOk == loginOk;
    }

    /**
     * Check if this SessionKey have the same values.
     *
     * @param key
     * @return true if key match this SessionKey.
     */
    public boolean checkSessionKey(SessionKey key)
    {
        return (playOk1 == key.playOk1 && accountId == key.accountId && playOk2 == key.playOk2 && loginOk == key.loginOk);
    }
}
