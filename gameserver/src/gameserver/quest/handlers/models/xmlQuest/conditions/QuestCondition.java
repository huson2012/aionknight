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

import gameserver.quest.model.ConditionOperation;
import gameserver.quest.model.QuestCookie;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestCondition")
@XmlSeeAlso( { NpcIdCondition.class, DialogIdCondition.class, PcInventoryCondition.class, QuestVarCondition.class,
	QuestStatusCondition.class })
public abstract class QuestCondition
{

	@XmlAttribute(required = true)
	protected ConditionOperation	op;

	/**
	 * Gets the value of the op property.
	 * 
	 * @return possible object is {@link ConditionOperation }
	 * 
	 */
	public ConditionOperation getOp()
	{
		return op;
	}

	public abstract boolean doCheck(QuestCookie env);

}
