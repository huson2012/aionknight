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
