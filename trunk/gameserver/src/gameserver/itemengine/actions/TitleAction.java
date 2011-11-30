/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.itemengine.actions;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TitleAction")
public class TitleAction extends AbstractItemAction
{
	@XmlAttribute
	protected int titleid;
	@XmlAttribute
	protected int expire;

	/**
	 * Gets the value of the id property.
	 */
	public int getTitleId() {
		return titleid;
	}

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		return player.getTitleList().canAddTitle(titleid);
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem)
	{
		Item item = player.getInventory().getItemByObjId(parentItem.getObjectId());

		if(item != null)
		{
			if(player.getInventory().removeFromBag(item, true))
			{
				if(player.getTitleList().addTitle(titleid, System.currentTimeMillis(), (expire  * 60L)))
				{
					if(expire > 0)
					    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300774));
					else
					    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300773));
				}
				PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(parentItem.getObjectId()));
			}
		}
	}
}