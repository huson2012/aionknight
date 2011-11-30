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


/**
 * @author Mr. Poke
 * 
 */
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
