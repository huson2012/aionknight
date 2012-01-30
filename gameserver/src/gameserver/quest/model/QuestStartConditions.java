/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
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
		if (this.conditions.isEmpty())
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
		if (this.conditions.isEmpty())
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