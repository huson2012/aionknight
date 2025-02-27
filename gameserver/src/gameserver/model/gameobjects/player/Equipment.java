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

package gameserver.model.gameobjects.player;

import commons.database.dao.DAOManager;
import gameserver.controllers.movement.StartMovingListener;
import gameserver.dao.InventoryDAO;
import gameserver.model.*;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.stats.listeners.ItemEquipmentListener;
import gameserver.model.items.ItemSlot;
import gameserver.model.templates.item.*;
import gameserver.model.templates.itemset.ItemSetTemplate;
import gameserver.network.aion.serverpackets.*;
import gameserver.services.StigmaService;
import gameserver.services.TemporaryObjectsService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.WorldPosition;
import org.apache.log4j.Logger;

import java.util.*;

public class Equipment
{
	private SortedMap<Integer, Item>	equipment		= new TreeMap<Integer, Item>();
	private Player						owner;
	private static final Logger			log				= Logger.getLogger(Equipment.class);
	private Set<Integer>				markedFreeSlots	= new HashSet<Integer>();
	private PersistentState				persistentState	= PersistentState.UPDATED;

	public Equipment(Player player)
	{
		this.owner = player;
	}

	/**
	 * 
	 * @param itemUniqueId
	 * @param slot
	 * @return item or null in case of failure
	 */
	public Item equipItem(int itemUniqueId, int slot)
	{
		Item item = owner.getInventory().getItemByObjId(itemUniqueId);

		if(item == null)
			return null;

		ItemTemplate itemTemplate = item.getItemTemplate();

		switch(itemTemplate.getRace())
		{
			case ASMODIANS:
				if(owner.getCommonData().getRace() != Race.ASMODIANS)
					return null;
				break;
			case ELYOS:
				if(owner.getCommonData().getRace() != Race.ELYOS)
					return null;
				break;
		}
		
		if (itemTemplate.getGenderPermitted() != Gender.ALL && itemTemplate.getGenderPermitted() != owner.getGender())
		{
			PacketSendUtility.sendMessage(owner, "You cannot wear this object because it can be weared only by " + (itemTemplate.getGenderPermitted() == Gender.MALE ? "a guy" : "a woman") + '.');
			return null;
		}

		//class restriction
		if (!itemTemplate.checkClassRestrict(owner.getCommonData().getPlayerClass()))
			return null;
		
		//level restriction
		LevelRestrict restrict = itemTemplate.getRectrict(owner.getCommonData().getPlayerClass(), owner.getLevel());
		LevelRestrictType restrictType = restrict.getType();
		if(restrictType != LevelRestrictType.NONE)
		{
			if (restrictType == LevelRestrictType.LOW)
			{
				PacketSendUtility.sendPacket(owner, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_TOO_LOW_LEVEL_MUST_BE_THIS_LEVEL(
					restrict.getLevel(), new DescriptionId(Integer.parseInt(item.getName()))));
			}
			else
			{
				PacketSendUtility.sendPacket(owner, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_TOO_HIGH_LEVEL(
					restrict.getLevel(), new DescriptionId(Integer.parseInt(item.getName()))));
			}
			return null;
		}

		int itemSlotToEquip = 0;

		synchronized(equipment)
		{			
			markedFreeSlots.clear();

			// validate item against current equipment and mark free slots
			switch(item.getEquipmentType())
			{
				case ARMOR:
					if(!validateEquippedArmor(item, true))
						return null;
					break;
				case WEAPON:
					if(!validateEquippedWeapon(item, true))
						return null;
					break;
			}

			// check whether there is already item in specified slot
			int itemSlotMask = 0;
			switch(item.getEquipmentType())
			{
				case STIGMA:
					itemSlotMask = slot;
					break;
				default:
					itemSlotMask = itemTemplate.getItemSlot();
					break;
			}

			// find correct slot
			List<ItemSlot> possibleSlots = ItemSlot.getSlotsFor(itemSlotMask);
			for(ItemSlot possibleSlot : possibleSlots)
			{
				int slotId = possibleSlot.getSlotIdMask();
				if(equipment.get(slotId) == null || markedFreeSlots.contains(slotId))
				{
					itemSlotToEquip = slotId;
					break;
				}
			}
						
			// equip first occupied slot if there is no free
			if(itemSlotToEquip == 0)
			{
				itemSlotToEquip = possibleSlots.get(0).getSlotIdMask();
			}
			
			if (!StigmaService.notifyEquipAction(owner, item, itemSlotToEquip))
				return null;
		}
		
		if(itemSlotToEquip == 0)
			return null;

		if(itemTemplate.isSoulBound() && !item.isSoulBound())
		{
			soulBindItem(owner, item, itemSlotToEquip);
			return null;
		}
		else
		{
			return equip(itemSlotToEquip, item);
		}
	}
	
