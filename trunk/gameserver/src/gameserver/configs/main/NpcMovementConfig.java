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

public class NpcMovementConfig
{
	@Property(key = "gameserver.npc.movement.active", defaultValue = "true")
	public static boolean ACTIVE_NPC_MOVEMENT;

	@Property(key = "gameserver.npc.movement.delay.minimum", defaultValue = "3")
	public static int MINIMIMUM_DELAY;

	@Property(key = "gameserver.npc.movement.delay.maximum", defaultValue = "15")
	public static int MAXIMUM_DELAY;
}