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

import gameserver.model.items.WrapperItem;
import gameserver.model.templates.item.ItemRace;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "wrapped_items")
@XmlAccessorType(XmlAccessType.FIELD)
public class WrappedItemData
{
	
	@XmlElement(required = true, name = "wrapper_item")
	protected List<WrapperItem> list;

	private TIntObjectHashMap<WrapperItem> wrappedItemData;

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		wrappedItemData = new TIntObjectHashMap<WrapperItem>();
		for(WrapperItem it : list)
		{
			wrappedItemData.put(it.getItemId(), it);
		}
		list = null;
	}

	public int size()
	{
		return wrappedItemData.size();
	}
	
	public WrapperItem getItemWrapper(int itemId)
	{
		return wrappedItemData.get(itemId);
	}
	
	public TIntIntHashMap rollItems(int wrapperItemId, int playerLevel, ItemRace race)
	{
		TIntIntHashMap itemCountMap = new TIntIntHashMap();
		
		final WrapperItem wrapperItem = wrappedItemData.get(wrapperItemId);
		if (wrapperItem == null)
			return itemCountMap;
		
		return wrapperItem.rollItems(playerLevel, race);
	}	
}