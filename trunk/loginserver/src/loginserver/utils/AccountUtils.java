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

package loginserver.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;
import commons.utils.Base64;

/**
 * Class with usefull methods to use with accounts
 */
public class AccountUtils
{
    /**
     * Logger
     */
    private static final Logger log = Logger.getLogger(AccountUtils.class);

    /**
     * Encodes password. SHA-1 is used to encode password bytes, Base64 wraps SHA1-hash to string.
     *
     * @param password password to encode
     * @return retunrs encoded password.
     */
    public static String encodePassword(String password)
    {
        try
        {
            MessageDigest messageDiegest = MessageDigest.getInstance("SHA-1");
            messageDiegest.update(password.getBytes("UTF-8"));
            return Base64.encodeToString(messageDiegest.digest(), false);
        }
        catch (NoSuchAlgorithmException e)
        {
            log.error("Exception while encoding password");
            throw new Error(e);
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("Exception while encoding password");
            throw new Error(e);
        }
    }
}
