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
import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.skill.model.SkillTemplate;
import gameserver.skill.model.learn.SkillClass;
import gameserver.skill.model.learn.SkillRace;
import gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkillLearnAction")
public class SkillLearnAction extends AbstractItemAction
{
	@XmlAttribute
	protected int skillid;
	@XmlAttribute
	protected int level;
	@XmlAttribute(name = "class")
	protected SkillClass playerClass;
	@XmlAttribute
	protected SkillRace race;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		return validateConditions(player);
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem)
	{
		//item animation and message
		ItemTemplate itemTemplate = parentItem.getItemTemplate();
		//PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.USE_ITEM(itemTemplate.getDescription()));
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
			parentItem.getObjectId(), itemTemplate.getTemplateId()), true);	
		//add skill
		player.getSkillList().addSkill(player, skillid, 1, true);
		SkillTemplate skill = DataManager.SKILL_DATA.getSkillTemplate(skillid);
		if(skill.isPassive())
			player.getController().updatePassiveStats();
		//remove book from inventory (assuming its not stackable)
		Item item = player.getInventory().getItemByObjId(parentItem.getObjectId());
		if(player.getInventory().removeFromBag(item, true))
			PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(parentItem.getObjectId()));	
	}

	private boolean validateConditions(Player player)
	{
		//1. check player level
		if(player.getCommonData().getLevel() < level)
			return false;

		PlayerClass pc = player.getCommonData().getPlayerClass();

		if(!validateClass(pc))
			return false;

		//4. check player race and SkillRace.ALL
		if(player.getCommonData().getRace().ordinal() != race.ordinal() 
			&& race != SkillRace.ALL)
			return false;
		//5. check whether this skill is already learned
        return !player.getSkillList().isSkillPresent(skillid);

    }

	private boolean validateClass(PlayerClass pc)
	{
		boolean result = false;
		//2. check if current class is second class and book is for starting class
		if(!pc.isStartingClass() && PlayerClass.getStartingClassFor(pc).ordinal() == playerClass.ordinal())
			result = true;
		//3. check player class and SkillClass.ALL
		if(pc.ordinal() == playerClass.ordinal()
			|| playerClass == SkillClass.ALL)
			result = true;

		return result;
	}
}