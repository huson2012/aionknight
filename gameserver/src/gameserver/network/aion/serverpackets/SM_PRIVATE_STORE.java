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
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PrivateStore;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.trade.TradePSItem;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.InventoryPacket;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;

public class SM_PRIVATE_STORE extends InventoryPacket
{
	/** Private store Information **/
	private PrivateStore store;

	public SM_PRIVATE_STORE(PrivateStore store)
	{
		this.store = store;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(store != null)
		{
			Player storePlayer = store.getOwner();
			LinkedHashMap<Integer, TradePSItem> soldItems = store.getSoldItems();
			
			writeD(buf, storePlayer.getObjectId());
			writeH(buf, soldItems.size());
			for(Integer itemObjId : soldItems.keySet())
			{
				Item item = storePlayer.getInventory().getItemByObjId(itemObjId);
				TradePSItem tradeItem = store.getTradeItemById(itemObjId);
				long price = tradeItem.getPrice();
				writeD(buf, itemObjId);
				writeD(buf, item.getItemTemplate().getTemplateId());
				writeH(buf, (int) tradeItem.getCount());
				writeD(buf, (int) price);

				ItemTemplate itemTemplate = item.getItemTemplate();

				if (itemTemplate.isWeapon())
				{
					writeWeaponInfo(buf, item);
				}
				else if (itemTemplate.isArmor())
				{
					writeArmorInfo(buf, item);
				}
				else
				{
					writeGeneralItemInfo(buf, item);
				}
			}
		}
	}
}