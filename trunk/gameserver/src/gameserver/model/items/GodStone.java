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

package gameserver.model.items;

import commons.utils.Rnd;
import gameserver.controllers.movement.ActionObserver;
import gameserver.controllers.movement.ActionObserver.ObserverType;
import gameserver.model.DescriptionId;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.GodstoneInfo;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.ItemService;
import gameserver.skill.SkillEngine;
import gameserver.skill.model.Effect;
import gameserver.skill.model.Skill;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

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