	/**
	 * 
	 * @param itemSlotToEquip
	 * @param item
	 */
	private Item equip(int itemSlotToEquip, Item item)
	{
		synchronized(equipment)
		{
			//remove item first from inventory to have at least one slot free
			boolean removeResult = owner.getInventory().removeFromBag(item, false);
			
			if(!removeResult)
				return null;
			
			if(item.getTempItemSettedTime() > 0)
				TemporaryObjectsService.getInstance().addObject(item);
			
			//do unequip of necessary items
			Item equippedItem = equipment.get(itemSlotToEquip);
			if(equippedItem != null)
				unEquip(itemSlotToEquip);
			
			switch(item.getEquipmentType())
			{
				case ARMOR:
					validateEquippedArmor(item, false);
					break;
				case WEAPON:
					validateEquippedWeapon(item, false);
					break;
			}

			if(equipment.get(itemSlotToEquip) != null)
			{
				log.error("CHECKPOINT : putting item to already equiped slot. Info slot: " +
					itemSlotToEquip + " new item: " + item.getItemTemplate().getTemplateId() + " old item: "
					+ equipment.get(itemSlotToEquip).getItemTemplate().getTemplateId());
				return null;
			}			

			//equip target item
			equipment.put(itemSlotToEquip, item);
			item.setEquipped(true);
			item.setEquipmentSlot(itemSlotToEquip);
			PacketSendUtility.sendPacket(owner, new SM_UPDATE_ITEM(item));
			
			//update stats
			ItemEquipmentListener.onItemEquipment(item, owner);
			owner.getObserveController().notifyItemEquip(item, owner);
			owner.getLifeStats().updateCurrentStats();
            persistentState = PersistentState.UPDATE_REQUIRED;
			return item;
		}	
	}


	/**
	 *	Called when CM_EQUIP_ITEM packet arrives with action 1
	 *
	 * @param itemUniqueId
	 * @param slot
	 * @return item or null in case of failure
	 */
	public Item unEquipItem(int itemUniqueId, int slot)
	{
		//if inventory is full unequip action is disabled
		if(owner.getInventory().isFull())
			return null;
		
		synchronized(equipment)
		{
			Item itemToUnequip = null;

			for(Item item : equipment.values())
			{
				if(item.getObjectId() == itemUniqueId)
				{
					itemToUnequip = item;
				}
			}

			if(itemToUnequip == null || !itemToUnequip.isEquipped())
				return null;

			//if unequip bow - unequip arrows also
			if(itemToUnequip.getItemTemplate().getWeaponType() == WeaponType.BOW)
			{
				Item possibleArrows = equipment.get(ItemSlot.SUB_HAND.getSlotIdMask());
				if(possibleArrows != null && possibleArrows.getItemTemplate().getArmorType() == ArmorType.ARROW)
				{
					//TODO more wise check here is needed
					if(owner.getInventory().getNumberOfFreeSlots() < 1)
						return null;
					unEquip(ItemSlot.SUB_HAND.getSlotIdMask());
				}
			}
			
			if(itemToUnequip.getEquipmentSlot() == ItemSlot.MAIN_HAND.getSlotIdMask())
			{
				Item ohWeapon = equipment.get(ItemSlot.SUB_HAND.getSlotIdMask());
				if(ohWeapon != null && ohWeapon.getItemTemplate().isWeapon())
				{
					if(owner.getInventory().getNumberOfFreeSlots() < 2)
						return null;
					else
						unEquip(ItemSlot.SUB_HAND.getSlotIdMask());
				}
			}
			
			//if unequip power shard
			if(itemToUnequip.getItemTemplate().isArmor() && itemToUnequip.getItemTemplate().getArmorType() == ArmorType.SHARD)
			{
				owner.unsetState(CreatureState.POWERSHARD);
				PacketSendUtility.sendPacket(owner, new SM_EMOTION(owner, EmotionType.POWERSHARD_OFF, 0, 0));
			}

			if (!StigmaService.notifyUnequipAction(owner, itemToUnequip))
				return null;

			unEquip(itemToUnequip.getEquipmentSlot());
			return itemToUnequip;
		}	
	}
	
