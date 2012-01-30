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

package gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "weapon_type")
@XmlEnum
public enum WeaponType
{
	DAGGER_1H(new int[]{30, 9}, 1),
	MACE_1H(new int[]{3, 10}, 1),
	SWORD_1H(new int[]{1, 8}, 1),
	TOOLHOE_1H(new int[]{}, 1),
	BOOK_2H(new int[]{64}, 2),
	ORB_2H(new int[]{64}, 2),
	POLEARM_2H(new int[]{16}, 2),
	STAFF_2H(new int[]{53}, 2),
	SWORD_2H(new int[]{15}, 2),
	TOOLPICK_2H(new int[]{}, 2),
	TOOLROD_2H(new int[]{}, 2),
	BOW(new int[]{17}, 2);

	private int[] requiredSkill;
	private int slots;

	private WeaponType(int[] requiredSkills, int slots)
	{
		this.requiredSkill = requiredSkills;
		this.slots = slots;
	}

	public int[] getRequiredSkills()
	{
		return requiredSkill;
	}
	
	public int getRequiredSlots()
	{
		return slots;
	}

	/**
	 * @return int
	 */
	public int getMask()
	{
		return 1 << this.ordinal();
	}
}
