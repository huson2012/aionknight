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

package gameserver.model;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum PlayerClass
{
	WARRIOR(0, true), GLADIATOR(1),	TEMPLAR(2), SCOUT(3, true),	ASSASSIN(4),
	RANGER(5), MAGE(6, true), SORCERER(7), SPIRIT_MASTER(8), PRIEST(9, true),
	CLERIC(10),	CHANTER(11);

	private byte classId;
	private int idMask;
	private boolean	startingClass;
	private PlayerClass(int classId)
	{
		this(classId, false);
	}

	private PlayerClass(int classId, boolean startingClass)
	{
		this.classId = (byte) classId;
		this.startingClass = startingClass;
		this.idMask = (int)Math.pow(2, classId);
	}

	public byte getClassId()
	{
		return classId;
	}

	public static PlayerClass getPlayerClassById(byte classId)
	{
		for(PlayerClass pc : values())
		{
			if(pc.classId == classId)
				return pc;
		}

		throw new IllegalArgumentException("There is no player class with id " + classId);
	}

	public boolean isStartingClass()
	{
		return startingClass;
	}

	public static PlayerClass getStartingClassFor(PlayerClass pc)
	{
		switch(pc)
		{
			case ASSASSIN:
			case RANGER:
				return SCOUT;
			case GLADIATOR:
			case TEMPLAR:
				return WARRIOR;
			case CHANTER:
			case CLERIC:
				return PRIEST;
			case SORCERER:
			case SPIRIT_MASTER:
				return MAGE;
			default:
				return pc;
		}
	}
	
	public static PlayerClass getPlayerClassByString(String fieldName)
	{
		for(PlayerClass pc : values())
		{
			if(pc.toString().equals(fieldName))
				return pc;
		}
		return null;
	}
	
	public int getMask()
	{
		return idMask;
	}
}