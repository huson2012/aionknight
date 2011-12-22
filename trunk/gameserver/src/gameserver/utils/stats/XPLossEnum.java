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

public enum XPLossEnum
{
	LEVEL_8(8, 2.997997521),
	LEVEL_9(9, 2.998974359),
	LEVEL_10(10, 2.999872482),
	LEVEL_16(16, 2.999258215),
	LEVEL_20(20, 2.999859021),
	LEVEL_21(21, 2.999782255),
	LEVEL_22(22, 2.999856511),
	LEVEL_24(24, 2.999925915),
	LEVEL_33(33, 2.999791422),
	LEVEL_41(41, 1.369142798),
	LEVEL_44(44, 1.081953696),
	LEVEL_50(50, 1.041314239),
	LEVEL_55(55, 1.018657119);
	
	private int level;
	private double param;
	
	private XPLossEnum(int level, double param)
	{
		this.level = level;
		this.param = param;
	}

	/**
	 * @return the level
	 */
	public int getLevel()
	{
		return level;
	}

	/**
	 * @return the param
	 */
	public double getParam()
	{
		return param;
	}
	
	/**
	 * 
	 * @param level
	 * @param expNeed
	 * @return long
	 */
	public static long getExpLoss(int level, long expNeed)
	{
		if(level < 8)
			return 0;
		
		for(XPLossEnum xpLossEnum : values())
		{
			if(level <= xpLossEnum.getLevel())
				return Math.round(expNeed / 100 * xpLossEnum.getParam());
		}
		return 0;
	}	
	
}