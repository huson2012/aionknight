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

/**
 * This class represents banned ip
 */
public class BannedIP
{
    /**
     * Returns id of ip ban
     */
    private Integer id;

    /**
     * Returns ip mask
     */
    private String mask;

    /**
     * Returns expiration time
     */
    private Timestamp timeEnd;

    /**
     * Checks if ban is still active
     *
     * @return true if ban is still active
     */
    public boolean isActive()
    {
        return timeEnd == null || timeEnd.getTime() > System.currentTimeMillis();
    }

    /**
     * Returns ban id
     *
     * @return ban id
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * Sets ban id
     *
     * @param id ban id
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * Retuns ip mask
     *
     * @return ip mask
     */
    public String getMask()
    {
        return mask;
    }

    /**
     * Sets ip mask
     *
     * @param mask ip mask
     */
    public void setMask(String mask)
    {
        this.mask = mask;
    }

    /**
     * Returns expiration time of ban
     *
     * @return expiration time of ban
     */
    public Timestamp getTimeEnd()
    {
        return timeEnd;
    }

    /**
     * Sets expiration time of ban
     *
     * @param timeEnd expiration time of ban
     */
    public void setTimeEnd(Timestamp timeEnd)
    {
        this.timeEnd = timeEnd;
    }

    /**
     * Returns true if this ip ban is equal to another. Based on {@link #mask}
     *
     * @param o another ip ban
     * @return true if ban's are equals
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof BannedIP))
        {
            return false;
        }

        BannedIP bannedIP = (BannedIP) o;

        return !(mask != null ? !mask.equals(bannedIP.mask) : bannedIP.mask != null);
    }

    /**
     * Returns ban's hashcode. Based on mask
     *
     * @return ban's hashcode
     */
    @Override
    public int hashCode()
    {
        return mask != null ? mask.hashCode() : 0;
    }
}
