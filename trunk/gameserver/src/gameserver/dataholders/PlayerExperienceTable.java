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

package gameserver.dataholders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="player_experience_table")
@XmlAccessorType(XmlAccessType.NONE)
public class PlayerExperienceTable
{
	@XmlElement(name = "exp")
	private long[]	experience;
	public long getStartExpForLevel(int level)
	{
		if(level > experience.length)
			throw new IllegalArgumentException("[!] The given level is higher than possible max");

		return level > experience.length ? 0 : experience[level - 1];
	}
	
	public int getLevelForExp(long expValue)
	{
		int level = 0;
		for(int i = experience.length;i > 0;i--)
		{
			if (expValue >= experience[(i-1)])
			{
				level = i;
				break;
			}

		}
		return level;
	}

	public int getMaxLevel()
	{
		return experience == null ? 0 : experience.length;
	}
}