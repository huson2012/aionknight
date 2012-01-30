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
