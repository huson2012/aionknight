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

import gameserver.dataholders.DataManager;
import gameserver.model.DescriptionId;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.recipe.RecipeTemplate;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CraftLearnAction")
public class CraftLearnAction extends AbstractItemAction
{
	@XmlAttribute
	protected int recipeid;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		RecipeTemplate template = DataManager.RECIPE_DATA.getRecipeTemplateById(recipeid);
		if(template == null)
			return false;

		if(template.getRace().ordinal() != player.getCommonData().getRace().getRaceId())
			return false;

		if(player.getRecipeList().isRecipePresent(recipeid))
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1330060));
			return false;
		}

		if(!player.getSkillList().isSkillPresent(template.getSkillid()))
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1330062, DataManager.SKILL_DATA
				.getSkillTemplate(template.getSkillid()).getName()));
			return false;
		}

		if(template.getSkillpoint() > player.getSkillList().getSkillLevel(template.getSkillid()))
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1330063));
			return false;
		}
		return true;
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem)
	{
		RecipeTemplate template = DataManager.RECIPE_DATA.getRecipeTemplateById(recipeid);
		
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.USE_ITEM(new DescriptionId(parentItem.getItemTemplate().getNameId())));
		PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId()));
		
		if (player.getInventory().removeFromBagByObjectId(parentItem.getObjectId(), 1))
		{
			player.getRecipeList().addRecipe(player, template);
		}
	}
}