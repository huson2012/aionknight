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

package gameserver.quest.handlers.models.xmlQuest.operations;

import gameserver.quest.model.QuestCookie;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestOperations", propOrder = { "operations" })
public class QuestOperations
{

    @XmlElements({
        @XmlElement(name = "take_item", type = TakeItemOperation.class),
        @XmlElement(name = "npc_dialog", type = NpcDialogOperation.class),
        @XmlElement(name = "set_quest_status", type = SetQuestStatusOperation.class),
        @XmlElement(name = "give_item", type = GiveItemOperation.class),
        @XmlElement(name = "start_quest", type = StartQuestOperation.class),
        @XmlElement(name = "npc_use", type = ActionItemUseOperation.class),
        @XmlElement(name = "set_quest_var", type = SetQuestVarOperation.class),
        @XmlElement(name = "collect_items", type = CollectItemQuestOperation.class)
    })
	protected List<QuestOperation>	operations;
	@XmlAttribute
	protected Boolean override;

	/**
	 * Gets the value of the override property.
	 * @return possible object is {@link Boolean }
	 */
	public boolean isOverride()
	{
		if(override == null)
		{
			return true;
		}
		else
		{
			return override;
		}
	}

	public boolean operate(QuestCookie env)
	{
		if(operations != null)
		{
			for(QuestOperation oper : operations)
			{
				oper.doOperate(env);
			}
		}
		return isOverride();
	}
}
