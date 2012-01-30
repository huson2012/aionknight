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

package gameserver.model.gameobjects;

import gameserver.model.gameobjects.player.Player;

public class LFGRecruitGroup
{
	private Player player;
	private String applyString;
	private int groupType;
	private int maxLevel;
	private long creationTime;

	public LFGRecruitGroup(Player player, String applyString, int groupType, int maxLevel, long creationTime)
	{
		this.player = player;
		this.applyString = applyString;
		this.creationTime = creationTime;
		this.groupType = groupType;
		this.maxLevel = maxLevel;
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

	public int getMaxLevel()
	{
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel)
	{
		this.maxLevel = maxLevel;
	}
}
