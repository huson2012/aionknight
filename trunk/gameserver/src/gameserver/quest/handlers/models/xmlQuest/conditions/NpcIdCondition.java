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

package gameserver.quest.handlers.models.xmlQuest.conditions;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.quest.model.QuestCookie;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NpcIdCondition")
public class NpcIdCondition extends QuestCondition
{

	@XmlAttribute(required = true)
	protected int	values;

	/**
	 * (non-Javadoc)
	 * @see
	 * gameserver.quest.handlers.template.xmlQuest.condition.QuestCondition#doCheck(gameserver
	 * .questEngine.model.QuestEnv)
	 */
	@Override
	public boolean doCheck(QuestCookie env)
	{
		int id = 0;
		VisibleObject visibleObject = env.getVisibleObject();
		if(visibleObject != null && visibleObject instanceof Npc)
		{
			id = ((Npc) visibleObject).getNpcId();
		}
		switch(getOp())
		{
			case EQUAL:
				return id == values;
			case GREATER:
				return id > values;
			case GREATER_EQUAL:
				return id >= values;
			case LESSER:
				return id < values;
			case LESSER_EQUAL:
				return id <= values;
			case NOT_EQUAL:
				return id != values;
			default:
				return false;
		}
	}
}