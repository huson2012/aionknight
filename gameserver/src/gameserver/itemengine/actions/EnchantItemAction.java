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

import gameserver.model.TaskId;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemCategory;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.EnchantService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnchantItemAction")
public class EnchantItemAction extends AbstractItemAction
{
	@XmlAttribute(name = "count")
	protected int sub_enchant_material_many;
	
	public int getEnchantCount()
	{
		return sub_enchant_material_many;
	}
	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		return canAct(player, parentItem, targetItem, 1);
	}

	public boolean canAct(Player player, Item parentItem, Item targetItem, int targetWeapon)
	{
		if(targetItem == null)
		{ // no item selected.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_ERROR);
			return false;
		}
	
		if(!targetItem.getItemTemplate().isWeapon()	&& !targetItem.getItemTemplate().isArmor(true))
		{
			Logger.getLogger(this.getClass()).info("[AUDIT] Player: "+player.getName()+" is trying to enchant/socket non-weapon or non-armor. Hacking!");
			return false;
		}
			
		if(parentItem.getItemTemplate().getItemCategory() == ItemCategory.MAGICSTONE)
		{
			int Manaslots = 0;
			int manaStones = 0;
			if(targetWeapon == 1)
			{
				Manaslots = targetItem.getSockets(false);
				manaStones = targetItem.getItemStones().size();
			}
			else
			{
				Manaslots = targetItem.getSockets(true);
				manaStones = targetItem.getFusionStones().size();
			}

			if(manaStones >= Manaslots)
			{
				Logger.getLogger(this.getClass()).info("[AUDIT] Player: "+player.getName()+" is trying to socket more manastones than manaslots. Hacking!");
				return false;
			}
		}
				


		return true;
	}

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem)
	{
		act(player, parentItem, targetItem, null, 1);
	}

	public void act(final Player player, final Item parentItem, final Item targetItem, final Item supplementItem, final int targetWeapon)
	{
		PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
		parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 5000, 0, 0));
		player.getController().cancelTask(TaskId.ITEM_USE);
		player.getController().addNewTask(TaskId.ITEM_USE,
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				if(parentItem.getItemTemplate().getItemCategory() == ItemCategory.ENCHANTSTONE)
				{
					boolean result = EnchantService.enchantItem(player, parentItem, targetItem, supplementItem);
					PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem
						.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, result ? 1 : 2, 0));
				}
				else
				{
					boolean result = EnchantService.socketManastone(player, parentItem, targetItem, supplementItem, targetWeapon);
					PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem
						.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, result ? 1 : 2, 0));
				}
			}			
		}, 5000));
	}
}