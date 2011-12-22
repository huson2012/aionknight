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