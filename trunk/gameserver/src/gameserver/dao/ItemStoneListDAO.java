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
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.items.FusionStone;
import gameserver.model.items.GodStone;
import gameserver.model.items.ManaStone;
import java.util.List;
import java.util.Set;

public abstract class ItemStoneListDAO implements DAO
{
	/**
	 * Loads stones of item
	 * @param item
	 */
	public abstract void load(List<Item> items);
	
	/**
	 * @param itemStones
	 */
	public abstract void storeManaStone(Set<ManaStone> itemStones);
	
	/**
	 * @param fusionStones
	 */
	public abstract void storeFusionStone(Set<FusionStone> fusionStones);

	/**
	 * Saves stones of player
	 * 
	 * @param itemStoneList
	 */
	public abstract void save(Player player);
	
	/**
	 * @param godStone
	 */
	public abstract void store(GodStone godStone);
	
	@Override
	public String getClassName()
	{
		return ItemStoneListDAO.class.getName();
	}
}