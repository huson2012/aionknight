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

public class GroupConfig
{
	@Property(key = "gameserver.playergroup.removetime", defaultValue = "600")
	public static int GROUP_REMOVE_TIME;

	@Property(key = "gameserver.playergroup.maxdistance", defaultValue = "100")
	public static int GROUP_MAX_DISTANCE;

	@Property(key = "gameserver.playeralliance.removetime", defaultValue = "600")
	public static int ALLIANCE_REMOVE_TIME;
}