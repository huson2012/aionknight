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
 * Class for storing account time data (last login time,
 * last session duration time, accumulated online time today,
 * accumulated rest time today)
 *
 * @author EvilSpirit
 */
public class AccountTime
{
    /**
     * Time the account has last logged in
     */
    private Timestamp lastLoginTime;
    /**
     * Time after the account will expired
     */
    private Timestamp expirationTime;
    /**
     * Time when the penalty will end
     */
    private Timestamp penaltyEnd;
    /**
     * The duration of the session
     */
    private long sessionDuration;
    /**
     * Accumulated Online Time
     */
    private long accumulatedOnlineTime;
    /**
     * Accumulated Rest Time
     */
    private long accumulatedRestTime;

    /**
     * Default constructor. Set the lastLoginTime to current time
     */
    public AccountTime()
    {
        this.lastLoginTime = new Timestamp(System.currentTimeMillis());
    }

    /**
     * @return lastLoginTime
     */
    public Timestamp getLastLoginTime()
    {
        return lastLoginTime;
    }

    /**
     * @param lastLoginTime
     */
    public void setLastLoginTime(Timestamp lastLoginTime)
    {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * @return sessionDuration
     */
    public long getSessionDuration()
    {
        return sessionDuration;
    }

    /**
     * @param sessionDuration
     */
    public void setSessionDuration(long sessionDuration)
    {
        this.sessionDuration = sessionDuration;
    }

    /**
     * @return accumulatedOnlineTime
     */
    public long getAccumulatedOnlineTime()
    {
        return accumulatedOnlineTime;
    }

    /**
     * @param accumulatedOnlineTime
     */
    public void setAccumulatedOnlineTime(long accumulatedOnlineTime)
    {
        this.accumulatedOnlineTime = accumulatedOnlineTime;
    }

    /**
     * @return accumulatedRestTime
     */
    public long getAccumulatedRestTime()
    {
        return accumulatedRestTime;
    }

    /**
     * @param accumulatedRestTime
     */
    public void setAccumulatedRestTime(long accumulatedRestTime)
    {
        this.accumulatedRestTime = accumulatedRestTime;
    }

    /**
     * @return expirationTime
     */
    public Timestamp getExpirationTime()
    {
        return expirationTime;
    }

    /**
     * @param expirationTime
     */
    public void setExpirationTime(Timestamp expirationTime)
    {
        this.expirationTime = expirationTime;
    }

    /**
     * @return penaltyEnd
     */
    public Timestamp getPenaltyEnd()
    {
        return penaltyEnd;
    }

    /**
     * @param penaltyEnd
     */
    public void setPenaltyEnd(Timestamp penaltyEnd)
    {
        this.penaltyEnd = penaltyEnd;
    }
}
