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

package gameserver.model.account;

public class AccountTime
{

	private long accumulatedOnlineTime;
	private long accumulatedRestTime;
	public long getAccumulatedOnlineTime()
	{
		return accumulatedOnlineTime;
	}

	public void setAccumulatedOnlineTime(long accumulatedOnlineTime)
	{
		this.accumulatedOnlineTime = accumulatedOnlineTime;
	}

	public long getAccumulatedRestTime()
	{
		return accumulatedRestTime;
	}

	public void setAccumulatedRestTime(long accumulatedRestTime)
	{
		this.accumulatedRestTime = accumulatedRestTime;
	}

	public int getAccumulatedOnlineHours()
	{
		return toHours(accumulatedOnlineTime);
	}

	public int getAccumulatedOnlineMinutes()
	{
		return toMinutes(accumulatedOnlineTime);
	}

	public int getAccumulatedRestHours()
	{
		return toHours(accumulatedRestTime);
	}

	public int getAccumulatedRestMinutes()
	{
		return toMinutes(accumulatedRestTime);
	}

	private static int toHours(long millis)
	{
		return (int) (millis / 1000) / 3600;
	}

	private static int toMinutes(long millis)
	{
		return (int) ((millis / 1000) % 3600) / 60;
	}
}