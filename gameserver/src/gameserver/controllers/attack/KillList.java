/**   
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
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