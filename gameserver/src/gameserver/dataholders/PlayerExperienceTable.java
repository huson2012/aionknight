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
			throw new IllegalArgumentException("The given level is higher than possible max");

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