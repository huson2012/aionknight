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

package gameserver.configs.main;

import commons.configuration.Property;

public class EnchantsConfig
{
	@Property(key = "gameserver.manastone.percent.slot1", defaultValue = "98")
	public static int MS_SLOT1;
	
	@Property(key = "gameserver.manastone.percent.slot2", defaultValue = "85")
	public static int MS_SLOT2;
	
	@Property(key = "gameserver.manastone.percent.slot3", defaultValue = "75")
	public static int MS_SLOT3;
	
	@Property(key = "gameserver.manastone.percent.slot4", defaultValue = "65")
	public static int MS_SLOT4;
	
	@Property(key = "gameserver.manastone.percent.slot5", defaultValue = "55")
	public static int MS_SLOT5;
	
	@Property(key = "gameserver.manastone.percent.slot6", defaultValue = "45")
	public static int MS_SLOT6;
	
	@Property(key = "gameserver.manastone.percent.slot7", defaultValue = "35")
	public static int MS_SLOT7;

	@Property(key = "gameserver.supplement.lesser", defaultValue = "10")
	public static int LSSUP;

	@Property(key = "gameserver.supplement.regular", defaultValue = "15")
	public static int RGSUP;

	@Property(key = "gameserver.supplement.greater", defaultValue = "20")
	public static int GRSUP;
}