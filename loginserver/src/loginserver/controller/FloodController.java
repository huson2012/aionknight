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

import java.util.ArrayList;
import loginserver.model.FloodIP;
import org.apache.log4j.Logger;

/**
 * Class that controlls all flood
 */
public class FloodController
{
    /**
     * Logger for this class.
     */
    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(FloodController.class);

    private static final ArrayList<FloodIP> IPs = new ArrayList<FloodIP>();

    public static void addIP(String IP)
    {
        IPs.add(new FloodIP(IP));
    }

    public static boolean checkFlood(String IP)
    {
        boolean found = false;
        int i = 0;

        while (!found && i < IPs.size())
        {
            if (IPs.get(i).getIP().equals(IP))
            {
                found = true;
            }
            else
            {
                i++;
            }
        }

        return IPs.get(i).checkFlood();
    }

    public static boolean exist(String IP)
    {
        for (FloodIP fIP : IPs)
        {
            if (fIP.getIP().equals(IP))
            {
                return true;
            }
        }

        return false;
    }

    private static int getIP(String ip)
    {
        boolean found = false;
        int i = 0;

        while (!found && i < IPs.size())
        {
            if (IPs.get(i).getIP().equals(ip))
            {
                found = true;
            }
            else
            {
                i++;
            }
        }

        return i;
    }

    public static void addConnection(String IP)
    {
        IPs.get(getIP(IP)).addConnection();
    }

    public static int getConnection(String IP)
    {
        return IPs.get(getIP(IP)).size();
    }
}