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

package gameserver.quest.model;

import gameserver.model.gameobjects.player.QuestStateList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestStartConditions")
public class QuestStartConditions
{
	@XmlElement(name = "condition")
	protected List<QuestStartCondition> conditions;
	public List<QuestStartCondition> getConditions() {
		if (conditions == null) {
			conditions = new ArrayList<QuestStartCondition>();
		}
		return this.conditions;
	}

	public static class Finished implements IQuestConditionChecker
	{
		@Override
		public boolean checkFailure(QuestState questState, QuestStep questStep)
		{
			return questState == null || questState.getStatus() != QuestStatus.COMPLETE ||
				   questStep.rewardNo != 0 && questStep.rewardNo != questState.getQuestVarById(0);
		}
	}

	public static class Unfinished implements IQuestConditionChecker
	{
		@Override
		public boolean checkFailure(QuestState questState, QuestStep questStep)
		{
			return questState != null && questState.getStatus() == QuestStatus.COMPLETE;
		}
	}

	public static class Acquired implements IQuestConditionChecker
	{
		@Override
		public boolean checkFailure(QuestState questState, QuestStep questStep)
		{
			return questState == null || questState.getStatus() == QuestStatus.NONE ||
				   questState.getStatus() == QuestStatus.LOCKED;
		}
	}

	public static class NoAcquired implements IQuestConditionChecker
	{
		@Override
		public boolean checkFailure(QuestState questState, QuestStep questStep)
		{
			return questState != null && (questState.getStatus() == QuestStatus.START ||
				questState.getStatus() == QuestStatus.REWARD);
		}
	}

	public boolean Check(QuestStateList questStateList, IQuestConditionChecker delegate)
	{
		if (this.conditions.size() == 0)
			return true;
		
		boolean matchedAny = false;
		for(QuestStartCondition conditions : this.conditions)
		{
			boolean matchedAll = true;
			for (QuestStep step : conditions.getQuests())
			{
				QuestState qs = questStateList.getQuestState(step.getQuestId());
				matchedAll &= !delegate.checkFailure(qs, step);
			}
			if (matchedAll) 
			{
				matchedAny = true;
				break;
			}
		}
		return matchedAny;
	}
	public List<Integer> Verify(QuestStateList questStateList, IQuestConditionChecker delegate)
	
	{
		List<Integer> failedQuests = new ArrayList<Integer>();
		if (this.conditions.size() == 0)
			return failedQuests;
		
		for(QuestStartCondition conditions : this.conditions)
		{
			for (QuestStep step : conditions.getQuests())
			{
				QuestState qs = questStateList.getQuestState(step.getQuestId());
				boolean matched = !delegate.checkFailure(qs, step);
				if (!matched) failedQuests.add(step.getQuestId());
			}
		}
		return failedQuests;		
	}
}