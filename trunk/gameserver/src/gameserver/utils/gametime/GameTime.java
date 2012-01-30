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

package gameserver.utils.gametime;

import gameserver.spawn.DayNightSpawnManager;
import java.security.InvalidParameterException;

/**
 * Represents the internal clock for the time in aion world
 */
public class GameTime
{
	public static final int	MINUTES_IN_YEAR		= 60 * 24 * 31 * 12;
	public static final int	MINUTES_IN_MONTH	= 60 * 24 * 31;
	public static final int	MINUTES_IN_DAY		= 60 * 24;
	public static final int	MINUTES_IN_HOUR		= 60;

	private int				gameTime			= 0;
	
	private DayTime dayTime;

	/**
	 * Constructs a GameTime with the given time in minutes since midnight 1/1/00
	 * 
	 * @param time
	 *           Minutes since midnight 1/1/00
	 */
	public GameTime(int time)
	{
		if(time < 0)
			throw new InvalidParameterException("Time must be >= 0");
		gameTime = time;
	}

	/**
	 * Gets the ingame time in minutes
	 * 
	 * @return The number of minutes since 1/1/00 00:00:00
	 */
	public int getTime()
	{
		return gameTime;
	}

	/**
	 * Increases game time by a minute
	 */
	protected void increase()
	{
		gameTime++;
		if(getMinute() == 0)
		{
			analyzeDayTime();
		}
	}

	/**
	 * 
	 */
	private void analyzeDayTime()
	{
		DayTime newDateTime = null;
		int hour = getHour();
		if(hour > 21 || hour < 4)
			newDateTime = DayTime.NIGHT;
		else if(hour > 16)
			newDateTime = DayTime.EVENING;
		else if (hour > 8)
			newDateTime = DayTime.AFTERNOON;
		else
			newDateTime = DayTime.MORNING;
		
		if(newDateTime != this.dayTime)
		{
			this.dayTime = newDateTime;
			DayNightSpawnManager.getInstance().notifyChangeMode();
		}
	}

	/**
	 * Gets the year in the game
	 * 
	 * @return Year
	 */
	public int getYear()
	{
		return gameTime / MINUTES_IN_YEAR;
	}

	/**
	 * Gets the month in the game, 1-12
	 * 
	 * @return Month 1-12
	 */
	public int getMonth()
	{
		return (gameTime % MINUTES_IN_YEAR) / MINUTES_IN_MONTH + 1;
	}

	/**
	 * Gets the day in the game, 1-31
	 * 
	 * @return Day 1-31
	 */
	public int getDay()
	{
		return (gameTime % MINUTES_IN_MONTH) / MINUTES_IN_DAY + 1;
	}

	/**
	 * Gets the hour in the game, 0-23
	 * 
	 * @return Hour 0-23
	 */
	public int getHour()
	{
		return (gameTime % MINUTES_IN_DAY) / (MINUTES_IN_HOUR);
	}

	/**
	 * Gets the minute in the game, 0-59
	 * 
	 * @return Minute 0-59
	 */
	public int getMinute()
	{
		return (gameTime % MINUTES_IN_HOUR);
	}

	/**
	 * @return the dayTime
	 */
	public DayTime getDayTime()
	{
		return dayTime;
	}
}
