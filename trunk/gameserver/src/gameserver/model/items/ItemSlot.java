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

import javolution.util.FastList;

public enum ItemSlot
{
	MAIN_HAND(1), SUB_HAND(1<<1), HELMET(1<<2), TORSO(1<<3), GLOVES(1<<4),
	BOOTS(1<<5), EARRINGS_LEFT(1<<6), EARRINGS_RIGHT(1<<7), RING_LEFT(1<<8),
	RING_RIGHT(1<<9), NECKLACE(1<<10), SHOULDER(1<<11), PANTS(1<<12),
	POWER_SHARD_RIGHT(1<<13), POWER_SHARD_LEFT(1<<14), WINGS(1<<15),
	//non-NPC equips (slot > Short.MAX)
	WAIST(1<<16), MAIN_OFF_HAND(1<<17), SUB_OFF_HAND(1<<18),
	
	//combo
	MAIN_OR_SUB(MAIN_HAND.slotIdMask | SUB_HAND.slotIdMask, true), // 3
	EARRING_RIGHT_OR_LEFT(EARRINGS_LEFT.slotIdMask | EARRINGS_RIGHT.slotIdMask, true), //192
	RING_RIGHT_OR_LEFT(RING_LEFT.slotIdMask | RING_RIGHT.slotIdMask, true), //768
	SHARD_RIGHT_OR_LEFT(POWER_SHARD_LEFT.slotIdMask | POWER_SHARD_RIGHT.slotIdMask, true), //24576
	TORSO_GLOVE_FOOT_SHOULDER_LEG(0, true),//TODO
	
	//STIGMA slots
	STIGMA1(1<<19),
	STIGMA2(1<<20),
	STIGMA3(1<<21),
	STIGMA4(1<<22),
	STIGMA5(1<<23),
	STIGMA6(1<<24),
	
	NONE(1<<25), // Unknown
	
	ADV_STIGMA1(1<<26),
	ADV_STIGMA2(1<<27),
	ADV_STIGMA3(1<<28),
	ADV_STIGMA4(1<<29),
	ADV_STIGMA5(1<<30),
	
	ADV_STIGMAS(ADV_STIGMA1.slotIdMask | ADV_STIGMA2.slotIdMask | ADV_STIGMA3.slotIdMask | ADV_STIGMA4.slotIdMask | ADV_STIGMA5.slotIdMask, true)
	;

	private int slotIdMask;
	private boolean combo;
	
	private ItemSlot(int mask)
	{
		this(mask, false);
	}
	
	private ItemSlot(int mask, boolean combo)
	{
		this.slotIdMask = mask;
		this.combo = combo;
	}
	
	public int getSlotIdMask()
	{
		return slotIdMask;
	}
	
	/**
	 * @return the combo
	 */
	public boolean isCombo()
	{
		return combo;
	}

	public static FastList<ItemSlot> getSlotsFor(int slotIdMask)
	{
		FastList<ItemSlot> slots = new FastList<ItemSlot>();
		for(ItemSlot itemSlot : values())
		{
			int sumMask = itemSlot.slotIdMask & slotIdMask;
			/**
			 * possible values in this check
			 * - one of combo slots (MAIN, RIGHT_RING etc)
			 */
			if(sumMask > 0 && sumMask <= slotIdMask && !itemSlot.combo)
					slots.add(itemSlot);
		}
		
		if(slots.size() == 0)
			throw new IllegalArgumentException("Invalid provided slotIdMask "+slotIdMask);
		
		return slots;
	}
}
