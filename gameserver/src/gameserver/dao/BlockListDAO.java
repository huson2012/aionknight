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
import gameserver.model.gameobjects.player.BlockList;
import gameserver.model.gameobjects.player.Player;

/**
 * Responsible for saving and loading data on players' block lists
 */
public abstract class BlockListDAO implements DAO
{
	/**
	 * Loads the blocklist for the player given
	 * @param player
	 * @return BlockList
	 */
	public abstract BlockList load(Player player);
	
	/**
	 * Adds the given object id to the list of blocked players for the given player
	 * @param playerObjId ID of player to edit the blocklist of
	 * @param objIdToBlock ID of player to add to the blocklist
	 * @return Success
	 */
	public abstract boolean addBlockedUser(int playerObjId, int objIdToBlock, String reason);

	/**
	 * Deletes the given object id from the list of blocked players for the given player
	 * @param playerObjId ID of player to edit the blocklist of
	 * @param objIdToDelete ID of player to remove from the blocklist
	 * @return Success
	 */
	public abstract boolean delBlockedUser(int playerObjId, int objIdToDelete);
	
	/**
	 * Sets the reason for blocking a player
	 * @param playerObjId Object ID of the player whos list is being edited
	 * @param blockedObjId Object ID of the player whos reason is being edited
	 * @param reason The reason to be set
	 * @return true or false
	 */
	public abstract boolean setReason(int playerObjId, int blockedObjId, String reason);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getClassName()
	{
		return BlockListDAO.class.getName();
	}
}