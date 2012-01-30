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

public abstract class AbstractRestrictions implements Restrictions
{
	public void activate()
	{
		RestrictionsManager.activate(this);
	}

	public void deactivate()
	{
		RestrictionsManager.deactivate(this);
	}

	@Override
	public int hashCode()
	{
		return getClass().hashCode();
	}

	/**
	 * To avoid accidentally multiple times activated restrictions.
	 */
	@Override
	public boolean equals(Object obj)
	{
		return getClass().equals(obj.getClass());
	}
	
	@DisabledRestriction
	public boolean isRestricted(Player player, Class<? extends Restrictions> callingRestriction)
	{
		throw new AbstractMethodError();
	}
	
	@DisabledRestriction
	public boolean canAttack(Player player, VisibleObject target)
	{
		throw new AbstractMethodError();
	}

	@DisabledRestriction
	public boolean canAffectBySkill(Player player, VisibleObject target)
	{
		throw new AbstractMethodError();
	}

	@DisabledRestriction
	public boolean canUseSkill(Player player, Skill skill)
	{
		throw new AbstractMethodError();
	}
	
	@DisabledRestriction
	public boolean canChat(Player player)
	{
		throw new AbstractMethodError();
	}
	
	@DisabledRestriction
	public boolean canInviteToGroup(Player player, Player target)
	{
		throw new AbstractMethodError();
	}
	
	@DisabledRestriction
	public boolean canChangeEquip(Player player)
	{
		throw new AbstractMethodError();
	}
	
	@DisabledRestriction
	public boolean canUseWarehouse(Player player)
	{
		throw new AbstractMethodError();
	}

	@DisabledRestriction
	public boolean canTrade(Player player)
	{
		throw new AbstractMethodError();
	}
	
	@DisabledRestriction
	public boolean canUseItem(Player player)
	{
		throw new AbstractMethodError();
	}
}
