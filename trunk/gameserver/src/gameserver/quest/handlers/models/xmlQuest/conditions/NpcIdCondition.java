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