	private void unEquip(int slot)
	{
		Item item = equipment.remove(slot);
		if(item == null)
		{ // NPE check, there is no item in the given slot.
			return;
		}

		item.setEquipped(false);
		
		ItemEquipmentListener.onItemUnequipment(item, owner);
		owner.getObserveController().notifyItemUnEquip(item, owner);
		
		owner.getLifeStats().updateCurrentStats();
		
		if(item.getTempItemSettedTime() > 0)
			TemporaryObjectsService.getInstance().removeObject(item);
		
		owner.getInventory().putToBag(item);
		PacketSendUtility.sendPacket(owner, new SM_UPDATE_ITEM(item));
	}
	
	public boolean removeTemporaryItem(int itemObjId, int slot)
	{
		Item tempItem = equipment.get(slot);
		if(tempItem != null && tempItem.getObjectId() == itemObjId)
		{
			Item item = equipment.remove(slot);
			if(item == null)
			{ // NPE check, there is no item in the given slot.
				return false;
			}
			
			ItemEquipmentListener.onItemUnequipment(item, owner);
			owner.getObserveController().notifyItemUnEquip(item, owner);
			
			owner.getInventory().useStorageTrashUtilizer(item);
			
			owner.getLifeStats().updateCurrentStats();			
			return true;
		}
		return false;
	}


	/**
	 * Used during equip process and analyzes equipped slots
	 *
	 * @param item
	 * @param itemInMainHand
	 * @param itemInSubHand
	 * @return
	 */
	private boolean validateEquippedWeapon(Item item, boolean validateOnly)
	{
		// check present skill
		int[] requiredSkills = item.getItemTemplate().getWeaponType().getRequiredSkills();

		if(!checkAvaialbeEquipSkills(requiredSkills))
			return false;
		
		Item itemInMainHand = equipment.get(ItemSlot.MAIN_HAND.getSlotIdMask());
		Item itemInSubHand = equipment.get(ItemSlot.SUB_HAND.getSlotIdMask());
		
		int requiredSlots = 0;
		switch(item.getItemTemplate().getWeaponType().getRequiredSlots())
		{
			case 2:
				switch(item.getItemTemplate().getWeaponType())
				{
					//if bow and arrows are equipped + new item is bow - dont uneqiup arrows
					case BOW:
						if(itemInSubHand != null &&
							itemInSubHand.getItemTemplate().getArmorType() != ArmorType.ARROW)
						{
							if(validateOnly)
							{
								requiredSlots++;
								markedFreeSlots.add(ItemSlot.SUB_HAND.getSlotIdMask());
							}
							else
								unEquip(ItemSlot.SUB_HAND.getSlotIdMask());
						}
						break;
						//if new item is not bow - unequip arrows
					default:
						if(itemInSubHand != null)
						{
							if(validateOnly)
							{
								requiredSlots++;
								markedFreeSlots.add(ItemSlot.SUB_HAND.getSlotIdMask());
							}
							else
								unEquip(ItemSlot.SUB_HAND.getSlotIdMask());
						}
				}//no break
			case 1:
				//check dual skill
				if(itemInMainHand != null && owner.getSkillList().getDualMasterySkill() == 0 )
				{
					if(validateOnly)
					{
						requiredSlots++;
						markedFreeSlots.add(ItemSlot.MAIN_HAND.getSlotIdMask());
					}
					else
						unEquip(ItemSlot.MAIN_HAND.getSlotIdMask());
				}
				//check 2h weapon in main hand
				else if(itemInMainHand != null && itemInMainHand.getItemTemplate().getWeaponType().getRequiredSlots() == 2)
				{
					if(validateOnly)
					{
						requiredSlots++;
						markedFreeSlots.add(ItemSlot.MAIN_HAND.getSlotIdMask());
					}
					else
						unEquip(ItemSlot.MAIN_HAND.getSlotIdMask());
				}

				//unequip arrows if bow+arrows were equipeed
				Item possibleArrows = equipment.get(ItemSlot.SUB_HAND.getSlotIdMask());
				if(possibleArrows != null && possibleArrows.getItemTemplate().getArmorType() == ArmorType.ARROW)
				{
					if(validateOnly)
					{
						requiredSlots++;
						markedFreeSlots.add(ItemSlot.SUB_HAND.getSlotIdMask());
					}
					else
						unEquip(ItemSlot.SUB_HAND.getSlotIdMask());
				}
				break;
		}
		
		//check agains = required slots - 1(equipping item)
		return owner.getInventory().getNumberOfFreeSlots() >= requiredSlots - 1;
	}

