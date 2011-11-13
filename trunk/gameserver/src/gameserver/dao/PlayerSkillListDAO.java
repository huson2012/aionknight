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

package gameserver.dao;

import commons.database.dao.DAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.SkillList;

public abstract class PlayerSkillListDAO implements DAO
{
	/**
     * Returns unique identifier for PlayerSkillListDAO
     * @return unique identifier for PlayerSkillListDAO
     */
	@Override
	public final String getClassName()
	{
		 return PlayerSkillListDAO.class.getName();
	}

	/**
	 * Returns a list of skilllist for player
	 * @param playerId Player object id.
	 * @return a list of skilllist for player
	 */
	public abstract SkillList loadSkillList(int playerId);
	
	/**
	 * Updates skill with new information
	 * @param playerId
	 * @param skillId
	 * @param skillLevel
	 */
	public abstract boolean storeSkills(Player player);

}