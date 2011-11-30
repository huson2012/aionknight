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
import gameserver.quest.handlers.template.MonsterHunt;
import javolution.util.FastMap;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MonsterHuntData", propOrder = { "monsterInfos" })
public class MonsterHuntData extends QuestScriptData
{
	@XmlElement(name = "monster_infos", required = true)
	protected List<MonsterInfo>	monsterInfos;
	@XmlAttribute(name = "start_npc_id")
	protected int startNpcId;
	@XmlAttribute(name = "end_npc_id")
	protected int endNpcId;
	@Override
	public void register(QuestEngine questEngine)
	{
		FastMap<Integer, MonsterInfo> monsterInfo = new FastMap<Integer, MonsterInfo>();
		for(MonsterInfo mi : monsterInfos) monsterInfo.put(mi.getNpcId(), mi);
		MonsterHunt template = new MonsterHunt(id, startNpcId, endNpcId, monsterInfo);
		questEngine.TEMP_HANDLERS.put(template.getQuestId(), template);
	}
}