	/**
	 *
	 * @param requiredSkills
	 * @return
	 */
	private boolean checkAvaialbeEquipSkills(int[] requiredSkills)
	{
		boolean isSkillPresent = false;

		//if no skills required - validate as true
		if(requiredSkills.length == 0)
			return true;

		for(int skill : requiredSkills)
		{
			if(owner.getSkillList().isSkillPresent(skill))
			{
				isSkillPresent = true;
				break;
			}
		}
		return isSkillPresent;
	}

	/**
	 * Used during equip process and analyzes equipped slots
	 *
	 * @param item
	 * @param itemInMainHand
	 * @return
	 */
	private boolean validateEquippedArmor(Item item, boolean validateOnly)
	{
		//allow wearing of jewelry etc stuff
		ArmorType armorType = item.getItemTemplate().getArmorType();
		if(armorType == null)
			return true;

		// check present skill
		int[] requiredSkills = armorType.getRequiredSkills();
		if(!checkAvaialbeEquipSkills(requiredSkills))
			return false;
		
		Item itemInMainHand = equipment.get(ItemSlot.MAIN_HAND.getSlotIdMask());		
		switch(item.getItemTemplate().getArmorType())
		{
			case ARROW:
				if(itemInMainHand == null
					|| itemInMainHand.getItemTemplate().getWeaponType() != WeaponType.BOW)
				{
					if(validateOnly)
						return false;
				}
				break;
			case SHIELD:
				if(itemInMainHand != null
					&& itemInMainHand.getItemTemplate().getWeaponType().getRequiredSlots() == 2)
				{
					if(validateOnly)
					{
						if(owner.getInventory().isFull())
							return false;
						markedFreeSlots.add(ItemSlot.MAIN_HAND.getSlotIdMask());
					}
					else
					{
						//remove 2H weapon
						unEquip(ItemSlot.MAIN_HAND.getSlotIdMask());
					}
				}
				break;
		}
		return true;
	}

	/**
	 * Will look item in equipment item set
	 *
	 * @param value
	 * @return Item
	 */
	public Item getEquippedItemByObjId(int value)
	{
		synchronized(equipment)
		{
			for(Item item : equipment.values())
			{
				if(item.getObjectId() == value)
					return item;
			}
		}
		
		return null;
	}

	/**
	 * 
	 * @param value
	 * @return List<Item>
	 */
	public List<Item> getEquippedItemsByItemId(int value)
	{
		List<Item> equippedItemsById = new ArrayList<Item>();
		synchronized(equipment)
		{
			for(Item item : equipment.values())
			{
				if(item.getItemTemplate().getTemplateId() == value)
					equippedItemsById.add(item);
			}
		}
		
		return equippedItemsById;
	}

	/**
	 * 
	 * @return List<Item>
	 */
	public List<Item> getEquippedItems()
	{
		List<Item> equippedItems = new ArrayList<Item>();
		equippedItems.addAll(equipment.values());

		return equippedItems;
	}
	
	/**
	 * 
	 * @return List<Item>
	 */
	public List<Item> getEquippedItemsWithoutStigma()
	{
		List<Item> equippedItems = new ArrayList<Item>();
		for(Item item : equipment.values())
		{
			if(item.getEquipmentSlot() < ItemSlot.STIGMA1.getSlotIdMask())
				equippedItems.add(item);
		}

		return equippedItems;
	}

	/**
	 * 
	 * @return List<Item>
	 */
	public List<Item> getEquippedItemsStigma()
	{
		List<Item> equippedItems = new ArrayList<Item>();
		for(Item item : equipment.values())
		{
			if(item.getEquipmentSlot() >= ItemSlot.STIGMA1.getSlotIdMask() && item.getEquipmentSlot() <= ItemSlot.ADV_STIGMA5.getSlotIdMask())
				equippedItems.add(item);
		}

		return equippedItems;
	}
	public List<Item> getEquippedItemsNormalStigma()
	{
		List<Item> equippedItems = new ArrayList<Item>();
		for(Item item : equipment.values())
		{
			if(item.getEquipmentSlot() >= ItemSlot.STIGMA1.getSlotIdMask() && item.getEquipmentSlot() <= ItemSlot.STIGMA6.getSlotIdMask())
				equippedItems.add(item);
		}

		return equippedItems;
	}
	public List<Item> getEquippedItemsAdvStigma()
	{
		List<Item> equippedItems = new ArrayList<Item>();
		for(Item item : equipment.values())
		{
			if(item.getEquipmentSlot() >= ItemSlot.ADV_STIGMA1.getSlotIdMask() && item.getEquipmentSlot() <= ItemSlot.ADV_STIGMA5.getSlotIdMask())
				equippedItems.add(item);
		}

		return equippedItems;
	}
	
