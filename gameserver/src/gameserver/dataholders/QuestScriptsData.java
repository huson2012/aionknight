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

import gameserver.quest.handlers.models.*;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "quest_scripts")
public class QuestScriptsData
{
	@XmlElements( { @XmlElement(name = "report_to", type = ReportToData.class),
		@XmlElement(name = "monster_hunt", type = MonsterHuntData.class),
		@XmlElement(name = "xml_quest", type = XmlQuestData.class),
		@XmlElement(name = "item_collecting", type = ItemCollectingData.class),
		@XmlElement(name = "work_order", type = WorkOrdersData.class)
		})
	protected List<QuestScriptData>	data;
	public List<QuestScriptData> getData()
	{
		return data;
	}

	public void setData(List<QuestScriptData> data)
	{
		this.data = data;
	}
}