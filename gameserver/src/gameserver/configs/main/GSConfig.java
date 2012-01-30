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
import java.util.regex.Pattern;

public class GSConfig
{
	/**
	 * Server name
	 */
	@Property(key = "gameserver.name", defaultValue = "Aion-Knight Free Server")
	public static String SERVER_NAME;

	/**
	 * Character name pattern (checked when character is being created)
	 */
	@Property(key = "gameserver.character.name.pattern", defaultValue = "[a-zA-Z]{2,16}")
	public static Pattern CHAR_NAME_PATTERN;

	/**
	 * Pet name pattern (checked when pet is being created or renamed)
	 */
	@Property(key = "gameserver.pet.name.pattern", defaultValue = "[a-zA-Z]{2,16}")
	public static Pattern PET_NAME_PATTERN;

	/**
	 * Server Country Code
	 */
	@Property(key = "gameserver.country.code", defaultValue = "1")
	public static int SERVER_COUNTRY_CODE;
	
	/**
	 * Server Version
	 */
	@Property(key = "gameserver.version", defaultValue = "2.7.0.3")
	public static String SERVER_VERSION;

	/**
	 * Server Mode
	 */
	@Property(key = "gameserver.mode", defaultValue = "1")
	public static int SERVER_MODE;
	
	@Property(key = "gameserver.motd.revision.display", defaultValue = "false")
	public static boolean SERVER_MOTD_DISPLAYREV;
	
	/**
	 * Disable chat server connection
	 */
	@Property(key = "gameserver.disable.chatserver", defaultValue = "true")
	public static boolean DISABLE_CHAT_SERVER;
	
	@Property(key = "gameserver.log.chat", defaultValue = "false")
	public static boolean LOG_CHAT;
	
	@Property(key = "gameserver.log.item", defaultValue = "false")
	public static boolean LOG_ITEM;
	
	@Property(key = "gameserver.factions.ratio.limited", defaultValue = "false")
	public static boolean FACTIONS_RATIO_LIMITED;
	
	@Property(key = "gameserver.factions.ratio.value", defaultValue = "50")
	public static int FACTIONS_RATIO_VALUE;
	
	@Property(key = "gameserver.factions.ratio.level", defaultValue = "10")
	public static int FACTIONS_RATIO_LEVEL;
	
	@Property(key = "gameserver.factions.ratio.minimum", defaultValue = "50")
	public static int FACTIONS_RATIO_MINIMUM;
	
	@Property(key = "gameserver.lang", defaultValue = "en")
	public static String LANG;

	@Property(key = "gameserver.disable.rdcserver", defaultValue = "false")
	public static boolean DISABLE_RDC_SERVER;

	@Property(key = "gameserver.enable.freefly", defaultValue = "true")
    public static boolean FREEFLY;

	@Property(key = "gameserver.enable.repurchase", defaultValue = "true")
	public static boolean ENABLE_REPURCHASE;

	@Property(key = "gameserver.enable.purchaselimit", defaultValue = "true")
	public static boolean ENABLE_PURCHASE_LIMIT;

	@Property(key = "gameserver.purchaselimit.restock.time", defaultValue = "4")
	public static int PURCHASE_LIMIT_RESTOCK_TIME;

	@Property(key = "gameserver.passkey.enable", defaultValue = "false")
	public static boolean PASSKEY_ENABLE;

	@Property(key = "gameserver.passkey.wrong.maxcount", defaultValue = "5")
	public static int PASSKEY_WRONG_MAXCOUNT;
	
	@Property(key = "gameserver.enable.geo", defaultValue = "false")
	public static boolean GEODATA_ENABLED;
}