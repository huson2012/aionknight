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

package gameserver.controllers;

import commons.utils.Rnd;
import gameserver.controllers.movement.StartMovingListener;
import gameserver.dataholders.DataManager;
import gameserver.model.DescriptionId;
import gameserver.model.gameobjects.Gatherable;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.GatherableTemplate;
import gameserver.model.templates.gather.Material;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.serverpackets.SM_DELETE;
import gameserver.network.aion.serverpackets.SM_GATHERABLE_INFO;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.RespawnService;
import gameserver.skill.model.SkillTemplate;
import gameserver.skill.task.GatheringTask;
import gameserver.utils.PacketSendUtility;
import java.util.List;

public class GatherableController extends VisibleObjectController<Gatherable>
{
	private int gatherCount;
	private int currentGatherer;
	private GatheringTask task;

	public enum GatherState
	{
		GATHERED,
		GATHERING,
		IDLE
	}

	private GatherState state = GatherState.IDLE;
	
	/**
	 * Start gathering process
	 * 
	 * @param player
	 */
	public void onStartUse(final Player player)
	{
		if(state == GatherState.GATHERING)
			return;
		
		final GatherableTemplate template = this.getOwner().getObjectTemplate();
		
		if(!checkStartConditions(player, template))
			return;
		
		List<Material> materials = null;
		
		if(template.getCheckType() == 1 && !player.getEquipment().getEquippedItemsByItemId(template.getRequiredItem()).isEmpty())
			materials = template.getExtraMaterials().getMaterial();
		else
			materials = template.getMaterials().getMaterial();
		
		int count = materials.size();
		
		if(count == 0)
			return;
		
		Material material = null;

		int percent = 10000000;
		for(Material mat : materials)
		{
			int rate = mat.getRate();
			if(Rnd.get(percent) <= rate)
			{
				material = mat;
				break;
			}
			else
				percent -= rate;
		}

		state = GatherState.GATHERING;
		currentGatherer = player.getObjectId();
		player.getObserveController().attach(new StartMovingListener(){
			@Override
			public void moved()
			{
				finishGathering(player);
			}
		});
		int skillLvlDiff = player.getSkillList().getSkillLevel(template.getHarvestSkill())-template.getSkillLevel();
		task = new GatheringTask(player, getOwner(), material, skillLvlDiff);
		task.start();
	}

	/**
	 * Checks whether player have needed skill for gathering also skill and player level is sufficient
	 * 
	 * @param player
	 * @param template
	 * @return
	 */
	private boolean checkStartConditions(final Player player, final GatherableTemplate template)
	{
		int harvestSkillId = template.getHarvestSkill();
		int charLevel = template.getCharLevel();
		int skillLevel = template.getSkillLevel();
		int requiredItem = template.getRequiredItem();
		
		if(template.getCheckType() == 1)
			requiredItem = 0;

		if(!player.getSkillList().isSkillPresent(harvestSkillId))
		{
			if(harvestSkillId == 30001)
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1330066));
			return false;
		}
		if(player.getCommonData().getLevel() < charLevel)
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400737, charLevel));
			return false;
		}
		if(player.getSkillList().getSkillLevel(harvestSkillId) < skillLevel)
		{
			SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(harvestSkillId);
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1330001, new DescriptionId(skillTemplate.getNameId())));
			return false;
		}
		if(requiredItem != 0)
		{
			if(player.getInventory().getItemCountByItemId(requiredItem) == 0)
			{
				ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(requiredItem);
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(902030, new DescriptionId(itemTemplate.getNameId())));
				return false;
			}
		}
		if(player.getInventory().isFull())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.EXTRACT_GATHER_INVENTORY_IS_FULL());
			return false;
		}
		return true;
	}

	public void completeInteraction(Player player)
	{
		state = GatherState.IDLE;
		gatherCount++;
		if(gatherCount >= getOwner().getObjectTemplate().getHarvestCount())
		{
			onDie();
			PacketSendUtility.sendPacket(player, new SM_DELETE(getOwner(), 1));
		}
	}

	public void rewardPlayer(Player player)
	{
		if(player != null)
		{
			GatherableTemplate template = getOwner().getObjectTemplate();
			int requiredItem = template.getRequiredItem();
			int harvestSkillId = template.getHarvestSkill();
			int skillLvl = template.getSkillLevel();
			int xpReward = 0;
			int eraseValue = template.getEraseValue();
			SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(harvestSkillId);
			
			if(requiredItem != 0 && eraseValue != 0)
				player.getInventory().removeFromBagByItemId(requiredItem, eraseValue);
			
			if(skillLvl + 40 > player.getSkillList().getSkillLevel(harvestSkillId))
			{
				if(harvestSkillId == 30003)
					xpReward = (int)((0.0288*skillLvl*skillLvl+3.5*skillLvl+270)*player.getRates().getGatheringLvlRate());
				else
					xpReward = (int)((0.0144*skillLvl*skillLvl+3.5*skillLvl+270)*player.getRates().getGatheringLvlRate());
				
				if(player.getXpBoost() > 0)
					xpReward = xpReward * ((player.getXpBoost() / 100) + 1);
				
				if(player.getSkillList().addSkillXp(player, harvestSkillId, xpReward))
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.EXTRACT_GATHER_SUCCESS_GETEXP());
					player.getCommonData().addExp((int)(xpReward*player.getRates().getGatheringXPRate()/player.getRates().getGatheringLvlRate()));
				}
				else
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_DONT_GET_PRODUCTION_EXP(new DescriptionId(skillTemplate.getNameId())));
			}
			else
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_DONT_GET_PRODUCTION_EXP(new DescriptionId(skillTemplate.getNameId())));
		}
	}

	/**
	 * Called by client when some action is performed or on finish gathering
	 * Called by move observer on player move
	 * 
	 * @param player
	 */
	public void finishGathering(Player player)
	{
		if(currentGatherer == player.getObjectId())
		{
			if(state == GatherState.GATHERING)
				task.abort();
			
			currentGatherer = 0;
			state = GatherState.IDLE;
		}
	}

	private void onDie()
	{
		Gatherable owner = getOwner();
		RespawnService.scheduleRespawnTask(owner);
		owner.getController().delete();
	}

	@Override
	public void onRespawn()
	{
		PacketSendUtility.broadcastPacket(getOwner(), new SM_GATHERABLE_INFO(getOwner()));
		this.gatherCount = 0;
	}

	@Override
	public Gatherable getOwner()
	{
		return super.getOwner();
	}
}