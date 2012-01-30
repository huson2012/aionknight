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

package gameserver.model.templates.bonus;

public enum InventoryBonusType
{
	BOSS,			// %Quest_L_boss; siege related?
	COIN,			// %Quest_L_coin
	ENCHANT,
	FOOD,			// %Quest_L_food
	FORTRESS,		// %Quest_L_fortress; sends promotion mails with medals?
	GOODS,			// %Quest_L_Goods
	GODSTONE,
	ISLAND,			// %Quest_L_3_island; siege related?
	LUNAR,			// %Quest_A_BranchLunarEvent
	MAGICAL,		// %Quest_L_magical
	MANASTONE,		// %Quest_L_matter_option
	MASTER_RECIPE,	// %Quest_ta_l_master_recipe
	MATERIAL,		// %Quest_L_material
	MEDAL,			// %Quest_L_medal
	MEDICINE,		// %Quest_L_medicine; potions and remedies
	MOVIE,			// %Quest_L_Christmas; cut scenes
	NONE,
	RECIPE,			// %Quest_L_Recipe
	REDEEM,			// %Quest_L_Rnd_Redeem and %Quest_L_redeem
	TASK,			// %Quest_L_task; craft related
	WORLD_DROP_A,	// Asmodian drops
	WORLD_DROP_B,	// Both race drops
	WORLD_DROP_E	// Elyos drops
}
