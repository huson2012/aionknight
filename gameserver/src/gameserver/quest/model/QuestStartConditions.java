/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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