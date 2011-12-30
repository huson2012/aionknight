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

import gameserver.model.gameobjects.Item;
import gameserver.model.items.GodStone;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.List;

public class SM_UPDATE_PLAYER_APPEARANCE extends AionServerPacket
{
	public int playerId;
	public int size;
	public List<Item> items;

	public SM_UPDATE_PLAYER_APPEARANCE(int playerId, List<Item> items)
	{
		this.playerId = playerId;
		this.items = items;
		this.size = items.size();
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, playerId);
		
		short mask = 0;
		for(Item item : items)
		{
			mask |= item.getEquipmentSlot();
		}
		
		writeH(buf, mask);

		for(Item item : items)
		{		
			writeD(buf, item.getItemSkinTemplate().getTemplateId());
			GodStone godStone = item.getGodStone();
			writeD(buf, godStone != null ? godStone.getItemId() : 0); 
			writeD(buf, item.getItemColor());
			writeH(buf, 0x00);// unk (0x00)
		}
	}
}