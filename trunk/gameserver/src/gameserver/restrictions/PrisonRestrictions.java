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
import gameserver.utils.PacketSendUtility;
import gameserver.world.WorldMapType;

public class PrisonRestrictions extends AbstractRestrictions
{
	@Override
	public boolean isRestricted(Player player, Class<? extends Restrictions> callingRestriction)
	{
		if(isInPrison(player))
		{
			PacketSendUtility.sendMessage(player, "You are in prison!");
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean canAttack(Player player, VisibleObject target)
	{
		if(isInPrison(player))
		{
			PacketSendUtility.sendMessage(player, "You cannot attack in prison!");
			return false;
		}

		return true;
	}

	@Override
	public boolean canUseSkill(Player player, Skill skill)
	{
		if(isInPrison(player))
		{
			PacketSendUtility.sendMessage(player, "You cannot use skills in prison!");
			return false;
		}

		return true;
	}
	
	
	@Override
	public boolean canAffectBySkill(Player player, VisibleObject target)
	{
		return true;
	}

	@Override
	public boolean canChat(Player player)
	{
		if(isInPrison(player))
		{
			PacketSendUtility.sendMessage(player, "You cannot chat in prison!");
			return false;
		}

		return true;
	}

	@Override
	public boolean canInviteToGroup(Player player, Player target)
	{
		if(isInPrison(player))
		{
			PacketSendUtility.sendMessage(player, "You cannot invite members to group in prison!");
			return false;
		}

		return true;
	}
	
	@Override
	public boolean canInviteToAlliance(Player player, Player target)
	{
		if(isInPrison(player))
		{
			PacketSendUtility.sendMessage(player, "You cannot invite members to alliance in prison!");
			return false;
		}

		return true;
	}

	@Override
	public boolean canChangeEquip(Player player)
	{
		if(isInPrison(player))
		{
			PacketSendUtility.sendMessage(player, "You cannot equip / unequip item in prison!");
			return false;
		}

		return true;
	}
	@Override
	public boolean canUseItem(Player player)
	{
		if(isInPrison(player))
		{
			PacketSendUtility.sendMessage(player, "You cannot use item in prison!");
			return false;			
		}
		return true;
	}
	
	private boolean isInPrison(Player player)
	{
		return player.isInPrison() || player.getWorldId() == WorldMapType.PRISON.getId();
	}
}