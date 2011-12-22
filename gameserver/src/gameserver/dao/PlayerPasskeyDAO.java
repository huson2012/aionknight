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
package gameserver.dao;

import commons.database.dao.DAO;

public abstract class PlayerPasskeyDAO implements DAO
{
	/**
	* @param accountId
	* @param passkey
	*/
	public abstract void insertPlayerPasskey(int accountId, String passkey);

	/**
	* @param accountId
	* @param oldPasskey
	* @param newPasskey
	* @return
	*/
	public abstract boolean updatePlayerPasskey(int accountId, String oldPasskey, String newPasskey);

	/**
	* @param accountId
	* @param newPasskey
	* @return
	*/
	public abstract boolean updateForcePlayerPasskey(int accountId, String newPasskey);

	/**
	* @param accountId
	* @param passkey
	* @return
	*/
	public abstract boolean checkPlayerPasskey(int accountId, String passkey);

	/**
	* @param accountId
	* @return
	*/
	public abstract boolean existCheckPlayerPasskey(int accountId);

	/**
	* (non-Javadoc)
	* @see commons.database.dao.DAO#getClassName()
	*/
	@Override
	public final String getClassName()
	{
		return PlayerPasskeyDAO.class.getName();
	}
}