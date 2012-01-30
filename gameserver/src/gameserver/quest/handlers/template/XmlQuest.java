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

package gameserver.quest.handlers.template;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.handlers.models.MonsterInfo;
import gameserver.quest.handlers.models.XmlQuestData;
import gameserver.quest.handlers.models.xmlQuest.events.OnKillEvent;
import gameserver.quest.handlers.models.xmlQuest.events.OnTalkEvent;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;

public class XmlQuest extends QuestHandler
{

	private final XmlQuestData xmlQuestData;

	public XmlQuest(XmlQuestData xmlQuestData)
	{
		super(xmlQuestData.getId());
		this.xmlQuestData = xmlQuestData;
	}

	@Override
	public void register()
	{
		if (xmlQuestData.getStartNpcId() != null)
		{
			qe.setNpcQuestData(xmlQuestData.getStartNpcId()).addOnQuestStart(getQuestId());
			qe.setNpcQuestData(xmlQuestData.getStartNpcId()).addOnTalkEvent(getQuestId());
		}
		if (xmlQuestData.getEndNpcId() != null)
			qe.setNpcQuestData(xmlQuestData.getEndNpcId()).addOnTalkEvent(getQuestId());
		
		for (OnTalkEvent talkEvent : xmlQuestData.getOnTalkEvent())
			for (int npcId : talkEvent.getIds())
				qe.setNpcQuestData(npcId).addOnTalkEvent(getQuestId());

		for (OnKillEvent killEvent : xmlQuestData.getOnKillEvent())
			for (MonsterInfo monsterInfo : killEvent.getMonsterInfos())
				qe.setNpcQuestData(monsterInfo.getNpcId()).addOnKillEvent(getQuestId());
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		env.setQuestId(getQuestId());
		for (OnTalkEvent talkEvent : xmlQuestData.getOnTalkEvent())
		{
			if (talkEvent.operate(env))
				return true;
		}
		
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(getQuestId());
		
		if(defaultQuestNoneDialog(env, xmlQuestData.getStartNpcId()))
			return true;
		if(qs == null)
			return false;
		return defaultQuestRewardDialog(env, xmlQuestData.getEndNpcId(), 0);
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		env.setQuestId(getQuestId());
		for (OnKillEvent killEvent : xmlQuestData.getOnKillEvent())
		{
			if(killEvent.operate(env))
				return true;
		}
		return false;
	}
}
