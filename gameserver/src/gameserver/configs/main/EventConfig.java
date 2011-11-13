/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.configs.main;

import commons.configuration.Property;

public class EventConfig
{
	@Property(key = "gameserver.events.givejuice.elyos", defaultValue = "160009017")
	public static int EVENT_GIVEJUICE_ELYOS;

	@Property(key = "gameserver.events.givejuice.asmos", defaultValue = "160009017")
	public static int EVENT_GIVEJUICE_ASMOS;

	@Property(key = "gameserver.events.givecake.elyos", defaultValue = "160010073")
	public static int EVENT_GIVECAKE_ELYOS;

	@Property(key = "gameserver.events.givecake.asmos", defaultValue = "160010073")
	public static int EVENT_GIVECAKE_ASMOS;
}