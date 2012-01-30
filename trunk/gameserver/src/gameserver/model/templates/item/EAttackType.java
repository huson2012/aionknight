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

import gameserver.model.SkillElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "attack_type")
@XmlEnum
public enum EAttackType
{
	PHYSICAL(false),
	MAGICAL_WATER(true),
	MAGICAL_FIRE(true);
	
	private boolean	magic;

	private EAttackType(boolean magic)
	{
		this.magic = magic;
	}

	public String value()
	{
		return name();
	}

	public static EAttackType fromValue(String v)
	{
		return valueOf(v);
	}

	/**
	 * @return Returns the magic.
	 */
	public boolean isMagic()
	{
		return magic;
	}
	
	public SkillElement getElement()
	{
		switch (this)
		{
			case MAGICAL_WATER: 
				return SkillElement.WATER;
			case MAGICAL_FIRE:
				return SkillElement.FIRE;
		}
		return SkillElement.NONE;
	}
	
	public static EAttackType fromElement(SkillElement element)
	{
		switch (element)
		{
			case WATER: 
				return MAGICAL_WATER; 
			case FIRE: 
				return MAGICAL_FIRE;
		}
		
		return PHYSICAL;
	}
	
}
