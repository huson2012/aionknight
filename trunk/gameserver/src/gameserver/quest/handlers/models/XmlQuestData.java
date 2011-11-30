/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.quest.handlers.models;

import gameserver.quest.QuestEngine;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.handlers.models.xmlQuest.events.OnKillEvent;
import gameserver.quest.handlers.models.xmlQuest.events.OnTalkEvent;
import gameserver.quest.handlers.template.XmlQuest;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XmlQuest", propOrder = {"onTalkEvent", "onKillEvent" })
public class XmlQuestData extends QuestScriptData
{
	@XmlElement(name = "on_talk_event")
	protected List<OnTalkEvent>	onTalkEvent;
	@XmlElement(name = "on_kill_event")
	protected List<OnKillEvent>	onKillEvent;
	@XmlAttribute(name = "start_npc_id")
	protected Integer startNpcId;
	@XmlAttribute(name = "end_npc_id")
	protected Integer endNpcId;
	public List<OnTalkEvent> getOnTalkEvent()
	{
		if(onTalkEvent == null)
		{
			onTalkEvent = new ArrayList<OnTalkEvent>();
		}
		return this.onTalkEvent;
	}

	public List<OnKillEvent> getOnKillEvent()
	{
		if(onKillEvent == null)
		{
			onKillEvent = new ArrayList<OnKillEvent>();
		}
		return this.onKillEvent;
	}

	public Integer getStartNpcId()
	{
		return startNpcId;
	}

	public Integer getEndNpcId()
	{
		return endNpcId;
	}

	@Override
	public void register(QuestEngine questEngine)
	{
		QuestHandler h = new XmlQuest(this);
		QuestEngine.getInstance().TEMP_HANDLERS.put(h.getQuestId(), h);
	}
}