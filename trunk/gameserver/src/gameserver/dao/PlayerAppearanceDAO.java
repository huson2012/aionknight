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
import gameserver.model.gameobjects.player.PlayerAppearance;

/**
 * Class that is responsible for loading/storing player appearance
 */
public abstract class PlayerAppearanceDAO implements DAO
{
	/**
	 * Returns unique identifier for PlayerAppearanceDAO
	 * @return unique identifier for PlayerAppearanceDAO
	 */
	@Override
	public final String getClassName()
	{
		return PlayerAppearanceDAO.class.getName();
	}

	/**
	 * Loads player apperance DAO by player ID.<br>
	 * Returns nullIf not found in database
	 * @param playerId player id
	 * @return player appearance or null
	 */
	public abstract PlayerAppearance load(int playerId);

	/**
	 * Saves player appearance in database.
	 * Actually calls {@link #store(int, gameserver.model.gameobjects.player.PlayerAppearance)}
	 * @param player whos appearance to store
	 * @return true, if sql query was successful, false overwise
	 */
	public final boolean store(Player player)
	{
		return store(player.getObjectId(), player.getPlayerAppearance());
	}

	/**
	 * Stores appearance in database
	 * 
	 * @param id player id
	 * @param playerAppearance player appearance
	 * @return true, if sql query was successful, false overwise
	 */
	public abstract boolean store(int id, PlayerAppearance playerAppearance);
}