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

import java.sql.Timestamp;
import java.util.ArrayList;
import loginserver.configs.Config;

/**
 * This class represents flood ip
 */
public class FloodIP
{
    private final String IP;
    private final ArrayList<Timestamp> dates;

    public FloodIP(String IP)
    {
        this.IP = IP;
        dates = new ArrayList<Timestamp>();
        this.addConnection();
    }

    public void addConnection()
    {
        dates.add(new Timestamp(System.currentTimeMillis()));

        deleteOldConnection();
    }

    void deleteOldConnection()
    {
        ArrayList<Timestamp> datesTmp = dates;
        int i = datesTmp.size() - 1;

        while (i >= 0)
        {
            if (datesTmp.get(i).getTime() < (System.currentTimeMillis() - (Config.FLOOD_CONTROLLER_INTERVAL * 60 * 1000)))
            {
                dates.remove(i);
            }
            i--;
        }
    }

    public String getIP()
    {
        return IP;
    }

    public boolean checkFlood()
    {
        deleteOldConnection();
        return (dates.size() >= Config.FLOOD_CONTROLLER_MAX_CONNECTION);
    }

    public int size()
    {
        return dates.size();
    }
}
