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
package gameserver.quest.handlers.models.xmlQuest.conditions;

import gameserver.quest.model.ConditionUnionType;
import gameserver.quest.model.QuestCookie;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestConditions", propOrder = { "conditions" })
public class QuestConditions
{

	@XmlElements( { @XmlElement(name = "quest_status", type = QuestStatusCondition.class),
		@XmlElement(name = "npc_id", type = NpcIdCondition.class),
		@XmlElement(name = "pc_inventory", type = PcInventoryCondition.class),
		@XmlElement(name = "quest_var", type = QuestVarCondition.class),
		@XmlElement(name = "dialog_id", type = DialogIdCondition.class) })
	protected List<QuestCondition>	conditions;
	@XmlAttribute(required = true)
	protected ConditionUnionType	operate;

    public boolean checkConditionOfSet(QuestCookie env)
    {
            boolean inCondition = (operate == ConditionUnionType.AND);
            for (QuestCondition cond : conditions)
            {
                    boolean bCond = cond.doCheck(env);
                    switch (operate)
                    {
                            case AND:
                                    if (!bCond) return false;
                                    inCondition = inCondition && bCond;
                                    break;
                            case OR:
                                    if (bCond) return true;
                                    inCondition = inCondition || bCond;
                                    break;
                    }
            }
            return inCondition;
    }

}
