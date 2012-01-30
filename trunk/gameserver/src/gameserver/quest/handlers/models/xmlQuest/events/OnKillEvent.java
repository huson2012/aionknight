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

package gameserver.quest.handlers.models.xmlQuest.events;

import gameserver.model.gameobjects.Npc;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.quest.handlers.models.MonsterInfo;
import gameserver.quest.handlers.models.xmlQuest.operations.QuestOperations;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OnKillEvent", propOrder = { "monsterInfos", "complite" })
public class OnKillEvent extends QuestEvent
{

	@XmlElement(name = "monster_infos")
	protected List<MonsterInfo>	monsterInfos;
	protected QuestOperations	complite;

	public List<MonsterInfo> getMonsterInfos()
	{
		if(monsterInfos == null)
		{
			monsterInfos = new ArrayList<MonsterInfo>();
		}
		return this.monsterInfos;
	}

	public boolean operate(QuestCookie env)
	{
		if(monsterInfos == null || !(env.getVisibleObject() instanceof Npc))
			return false;

		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(env.getQuestId());
		if(qs == null)
			return false;

		Npc npc = (Npc) env.getVisibleObject();
		for(MonsterInfo monsterInfo : monsterInfos)
		{
			if(monsterInfo.getNpcId() == npc.getNpcId())
			{
				int var = qs.getQuestVarById(monsterInfo.getVarId());
				if(var >= (monsterInfo.getMinVarValue() == null ? 0 : monsterInfo.getMinVarValue()) && var < monsterInfo.getMaxKill())
				{
					qs.setQuestVarById(monsterInfo.getVarId(), var + 1);
					PacketSendUtility.sendPacket(env.getPlayer(), new SM_QUEST_ACCEPTED(2, env.getQuestId(), qs.getStatus(),
						qs.getQuestVars().getQuestVars()));
				}
			}
		}

		if(complite != null)
		{
			for(MonsterInfo monsterInfo : monsterInfos)
			{
				if(qs.getQuestVarById(monsterInfo.getVarId()) != qs.getQuestVarById(monsterInfo.getVarId()))
					return false;
			}
			complite.operate(env);
		}
		return false;
	}
}
