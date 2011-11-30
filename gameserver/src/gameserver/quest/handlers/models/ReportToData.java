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
import gameserver.quest.handlers.template.ReportTo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReportToData")
public class ReportToData extends QuestScriptData
{
	@XmlAttribute(name = "start_npc_id", required = true)
	protected int startNpcId;
	@XmlAttribute(name = "end_npc_id", required = true)
	protected int endNpc;
	@XmlAttribute(name = "add_end_npc_id")
	protected int addEndNpc;
	@XmlAttribute(name = "item_id")
	protected int itemId;
	@XmlAttribute(name = "readable_item_id")
	protected int readableItemId;
	@Override
	public void register(QuestEngine questEngine)
	{
		ReportTo template = new ReportTo(id, startNpcId, endNpc, addEndNpc, itemId, readableItemId);
		questEngine.TEMP_HANDLERS.put(template.getQuestId(), template);
	}
}