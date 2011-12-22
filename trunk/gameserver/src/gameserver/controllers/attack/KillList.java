/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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
			if (now - i.next().longValue() > DAY_IN_MILLISECONDS)
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