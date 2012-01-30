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

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.model.QuestCookie;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PcInventoryCondition")
public class PcInventoryCondition extends QuestCondition
{

	@XmlAttribute(name = "item_id", required = true)
	protected int	itemId;
	@XmlAttribute(required = true)
	protected long	count;

	/**
	 * Gets the value of the itemId property.
	 */
	public int getItemId()
	{
		return itemId;
	}

	/**
	 * Gets the value of the count property.
	 */
	public long getCount()
	{
		return count;
	}

	/** (non-Javadoc)
	 * @see gameserver.quest.handlers.template.xmlQuest.condition.QuestCondition#doCheck(gameserver.quest.model.QuestEnv)
	 */
    @Override
    public boolean doCheck(QuestCookie env)
    {
        Player player = env.getPlayer();
            long itemCount = player.getInventory().getItemCountByItemId(itemId);
            switch (getOp())
            {
                case EQUAL:
                        return itemCount == count;
                case GREATER:
                        return itemCount > count;
                case GREATER_EQUAL:
                        return itemCount >= count;
                case LESSER:
                        return itemCount < count;
                case LESSER_EQUAL:
                        return itemCount <= count;
                case NOT_EQUAL:
                        return itemCount != count;
                default:
            return false;
        }
    }
}
