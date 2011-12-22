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

package gameserver.model;

import gameserver.configs.network.FloodConfig;
import java.sql.Timestamp;
import java.util.ArrayList;

public class FloodIP
{
	private String					IP;
	private ArrayList<Timestamp>	dates;
	
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
	
	public void deleteOldConnection()
	{
		ArrayList<Timestamp> datesTmp = dates;
		int i = datesTmp.size()-1;
		
		while (i >= 0)
		{
			if (datesTmp.get(i).getTime() < (System.currentTimeMillis() - (FloodConfig.FLOOD_CONTROLLER_INTERVAL * 60 * 1000)))
				dates.remove(i);
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
		return (dates.size() >= FloodConfig.FLOOD_CONTROLLER_MAX_CONNECTION ? true : false);
	}
	
	public int size()
	{
		return dates.size();
	}
}