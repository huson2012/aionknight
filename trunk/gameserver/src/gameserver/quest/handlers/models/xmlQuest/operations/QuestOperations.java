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

package gameserver.quest.handlers.models.xmlQuest.operations;

import gameserver.quest.model.QuestCookie;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestOperations", propOrder = { "operations" })
public class QuestOperations
{

    @XmlElements({
        @XmlElement(name = "take_item", type = TakeItemOperation.class),
        @XmlElement(name = "npc_dialog", type = NpcDialogOperation.class),
        @XmlElement(name = "set_quest_status", type = SetQuestStatusOperation.class),
        @XmlElement(name = "give_item", type = GiveItemOperation.class),
        @XmlElement(name = "start_quest", type = StartQuestOperation.class),
        @XmlElement(name = "npc_use", type = ActionItemUseOperation.class),
        @XmlElement(name = "set_quest_var", type = SetQuestVarOperation.class),
        @XmlElement(name = "collect_items", type = CollectItemQuestOperation.class)
    })
	protected List<QuestOperation>	operations;
	@XmlAttribute
	protected Boolean override;

	/**
	 * Gets the value of the override property.
	 * @return possible object is {@link Boolean }
	 */
	public boolean isOverride()
	{
		if(override == null)
		{
			return true;
		}
		else
		{
			return override;
		}
	}

	public boolean operate(QuestCookie env)
	{
		if(operations != null)
		{
			for(QuestOperation oper : operations)
			{
				oper.doOperate(env);
			}
		}
		return isOverride();
	}
}