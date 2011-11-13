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

import commons.configuration.Property;

public class SiegeConfig
{
	@Property(key = "gameserver.siege.schedule.type", defaultValue = "1")
	public static int SIEGE_SCHEDULE_TYPE;
	
	@Property(key = "gameserver.siege.enabled", defaultValue = "true")
	public static boolean SIEGE_ENABLED;
	
	@Property(key = "gameserver.siege.influence.fortress", defaultValue = "10")
	public static int SIEGE_POINTS_FORTRESS;
	
	@Property(key = "gameserver.siege.influence.artifact", defaultValue = "1")
	public static int SIEGE_POINTS_ARTIFACT;
}