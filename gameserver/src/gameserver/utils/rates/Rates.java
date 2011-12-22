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

package gameserver.utils.rates;

public abstract class Rates
{
	public abstract int getGroupXpRate();
	public abstract int getXpRate();
	public abstract float getApNpcRate();
	public abstract float getApPlayerRate();
	public abstract float getGatheringXPRate();
	public abstract float getGatheringLvlRate();
	public abstract float getCraftingXPRate();
	public abstract float getCraftingLvlRate();
	public abstract int getDropRate();
	public abstract int getChestDropRate();
	public abstract int getQuestXpRate();
	public abstract int getQuestKinahRate();
	public abstract int getKinahRate();
	public static Rates getRatesFor(byte membership)
	{
		switch(membership)
		{
			case 0:
				return new RegularRates();
			case 1:
				return new PremiumRates();
			default:
				return new RegularRates();
		}
	}
}