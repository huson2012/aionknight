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

import gameserver.model.templates.chest.ChestTemplate;
import gnu.trove.THashMap;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "chest_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChestData
{
	@XmlElement(name = "chest")
	private List<ChestTemplate> chests;
	private TIntObjectHashMap<ChestTemplate> chestData	= new TIntObjectHashMap<ChestTemplate>();
	private TIntObjectHashMap<ArrayList<ChestTemplate>> instancesMap = new TIntObjectHashMap<ArrayList<ChestTemplate>>();
	private THashMap<String, ChestTemplate> namedChests = new THashMap<String, ChestTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		chestData.clear();
		instancesMap.clear();
		namedChests.clear();
		
		for(ChestTemplate chest : chests)
		{
			chestData.put(chest.getNpcId(), chest);
			if(chest.getName() != null && !chest.getName().isEmpty())
				namedChests.put(chest.getName(), chest);
		}
	}
	
	public int size()
	{
		return chestData.size();
	}

	public ChestTemplate getChestTemplate(int npcId)
	{
		return chestData.get(npcId);
	}

	public List<ChestTemplate> getChests()
	{
		return chests;
	}

	public void setChests(List<ChestTemplate> chests)
	{
		this.chests = chests;
		afterUnmarshal(null, null);
	}
}