	public Item getEquippedItemBySlot(int slot)
	{
		return this.equipment.get(slot);
	}
	/**
	 * @return Number of parts equipped belonging to requested itemset
	 */
	public int itemSetPartsEquipped(int itemSetTemplateId)
	{

		Set<Integer> parts = new HashSet<Integer>();
		for(Item item : equipment.values())
		{
			if (item.getEquipmentSlot() == ItemSlot.MAIN_OFF_HAND.getSlotIdMask() || item.getEquipmentSlot() == ItemSlot.SUB_OFF_HAND.getSlotIdMask())
				continue;
			ItemSetTemplate setTemplate = item.getItemTemplate().getItemSet();
			if(setTemplate != null && setTemplate.getId() == itemSetTemplateId)
					parts.add(item.getItemId());
		}
		
		return parts.size();
	}
	
	/**
	 * Should be called only when loading from DB for items isEquipped=1
	 * 
	 * @param item
	 */
	public void onLoadHandler(Item item)
	{
		if(equipment.containsKey(item.getEquipmentSlot()))
		{
			log.warn("Duplicate equipped item in slot : " + item.getEquipmentSlot() + " " + owner.getObjectId());
			return;
		}
		//logger to catch cheaters which equipped items not for their class
		if (!item.getItemTemplate().checkClassRestrict(owner.getPlayerClass()))
			log.info("[AUDIT]Player: "+owner.getName()+" has equipped item: "+item.getObjectId()+" which is unequiptable by its class!");
		
		equipment.put(item.getEquipmentSlot(), item);
		
		if(item.getTempItemSettedTime() > 0)
			TemporaryObjectsService.getInstance().addObject(item);
	}

	/**
	 * Should be called only when equipment object totaly constructed on player loading
	 * Applies every equipped item stats modificators
	 */
	public void onLoadApplyEquipmentStats()
	{
		for(Item item : equipment.values())
		{
			if (owner.getGameStats() != null)
			{
				if(item.getEquipmentSlot() != ItemSlot.MAIN_OFF_HAND.getSlotIdMask()
					&& item.getEquipmentSlot() != ItemSlot.SUB_OFF_HAND.getSlotIdMask())
					ItemEquipmentListener.onItemEquipment(item, owner);
			}
			if(owner.getLifeStats() != null)
			{
				if(item.getEquipmentSlot() != ItemSlot.MAIN_OFF_HAND.getSlotIdMask()
					&& item.getEquipmentSlot() != ItemSlot.SUB_OFF_HAND.getSlotIdMask())
					owner.getLifeStats().synchronizeWithMaxStats();
			}
		}
	}

	/**
	 * @return true or false
	 */
	public boolean isShieldEquipped()
	{
		Item subHandItem = equipment.get(ItemSlot.SUB_HAND.getSlotIdMask());
		return subHandItem != null && subHandItem.getItemTemplate().getArmorType() == ArmorType.SHIELD;
	}

	/**
	 *
	 * @return <tt>WeaponType</tt> of current weapon in main hand or null
	 */
	public WeaponType getMainHandWeaponType()
	{
		Item mainHandItem = equipment.get(ItemSlot.MAIN_HAND.getSlotIdMask());
		if(mainHandItem == null)
			return null;

		return mainHandItem.getItemTemplate().getWeaponType();
	}

	/**
	 *
	 * @return <tt>WeaponType</tt> of current weapon in sub hand or null
	 */
	public WeaponType getSubHandWeaponType()
	{
		Item subHandItem = equipment.get(ItemSlot.SUB_HAND.getSlotIdMask()); ///
		if(subHandItem != null && subHandItem.getItemTemplate().isWeapon())
			return subHandItem.getItemTemplate().getWeaponType();

		return null;
	}

	/**
	 *
	 * @return <tt>WeaponType</tt> of current weapon in sub off hand or null
	 */
	public WeaponType getSubOffHandWeaponType()
	{
		Item subOffHandItem = equipment.get(ItemSlot.SUB_OFF_HAND.getSlotIdMask()); ///
		if(subOffHandItem != null && subOffHandItem.getItemTemplate().isWeapon())
			return subOffHandItem.getItemTemplate().getWeaponType();

		return null;
	}
	
