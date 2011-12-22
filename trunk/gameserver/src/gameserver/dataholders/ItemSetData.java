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

import gameserver.model.templates.itemset.ItemPart;
import gameserver.model.templates.itemset.ItemSetTemplate;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "item_sets")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemSetData
{
	@XmlElement(name="itemset")
	protected List<ItemSetTemplate> itemsetList;
	private TIntObjectHashMap<ItemSetTemplate> sets;
	private TIntObjectHashMap<ItemSetTemplate> setItems;
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		sets = new TIntObjectHashMap<ItemSetTemplate>();
		setItems = new TIntObjectHashMap<ItemSetTemplate>();
		
		for(ItemSetTemplate set: itemsetList)
		{
			sets.put(set.getId(), set);

			for(ItemPart part : set.getItempart())
			{
				setItems.put(part.getItemid(), set);
			}
		}
		itemsetList = null;
	}

	public ItemSetTemplate getItemSetTemplate(int itemSetId)
	{
		return sets.get(itemSetId);
	}

	public ItemSetTemplate getItemSetTemplateByItemId(int itemId)
	{
		return setItems.get(itemId);
	}

	public int size()
	{
		return sets.size();
	}
}