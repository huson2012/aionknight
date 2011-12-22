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