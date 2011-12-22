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

import gameserver.model.templates.npcshouts.Shout;
import gameserver.model.templates.npcshouts.ShoutNpc;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "npc_shouts")
@XmlAccessorType(XmlAccessType.FIELD)
public class NpcShoutsData
{
	@XmlElement(name = "shout_npc")
	protected List<ShoutNpc> shouts;
	
	private TIntObjectHashMap<List<Shout>> npcShouts = new TIntObjectHashMap<List<Shout>>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		npcShouts.clear();
		for(ShoutNpc shout : shouts)
		{
			if(!npcShouts.containsKey(shout.getNpcId()))
				npcShouts.put(shout.getNpcId(), new ArrayList<Shout>());
			
			npcShouts.get(shout.getNpcId()).addAll(shout.getShouts());
		}
		shouts.clear();
	}
	
	public List<Shout> getShoutsForNpc(int npcId)
	{
		return npcShouts.get(npcId);
	}
	
	public int size()
	{
		return npcShouts.size();
	}	
}