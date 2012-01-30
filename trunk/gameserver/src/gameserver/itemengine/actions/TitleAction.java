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