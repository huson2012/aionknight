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

package loginserver.controller;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Set;
import com.sun.istack.internal.Nullable;
import loginserver.dao.BannedIpDAO;
import loginserver.model.BannedIP;
import org.apache.log4j.Logger;
import commons.database.dao.DAOManager;
import commons.utils.NetworkUtils;

/**
 * Class that controlls all ip banning activity
 */
public class BannedIpController
{
    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(BannedIpController.class);

    /**
     * List of banned ip adresses
     */
    private static Set<BannedIP> banList;

    /**
     * Loads list of banned ips
     */
    public static void load()
    {
        reload();
    }

    /**
     * Loads list of banned ips
     */
    private static void reload()
    {
        // we are not going to make ip ban every minute, so it's ok to simplify a concurrent code a bit
        banList = getDAO().getAllBans();
        log.info("Banned IP loaded " + banList.size() + " IP bans.");
    }

    /**
     * Checks if ip (or mask) is banned
     *
     * @param ip ip address to check for ban
     * @return is it banned or not
     */
    public static boolean isBanned(String ip)
    {
        for (BannedIP ipBan : banList)
        {
            if (ipBan.isActive() && NetworkUtils.checkIPMatching(ipBan.getMask(), ip))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Bans ip or mask for infinite period of time
     *
     * @param ip ip to ban
     * @return was ip banned or not
     */
    public static boolean banIp(String ip)
    {
        return banIp(ip, null);
    }

    /**
     * Bans ip (or mask)
     *
     * @param ip         ip to ban
     * @param expireTime ban expiration time, null = never expires
     * @return was ip banned or not
     */
    public static boolean banIp(String ip, @Nullable Timestamp expireTime)
    {
        BannedIP ipBan = new BannedIP();
        ipBan.setMask(ip);
        ipBan.setTimeEnd(expireTime);
        if (getDAO().insert(ipBan))
        {
            banList.add(ipBan);
            return true;
        }
        return false;
    }

    /**
     * Adds or updates ip ban. Changes are reflected in DB
     *
     * @param ipBan banned ip to add or change
     * @return was it updated or not
     */
    public static boolean addOrUpdateBan(BannedIP ipBan)
    {
        if (ipBan.getId() == null)
        {
            if (getDAO().insert(ipBan))
            {
                banList.add(ipBan);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return getDAO().update(ipBan);
        }
    }

    /**
     * Removes ip ban.
     *
     * @param ip ip to unban
     * @return returns true if ip was successfully unbanned
     */
    public static boolean unbanIp(String ip)
    {
        Iterator<BannedIP> it = banList.iterator();
        while (it.hasNext())
        {
            BannedIP ipBan = it.next();
            if (ipBan.getMask().equals(ip))
            {
                if (getDAO().remove(ipBan))
                {
                    it.remove();
                    return true;
                }
                else
                {
                    break;
                }
            }
        }
        return false;
    }

    /**
     * Retuns {@link loginserver.dao.BannedIpDAO} , just a shortcut
     *
     * @return {@link loginserver.dao.BannedIpDAO}
     */
    private static BannedIpDAO getDAO()
    {
        return DAOManager.getDAO(BannedIpDAO.class);
    }
}
