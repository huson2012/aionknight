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

package gameserver.task.impl;

import gameserver.model.alliance.PlayerAllianceEvent;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.GroupEvent;
import gameserver.services.AllianceService;
import gameserver.task.AbstractIterativePeriodicTaskManager;

public final class GroupUpdater extends AbstractIterativePeriodicTaskManager<Player>
{
	private static final class SingletonHolder
	{
		private static final GroupUpdater INSTANCE	= new GroupUpdater();
	}

	public static GroupUpdater getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	public GroupUpdater()
	{
		super(2000);
	}
	
	@Override
	protected void callTask(Player player)
	{
		if (player.isInGroup())
			player.getPlayerGroup().updateGroupUIToEvent(player, GroupEvent.MOVEMENT);
		
		if (player.isInAlliance())
			AllianceService.getInstance().updateAllianceUIToEvent(player, PlayerAllianceEvent.MOVEMENT);
		
		this.stopTask(player);
	}
	
	@Override
	protected String getCalledMethodName()
	{
		return "groupAllianceUpdate()";
	}
}