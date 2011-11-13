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

package gameserver.restrictions;

import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.skill.model.Skill;

public interface Restrictions
{
	public boolean isRestricted(Player player, Class<? extends Restrictions> callingRestriction);
	public boolean canAttack(Player player, VisibleObject target);
	public boolean canAffectBySkill(Player player, VisibleObject target);
	public boolean canUseSkill(Player player, Skill skill);
	public boolean canChat(Player player);
	public boolean canInviteToGroup(Player player, Player target);
	public boolean canInviteToAlliance(Player player, Player target);
	public boolean canChangeEquip(Player player);
	public boolean canUseWarehouse(Player player);
	public boolean canTrade(Player player);
	public boolean canUseItem(Player player);
}