	public boolean isArrowEquipped()
	{
		Item arrow = equipment.get(ItemSlot.SUB_HAND.getSlotIdMask());

        return arrow != null && arrow.getItemTemplate().getArmorType() == ArmorType.ARROW;

    }
	
	public boolean isPowerShardEquipped()
	{
		Item leftPowershard = equipment.get(ItemSlot.POWER_SHARD_LEFT.getSlotIdMask());
		if(leftPowershard != null)
			return true;
		
		Item rightPowershard = equipment.get(ItemSlot.POWER_SHARD_RIGHT.getSlotIdMask());
        return rightPowershard != null;

    }

	public Item getMainHandPowerShard()
	{
		Item mainHandPowerShard = equipment.get(ItemSlot.POWER_SHARD_RIGHT.getSlotIdMask());
		if(mainHandPowerShard != null)
			return mainHandPowerShard;

		return null;
	}

	public Item getSubHandPowerShard()
	{
		Item subHandPowerShard = equipment.get(ItemSlot.POWER_SHARD_LEFT.getSlotIdMask());
		if(subHandPowerShard != null)
			return subHandPowerShard;

		return null;
	}

	/**
	 * @param powerShardItem
	 * @param count
	 */
	public void usePowerShard(Item powerShardItem, int count)
	{
		decreaseEquippedItemCount(powerShardItem.getObjectId(), count);

		if(powerShardItem.getItemCount() <= 0)
		{// Search for next same power shards stack
			List<Item> powerShardStacks = owner.getInventory().getItemsByItemId(powerShardItem.getItemTemplate().getTemplateId());
			if(powerShardStacks.size() != 0)
			{
				equipItem(powerShardStacks.get(0).getObjectId(), powerShardItem.getEquipmentSlot());
			}
			else
			{
				PacketSendUtility.sendPacket(owner, SM_SYSTEM_MESSAGE.NO_POWER_SHARD_LEFT());
				owner.unsetState(CreatureState.POWERSHARD);
			}
		}
	}

	public void useArrow()
	{
		Item arrow = equipment.get(ItemSlot.SUB_HAND.getSlotIdMask());

		if(arrow == null || !arrow.getItemTemplate().isArmor() && arrow.getItemTemplate().getArmorType() != ArmorType.ARROW)
			return;

		decreaseEquippedItemCount(arrow.getObjectId(), 1);
	}


	private void decreaseEquippedItemCount(int itemObjId, int count)
	{
		Item equippedItem = getEquippedItemByObjId(itemObjId);

		// Only Arrows and Shards can be decreased
		if(equippedItem.getItemTemplate().getArmorType() != ArmorType.SHARD
			&& equippedItem.getItemTemplate().getArmorType() != ArmorType.ARROW)
			return;

		if(equippedItem.getItemCount() >= count)
			equippedItem.decreaseItemCount(count);
		else
			equippedItem.decreaseItemCount(equippedItem.getItemCount());

		if(equippedItem.getItemCount() == 0)
		{
			equipment.remove(equippedItem.getEquipmentSlot());
			PacketSendUtility.sendPacket(owner, new SM_DELETE_ITEM(equippedItem.getObjectId()));
			DAOManager.getDAO(InventoryDAO.class).store(equippedItem, owner.getObjectId());
		}
		
		PacketSendUtility.sendPacket(owner, new SM_UPDATE_ITEM(equippedItem));
        persistentState = PersistentState.UPDATE_REQUIRED;
	}

