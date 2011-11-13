/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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