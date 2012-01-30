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

package gameserver.model.gameobjects.player;

public class Title
{
	private int titleId;
	private int race;
	private long title_date = 0;
	private long title_expires_time = 0;

	public Title(int titleId, int race, long title_date, long title_expires_time)
	{
		this.titleId = titleId;
		this.race = race;
		this.title_date = title_date;
		this.title_expires_time = title_expires_time;
	}

	public int getTitleId()
	{
		return titleId;
	}

	public int getRace()
	{
		return race;
	}

	public long getTitleDate()
	{
		return title_date;
	}

	public long getTitleExpiresTime()
	{
		return title_expires_time;
	}

	public void setTitleId(int titleId)
	{
		this.titleId = titleId;
	}

	public void setRace(int race)
	{
		this.race = race;
	}

	public long getTitleTimeLeft()
	{
		if(title_expires_time == 0)
			return 0;

		long timeLeft = (title_date + ((title_expires_time )  * 1000L)) - System.currentTimeMillis();
		if(timeLeft < 0)
			timeLeft = 0;

		return timeLeft /1000L ;
	}

	public void setTitleDate(long title_date)
	{
		this.title_date = title_date;
	}

	public void setTitleExpiresTime(long title_expires_time)
	{
		this.title_expires_time = title_expires_time;
	}
}
