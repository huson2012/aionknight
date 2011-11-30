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

import gameserver.model.templates.quest.QuestItems;
import gameserver.quest.QuestEngine;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.handlers.template.WorkOrders;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkOrdersData", propOrder = { "giveComponent" })
public class WorkOrdersData extends QuestScriptData
{
	@XmlElement(name = "give_component", required = true)
	protected List<QuestItems>	giveComponent;
	@XmlAttribute(name = "start_npc_id", required = true)
	protected int startNpcId;
	@XmlAttribute(name = "recipe_id", required = true)
	protected int recipeId;
	public List<QuestItems> getGiveComponent()
	{
		if(giveComponent == null)
		{
			giveComponent = new ArrayList<QuestItems>();
		}
		return this.giveComponent;
	}

	public int getStartNpcId()
	{
		return startNpcId;
	}

	public int getRecipeId()
	{
		return recipeId;
	}

	@Override
	public void register(QuestEngine questEngine)
	{
		QuestHandler wo = new WorkOrders(this);
		questEngine.TEMP_HANDLERS.put(wo.getQuestId(), wo);
	}
}