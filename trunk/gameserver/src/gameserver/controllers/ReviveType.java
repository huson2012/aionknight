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

public enum ReviveType
{
	/**
	 * Revive to bindpoint
	 */
	BIND_REVIVE(0),
	/**
	 * Revive from rebirth effect
	 */
	REBIRTH_REVIVE(1),
	/**
	 * Self-Rez Stone
	 */
	ITEM_SELF_REVIVE(2),
	/**
	 * Revive from skill
	 */
	SKILL_REVIVE(3),
	/**
	 * Revive to Kisk
	 */	
	KISK_REVIVE(4),
	/**
	 * Revive to Instance Entry Point
	 */
	INSTANCE_ENTRY(6);
	

	private int	typeId;

	/**
	 * Constructor.
	 * 
	 * @param typeId
	 */
	private ReviveType(int typeId)
	{
		this.typeId = typeId;
	}

	public int getReviveTypeId()
	{
		return typeId;
	}

	public static ReviveType getReviveTypeById(int id)
	{
		for(ReviveType rt : values())
		{
			if(rt.typeId == id)
				return rt;
		}
		throw new IllegalArgumentException("Unsupported revive type: " + id);
	}
}