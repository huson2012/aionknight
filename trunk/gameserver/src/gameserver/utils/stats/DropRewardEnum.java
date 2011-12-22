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

package gameserver.utils.stats;

import java.util.NoSuchElementException;

public enum DropRewardEnum
{
	MINUS_11(-11, 0),
	MINUS_10(-10, 1),
	MINUS_9(-9, 10),
	MINUS_8(-8, 20),
	MINUS_7(-7, 30),
	MINUS_6(-6, 40),
	MINUS_5(-5, 50),
	MINUS_4(-4, 60),
	MINUS_3(-3, 90),
	MINUS_2(-2, 100),
	MINUS_1(-1, 100),
	ZERO(0, 100);

	private int dropRewardPercent;
	private int levelDifference;
	
	private DropRewardEnum(int levelDifference, int dropRewardPercent)
	{
		this.levelDifference = levelDifference;
		this.dropRewardPercent = dropRewardPercent;
	}
	
	public int rewardPercent()
	{
		return dropRewardPercent;
	}
	
	/**
	 * 
	 * @param levelDifference between two objects
	 * @return Drop reward percentage
	 */
	public static int dropRewardFrom(int levelDifference)
	{
		if(levelDifference < MINUS_11.levelDifference)
		{
			return MINUS_11.dropRewardPercent;
		}
		if(levelDifference > ZERO.levelDifference)
		{
			return ZERO.dropRewardPercent;
		}
	
		for(DropRewardEnum dropReward : values())
		{
			if(dropReward.levelDifference == levelDifference)
			{
				return dropReward.dropRewardPercent;
			}
		}
		throw new NoSuchElementException("Drop reward for such level difference was not found");
	}
}