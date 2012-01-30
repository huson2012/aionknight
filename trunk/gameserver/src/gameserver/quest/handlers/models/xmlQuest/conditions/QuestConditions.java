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

import gameserver.quest.model.ConditionUnionType;
import gameserver.quest.model.QuestCookie;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestConditions", propOrder = { "conditions" })
public class QuestConditions
{

	@XmlElements( { @XmlElement(name = "quest_status", type = QuestStatusCondition.class),
		@XmlElement(name = "npc_id", type = NpcIdCondition.class),
		@XmlElement(name = "pc_inventory", type = PcInventoryCondition.class),
		@XmlElement(name = "quest_var", type = QuestVarCondition.class),
		@XmlElement(name = "dialog_id", type = DialogIdCondition.class) })
	protected List<QuestCondition>	conditions;
	@XmlAttribute(required = true)
	protected ConditionUnionType	operate;

    public boolean checkConditionOfSet(QuestCookie env)
    {
            boolean inCondition = (operate == ConditionUnionType.AND);
            for (QuestCondition cond : conditions)
            {
                    boolean bCond = cond.doCheck(env);
                    switch (operate)
                    {
                            case AND:
                                    if (!bCond) return false;
                                    inCondition = inCondition && bCond;
                                    break;
                            case OR:
                                    if (bCond) return true;
                                    inCondition = inCondition || bCond;
                                    break;
                    }
            }
            return inCondition;
    }

}
