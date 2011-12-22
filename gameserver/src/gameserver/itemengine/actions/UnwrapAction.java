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

package gameserver.itemengine.actions;

import gameserver.dataholders.DataManager;
import gameserver.model.Race;
import gameserver.model.TaskId;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.templates.item.ItemRace;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gnu.trove.TIntIntHashMap;
import java.util.List;

public class UnwrapAction extends AbstractItemAction
{
	@Override
	public void act(final Player player, final Item parentItem, Item targetItem)
	{
		final Storage storage = player.getInventory();
		ItemRace race = player.getCommonData().getRace() == Race.ASMODIANS ?
			ItemRace.ASMODIANS : ItemRace.ELYOS;
		final TIntIntHashMap itemCountMap = 
			DataManager.WRAPPED_ITEM_DATA.rollItems(parentItem.getItemId(), player.getLevel(), race);
		
		int slotsNeeded = 0;
		for (int itemId : itemCountMap.keys())
		{
			int countToAdd = itemCountMap.get(itemId);
			List<Item> items = storage.getItemsByItemId(itemId);
			ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(itemId);
			int stackCount = template.getMaxStackCount();
			
			if (items.size() != 0)
			{
				for (Item stackItem : items)
				{
					int freeSpace = template.getMaxStackCount() - (int)stackItem.getItemCount();
					countToAdd -= freeSpace;
				}
			}
			
			if (countToAdd <= stackCount)
				slotsNeeded++;
			else
			{
				int count = countToAdd / stackCount;
				if (countToAdd % stackCount != 0)
					count++;
				slotsNeeded += count;
			}

		}
		
		if (storage.getNumberOfFreeSlots() < slotsNeeded - 1)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_FULL_INVENTORY);
			return;
		}
		
		PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
			parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 5000, 0, 0));
		player.getController().cancelTask(TaskId.ITEM_USE);
		player.getController().addNewTask(TaskId.ITEM_USE,
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem
					.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, 1, 0));

				storage.removeFromBagByObjectId(parentItem.getObjectId(), 1);
				for (int itemId : itemCountMap.keys())
				{
					ItemService.addItem(player, itemId, itemCountMap.get(itemId));
				}
			}
		}, 5000));
	}
	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		return true;
	}
}