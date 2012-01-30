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

package gameserver.controllers.attack;

import gameserver.configs.main.CustomConfig;
import javolution.util.FastMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KillList
{
	private static final long DAY_IN_MILLISECONDS = CustomConfig.DAILY_PVP_PERIOD * 3600000;
	private FastMap<Integer, List<Long>> killList;
	
	public KillList()
	{
		killList = new FastMap<Integer, List<Long>>();
	}

	/**
	 * @param winnerId
	 * @param victimId
	 * @return killsForVictimId
	 */
	public int getKillsFor(int victimId)
	{
		List<Long> killTimes = killList.get(victimId);
		
		if (killTimes == null)
			return 0;
		
		long now = System.currentTimeMillis();
		int killCount = 0;
		
		for(Iterator<Long> i = killTimes.iterator(); i.hasNext(); )
		{
			if (now - i.next() > DAY_IN_MILLISECONDS)
			{
				i.remove();
			}
			else
			{
				killCount++;
			}
		}
		
		return killCount;
	}

	/**
	 * @param victimId
	 */
	public void addKillFor(int victimId)
	{
		List<Long> killTimes = killList.get(victimId);
		if (killTimes == null)
		{
			killTimes = new ArrayList<Long>();
			killList.put(victimId, killTimes);
		}
		
		killTimes.add(System.currentTimeMillis());
	}
}