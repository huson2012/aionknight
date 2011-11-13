/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.configs.main;

import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import commons.configuration.Property;

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