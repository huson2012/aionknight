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

package gameserver.utils.stats;

import java.util.NoSuchElementException;

public enum DropRewardEnum
{
	MINUS_11(-11, 0),
	MINUS_10(-10, 1),
	MINUS_9(-9, 10),
	MINUS_8(-8, 20),
	MINUS_7(-7, 30),
	MINUS_6(-6, 40),
	MINUS_5(-5, 50),
	MINUS_4(-4, 60),
	MINUS_3(-3, 90),
	MINUS_2(-2, 100),
	MINUS_1(-1, 100),
	ZERO(0, 100);

	private int dropRewardPercent;
	private int levelDifference;
	
	private DropRewardEnum(int levelDifference, int dropRewardPercent)
	{
		this.levelDifference = levelDifference;
		this.dropRewardPercent = dropRewardPercent;
	}
	
	public int rewardPercent()
	{
		return dropRewardPercent;
	}
	
	/**
	 * 
	 * @param levelDifference between two objects
	 * @return Drop reward percentage
	 */
	public static int dropRewardFrom(int levelDifference)
	{
		if(levelDifference < MINUS_11.levelDifference)
		{
			return MINUS_11.dropRewardPercent;
		}
		if(levelDifference > ZERO.levelDifference)
		{
			return ZERO.dropRewardPercent;
		}
	
		for(DropRewardEnum dropReward : values())
		{
			if(dropReward.levelDifference == levelDifference)
			{
				return dropReward.dropRewardPercent;
			}
		}
		throw new NoSuchElementException("Drop reward for such level difference was not found");
	}
}
