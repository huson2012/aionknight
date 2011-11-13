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

package gameserver.model.gameobjects;

import gameserver.model.gameobjects.player.Player;

public class LFGApplyGroup
{
	private Player player;
	private String applyString;
	private long creationTime;
	private int groupType;

	public LFGApplyGroup(Player player, String applyString, int groupType, long creationTime)
	{
		this.player = player;
		this.applyString = applyString;
		this.creationTime = creationTime;
		this.groupType = groupType;
	}

	public Player getPlayer()
	{
		return player;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public String getApplyString()
	{
		return applyString;
	}

	public void setApplyString(String applyString)
	{
		this.applyString = applyString;
	}

	public long getCreationTime()
	{
		return creationTime;
	}

	public void setCreationTime(long creationTime)
	{
		this.creationTime = creationTime;
	}

	public int getGroupType()
	{
		return groupType;
	}

	public void setGroupType(int groupType)
	{
		this.groupType = groupType;
	}
}