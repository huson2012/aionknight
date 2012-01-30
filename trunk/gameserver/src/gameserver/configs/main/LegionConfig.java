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
import org.apache.log4j.Logger;
import java.util.regex.Pattern;

public class LegionConfig
{
	protected static final Logger log = Logger.getLogger(LegionConfig.class);

	@Property(key = "gameserver.legion.pattern", defaultValue = "[a-zA-Z ]{2,16}")
	public static Pattern LEGION_NAME_PATTERN;

	@Property(key = "gameserver.legion.selfintropattern", defaultValue = "[a-zA-Z]{2,25}")
	public static Pattern SELF_INTRO_PATTERN;

	@Property(key = "gameserver.legion.nicknamepattern", defaultValue = "[a-zA-Z]{2,10}")
	public static Pattern NICKNAME_PATTERN;

	@Property(key = "gameserver.legion.announcementpattern", defaultValue = ".*{2,120}")
	public static Pattern ANNOUNCEMENT_PATTERN;

	@Property(key = "gameserver.legion.disbandtime", defaultValue = "86400")
	public static int LEGION_DISBAND_TIME;

	@Property(key = "gameserver.legion.disbanddifference", defaultValue = "604800")
	public static int LEGION_DISBAND_DIFFERENCE;

	@Property(key = "gameserver.legion.creationrequiredkinah", defaultValue = "10000")
	public static int LEGION_CREATE_REQUIRED_KINAH;

	@Property(key = "gameserver.legion.emblemrequiredkinah", defaultValue = "10000")
	public static int LEGION_EMBLEM_REQUIRED_KINAH;

	@Property(key = "gameserver.legion.level2requiredkinah", defaultValue = "100000")
	public static int LEGION_LEVEL2_REQUIRED_KINAH;

	@Property(key = "gameserver.legion.level3requiredkinah", defaultValue = "1000000")
	public static int LEGION_LEVEL3_REQUIRED_KINAH;

	@Property(key = "gameserver.legion.level4requiredkinah", defaultValue = "2000000")
	public static int LEGION_LEVEL4_REQUIRED_KINAH;

	@Property(key = "gameserver.legion.level5requiredkinah", defaultValue = "6000000")
	public static int LEGION_LEVEL5_REQUIRED_KINAH;

	@Property(key = "gameserver.legion.level2requiredmembers", defaultValue = "10")
	public static int LEGION_LEVEL2_REQUIRED_MEMBERS;

	@Property(key = "gameserver.legion.level3requiredmembers", defaultValue = "20")
	public static int LEGION_LEVEL3_REQUIRED_MEMBERS;

	@Property(key = "gameserver.legion.level4requiredmembers", defaultValue = "30")
	public static int LEGION_LEVEL4_REQUIRED_MEMBERS;

	@Property(key = "gameserver.legion.level5requiredmembers", defaultValue = "40")
	public static int LEGION_LEVEL5_REQUIRED_MEMBERS;

	@Property(key = "gameserver.legion.level2requiredcontribution", defaultValue = "0")
	public static int LEGION_LEVEL2_REQUIRED_CONTRIBUTION;

	@Property(key = "gameserver.legion.level3requiredcontribution", defaultValue = "20000")
	public static int LEGION_LEVEL3_REQUIRED_CONTRIBUTION;

	@Property(key = "gameserver.legion.level4requiredcontribution", defaultValue = "100000")
	public static int LEGION_LEVEL4_REQUIRED_CONTRIBUTION;

	@Property(key = "gameserver.legion.level5requiredcontribution", defaultValue = "500000")
	public static int LEGION_LEVEL5_REQUIRED_CONTRIBUTION;

	@Property(key = "gameserver.legion.level1maxmembers", defaultValue = "30")
	public static int LEGION_LEVEL1_MAX_MEMBERS;

	@Property(key = "gameserver.legion.level2maxmembers", defaultValue = "60")
	public static int LEGION_LEVEL2_MAX_MEMBERS;

	@Property(key = "gameserver.legion.level3maxmembers", defaultValue = "90")
	public static int LEGION_LEVEL3_MAX_MEMBERS;

	@Property(key = "gameserver.legion.level4maxmembers", defaultValue = "120")
	public static int LEGION_LEVEL4_MAX_MEMBERS;

	@Property(key = "gameserver.legion.level5maxmembers", defaultValue = "150")
	public static int LEGION_LEVEL5_MAX_MEMBERS;

	@Property(key = "gameserver.legion.warehouse", defaultValue = "false")
	public static boolean LEGION_WAREHOUSE;

	@Property(key = "gameserver.legion.inviteotherfaction", defaultValue = "false")
	public static boolean LEGION_INVITEOTHERFACTION;

	@Property(key = "gameserver.legion.ranking.periodicupdate", defaultValue = "1200")
	public static int LEGION_RANKING_PERIODICUPDATE;
}