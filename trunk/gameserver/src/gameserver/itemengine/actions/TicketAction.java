/**
 * This file is part of Aion-Knight Dev. Team [http://www.aion-knight.ru]
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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import gameserver.services.CubeExpandService;
import gameserver.services.WarehouseService;
import gameserver.utils.PacketSendUtility;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TicketAction")
public class TicketAction extends AbstractItemAction
{
	@XmlAttribute
	protected String function;
	@XmlAttribute
	protected int param;

	/**
	 * Gets the value of the function property.
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * Gets the value of the param property.
	 */
	public int getParam() {
		return param;
	}

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		if(function.equals("addCube"))
		{
			return (player.getCubeSize() < 9);
		}

		if(function.equals("addWharehouse"))
		{
			return (player.getWarehouseSize() < 9);
		}

		return false;
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem)
	{
		Item item = player.getInventory().getItemByObjId(parentItem.getObjectId());

		if(item != null)
		{
			if(player.getInventory().removeFromBag(item, true))
			{
				if(function.equals("addCube"))
				{
					CubeExpandService.expand(player);
				}

				if(function.equals("addWharehouse"))
				{
					WarehouseService.expand(player);
				}
				PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(parentItem.getObjectId()));
			}
		}
	}
}