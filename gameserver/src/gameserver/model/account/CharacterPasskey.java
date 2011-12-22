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

public class CharacterPasskey
{
	private int objectId;
	private int wrongCount = 0;
	private boolean isPass = false;
	private ConnectType	connectType;
	public int getObjectId()
	{
		return objectId;
	}

	public void setObjectId(int objectId)
	{
		this.objectId = objectId;
	}

	public int getWrongCount()
	{
		return wrongCount;
	}

	public void setWrongCount(int count)
	{
		this.wrongCount = count;
	}

	public boolean isPass()
	{
		return isPass;
	}

	public void setIsPass(boolean isPass)
	{
		this.isPass = isPass;
	}

	public ConnectType getConnectType()
	{
		return connectType;
	}

	public void setConnectType(ConnectType connectType)
	{
		this.connectType = connectType;
	}

	public enum ConnectType
	{
		ENTER,
		DELETE
	}
}