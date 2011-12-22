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

package gameserver.dataholders;

import gameserver.model.items.ItemBonus;
import gameserver.model.templates.bonus.InventoryBonusType;
import gameserver.model.templates.item.ItemCategory;
import gameserver.model.templates.item.ItemRace;
import gameserver.model.templates.item.ItemTemplate;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement(name = "item_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemData
{
	@XmlElement(name="item_template")
	private List<ItemTemplate> its;
	private TIntObjectHashMap<ItemTemplate> items;
	private Hashtable<InventoryBonusType, TreeMap<Integer, List<Integer>>> itemsByBonus;
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		items = new TIntObjectHashMap<ItemTemplate>();
		itemsByBonus = new Hashtable<InventoryBonusType, 
		TreeMap<Integer, List<Integer>>>();
		for(ItemTemplate it: its)
		{
			items.put(it.getTemplateId(), it);
			ItemBonus bonusInfo = it.getBonusInfo();
			if(bonusInfo != null) {
				InventoryBonusType bonusType = bonusInfo.getBonusType();

				TreeMap<Integer, List<Integer>> map = itemsByBonus.get(bonusType);
				if(map == null) {
					map = new TreeMap<Integer, List<Integer>>();
					itemsByBonus.put(bonusType, map);
				}
				String[] bonusLevels = bonusInfo.getBonusLevels().split(",");
				for(int i = 0; i < bonusLevels.length; i++)
				{
					int bonusLevel = Integer.parseInt(bonusLevels[i]);
					List<Integer> list = map.get(bonusLevel);
					if(list == null)
					{
						list = new ArrayList<Integer>();
						map.put(bonusLevel, list);
					}
					list.add(it.getTemplateId());
				}
			}
			else if (it.getItemCategory() == ItemCategory.HOLYSTONE ||
					 it.getItemCategory() == ItemCategory.ENCHANTSTONE) 
			{
				InventoryBonusType bonusType = it.getItemCategory() == ItemCategory.HOLYSTONE ?
					InventoryBonusType.GODSTONE : InventoryBonusType.ENCHANT;
				mapTemplateBonus(it, bonusType);
			} else if (it.getIsWorldDrop()) {
				if (it.getOriginRace() == ItemRace.ALL)
					mapTemplateBonus(it, InventoryBonusType.WORLD_DROP_B);
				else if (it.getOriginRace() == ItemRace.ELYOS)
					mapTemplateBonus(it, InventoryBonusType.WORLD_DROP_E);
				else
					mapTemplateBonus(it, InventoryBonusType.WORLD_DROP_A);
			}
		}
		its = null;
	}
	
	private void mapTemplateBonus(ItemTemplate template, InventoryBonusType mapType) 
	{
		TreeMap<Integer, List<Integer>> map = itemsByBonus.get(mapType);
		if(map == null) {
			map = new TreeMap<Integer, List<Integer>>();
			itemsByBonus.put(mapType, map);
		}
		List<Integer> list = map.get(template.getLevel());
		if(list == null)
		{
			list = new ArrayList<Integer>();
			map.put(template.getLevel(), list);
		}
		list.add(template.getTemplateId());		
	}
	
	public ItemTemplate getItemTemplate(int itemId)
	{
		return items.get(itemId);
	}

	public int size()
	{
		return items.size();
	}

	public List<Integer> getBonusItems(InventoryBonusType type, int startLevel, int endLevel)
	{
		List<Integer> list = new ArrayList<Integer>();
		synchronized(itemsByBonus) {
			TreeMap<Integer, List<Integer>> map = itemsByBonus.get(type);
			if(map == null)
				return list;
			SortedMap<Integer, List<Integer>> submap = map.subMap(startLevel, endLevel);
			if(submap.size() == 0)
				return list;
			for(List<Integer> itemsList : submap.values())
				list.addAll(itemsList);
		}
		return list;
	}
}