	/**
	 * Switch SUB and MAIN hands
	 */
	public void switchHands()
	{
		Item mainHandItem = equipment.get(ItemSlot.MAIN_HAND.getSlotIdMask());
		Item subHandItem = equipment.get(ItemSlot.SUB_HAND.getSlotIdMask());
		Item mainOffHandItem = equipment.get(ItemSlot.MAIN_OFF_HAND.getSlotIdMask());
		Item subOffHandItem = equipment.get(ItemSlot.SUB_OFF_HAND.getSlotIdMask());

		List<Item> equippedWeapon = new ArrayList<Item>();

		if(mainHandItem != null)
			equippedWeapon.add(mainHandItem);
		if(subHandItem != null)
			equippedWeapon.add(subHandItem);
		if(mainOffHandItem != null)
			equippedWeapon.add(mainOffHandItem);
		if(subOffHandItem != null)
			equippedWeapon.add(subOffHandItem);

		for(Item item : equippedWeapon)
		{
			equipment.remove(item.getEquipmentSlot());
			item.setEquipped(false);
			PacketSendUtility.sendPacket(owner, new SM_UPDATE_ITEM(item, true));
			if (owner.getGameStats() != null)
			{
				if(item.getEquipmentSlot() == ItemSlot.MAIN_HAND.getSlotIdMask()
					|| item.getEquipmentSlot() == ItemSlot.SUB_HAND.getSlotIdMask())
					ItemEquipmentListener.onItemUnequipment(item, owner);
			}
		}


		for(Item item : equippedWeapon)
		{
			if(item.getEquipmentSlot() == ItemSlot.MAIN_HAND.getSlotIdMask())
				item.setEquipmentSlot(ItemSlot.MAIN_OFF_HAND.getSlotIdMask());

			else if(item.getEquipmentSlot() == ItemSlot.SUB_HAND.getSlotIdMask())
				item.setEquipmentSlot(ItemSlot.SUB_OFF_HAND.getSlotIdMask());

			else if(item.getEquipmentSlot() == ItemSlot.MAIN_OFF_HAND.getSlotIdMask())
				item.setEquipmentSlot(ItemSlot.MAIN_HAND.getSlotIdMask());

			else if(item.getEquipmentSlot() == ItemSlot.SUB_OFF_HAND.getSlotIdMask())
				item.setEquipmentSlot(ItemSlot.SUB_HAND.getSlotIdMask());
		}

		for(Item item : equippedWeapon)
		{
			equipment.put(item.getEquipmentSlot(), item);
			item.setEquipped(true);
			PacketSendUtility.sendPacket(owner, new SM_UPDATE_ITEM(item, true));
		}

		if (owner.getGameStats() != null)
		{
			for(Item item : equippedWeapon)
			{
				if(item.getEquipmentSlot() == ItemSlot.MAIN_HAND.getSlotIdMask()
					|| item.getEquipmentSlot() == ItemSlot.SUB_HAND.getSlotIdMask())
					ItemEquipmentListener.onItemEquipment(item, owner);
			}
		}

		owner.getLifeStats().updateCurrentStats();
        persistentState = PersistentState.UPDATE_REQUIRED;
	}

	/**
	 * @param weaponType
	 */
	public boolean isWeaponEquipped(WeaponType weaponType)
	{
		if(equipment.get(ItemSlot.MAIN_HAND.getSlotIdMask()) != null &&
			equipment.get(ItemSlot.MAIN_HAND.getSlotIdMask()).getItemTemplate().getWeaponType() == weaponType)
		{
			return true;
		}
        return equipment.get(ItemSlot.MAIN_HAND.getSlotIdMask()) == null  //check this later
                && equipment.get(ItemSlot.SUB_HAND.getSlotIdMask()) != null
                && equipment.get(ItemSlot.SUB_HAND.getSlotIdMask()).getItemTemplate().getWeaponType() == weaponType;
    }
	
	/**
	 * @param armorType
	 */
	public boolean isArmorEquipped(ArmorType armorType)
	{
		int[] armorSlots = new int[]{ItemSlot.BOOTS.getSlotIdMask(), ItemSlot.GLOVES.getSlotIdMask(),
			 ItemSlot.PANTS.getSlotIdMask(), ItemSlot.SHOULDER.getSlotIdMask(), ItemSlot.TORSO.getSlotIdMask()};
		boolean equiped = false;
		for(int slot : armorSlots)
		{
			if(equipment.get(slot) != null && equipment.get(slot).getItemTemplate().getArmorType() != armorType)
				return false;
			else if (equipment.get(slot) != null && equipment.get(slot).getItemTemplate().getArmorType() == armorType)
				equiped = true;
		}
        return equiped;
    }
	
	public Item getMainHandWeapon()
	{
		return equipment.get(ItemSlot.MAIN_HAND.getSlotIdMask());
	}
	
	public Item getSubHandWeapon()
	{
		return equipment.get(ItemSlot.SUB_HAND.getSlotIdMask());
	}

	public Item getMainOffHandWeapon()
	{
		return equipment.get(ItemSlot.MAIN_OFF_HAND.getSlotIdMask());
	}
	
	public Item getSubOffHandWeapon()
	{
		return equipment.get(ItemSlot.SUB_OFF_HAND.getSlotIdMask());
	}

	/**
	 * @return the persistentState
	 */
	public PersistentState getPersistentState()
	{
		return persistentState;
	}

	/**
	 * @param persistentState the persistentState to set
	 */
	public void setPersistentState(PersistentState persistentState)
	{
		this.persistentState = persistentState;
	}

	/**
	 * @param player
	 */
	public void setOwner(Player player)
	{
		this.owner = player;
	}

