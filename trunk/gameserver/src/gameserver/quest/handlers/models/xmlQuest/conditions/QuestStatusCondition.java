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

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestStatusCondition")
public class QuestStatusCondition extends QuestCondition
{

	@XmlAttribute(required = true)
	protected QuestStatus	value;
	@XmlAttribute(name = "quest_id")
	protected Integer		questId;


	/** (non-Javadoc)
	 * @see gameserver.quest.handlers.template.xmlQuest.condition.QuestCondition#doCheck(gameserver.quest.model.QuestEnv)
	 */
    @Override
    public boolean doCheck(QuestCookie env)
    {
            Player player = env.getPlayer();
            int qstatus = 0;
            int id = env.getQuestId();
            if (questId != null)
                    id = questId;
            QuestState qs = player.getQuestStateList().getQuestState(id);
            if (qs != null)
                    qstatus = qs.getStatus().value();
                    
            switch (getOp())
            {
                    case EQUAL:
                            return qstatus == value.value();
                    case GREATER:
                            return qstatus > value.value();
                    case GREATER_EQUAL:
                            return qstatus >= value.value();
                    case LESSER:
                            return qstatus < value.value();
                    case LESSER_EQUAL:
                            return qstatus <= value.value();
                    case NOT_EQUAL:
                            return qstatus != value.value();
                    default:
                            return false;
            }
    }
}
