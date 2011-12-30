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

package gameserver.network.aion.serverpackets;

import gameserver.model.drop.DropItem;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

public class SM_LOOT_ITEMLIST extends AionServerPacket
{
	private int	targetObjectId;
	private DropItem[] dropItems;
	private int size;

	public SM_LOOT_ITEMLIST(int targetObjectId, Set<DropItem> dropItems, Player player)
	{
		this.targetObjectId = targetObjectId;
		Set<DropItem> tmp = new HashSet<DropItem>();
		for (DropItem item : dropItems)
		{
			if(item.hasQuestPlayerObjId(player.getObjectId()))
				tmp.add(item);
		}
		this.dropItems = tmp.toArray(new DropItem[tmp.size()]);
		size = this.dropItems.length;
	}

	/**
	 * {@inheritDoc} dc
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf) 
	{
		writeD(buf, targetObjectId);
		writeC(buf, size);

		for(DropItem dropItem : dropItems)
		{
			writeC(buf, dropItem.getIndex()); // index in droplist
			writeD(buf, dropItem.getDropTemplate().getItemId());
			writeH(buf, (int) dropItem.getCount());
			writeD(buf, 0);
		}
	}
}
