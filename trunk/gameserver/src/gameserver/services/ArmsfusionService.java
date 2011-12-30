/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
 */

package gameserver.services;

import commons.database.dao.DAOManager;
import gameserver.dao.InventoryDAO;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.items.ManaStone;
import gameserver.model.templates.item.ItemQuality;
import gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;
import java.util.Set;

/**
 * Этот класс отвечает за слияние предметов
 */
public class ArmsfusionService
{
	private static final Logger	log	= Logger.getLogger(ArmsfusionService.class);
		
	public static void fusionWeapons(Player player, int firstItemUniqueId, int secondItemUniqueId)
	{
		Item firstItem = player.getInventory().getItemByObjId(firstItemUniqueId);
		if(firstItem == null)
			firstItem = player.getEquipment().getEquippedItemByObjId(firstItemUniqueId);
		
		Item secondItem = player.getInventory().getItemByObjId(secondItemUniqueId);
		if(secondItem == null)
			secondItem = player.getEquipment().getEquippedItemByObjId(secondItemUniqueId);
		
		/**
		 * Нужно иметь необходимые элементы в инвентаре, и цель слияния NPC
		 */
		if(firstItem == null || secondItem == null || !(player.getTarget() instanceof Npc))
			return;
		if (!MathUtil.isIn3dRange(player.getTarget(), player, 10))
			return;
		/**
		 * Оба должны быть 2h оружия
		 */
		if (!firstItem.getItemTemplate().isWeapon() || !secondItem.getItemTemplate().isWeapon())
		{
			Logger.getLogger(ArmsfusionService.class).info("[AUDIT]Player: "+player.getName()+" trying to fusion non-weapon. Hacking!");
			return;
		}
		if (firstItem.getItemTemplate().getWeaponType() == null || secondItem.getItemTemplate().getWeaponType() == null)
			return;
		else
		{
			switch(firstItem.getItemTemplate().getWeaponType())
			{
				case DAGGER_1H:
				case MACE_1H:
				case SWORD_1H:
				case TOOLHOE_1H:
					Logger.getLogger(ArmsfusionService.class).info("[AUDIT]Player: "+player.getName()+" trying to fusion 1h weapon. Hacking!");
					return;
			}
			switch(secondItem.getItemTemplate().getWeaponType())
			{
				case DAGGER_1H:
				case MACE_1H:
				case SWORD_1H:
				case TOOLHOE_1H:
					Logger.getLogger(ArmsfusionService.class).info("[AUDIT]Player: "+player.getName()+" trying to fusion 1h weapon. Hacking!");
					return;
			}
		}
		
		// Проверка могут быть эти предметы объеденены
		if (!firstItem.getItemTemplate().isCanFuse() || !secondItem.getItemTemplate().isCanFuse())
			return;
		
		double rarity = rarityRate(firstItem.getItemTemplate().getItemQuality());
		double priceRate = player.getPrices().getGlobalPrices(player.getCommonData().getRace()) * .01;
		double taxRate = player.getPrices().getTaxes(player.getCommonData().getRace()) * .01;
		int priceMod = player.getPrices().getGlobalPricesModifier() * 2;
		int level = firstItem.getItemTemplate().getLevel();
		int price = (int)(priceMod * priceRate * taxRate * rarity * level * level);
		log.debug("Rarity: " + rarity + " Price Rate: " + priceRate + " Tax Rate: " + taxRate + " Price Mod: " + priceMod + " Weapon Level: " + level);
		log.debug("Price: " + price);
		
		if(player.getInventory().getKinahItem().getItemCount() < price)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_COMPOUND_ERROR_NOT_ENOUGH_MONEY(firstItem.getNameID(), secondItem.getNameID()));
			return;
		}
		
		/**
		 * Слияемое орудия должны иметь тот же тип
		 */		
		if(firstItem.getItemTemplate().getWeaponType() != secondItem.getItemTemplate().getWeaponType())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_COMPOUND_ERROR_DIFFERENT_TYPE);
			return;
		}
		
		/**
		 * Второе оружие должно быть ниже или равное по lvl. в отношении первого оружия
		 */
		if(secondItem.getItemTemplate().getLevel() > firstItem.getItemTemplate().getLevel())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_COMPOUND_ERROR_MAIN_REQUIRE_HIGHER_LEVEL);
			return;
		}
		
		boolean decreaseResult = player.getInventory().decreaseKinah(price);
		if(!decreaseResult)
			return;
		
		boolean removeResult = player.getInventory().removeFromBagByObjectId(secondItemUniqueId, 1);
		if(!removeResult)
			return;
		
		firstItem.setFusionedItem(secondItem.getItemTemplate().getTemplateId());

		ItemService.removeAllFusionStone(player,firstItem);

		if (secondItem.hasOptionalSocket())
		{
			firstItem.setOptionalFusionSocket(secondItem.getOptionalSocket());
		}
		else
		{
			firstItem.setOptionalFusionSocket(0);
		}
		
		Set<ManaStone> manastones = secondItem.getItemStones();
		for(ManaStone stone : manastones)
		ItemService.addFusionStone(firstItem, stone.getItemId());
		
		if (firstItem.getPersistentState() != PersistentState.NEW && firstItem.getPersistentState() != PersistentState.UPDATE_REQUIRED)
			firstItem.setPersistentState(PersistentState.UPDATE_REQUIRED);
		
		DAOManager.getDAO(InventoryDAO.class).store(firstItem, player.getObjectId());		
		
		PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(secondItemUniqueId));
		
		PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(firstItem));		
		
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_COMPOUND_SUCCESS(firstItem.getNameID(), secondItem.getNameID()));
		
	}
	
	private static double rarityRate(ItemQuality rarity)
	{
		switch(rarity)
		{
		case COMMON: return 1.0;
		case RARE: return 1.25;
		case LEGEND: return 1.5;
		case UNIQUE: return 2.0;
		case EPIC: return 2.5;
		default: return 1.0;
		}
	}

	public static void breakWeapons(Player player, int weaponToBreakUniqueId)
	{
		Item weaponToBreak = player.getInventory().getItemByObjId(weaponToBreakUniqueId);
		if(weaponToBreak == null)
			weaponToBreak = player.getEquipment().getEquippedItemByObjId(weaponToBreakUniqueId);
		
		if(weaponToBreak == null || !(player.getTarget() instanceof Npc))
			return;

		if(!weaponToBreak.hasFusionedItem())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOUND_ERROR_NOT_AVAILABLE(weaponToBreak.getNameID()));
			return;
		}
	
		weaponToBreak.setFusionedItem(0);
		ItemService.removeAllFusionStone(player,weaponToBreak);
		DAOManager.getDAO(InventoryDAO.class).store(weaponToBreak, player.getObjectId());
		
		PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(weaponToBreak));
		
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_COMPOUNDED_ITEM_DECOMPOUND_SUCCESS(weaponToBreak.getNameID()));
		
	}	
}