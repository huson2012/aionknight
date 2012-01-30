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

package gameserver.model.templates.pet;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "FoodType")
@XmlEnum
public enum FoodType {

	NOT_FOOD("NOT_FOOD"),
	DOPING("DOPING"),		// 2.5 version
	@XmlEnumValue("MISC1")
	MISC_1("MISC1"),
	@XmlEnumValue("MISC2")
	MISC_2("MISC2"),
	MISC("MISC"),
	FLUID("FLUID"),
	ARMOR("ARMOR"),
	THORN("THORN"),
	BONE("BONE"),
	BALAUR("BALAUR"),
	SOUL("SOUL"),
	@XmlEnumValue("HEALTHY1")
	HEALTHY_1("HEALTHY1"),
	@XmlEnumValue("HEALTHY2")
	HEALTHY_2("HEALTHY2"), // 2.5 version
	@XmlEnumValue("CASH1")
	CASH_1("CASH1"),
	@XmlEnumValue("CASH2")
	CASH_2("CASH2"),
	@XmlEnumValue("CASH3")
	CASH_3("CASH3"),
	@XmlEnumValue("CASH4")
	CASH_4("CASH4"),
	POWDER("POWDER"),
	CRYSTAL("CRYSTAL"),
	GEM("GEM");

	private final String value;

	FoodType(String v) 
	{
		value = v;
	}

	public String value() 
	{
		return value;
	}

	public static FoodType fromValue(String v) 
	{
		for (FoodType c: FoodType.values()) 
		{
			if (c.value.equals(v))
				return c;
		}

		throw new IllegalArgumentException(v);
	}

	public static boolean isLoved(FoodType foodType)
	{
		return foodType == POWDER || foodType == CRYSTAL || foodType == GEM;
	}

}