	/**
	 * 
	 * @param player
	 * @param item
	 * @return
	 */
	private boolean soulBindItem(final Player player, final Item item, final int slot)
	{
		RequestResponseHandler responseHandler = new RequestResponseHandler(player){

			public void acceptRequest(Creature requester, Player responder)
			{
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item
					.getObjectId(), item.getItemId(), 5000, 4), true);

				int sysMsgId = 0;
				if (player.isInState(CreatureState.CHAIR))
					sysMsgId = SM_SYSTEM_MESSAGE.STR_MSG_ACT_STATE_SITTING_ON_CHAIR;
				else if (player.isInState(CreatureState.FLYING))
					sysMsgId = SM_SYSTEM_MESSAGE.STR_MSG_ACT_STATE_FREE_FLYING;
				else if (player.isInState(CreatureState.RESTING))
					sysMsgId = SM_SYSTEM_MESSAGE.STR_MSG_ACT_STATE_SITTING;
				else if (player.isInState(CreatureState.WEAPON_EQUIPPED))
					sysMsgId = SM_SYSTEM_MESSAGE.STR_MSG_ASF_COMBAT;
				else if (player.isInState(CreatureState.GLIDING))
					sysMsgId = SM_SYSTEM_MESSAGE.STR_MSG_ASF_GLIDE;
		
				if (sysMsgId != 0)
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_SOUL_BOUND_INVALID_STANCE(sysMsgId));
					return;
				}
				player.getController().cancelTask(TaskId.ITEM_USE);
		
				player.getObserveController().attach(new StartMovingListener() {
					@Override
					public void moved()
					{
						if (item.isEquipped())
							return;
						player.getController().cancelTask(TaskId.ITEM_USE);
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.SOUL_BOUND_ITEM_CANCELED(new DescriptionId(Integer.parseInt(item.getName()))));
						PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item.getObjectId(), item.getItemId(), 0, 8), true);
					}
				});

				final WorldPosition position = player.getCommonData().getPosition().clone();

				// item usage animation
				player.getController().addNewTask(TaskId.ITEM_USE,
					ThreadPoolManager.getInstance().schedule(new Runnable(){
	
						public void run()
						{
							PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
								item.getObjectId(), item.getItemId(), 0, 6), true);
							if(!position.equals(player.getCommonData().getPosition()))
							{ // player moved, binding is canceled.
								return;
							}
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE
								.SOUL_BOUND_ITEM_SUCCEED(new DescriptionId(item.getNameID())));
							item.setSoulBound(true);
							equip(slot, item);
							PacketSendUtility.broadcastPacket(player, new SM_UPDATE_PLAYER_APPEARANCE(player.getObjectId(),
								getEquippedItemsWithoutStigma()), true);	
						}
				}, 5100));
			}

			public void denyRequest(Creature requester, Player responder)
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.SOUL_BOUND_ITEM_CANCELED(new DescriptionId(item
					.getNameID())));
			}
		};

		boolean requested = player.getResponseRequester().putRequest(
			SM_QUESTION_WINDOW.STR_SOUL_BOUND_ITEM_DO_YOU_WANT_SOUL_BOUND, responseHandler);
		if(requested)
		{
			PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(
				SM_QUESTION_WINDOW.STR_SOUL_BOUND_ITEM_DO_YOU_WANT_SOUL_BOUND, 0, new DescriptionId(item.getNameID())));
		}

		return false;
	}
	
	public boolean isDualWieldEquipped()
	{
		if (this.getMainHandWeapon() != null && this.getSubHandWeapon() != null && getSubHandWeaponType() != null)
		{
			switch (getSubHandWeaponType())
			{
				case DAGGER_1H:
				case MACE_1H:
				case SWORD_1H:
				case TOOLHOE_1H:
					if (this.getMainHandWeapon().getObjectId() != this.getSubHandWeapon().getObjectId())
						return true;
				break;
			}
		}
		return false;
	}
	
	public boolean isOffDualWieldEquipped()
	{
		if (this.getMainOffHandWeapon() != null && this.getSubOffHandWeapon() != null && getSubOffHandWeaponType() != null)
		{
			switch (getSubOffHandWeaponType())
			{
				case DAGGER_1H:
				case MACE_1H:
				case SWORD_1H:
				case TOOLHOE_1H:
					if (this.getMainOffHandWeapon().getObjectId() != this.getSubOffHandWeapon().getObjectId())
						return true;
				break;
			}
		}
		return false;
	}
}
