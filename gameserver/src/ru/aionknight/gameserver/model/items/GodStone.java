/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.model.items;

import org.apache.log4j.Logger;
import org.openaion.commons.utils.Rnd;

import ru.aionknight.gameserver.controllers.movement.ActionObserver;
import ru.aionknight.gameserver.controllers.movement.ActionObserver.ObserverType;
import ru.aionknight.gameserver.model.DescriptionId;
import ru.aionknight.gameserver.model.gameobjects.Creature;
import ru.aionknight.gameserver.model.gameobjects.Item;
import ru.aionknight.gameserver.model.gameobjects.PersistentState;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.model.templates.item.GodstoneInfo;
import ru.aionknight.gameserver.model.templates.item.ItemTemplate;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import ru.aionknight.gameserver.services.ItemService;
import ru.aionknight.gameserver.skill.SkillEngine;
import ru.aionknight.gameserver.skill.model.Effect;
import ru.aionknight.gameserver.skill.model.Skill;
import ru.aionknight.gameserver.utils.PacketSendUtility;


/**
 * @author ATracer
 * 
 */
public class GodStone extends ItemStone
{
	private static final Logger	log	= Logger.getLogger(GodStone.class);

	private final GodstoneInfo	godstoneInfo;
	private ActionObserver		actionListener;
	private final int			probability;
	private final int			probabilityLeft;
	private ItemTemplate		itemTemplate;

	public GodStone(int itemObjId, int itemId, PersistentState persistentState)
	{
		super(itemObjId, itemId, 0, ItemStoneType.GODSTONE, persistentState);
		itemTemplate = ItemService.getItemTemplate(itemId);
		godstoneInfo = itemTemplate.getGodstoneInfo();
		
		if(godstoneInfo != null)
		{
			probability = godstoneInfo.getProbability();
			probabilityLeft = godstoneInfo.getProbabilityleft();
		}
		else
		{
			probability = 0;
			probabilityLeft = 0;
			log.warn("CHECKPOINT: Godstone info missing for item : " + itemId);
		}
		
	}

	/**
	 * 
	 * @param player
	 */
	public void onEquip(final Player player)
	{
		if(godstoneInfo == null)
			return;
		
		final Item item = player.getEquipment().getEquippedItemByObjId(itemObjId);
			
		actionListener = new ActionObserver(ObserverType.ATTACK){
			@Override
			public void attack(Creature creature)
			{
				int rand = Rnd.get(probability - probabilityLeft, probability);
				//half the chance for subhandWeapon
				if (item == player.getEquipment().getSubHandWeapon() && player.getEquipment().isDualWieldEquipped())
					rand *= 0.5;

				if(rand > Rnd.get(0, 1000))
				{
					Skill skill = SkillEngine.getInstance().getSkill(player, godstoneInfo.getSkillid(),
						godstoneInfo.getSkilllvl(), player.getTarget(), itemTemplate);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1301062, new DescriptionId(skill.getSkillTemplate().getNameId())));
					skill.setFirstTargetRangeCheck(false);
					if (skill.canUseSkill() && player.getTarget() instanceof Creature)
					{
						Effect ef = new Effect(player, (Creature)player.getTarget(), skill.getSkillTemplate(), skill.getSkillLevel(), 0, itemTemplate);
						ef.initialize();
						ef.applyEffect();
						ef = null;
					}
					skill = null;
				}
			}
		};

		player.getObserveController().addObserver(actionListener);
	}

	/**
	 * 
	 * @param player
	 */
	public void onUnEquip(Player player)
	{
		if(actionListener != null)
			player.getObserveController().removeObserver(actionListener);

	}
}
